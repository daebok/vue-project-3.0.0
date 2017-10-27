package com.rd.ifaes.core.tpp.controller.jxbank;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.tpp.service.tpp.JxbankBondInvestService;

@Controller
public class JxCreditInvestController extends BaseController {
    /**
     * 日志跟踪器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JxCreditInvestController.class);
    @Resource
    private transient BondInvestService bondInvestService;
    @Resource
    private JxbankBondInvestService jxbankBondInvestService;

    /**
     * 债权投资同步回调处理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/jxbank/creditInvest/return")
    public String bidApply(JxCreditInvestModel jxModel, final HttpServletRequest request, final Model model) {
        LOGGER.info("债权投资进入同步回调，回调参数：{}", getRequestParams(request));
        String picType = CallbackConstant.PAY_PICTYPE;
        model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
        model.addAttribute("resultUrl", URLConstant.BOND_LIST_BOUGHT); // 点击“我的资产>我的投资”跳转页面地址
        String url = "";
        final BondInvest bondInvest = bondInvestService.getBondInvestByOrderNo(jxModel.getOrderId());
        if (bondInvest == null) {
            url = URLConstant.BOND_HOME_PAGE;
        } else {
            url = URLConstant.BOND_DETAIL_PAGE_PREFIX + bondInvest.getBondId();
        }
        model.addAttribute("buttonUrl", url); // 点击按钮跳转页面地址
        model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
        model.addAttribute("otherUrl", URLConstant.BOND_HOME_PAGE); // 点击"其它产品"跳转页面地址
        String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
        if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {//移动端
            model.addAttribute("res_msg", "债券投资处理完成");
            model.addAttribute("res_code", "bond_success");
            model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
            return "/mobileResult";
        } else {
            model.addAttribute("picType", picType);
            model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);
            model.addAttribute("otherUrl", URLConstant.BOND_HOME_PAGE); // 点击"其它产品"跳转页面地址
            model.addAttribute("errorMsg", null);
            return "/retResult";
        }
    }


    /**
     * 债权投资异步回调处理
     *
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/creditInvest/notify")
    public void ufxInvestNotify(final HttpServletRequest request, final HttpServletResponse response, String bgData) {
        LOGGER.info("债权投资进入异步回调，回调参数：{}", getRequestParams(request));
        JxCreditInvestModel jxModel = JSON.parseObject(bgData, JxCreditInvestModel.class);
        boolean ret = jxModel.responseVerify();
        if (ret) { // 验签成功
            jxbankBondInvestService.bondInvestHandle(jxModel);
        } else {
            LOGGER.error("债权投资回调验签失败，订单号：{}", jxModel.getOrderId());
        }
        printSuccessReturn(response);
    }

}

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
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.tpp.model.jx.JxBidApplyModel;
import com.rd.ifaes.core.tpp.service.tpp.JxbankInvestService;
import com.rd.ifaes.core.tpp.util.JxConfig;

@Controller
public class JxBidApplyController extends BaseController {
    /**
     * 日志跟踪器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JxBidApplyController.class);
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private JxbankInvestService jxbankInvestService;

    /**
     * 投资同步回调处理
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/jxbank/bidApply/return")
    public String bidApply(JxBidApplyModel jxModel, final HttpServletRequest request, final Model model) {
        LOGGER.info("投资进入同步回调，回调参数：{}", getRequestParams(request));
        final boolean ret = jxModel.responseVerify();
        String errorMsg = null;// 错误信息
        String picType = CallbackConstant.PAY_PICTYPE;
        if (!ret) {
            // 验签失败
            errorMsg = ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL);
            picType = CallbackConstant.ERROR_PICTYPE;
        } else if (!JxConfig.SUCCESS.equals(jxModel.getRetCode())) {
            // 三方处理出错
            errorMsg = jxModel.getRetMsg();
            picType = CallbackConstant.ERROR_PICTYPE;
        } else {
//            jxbankService.investHandleJx(jxModel);
            ProjectInvest invest = projectInvestService.getInvestByOrderNo(jxModel.getOrderId());
            if (invest != null) {
                model.addAttribute("buttonUrl", URLConstant.INVEST_DETAIL_PAGE_PREFIX + invest.getProjectId()); // 点击按钮跳转页面地址
            }
            model.addAttribute("resultName", CallbackConstant.PAY_REQUEST_NAME);
            model.addAttribute("resultUrl", URLConstant.MEMBER_MYINVEST_PAGE); // 点击“我的资产>我的投资”跳转页面地址
            model.addAttribute("buttonName", CallbackConstant.PAY_BUTTON_NAME);
        }
        String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
        // 是否是移动端
        if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {// 移动端
        	model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
        	if (errorMsg != null) {// 失败
                model.addAttribute("r_msg", errorMsg);
                model.addAttribute("res_code", "register_fail");
                return "/mobileRetresult";
            } else {// 成功
                model.addAttribute("res_msg", "投资处理完成");
                model.addAttribute("res_code", "invest_success");
                return "/mobileResult";
            }
        } else {
            model.addAttribute("picType", picType);
            model.addAttribute("requestType", CallbackConstant.PAY_REQUEST_TYPE);
            model.addAttribute("otherUrl", URLConstant.INVEST_HOME_PAGE); // 点击"其它产品"跳转页面地址
            model.addAttribute("errorMsg", errorMsg);
            return "/retResult";
        }
    }


    /**
     * 投资异步回调处理
     *
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/bidApply/notify")
    public void ufxInvestNotify(final HttpServletRequest request, final HttpServletResponse response, String bgData) {
        LOGGER.info("投资进入异步回调，回调参数：{}", getRequestParams(request));
        JxBidApplyModel jxModel = JSON.parseObject(bgData, JxBidApplyModel.class);
        boolean ret = jxModel.responseVerify();
        if (ret) { // 验签成功
            jxbankInvestService.investHandle(jxModel);
        } else {
            LOGGER.error("投资回调验签失败，订单号：{}", jxModel.getOrderId());
        }
        printSuccessReturn(response);
    }

}

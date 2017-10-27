package com.rd.ifaes.core.tpp.controller.jxbank;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthPlusModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;

@Controller
public class JxAutoCreditInvestAuthPlusController extends BaseController {

    @Resource
    private transient UserAuthSignLogService userAuthSignLogService;

    /**
     * 投资人自动债权转让签约增强同步回调
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/autoCreditInvestAuthPlus/return")
    public String tppReturn(final HttpServletRequest request, final Model model) {
        LOGGER.info("投资人自动债权转让签约增强进入同步回调,{}", getRequestParams(request));
        String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
        if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {// 移动端
        	model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
            model.addAttribute("res_msg", "自动债权转让签约处理完成");
            model.addAttribute("res_code", "autoCreditAuth_success");
        	return "/mobileResult";
        } else {
        	model.addAttribute("picType", CallbackConstant.AUTH_SIGN_PICTYPE);
    		model.addAttribute("requestType", CallbackConstant.AUTH_SIGN_REQUEST_TYPE);
    		model.addAttribute("resultName", CallbackConstant.AUTH_SIGN_REQUEST_NAME);
    		model.addAttribute("resultUrl", CallbackConstant.AUTH_SIGN_REQUEST_URL);
            return "/retResult";
        }
    }

    /**
     * 投资人自动债权转让签约增强异步回调
     *
     * @throws Exception
     */
    @RequestMapping(value = "/jxbank/autoCreditInvestAuthPlus/notify")
    public void tppNotify(final String bgData, final HttpServletRequest request, final HttpServletResponse response) {
        LOGGER.info("投资人自动债权转让签约增强进入异步回调,{}", getRequestParams(request));
        final JxAutoCreditInvestAuthPlusModel model = JSON.parseObject(bgData, JxAutoCreditInvestAuthPlusModel.class);
        if (model.responseVerify()) {
            userAuthSignLogService.doAutoCreditInvestAuth(model);
        } else {
            LOGGER.error("验签失败：{}", model.getOrderNo());
        }
        printSuccessReturn(response);
    }
}

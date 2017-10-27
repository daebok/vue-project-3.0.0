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
import com.rd.ifaes.core.tpp.model.jx.JxPasswordSetModel;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;

@Controller
public class JxSetPayPwdController extends BaseController {

    @Resource
    private JxbankService jxbankService;
	/**
     * 设置密码同步回调
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/jxbank/setPayPwd/return")
	public String tppReturn(final JxPasswordSetModel jxModel, final HttpServletRequest request, final Model model) {
	    LOGGER.info("设置密码({})进入同步回调,{}",jxModel.getOrderNo(), JSON.toJSON(jxModel).toString());
	    
	    String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
        // 是否是移动端
        if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {// 移动端
        	model.addAttribute("res_msg", "设置密码处理完成");
            model.addAttribute("res_code", "set_success");
            model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
        	return "/mobileResult";
           
        } else {
        	model.addAttribute("resultUrl", CallbackConstant.AUTH_SIGN_REQUEST_URL);
            model.addAttribute("picType", CallbackConstant.BINDPASS_PICTYPE);
            model.addAttribute("requestType", CallbackConstant.BINDPASS_REQUEST_TYPE);
            model.addAttribute("resultName", CallbackConstant.AUTH_SIGN_REQUEST_NAME);
            return "/retResult";
        }
		
	}

	/**
	 * 设置密码异步回调
	 * @throws Exception
	 */
	@RequestMapping(value = "/jxbank/setPayPwd/notify")
	public void tppNotify(final String bgData, final HttpServletRequest request, final HttpServletResponse response){
		LOGGER.info("设置密码({})进入异步回调", getRequestParams(request));
		final JxPasswordSetModel model = JSON.parseObject(bgData, JxPasswordSetModel.class);
		if (model.responseVerify()) {
			jxbankService.setPwd(model);
		} else {
			LOGGER.error("验签失败：{}", model.getOrderNo());
		}
		printSuccessReturn(response);
	}
}

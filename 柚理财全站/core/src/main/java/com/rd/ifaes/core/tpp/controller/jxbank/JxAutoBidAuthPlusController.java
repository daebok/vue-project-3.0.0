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
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;

@Controller
public class JxAutoBidAuthPlusController extends BaseController {

	@Resource
	private transient UserAuthSignLogService userAuthSignLogService;

	/**
	 * 投资人自动投标签约增强同步回调
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/jxbank/autoBidAuthPlus/return")
	public String tppReturn(final JxAutoBidAuthPlusModel jxModel, final HttpServletRequest request, final Model model) {
		LOGGER.info("投资人自动投标签约增强({})进入同步回调,{}", jxModel.getOrderNo(), getRequestParams(request));
		// userAuthSignLogService.doAuth(jxModel);
		String userAgent = request.getHeader("User-Agent").toLowerCase();// 判断访问来源
		// 是否是移动端
		if (userAgent.contains("micromessenger") || userAgent.contains("android") || userAgent.contains("iphone")) {// 移动端
			model.addAttribute("mobile_url", ConfigUtils.getValue(ConfigConstant.MOBILE_URL));
			model.addAttribute("res_msg", "自动投标签约处理完成");
			model.addAttribute("res_code", "autoBidAuth_success");
			return "/mobileResult";

		} else {
			model.addAttribute("picType", CallbackConstant.AUTH_SIGN_PICTYPE);
			model.addAttribute("requestType", CallbackConstant.AUTH_SIGN_REQUEST_TYPE);
			model.addAttribute("resultUrl", CallbackConstant.AUTH_SIGN_REQUEST_URL);
			model.addAttribute("resultName", CallbackConstant.AUTH_SIGN_REQUEST_NAME);
			return "/retResult";
		}

	}

	/**
	 * 投资人自动投标签约增强异步回调
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/jxbank/autoBidAuthPlus/notify")
	public void tppNotify(final String bgData, final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("投资人自动投标签约增强({})进入异步回调", getRequestParams(request));
		final JxAutoBidAuthPlusModel model = JSON.parseObject(bgData, JxAutoBidAuthPlusModel.class);
		// ufxCashService.doCash(model);
		if (model.responseVerify()) {
			userAuthSignLogService.doAuth(model);
		} else {
			LOGGER.error("验签失败：{}", model.getOrderNo());
		}
		printSuccessReturn(response);
	}
}

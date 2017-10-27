package com.rd.ifaes.core.tpp.controller.ufx;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.CallbackConstant;
import com.rd.ifaes.core.tpp.model.ufx.UfxAuthSignModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;

/**
 * 用户授权回调
 * 
 * @author yinliang
 * @version 2.3.0.0
 * @date 2015年12月31日 下午2:49:30 Copyright 杭州融都科技股份有限公司 P2P All Rights Reserved
 *       官方网站：www.erongdu.com 研发中心：rdc@erongdu.com 未经授权不得进行修改、复制、出售及商业使用
 */
@Controller
public class UfxAuthSignController extends BaseController {

	@Resource
	private transient UserAuthSignLogService userAuthSignLogService;

	/**
	 * 
	 * 授权同步回调处理
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月26日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ufx/authSign/return")
	public String tppReturn(final UfxAuthSignModel sign, final HttpServletRequest request, final Model model) {
		LOGGER.info("授权进入同步回调，回调参数：{}", getRequestParams(request));
		userAuthSignLogService.doAuth(sign);
		 String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是移动端
		 if(userAgent.contains("micromessenger")||userAgent.contains("android")||userAgent.contains("iphone")){//移动端
						model.addAttribute("res_msg", "授权处理完成");
						model.addAttribute("res_code", "auth_success");
						return "/mobileResult";
			}else{
		model.addAttribute("picType", CallbackConstant.AUTH_SIGN_PICTYPE);
		model.addAttribute("requestType", CallbackConstant.AUTH_SIGN_REQUEST_TYPE);
		model.addAttribute("resultName", CallbackConstant.AUTH_SIGN_REQUEST_NAME);
		model.addAttribute("resultUrl", CallbackConstant.AUTH_SIGN_REQUEST_URL);
		return "/retResult";
			}
	}

	/**
	 * 
	 * 授权异步回调处理
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月26日
	 * @param model
	 */
	@RequestMapping(value = "/ufx/authSign/notify")
	public void tppNotify(final UfxAuthSignModel model, final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("授权进入异步回调，回调参数：{}", getRequestParams(request));
		userAuthSignLogService.doAuth(model);
		printSuccessReturn(response);
	}

}

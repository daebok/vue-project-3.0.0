/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.mobile.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.user.UserController;
import com.rd.ifaes.mobile.model.AppResponse;
import com.rd.ifaes.mobile.util.JsonPrinter;

/**
 * 令牌校验
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月3日
 */
public class TokenValidInterceptor extends HandlerInterceptorAdapter {
	/**
	 * 日志记录器
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private JsonPrinter jsonPrinter = new JsonPrinter(this.LOGGER );
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			// 判断方法是否已申明token验证（@TokenValid）
			final TokenValid tokenValid = method.getMethodAnnotation(TokenValid.class);
			if (tokenValid != null) {
				// 令牌名称
				final String tokenName = tokenValid.value();
				// 用户提交令牌
				final String tokenInput = TokenUtils.getInputToken(request, tokenName);
				final Session session = SessionUtils.getSession();
				if (tokenValid.dispatch()) {
					// 传递用户令牌到service层
					session.setAttribute(tokenName + TokenUtils.INPUT_TOKEN_SUFFIX, tokenInput);
					session.setAttribute(tokenName, tokenInput);
				}
				// 比对令牌
				if (tokenValid.valid()) {
					if (ObjectUtils.notEqual(tokenInput, session.getAttribute(tokenName))) {
						jsonPrinter.printBeanJson( new AppResponse(AppResultCode.ERROR,ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR)) , response);
						return false;
						
					} else if (tokenValid.clear()) {
						TokenUtils.clearShiroToken(tokenName);
					}
				}
			}
		}

		return true;
	}
}
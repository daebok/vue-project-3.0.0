package com.rd.ifaes.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rd.ifaes.common.exception.BussinessException;


/**
 * ajax拦截器 禁止跨域访问 禁止url直接访问
 * @version 3.0
 * @since 2016年4月25日
 */
public class AjaxSafeInterceptor extends BaseInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object obj) throws Exception {

		String requestType = req.getHeader("X-Requested-With");
		if (("XMLHttpRequest").equals(requestType)) {// Ajax请求
			/*String refererUrl = request.getHeader("Referer");
			HttpURL url = new HttpURL(refererUrl);
			if (!request.getServerName().equals(url.getHost())) {// 禁止跨域访问
				message("抱歉，您访问的页面不存在！", null);
				return "msg";
			}*/
		} else {// 禁止url直接访问
			throw new BussinessException("抱歉，您访问的页面不存在！");
		}
		return true;
	}

	
}

package com.rd.ifaes.core.core.web.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
import com.rd.ifaes.core.core.Global;

/**
 * 
 * @author lh
 * @version 3.0
 * @since 2016-06-21
 *
 */
public class ActionInterceptor extends BaseInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ip = IPUtil.getRemortIP(request);
		Global.IP_THREADLOCAL.set(ip);
		return super.preHandle(request, response, handler);
	}

	
}

package com.rd.ifaes.common.interpolator;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InterceptorEnum;
import com.rd.ifaes.common.dict.LogEnum;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.Log;
import com.rd.ifaes.core.sys.service.LogService;

/**
 * 日志记录Interceptor
 * 
 * @author lh
 * @version 3.0
 * @since 2016-09-21
 */
public class LogFilterInterceptor extends BaseInterceptor {

	private static final String ACCESS_TIME_TAG_START = "start";
	/**
	 * 
	 * 无需过滤的uri <br>
	 * 1、登录; 
	 * 
	 */
	private static final String[] NO_FILTER_URI = { InterceptorEnum.LOGIN_URL_MANAGE.getValue(),InterceptorEnum.SYS_EXPORT_PROGRESS.getValue()};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();
		if (checkUri(uri)) {
			String sessionId = request.getSession().getId();
			String startKey = sessionId + ":" + uri + ":" + ACCESS_TIME_TAG_START;
			CacheUtils.set(startKey, System.currentTimeMillis(), ExpireTime.ONE_MIN);
		}

		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String uri = request.getRequestURI();
		
		if (checkUri(uri) && handler instanceof HandlerMethod) {
			String sessionId = request.getSession().getId();
			String method = null;
			String operationContent = null;
			String ip = IPUtil.getRemortIP(request);
			SystemLog sysLog = ((HandlerMethod)handler).getMethod().getAnnotation(SystemLog.class);
			if(sysLog!=null){
				method = sysLog.method();
				operationContent = sysLog.content();
			}
			String startKey = sessionId + ":" + uri + ":" + ACCESS_TIME_TAG_START;
			double takeTime = 0;
			long startTime = CacheUtils.getLong(startKey);
			if (startTime > 0L) {
				long endTime = System.currentTimeMillis();
				takeTime = (endTime - startTime) / 1000.0d;
			}

			StringBuilder params = new StringBuilder();
			Enumeration<String> enums = request.getParameterNames();
			while (enums.hasMoreElements()) {
				String name = enums.nextElement();
				params.append(name).append("=").append(request.getParameter(name)).append("&");
			}
			
			
			String exception = ex == null ? null : ex.getMessage();
			String remark = "耗时：" + takeTime + "s";

			Log log = new Log(LogEnum.LOG_TYPE_SYS_ACCESS.getValue(), ip, uri, method, operationContent,
				params.length()>1024 ?(params.substring(0, 1024)+"..."):params.toString(),exception, DateUtils.getNow(), remark,PrincipalUtils.getCurrUserId(), takeTime);
			LogService logService = SpringContextHolder.getBean("logService");
			logService.save(log);

		}

		super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * 检查uri
	 * 
	 * @param uri
	 * @return
	 */
	private boolean checkUri(String uri) {
		for (String nfu : NO_FILTER_URI) {
			if (nfu.equals(uri)) {
				return false;
			}
		}
		return true;
	}

}

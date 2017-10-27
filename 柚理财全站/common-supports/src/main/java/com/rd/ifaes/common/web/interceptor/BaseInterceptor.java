package com.rd.ifaes.common.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.common.util.StringUtils;

/**
 * @version 3.0
 * @since 2016-04-25
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);
	
	private static final String BACK_URL_KEY = "backurl";
	private static final String BACK_URL_VAL = "<a href='javascript:history.go(-1)'>返回</a>";
	public static final String RESPONSE_TYPE_JSON = "application/json";
	
	
	protected void message(String msg, String url, String text) {
		Map<String, Object> data = new HashMap<>();
		data.put("msg", msg);
		data.put(BACK_URL_KEY, "<a href=" + getRequest().getContextPath() + url + " >" + text + "</a>");
		printJson(JsonMapper.toJsonString(data));
		
	}

	protected void message(String msg, String url) {
		String urltext ;
		if (StringUtils.isNotBlank(url)) {
			urltext = "<a href=" + getRequest().getContextPath() + url + " >返回</a>";
		} else {
			urltext = BACK_URL_VAL;
		}
		Map<String, Object> data = new HashMap<>();
		data.put("msg", msg);
		data.put(BACK_URL_KEY, urltext);
		printJson(JsonMapper.toJsonString(data));
	}
	
	protected void message(String msg) {
		this.message(msg, null);
	}
	
	protected HttpServletRequest getRequest(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	protected HttpServletResponse getResponse(){
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	protected Object getParam(String name) {
		return getRequest().getParameter(name);
	}

	protected void printJson(String json)  {
		PrintWriter out = null;
		HttpServletResponse response = getResponse();
		try {
			out = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			response.setContentType( BaseInterceptor.RESPONSE_TYPE_JSON + ";charset=UTF-8");
			out.print(json);
			out.close();
		} catch (IOException e) {
			LOGGER.error("printJson error",e);
		}finally{
			if(out!=null){
				out.close();
			}
		}
	}

}

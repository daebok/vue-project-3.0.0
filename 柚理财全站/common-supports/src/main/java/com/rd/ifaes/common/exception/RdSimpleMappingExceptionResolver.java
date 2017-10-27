package com.rd.ifaes.common.exception;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.constants.ExceptionCodeConstant;

/**
 * 互联网金融交易系统ifaes (Internet Finance Assets Exchange System) Copyright (c) 2016
 * 杭州融都科技股份有限公司 版权所有<br>
 * 
 * 异常拦截处理器
 * 
 * @author xhf
 * @version 3.0
 * @since 2016-6-6
 */
public class RdSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(RdSimpleMappingExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		LOGGER.debug("---request.getHeader()----: X-Requested-With: {} , accept:{}",
				                        request.getHeader("X-Requested-With"), request.getHeader("accept"));
		String viewName = determineViewName(ex, request);

		if (viewName != null) {// if there is an exception
			// AJAX request
			if ((request.getHeader("X-Requested-With") != null
					&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {

				String result = null;
				String msg = getMsg(ex);
				response.setCharacterEncoding("UTF-8");
				if (request.getHeader("accept").indexOf("text/plain") > -1) {// 弹窗view
					LOGGER.debug("-----doResolveException--------text/plain-------------");
					response.setContentType("text/plain;charset=UTF-8");
					result = "exception:" + msg;
				} else if (request.getHeader("accept").indexOf("application/json") > -1 ||
						request.getHeader("accept").indexOf("application/javascript") > -1 ||
						request.getHeader("accept").indexOf("*/*") > -1) {// JSON格式返回
					LOGGER.debug("------doResolveException------application/json------------");
					response.setContentType("application/json;charset=UTF-8");
					Map<String, Object> map = new HashMap<>();
					map.put("result", false);
					map.put("msg", msg);
					if (ex instanceof BussinessException) {
						map.put("url", ((BussinessException) ex).getUrl());
					}
					result = JsonMapper.toJsonString(map);
				}
				if(result!=null){
					try {
						//修正chrome 报错net::ERR_INCOMPLETE_CHUNKED_ENCODING，设定内容长度后，响应头中不会出现：Transfer-Encoding: chunked
						response.setHeader("Content-Length", Integer.toString(result.getBytes("UTF-8").length));
						Writer writer = new OutputStreamWriter(response.getOutputStream(), Charset.forName("UTF-8"));
						writer.write(result);
						writer.flush();
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
			    }else{
			    	LOGGER.warn("request  header : 'accept' is not right ,please check : text/json!", ex);
			  }
			} else {
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				if (ex instanceof BussinessException) {
					message(request, (BussinessException) ex);
				}
				return getModelAndView(viewName, ex, request);
			}

		}
		return null;

	}

	private static List<String> getMatcher(String regex, String source) {
		List<String> list = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	public void message(HttpServletRequest request, BussinessException e) {
		String urlback = null;
		if (e.getType() == BussinessException.TYPE_JSON) {
			final StringBuilder btnBuffer = new StringBuilder();
			btnBuffer.append("<a href='")
					.append(StringUtils.isNotBlank(e.getUrl()) ? e.getUrl() : "javascript:history.go(-1)").append("'>")
					.append(StringUtils.isNotBlank(e.getButtonName()) ? e.getButtonName() : "返回").append("</a>");
			urlback = btnBuffer.toString();
		} else {
			urlback = "<a href='javascript:window.close()'>关闭</a>";
		}

		request.setAttribute("backurl", urlback);
		request.setAttribute("rsmsg", StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "");
	}

	private static String getMsg(String source) {
		String regex = "(?<=\\')(.+?)(?=\\')";
		List<String> properties = getMatcher(regex, source);
		StringBuilder propBuilder = new StringBuilder();
		for (String prop : properties) {
			propBuilder.append(prop).append(",");
		}
		String msg = "输入参数";
		if (propBuilder.length() > 0) {
			msg += "[" + propBuilder.deleteCharAt(propBuilder.length() - 1) + "]";
		}
		msg += "非法";
		return msg;
	}

	/**
	 * 取得异常信息
	 * 
	 * @param ex
	 * @return
	 */
	private static String getMsg(Exception ex) {
		String msg = ex.getMessage();
		if (ex instanceof IllegalArgumentException || ex instanceof BindException) {
			msg = getMsg(msg);
		} else if (ex instanceof NullPointerException) {
			msg = (msg == null || "".equals(msg)) ? "程序响应异常，请联系系统管理员！" : msg;
		} else if (ex instanceof UnauthorizedException) {
			msg = "没有访问权限";
		} else if (StringUtils.isBlank(msg)) {
			msg = "程序响应异常，请联系系统管理员！";
		}

		String code = ExceptionCodeConstant.getCodeByExceptionName(ex.getClass().getSimpleName());
		if (StringUtils.isNotBlank(code)) {
			msg += StringUtils.format("(错误代码：%s)", code);
		}
		//LOGGER.error(msg, ex);
		return msg;
	}

}
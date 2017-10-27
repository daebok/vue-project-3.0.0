 package com.rd.ifaes.core.base.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-3-23
 * @notice 未经授权不得进行修改、复制、出售及商业使用
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	/**
	 *  插入隐藏域模板
	 */
	protected static final String TOKEN_INPUT = "<input type='hidden' name='%s' value='%s'/>";
	/**
	 * JSON输出：结果标识
	 */
	protected static final String RESULT_NAME = "result";
	/**
	 * JSON输出：结果标识
	 */
	protected static final String DATA_NAME = "data";
	/**
	 * JSON输出：结果标识
	 */
	protected static final String URL = "url";
	/**
	 * 查询无数据，操作数据库无更新
	 */
	protected static final int NO_RESULT = 0;
	/**
	 *  JSON输出：分页数据属性名称
	 */
	protected static final String  RESULT_PAGE = "page";
	/**
	 * 跳转的tab切换按钮
	 */
	protected static final String BUTTON =  "button";
	/**
	 *  JSON输出：列表数据属性名称
	 */
	protected static final String  RESULT_LIST = "list";
	/**
	 * JSON输出：提示信息
	 */
	protected static final String MSG_NAME = "msg";
	protected static final String MSG_SUCCESS = ResourceUtils.get("global.msg.success");
	protected static final String MSG_FAIL = ResourceUtils.get("global.msg.fail");
	
	private static final String ERROR_400 = "error/400";
	private static final String ERROR_403 = "error/403";
	
	public static final String RESPONSE_TYPE_JSON = "application/json";
	
 
	/**
	 * 回调打印，用于响应UFX
	 */
	protected void printSuccessReturn(final HttpServletResponse response) {
		try {
			response.reset();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(UfxConstant.UFX_NOTIFY_RECEIVED_SUCCESS);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 参数绑定异常
	 * @return
	 */
	@ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {  
        return ERROR_400;
    }
	
	/**
	 * 授权登录异常
	 * @return
	 */
	@ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {  
        return ERROR_403;
    }
	
	/**
	 * 处理结果返回集
	 * @author QianPengZhan
	 * @date 2016年8月2日
	 * @param result
	 * @param msg
	 * @param url
	 * @return
	 */
	protected Map<String, Object> renderResult(boolean result,String msg,String url) {
		Map<String, Object> rs = Maps.newHashMap();
		rs.put(RESULT_NAME, result);
		rs.put(MSG_NAME, msg);
		rs.put(URL, url);
		return rs;
	}
	
	protected Map<String, Object> renderResult(boolean result,String msg,String url,String button) {
		Map<String, Object> rs = Maps.newHashMap();
		rs.put(RESULT_NAME, result);
		rs.put(MSG_NAME, msg);
		rs.put(URL, url);
		rs.put(BUTTON, button);
		return rs;
	}
	
	
	/**
	 * 处理结果
	 * @param response
	 * @param result	是否成功
	 * @param msg		消息
	 * @return
	 */
	protected Map<String, Object> renderResult(final boolean result, final String msg) {
		final Map<String, Object> rtn = Maps.newHashMap();
		rtn.put(RESULT_NAME, result);
		rtn.put(MSG_NAME, msg);
		return rtn;
	}
	
	/**
	 * 处理结果
	 * @param response
	 * @param result	是否成功
	 * @param msg		消息
	 * @return
	 */
	protected Map<String, Object> renderSuccessResult() {
		final Map<String, Object> rtn = Maps.newHashMap();
		rtn.put(RESULT_NAME, true);
		rtn.put(MSG_NAME, MSG_SUCCESS);
		return rtn;
	}

	/**
	 * 取得认证信息
	 * @return
	 */
	protected Object getPrincipal(){
		final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		if(principals == null){
			return null;
		}
		return principals.getPrimaryPrincipal();		
	}
	
	/**
	 * 取得错误信息
	 * @param bindingResult
	 * @return
	 */
	public String getBindErrors(final BindingResult bindingResult){
		final StringBuilder errorStr = new StringBuilder();
		final List<ObjectError> errors = bindingResult.getAllErrors();
		final int errorLen = errors.size()-1;
		for (int i = errorLen; i >=0 ; i--) {
			errorStr.append(errors.get(i).getDefaultMessage()).append("<br>");
		}
		return errorStr.substring(0, errorStr.length()-4);		
	}

	/**
	 * 校验出现错误，返回错误信息
	 * @param bindingResult
	 * @return
	 */
	public Map<String, Object> renderBindingResult(final BindingResult bindingResult){
		if (bindingResult.hasErrors()) {  
	    	return renderResult(false, getBindErrors(bindingResult));
	    }
		return null;		
	}
	
	public String getUserId(){
		return PrincipalUtils.getCurrUserId();
	}
	
	/**
	 * 回调参数拼接共用方法
	 * 
	 * @return
	 */
	public String getRequestParams(final HttpServletRequest request) {
		StringBuilder params = new StringBuilder();
		try {
			@SuppressWarnings("rawtypes")
			final Enumeration e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				params.append(parName).append("=").append(value).append("&");
			}
		} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
		}
		return params.toString();
	}	
	
	
	/**
	 * 回调参数拼接共用方法  渤海银行
	 * 
	 * @return
	 */
	public Map<String,String> getRequestMap(final HttpServletRequest request) {
		Map<String,String> map = new HashMap<String, String>();
		try {
			@SuppressWarnings("rawtypes")
			final Enumeration e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				map.put(parName, value);
			}
		} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
		}
		return map;
	}	
	
	
	/**
	 * 过滤参数json串中的空值，避免数据库存储太多无用数据
	 * @param object
	 * @return
	 */
	protected String reBuildJson(Object object) {
		String jsonStr = JSONObject.toJSONString(object);
		return StringUtils.reBuildJson(jsonStr);
	}
	
}

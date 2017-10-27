 package com.rd.ifaes.common.security.shiro;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 表单验证（包含验证码）过滤类
 * @version 2014-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormAuthenticationFilter.class);

	public static final String DEFAULT_CAPTCHA_PARAM = "validCode";
	public static final String DEFAULT_MESSAGE_PARAM = "message";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = StringUtils.isNull(getPassword(request));
		boolean rememberMe = isRememberMe(request);
		String host = StringUtils.getRemoteAddr((HttpServletRequest)request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}
 
	
	public String getMessageParam() {
		return messageParam;
	}
	
	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request, ServletResponse response) {
		String message =  "用户或密码错误, 登录失败请重试！";
		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
    	String userName = uptoken.getUsername();
        //retry count + 1
		@SuppressWarnings("unchecked")
		Map<String, AtomicInteger> loginFailMap =  (Map<String, AtomicInteger>)CacheUtils.get("loginFailMap", Map.class);
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
		}
		AtomicInteger loginFailNum = loginFailMap.get(userName);	
		if(loginFailNum==null){
			loginFailNum = new AtomicInteger(0);
		}
		int maxErrorCount = ConfigUtils.getInt(Constant.LOGIN_LOCK_NUMBER);//获取系统配置最大登录次数
		if(maxErrorCount>2 && (maxErrorCount-2)==loginFailNum.get()){
			message = "用户名或密码错误，您还可以输入两次"; 
		}
		if(maxErrorCount>1 && (maxErrorCount-1)==loginFailNum.get()){
			message = "用户名或密码错误，您还可以输入一次"; 
		}
		if(maxErrorCount<=loginFailNum.get()){
			message = "账号被锁定，24小时后解锁，可联系管理人员提前解锁";
			loginFailMap.remove(userName);//清掉最大登录次数
			CacheUtils.set("loginFailMap", loginFailMap, ExpireTime.ONE_DAY);
		}
        request.setAttribute(getFailureKeyAttribute(), e.getClass().getName());
        request.setAttribute(getMessageParam(), message);
        return true;
	}
	
}
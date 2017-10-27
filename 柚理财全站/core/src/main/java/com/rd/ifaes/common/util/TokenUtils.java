package com.rd.ifaes.common.util;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.exception.TokenException;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 表单重复提交，令牌处理工具类
 * 
 * @author fangjun
 * @date 2015年6月25日 下午3:21:28
 */
public abstract class TokenUtils {
	
	private TokenUtils() {	}

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

	/**
	 * 转存在session中的用户提交的token，统一后缀
	 */
	public static final String INPUT_TOKEN_SUFFIX = "_INPUT";

	/**
	 * 随机数工具
	 */
	private static final Random RANDOM = new Random();

	/**
	 * 生成一个唯一值的令牌
	 * 
	 * @author fangjun
	 * @Date 2015年6月25日 下午3:21:03
	 * @return
	 */
	private static String generateGUID() {
		return new BigInteger(165, RANDOM).toString(36).toUpperCase();
	}

	/**
	 * Sets a transaction token into the session based on the provided token
	 * name.
	 *
	 * @param tokenName the token name based on which a generated token value is
	 *            stored into session; for actual session store, this name will
	 *            be prefixed by a namespace.
	 *
	 * @return the token string
	 */
	public static String setToken(final HttpSession session, final String tokenName) {
		String token = generateGUID();
		session.setAttribute(tokenName, token);
		return token;
	}

	/**
	 * Gets the Token value from the params in the session using the given name
	 *
	 * @param tokenName the name of the parameter which holds the token value
	 * @return the token String or null, if the token could not be found
	 */
	public static String getToken(final HttpSession session, final String tokenName) {
		if (StringUtils.isBlank(tokenName)) {
			LOGGER.debug("token name is null!");
			return null;
		}
		return (String) session.getAttribute(tokenName);
	}

	/**
	 * 验证表单token值和session中的token值是否一致
	 * 
	 * @author fangjun
	 * @Date 2015年6月25日 下午3:21:40
	 * @param request
	 * @return 一致返回true,反之
	 */
	public static boolean validToken(final HttpServletRequest request, final String tokenName) {
		if (StringUtils.isBlank(tokenName)) {
			LOGGER.debug("token name is null!");
			return false;
		}

		final String inputToken = getInputToken(request, tokenName);
		final HttpSession session = request.getSession();
		final String sessionToken = (String) session.getAttribute(tokenName);
		return validToken(inputToken, sessionToken);
	}

	/**
	 * 比对用户提交token和会话中的token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param inputToken 用户提交token
	 * @param sessionToken 会话中的token
	 * @return 两者一致返回true,不一致或者两者一个为空，返回false
	 */
	private static boolean validToken(final String inputToken, final String sessionToken) {
		if (StringUtils.isNotBlank(inputToken) && inputToken.equals(sessionToken)) {
			return true;
		} else {
			LOGGER.warn("token is not valid!inputToken='{}',sessionToken = '{}'", inputToken, sessionToken);
		}
		return false;
	}

	/**
	 * 清除会话中指定token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param request
	 * @param tokenName token名称
	 */
	public static void clearToken(final HttpServletRequest request, final String tokenName) {
		clearToken(request.getSession(), tokenName);
	}

	/**
	 * 校验shiro会话中指定token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param request
	 * @param tokenName token名称
	 */
	public static void validShiroToken(final String tokenName) {
		if (null == SessionUtils.getSession(false)) {
			throw new BussinessException("please login first!");
		}
		final String sessionToken = getShiroToken(tokenName);
		final String inputToken = getShiroToken(tokenName + INPUT_TOKEN_SUFFIX);
		final boolean result = validToken(inputToken, sessionToken);
		LOGGER.info("[token校验清除]:tokenName={},result={},sessionToken={},inputToken={}",tokenName,result,sessionToken,inputToken);
		clearShiroToken(tokenName + INPUT_TOKEN_SUFFIX);
		if (result) {
			clearShiroToken(tokenName); 
		} else {
			throw new TokenException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), TokenException.TYPE_JSON);
		}
	}

	/**
	 * 获取shiro会话中指定token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param request
	 * @param tokenName token名称
	 */
	private static String getShiroToken(final String tokenName) {
		final Session session = SessionUtils.getSession();
		final Object token = session.getAttribute(tokenName);
		return null!=token?token.toString():StringUtils.EMPTY;
	}

	/**
	 * 清除shiro会话中的token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param tokenName token名称
	 */
	public static void clearShiroToken(final String tokenName) {
		final Session session = SessionUtils.getSession(false);
		if (session != null && null == session.removeAttribute(tokenName)){
			throw new TokenException(ResourceUtils.get(ResourceConstant.REPEAT_SUBMIT_ERROR), TokenException.TYPE_JSON);
		}
	}

	/**
	 * 清除会话中指定token
	 * 
	 * @author FangJun
	 * @date 2016年8月3日
	 * @param request
	 * @param tokenName token名称
	 */
	public static void clearToken(final HttpSession session, final String tokenName) {
		session.removeAttribute(tokenName);
		LOGGER.debug("Token ({}) has cleared !", tokenName);
	}

	/**
	 * 获取表单中token值
	 * 
	 * @author fangjun
	 * @Date 2015年6月25日 下午3:20:35
	 * @param request
	 * @return 表单提交中的token值
	 */
	public static String getInputToken(final HttpServletRequest request, final String tokenName) {
		String token = null;
		Map<String, String[]> params = request.getParameterMap();

		if (!params.containsKey(tokenName)) {
			LOGGER.debug("Could not find token name in params.");
		}

		final String[] tokens = params.get(tokenName);
		if (ArrayUtils.isEmpty(tokens)) {
			LOGGER.warn("Could not find token mapped to token name {} ", tokenName);
			return null;
		} else {
			token = tokens[0];
		}
		return token;
	}

}

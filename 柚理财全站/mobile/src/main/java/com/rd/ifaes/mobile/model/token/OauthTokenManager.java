package com.rd.ifaes.mobile.model.token;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.common.AppCommons;
import com.rd.ifaes.mobile.common.AppDateUtil;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.common.MD5Generator;
import com.rd.ifaes.mobile.rdtokenstore.domain.TokenStore;
import com.rd.ifaes.mobile.rdtokenstore.service.TokenStoreService;

/**
 * token管理类（JDBC）
 * 
 * @author Kunkka
 * 
 *
 */
public class OauthTokenManager {
	private TokenStoreService rdTokenStoreService;
	private UserService userService;
	public OauthTokenManager() {
		rdTokenStoreService = (TokenStoreService)SpringContextHolder.getBean("rdTokenStoreService");
		userService = (UserService)SpringContextHolder.getBean("userService");
	}
	
	public void removeOldToken(String userId) {
		this.rdTokenStoreService.removeOldToken(userId);
	}

	/**
	 * 根据oauth_token查询token是否存在，存在则认为正常访问
	 * 
	 * @param oauth_token
	 * @return
	 */
	public OauthAccessToken getOauthTokenStore(String userId , String clientType,String oauth_token) {
		/*TokenStore entity = this.rdTokenStoreService.getOauthTokenStore(oauth_token);
		if (entity == null) {*/
		TokenStore	entity = this.rdTokenStoreService.getOauthTokenStoreByUserId(userId, clientType,oauth_token);
			if( entity == null ){
				/*	throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "token不存在");
			else*/
				throw new AppException(AppResultCode.TOKEN_NOT_UNIQUE, "账号已从其他设备登录");
		}
		long token_addtime = entity.getAddTime().getTime() / 1000;
		long token_expires_in = Long.parseLong(entity.getExpiresIn());
		if (token_addtime + token_expires_in < System.currentTimeMillis() / 1000) {
			throw new AppException(AppResultCode.TOKEN_REFRESH_TIMEOUT, "长时间不在线，请重新登录");
		}
		OauthAccessToken accessToken = new OauthAccessToken();
		try {
			BeanUtils.copyProperties(accessToken, entity);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	/**
	 * 根据refresh_token获取token
	 * 
	 * @param refresh_token
	 * @return
	 */
	public OauthRefreshToken getRefreshTokenStore(String refresh_token,String userId) {
		TokenStore entity = this.rdTokenStoreService.getRefreshTokenStore(userId,refresh_token);
		if (entity == null) {
			throw new AppException(AppResultCode.TOKEN_NOT_EXIT, "token不存在");
		}
		long token_addtime = entity.getAddTime().getTime() / 1000;
		long token_expires_in = Long.parseLong(entity.getExpiresIn());
		if (token_addtime + 3 * token_expires_in < System.currentTimeMillis() / 1000) {
			this.rdTokenStoreService.removeOldToken(entity.getBindingId());
			throw new AppException(AppResultCode.TOKEN_REFRESH_TIMEOUT, "长时间不在线，请重新登录");
		}
		User user = userService.get(entity.getBindingId());
		if (user==null) {
			throw new AppException(AppResultCode.ERROR, "用户不存在");
		}
		if ("1".equals(user.getStatus())) {
			throw new AppException(AppResultCode.ERROR, "该账户已被锁定");
		}

		OauthRefreshToken refreshBean = new OauthRefreshToken();
		try {
			BeanUtils.copyProperties(refreshBean, entity);
			refreshBean.setAccess_token(entity.getOauthToken());
			refreshBean.setRefresh_token(entity.getRefreshToken());
			refreshBean.setExpires_in(entity.getExpiresIn());
			refreshBean.setStore_time(AppDateUtil.getTimes(entity.getAddTime()));
			refreshBean.setBindingId(entity.getBindingId());
			refreshBean.setUser_id(entity.getBindingId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return refreshBean;
	}

	/**
	 * 保存token
	 * 
	 * @param userId
	 * @param token_type
	 * @return
	 */
	public OauthAccessToken saveToken(String token_type,String oauth_token,User user) {
		MD5Generator oauthmdt5 = new MD5Generator();
		if("refresh".equals(oauth_token)){//如果是刷新token 就判断sid是否为空 
			//if(SessionUtils.getSessionAttr(Constant.SESSION_USER)==null){//查询当前用户  如果为空就重新登录
				 userService.loginSucess(user);
				 oauth_token=(String) SessionUtils.getSession().getId();//重新登录获取新的sid 
	/*		}else{
				oauth_token=(String) SessionUtils.getSession().getId();//有当前用户 直接从缓存中取 当前的sid
			}*/
			}
		String refresh_token = oauthmdt5.generateValue(oauth_token);
		TokenStore entity = new TokenStore();
		entity.setExpiresIn(AppCommons.TOKEN_EFFECTIVE_TIME);
		entity.setOauthToken(oauth_token);
		entity.setRefreshToken(refresh_token);
		entity.setBindingId(String.valueOf(user.getUuid()));
		entity.setAddTime(new Date());
		if (StringUtils.isEmpty(token_type)) {
			token_type = "mobile";
		}
		entity.setClientId(token_type);
		this.rdTokenStoreService.insert(entity);

		OauthAccessToken oauthToken = new OauthAccessToken();
		try {
			oauthToken.setBindingId(entity.getBindingId());
			oauthToken.setExpiresIn(entity.getExpiresIn());
			oauthToken.set__sid(entity.getOauthToken());
			oauthToken.setRefreshToken(entity.getRefreshToken());
			oauthToken.setUserId(entity.getBindingId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oauthToken;
	}
}

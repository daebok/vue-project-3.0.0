package com.rd.ifaes.mobile.rdtokenstore.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

public class TokenStore extends BaseEntity<TokenStore> {

	private static final long serialVersionUID = 1L;
	/** taoken值 */ 
	private String oauthToken;
	/** 刷新token值 */ 
	private String refreshToken;
	/** 有效时间 */ 
	private String expiresIn;
	/** token来源 */ 
	private String clientId;
	/** 对应用户 */ 
	private String bindingId;
	/** 添加时间 */ 
	private Date addTime;

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getOauthToken() {
		return this.oauthToken;
	}

	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getExpiresIn() {
		return this.expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getBindingId() {
		return this.bindingId;
	}

	public void setBindingId(String bindingId) {
		this.bindingId = bindingId;
	}
}

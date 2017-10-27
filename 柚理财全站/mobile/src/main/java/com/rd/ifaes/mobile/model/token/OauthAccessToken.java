package com.rd.ifaes.mobile.model.token;

public class OauthAccessToken {
	private String __sid;
	private String refreshToken;
	public String get__sid() {
		return __sid;
	}

	public void set__sid(String __sid) {
		this.__sid = __sid;
	}

	private String expiresIn;
	private String tokenType;
	private String bindingId;
	private String userId;
	public String getPwdResetFlag() {
		return pwdResetFlag;
	}

	public void setPwdResetFlag(String pwdResetFlag) {
		this.pwdResetFlag = pwdResetFlag;
	}

	private String pwdResetFlag;
	public String getHideUserName() {
		return hideUserName;
	}

	public void setHideUserName(String hideUserName) {
		this.hideUserName = hideUserName;
	}

	private String username;
	private String hideUserName;

	public String getAvatarPhoto() {
		return avatarPhoto;
	}

	public void setAvatarPhoto(String avatarPhoto) {
		this.avatarPhoto = avatarPhoto;
	}

	/** 
	 * 头像照片路径 
	 */ 
	private String	avatarPhoto;


	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getBindingId() {
		return bindingId;
	}

	public void setBindingId(String bindingId) {
		this.bindingId = bindingId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

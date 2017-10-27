package com.rd.ifaes.mobile.model.token;

public class OauthRefreshToken {
	private String access_token;
	private String refresh_token;
	private String store_time;
	private String expires_in;
	private String bindingId;
	private String user_id;

	public String getBindingId() {
		return this.bindingId;
	}

	public void setBindingId(String bindingId) {
		this.bindingId = bindingId;
	}

	public String getExpires_in() {
		return this.expires_in;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return this.refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getStore_time() {
		return this.store_time;
	}

	public void setStore_time(String store_time) {
		this.store_time = store_time;
	}

	public String getAccess_token() {
		return this.access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}

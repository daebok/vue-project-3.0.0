package com.rd.ifaes.mobile.model.account;


/**
 * @author shenliang
 * 用户状态信息
 */
public class AccountSecurityInfoModel {
	
	private String authorizated;
	/** 
	 * 用户资金存管平台账户号(汇付天下账号如：xxx_2016051150021 ) 
	 */ 
	private String	tppUserAccId;	
	private String hideUserName;
	public String getHideUserName() {
		return hideUserName;
	}

	public void setHideUserName(String hideUserName) {
		this.hideUserName = hideUserName;
	}

	public String getTppUserAccId() {
		return tppUserAccId;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public void setTppUserAccId(String tppUserAccId) {
		this.tppUserAccId = tppUserAccId;
	}
	/**
	 * Vip等级 1,2,3等 
	 */
	private String	vipLevel;	
	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	/** 
	 * 风险评估等级(数字等级) 
	 */ 
	private String	riskLevel;	
	public String getVipGift() {
		return vipGift;
	}

	public void setVipGift(String vipGift) {
		this.vipGift = vipGift;
	}
	/** 
	 * 是否有未领取礼包 1 有 0 无
	 */ 
	private String	vipGift;	
	/** 
	 * 风险评估等级(数字等级) 说明 
	 */ 
	private String	riskLevelStr;	
	public String getRiskLevelStr() {
		return riskLevelStr;
	}

	public void setRiskLevelStr(String riskLevelStr) {
		this.riskLevelStr = riskLevelStr;
	}
	/**
	 * 用户名
	 */
	private String username;
	public String getMobilePhoneStatus() {
		return mobilePhoneStatus;
	}

	public void setMobilePhoneStatus(String mobilePhoneStatus) {
		this.mobilePhoneStatus = mobilePhoneStatus;
	}

	/** 
	 * 手机认证 -1:未通过,0:未认证,1:已认证 
	 */ 
	private String	mobilePhoneStatus;	

	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 手机号
	 */
	private String	mobile;	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/** 
	 * 实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过 
	 */
	private String realnameStatus;
	/**
	 * 姓名
	 */
	private String realname;
	
	/** 
	 * 邮箱认证 0:未认证 1：邮箱认证通过 
	 */ 
	private String	emailStatus;	
	/**
	 * 是否设置交易密码 1:设置 0：未设置
	 */
	private int hasSetPayPwd;
	/**
	 * 银行卡个数
	 */
	private int bankNum;
	/** 
	 * 头像照片路径
	  */ 
	private String	avatarPhoto;
	public String getAvatarPhoto() {
		return avatarPhoto;
	}

	public void setAvatarPhoto(String avatarPhoto) {
		this.avatarPhoto = avatarPhoto;
	}

	public String getAuthorizated() {
		return authorizated;
	}

	public void setAuthorizated(String authorizated) {
		this.authorizated = authorizated;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRealnameStatus() {
		return realnameStatus;
	}

	public void setRealnameStatus(String realnameStatus) {
		this.realnameStatus = realnameStatus;
	}
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	public int getHasSetPayPwd() {
		return hasSetPayPwd;
	}

	public void setHasSetPayPwd(int hasSetPayPwd) {
		this.hasSetPayPwd = hasSetPayPwd;
	}
	public int getBankNum() {
		return bankNum;
	}
	public void setBankNum(int bankNum) {
		this.bankNum = bankNum;
	}
}

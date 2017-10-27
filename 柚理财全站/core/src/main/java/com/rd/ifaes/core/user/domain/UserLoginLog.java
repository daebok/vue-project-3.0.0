package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserLoginLog
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserLoginLog extends BaseEntity<UserLoginLog> {
	
	private static final long serialVersionUID = 1L;
	
	/** 用户ID */ 
	private String	userId;		 
	/** 登录时间 */ 
	private Date	loginTime;	
	/** 登录IP */ 
	private String	loginIp;		

	//其他自定义属性
	private String userName ;
	private String realName ;
	private String mobile ;
	private String email ;
	private String userNature ;

	/**
	 * 构造函数
	 */
	public UserLoginLog() {
		super();
	}

	/**
	 * 构造函数
	 */
	public UserLoginLog(final String uuid, final String userId, final Date loginTime, final String loginIp) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.loginTime = loginTime;
		this.loginIp = loginIp;
	}

	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取登录时间
	 * @return loginTime
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/**
	 * 设置登录时间
	 * @param  loginTime
	 */
	public void setLoginTime(final Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 获取登录IP
	 * @return loginIp
	 */
	public String getLoginIp() {
		return loginIp;
	}

	/**
	 * 设置登录IP
	 * @param  loginIp
	 */
	public void setLoginIp(final String loginIp) {
		this.loginIp = loginIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserNature() {
		return userNature;
	}

	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserLoginLog [" + "uuid=" + uuid + ", userId=" + userId + ", loginTime=" + loginTime + ", loginIp=" + loginIp +  "]";
	}
}

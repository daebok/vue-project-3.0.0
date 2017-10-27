package com.rd.ifaes.core.user.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.core.user.domain.UserIdentify;

/**
 * 用户Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月1日
 */
public class UserIdentifyModel extends UserIdentify {
	
	private static final long serialVersionUID = 1L;
	
	/****************通用属性 start*****************/
	/** 原先属性 **/
	private String preStatus;
	
	/****************通用属性 end*****************/
	
	/********************手机认证状态************************/
	/**	手机认证 0:未认证 	 */
	public static final String STATUS_MOBILE_WAIT = "0";
	/**	手机认证  1：认证通过 	 */
	public static final String STATUS_MOBILE_SUCC = "1";
	/**	手机认证 -1：认证未通过 */
	public static final String STATUS_MOBILE_FAIL = "-1";
	
	/********************实名认证状态************************/
	/** 实名认证 0:未认证  */
	public static final String STATUS_REALNAME_WAIT = "0";
	/**	实名认证  1：实名认证通过  */
	public static final String STATUS_REALNAME_SUCC = "1";
	/**	实名认证 -1：实名认证未通过		*/
	public static final String STATUS_REALNAME_FAIL = "-1";
	
	/********************邮箱认证状态************************/
	/** 邮箱认证0:未认证  */
	public static final String STATUS_EMAIL_WAIT = "0";
	/**	邮箱认证  1：邮箱认证通过  */
	public static final String STATUS_EMAIL_SUCC = "1";
	/**	邮箱认证-1：邮箱认证未通过		*/
	public static final String STATUS_EMAIL_FAIL = "-1";
	
	/********************授权状态************************/
	/** 授权0: 未授权 */
	public static final String STATUS_AUTH_SIGN_WAIT = "0";
	/** 授权1: 已授权 */
	public static final String STATUS_AUTH_SIGN_SUCC = "1";
	
	
	public UserIdentify prototype() {
		UserIdentify userIdentify = new UserIdentify();
		BeanUtils.copyProperties(this, userIdentify);
		return userIdentify;
	}

	/**
	 * 获取原先属性
	 * @return
	 */
	public String getPreStatus() {
		return preStatus;
	}

	/**
	 * 设置原先属性
	 * @return
	 */
	public void setPreStatus(final String preStatus) {
		this.preStatus = preStatus;
	}
	
}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 *  用户信息资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月23日
 */
public class UserResource {
	
	private UserResource() {
	}
	/**
	 * 当前用户未登录
	 */
	public static final String USER_NOT_LOGIN = "user.not.login";
	
	/**
	 * 手机号码格式错误!
	 */
	public static final String USER_REGISTER_MOBILE_STYLE_ERROR = "user.register.mobile.style.error";
	/**
	 * 手机已经被使用!
	 */
	public static final String USER_MOBILE_IS_USED = "user.mobile.is.used";
	/**
	 * 请确认注册信息是否正确！
	 */
	public static final String USER_INFO_IS_RIGHT = "user.info.is.right";
	/**
	 * 手机号码不正确,请返回重新输入
	 */
	public static final String MOBILE_IS_NOT_RIGHT = "mobile.is.not.right";
	/**
	 * 验证码错误！
	 */
	public static final String  USER_VALIDCODE_ERROR= "user.validcode.error";
	/**
	 * 动态验证码错误（短信、邮箱）
	 */
	public static final String  DYNAMIC_VALIDCODE_ERROR= "dynamic.validcode.error";
	/**
	 * 密码不能有空格
	 */
	public static final String  USER_PASS_HAVA_SPACE= "user.pwd.hava.space";
	/**
	 * 登录密码由8-16位字符组成，且必须包含字母和数字
	 */
	public static final String  USER_PASS_ERROR= "user.pwd.error";
	/**
	 * 密码不能含有中文
	 */
	public static final String  USER_PASS_HAVA_CHINESE= "user.pwd.hava.chinese";
	
	/**
	 * 手机未注册!
	 */
	public static final String USER_MOBILE_NOT_EXIST = "user.mobile.not.exist";
	/**
	 * 邮箱未绑定!
	 */
	public static final String USER_EMAIL_NOT_EXIST = "user.email.not.exist";
	
	/****** VIP 信息**************/
	/**
	 *VIP 成长增加，添加备注-投资成功
	 */
	public static final String  USER_VIP_GROWTH_UP_INVEST= "user.vip.growthUp.investSuccess";
	
}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 *  OperatorCustomerResource 资源配置类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月22日
 */
public class OperatorCustomerResource {
	/**
	 * 备注过长,请不要输入超过128个字符!
	 */
	public static final String CUSTOMER_REMARK_TOO_LONG = "customer.message.remark.too.long"; 
	/**
	 * 手机号格式或者长度异常
	 */
	public static final String CUSTOMER_MOBILE_STYLE_ERROR = "customer.message.mobile.style.error";
	
	/**
	 * 手机号码不存在
	 */
	public static final String CUSTOMER_MOBILE_NOT_EXISTS = "customer.mobile.noExists";
	/**
	 * 客户已经被添加经纪人
	 */
	public static final String CUSTOMER_OPERATOR_EXISTS = "customer.operator.exists";
	
	/**
	 * 客户是企业或者担保（添加客户不能是担保用户或者企业用户）
	 */
	public static final String CUSTOMER_USER_NOT_PERSONAL = "customer.user.is.not.personal";
}

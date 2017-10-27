package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 授权model
 * @author zxc
 * @version 1.0
 * @date 2015年12月9日 上午11:06:19
 * Copyright 杭州融都科技股份有限公司 资金存管系统UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxAuthSignModel extends UfxBaseModel {
	
	 /**
	  * 免密协议授权
	  */
	public static final int AUTH_NO_PASSWORD_PAY = 1;
	
	/**
	 * 认证支付协议授权
	 */
	public static final int AUTH_IDCARD_PAY = 2;
	
	 /**
	  * 投资协议授权
	  */
	public static final int AUTH_BORROW = 3;
	
	/**
	 * 提现协议授权
	 */
	public static final int AUTH_CASH = 4;
	
	/**
	 * 开启授权
	 */
	public static final String AUTH_OPTION_OPEN = "1";
	
	/**
	 * 关闭授权
	 */
	public static final String AUTH_OPTION_CLOSE = "0";
	
	/**
	 * 授权申请成功
	 */
	public static final int STATUS_SUCCESS_APPLY = 0;
	
	/**
	 * 处理成功
	 */
	public static final int STATUS_SUCCESS_VERIFY = 1;
	
	/**
	 * 申请失败
	 */
	public static final int STATUS_ERROR_APPLY = 2;
	
	/**
	 * 签约类型	
	 * 授权签约type=1，授权解约type=0
	 */
	private String authOption ;
	
	/**
	 * 业务类型  
	 * "no_psw_fast=1,无密快捷协议
	 * tender=2,投资; 
	 * repay=3,还款;
	 * debitcard_fast=4,借记卡快捷协议（无法解约）;
	 * trans=5,二次分账
	 * 数据格式如下：数据格式如下：数字之间用引文半角逗号隔开：1或者1,2等
	 */
	private String serviceTypes ;
	
	/**
	 * 签约状态
	 * 0申请成功
	 * 1签约成功|解除签约成功
	 * 2失败
	 */
	private int status;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "userCustId", "userAccId",
			"authOption", "serviceTypes", "reqExt", "merPriv", "returnUrl",
			"notifyUrl", "signInfo" };
	
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "userCustId", "userAccId",
			"authOption", "serviceTypes", "status", "reqExt", "merPriv",
			"respCode", "respDesc", "returnUrl", "notifyUrl", "signInfo" };

	public UfxAuthSignModel() {
		super();
		this.setService(UfxConstant.AUTH_SIGN);
		this.setReturnUrl("/ufx/authSign/return.html");
		this.setNotifyUrl("/ufx/authSign/notify.html");
	}

	/**  
	 * 获取签约类型授权签约type=1，授权解约type=0  
	 * @return authOption  
	 */
	public String getAuthOption() {
		return authOption;
	}

	/**  
	 * 设置签约类型授权签约type=1，授权解约type=0  
	 * @param authOption  
	 */
	public void setAuthOption(String authOption) {
		this.authOption = authOption;
	}

	/**  
	 * 获取业务类型"no_psw_fast=1无密快捷协议tender=2投资;repay=3还款;debitcard_fast=4借记卡快捷协议（无法解约）;trans=5二次分账数据格式如下：数据格式如下：数字之间用引文半角逗号隔开：1或者12等  
	 * @return serviceTypes  
	 */
	public String getServiceTypes() {
		return serviceTypes;
	}

	/**  
	 * 设置业务类型"no_psw_fast=1无密快捷协议tender=2投资;repay=3还款;debitcard_fast=4借记卡快捷协议（无法解约）;trans=5二次分账数据格式如下：数据格式如下：数字之间用引文半角逗号隔开：1或者12等  
	 * @param serviceTypes  
	 */
	public void setServiceTypes(String serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	/**  
	 * 获取签约状态0申请成功1签约成功|解除签约成功2失败  
	 * @return status  
	 */
	public int getStatus() {
		return status;
	}

	/**  
	 * 设置签约状态0申请成功1签约成功|解除签约成功2失败  
	 * @param status  
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**  
	 * 获取请求参数数组  
	 * @return requestParamNames  
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**  
	 * 设置请求参数数组  
	 * @param requestParamNames  
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**  
	 * 获取响应参数数组  
	 * @return responseParamNames  
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**  
	 * 设置响应参数数组  
	 * @param responseParamNames  
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

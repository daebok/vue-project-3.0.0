package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 撤销项目 model
 * 	无前后台通知
 * @author lh
 * @version 3.0
 * @date 2016年07月8日 下午7:56:39
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX  All Rights Reserved
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxProjectRevokeModel extends UfxBaseModel {
	
	/**
	 * 项目状态 : 0 - 发布
	 */
	public static final String STATE_PUBLISH = "0";
	/**
	 *  项目状态 : 1 - 投标中
	 */
	public static final String STATE_TENDER = "1";
	/**
	 * 项目状态：-2 - 已撤销
	 */
	public static final String STATE_REVOKE = "2";
		
	/**
	 * 用户ID,必须保证在其平台下唯一性 
	 */
	private String userId;
	
	/**
	 * 账户密码,用户在资金存管平台的交易密码/消费密码/查询密码
	 */
	private String payPwd;
	
	/**
	 * 监管协议编号，兴业使用
	 */
	private String superviseNo;
	
	/**
	 * 项目状态 :0：发布 1：投标中 -2：已撤销
	 */
	private String state;
	
	
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { 
			"service", "orderNo", "ufxCustomerId", "businessWay",
			"projectId", "sponsorer", "projectAmount", "userId", "payPwd", "superviseNo",
			"reqExt", "merPriv", "signInfo" };
	
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { 
			"service", "orderNo","ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "state", 
			"reqExt", "merPriv", "signInfo" };

	public UfxProjectRevokeModel() {
		super();
		this.setService(UfxConstant.PROJECT_REVOKE);
		//异步/后台通知地址:	徽商、兴业此业务暂无异步通知
		this.setNotifyUrl(ConfigUtils.getValue("web_url")+"/ufx/projectRevoke/notify.html");
	}
	
	/**
	 * 设置页面回调地址
	 * @param returnUrl
	 */
	@Override
	public void setReturnUrl(String returnUrl) {
		super.setReturnUrl(ConfigUtils.getValue("web_url") + returnUrl);
	}

	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getSuperviseNo() {
		return superviseNo;
	}

	public void setSuperviseNo(String superviseNo) {
		this.superviseNo = superviseNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

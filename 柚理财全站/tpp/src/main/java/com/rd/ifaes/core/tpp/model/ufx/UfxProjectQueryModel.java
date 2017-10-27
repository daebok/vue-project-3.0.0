package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 项目信息查询
 * @author wj
 * @version 1.0
 * @date 2015年12月22日 下午2:31:42 
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxProjectQueryModel extends UfxBaseModel {
	
	/**
	 * 项目状态
	 */
	private String projectState;
	
	private String projectAccountState;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "userCustId",
			"reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "projectState", "projectAccountState", "userCustId",
			"amount", "reqExt", "merPriv", "notifyUrl", "signInfo" };

	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	public String getProjectAccountState() {
		return projectAccountState;
	}

	public void setProjectAccountState(String projectAccountState) {
		this.projectAccountState = projectAccountState;
	}
	
}
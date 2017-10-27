package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 补录项目信息<br>
 * 当发布项目和项目信息查询接口返回状态码为 03 待上传证件照时需要调用此接口。
 * 
 * @author wj
 * @version 1.0
 * @date 2015年12月12日 下午2:43:48 Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights
 *       Reserved 官方网站：www.erongdu.com 研发中心：rdc@erongdu.com
 *       未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxProjectAttachInfoModel extends UfxBaseModel {
	
	/**
	 * 项目名称，浙商必传
	 */
	private String projectName;

	/**
	 * 项目开始时间,格式 yyyyMMddHHmmss
	 */
	private String startTime;
	
	/**
	 * 项目期限，单位天，浙商必传
	 */
	private String projectPeriod;
	

	public UfxProjectAttachInfoModel() {
		super();
		this.setService(UfxConstant.PROJECT_UPDATE);
		this.setNotifyUrl("/ufx/projectAttachInfo/return.html");
		this.setNotifyUrl("/ufx/projectAttachInfo/notify.html");
	}

	/**
	 * 请求参数数组
	 */
	/**
	 * service+orderNo+ufxCustomerId+businessWay+
	 * projectId+sponsorer+projectName+projectAmount+startTime+projectPeriod+
	 * reqExt+merPriv+returnUrl+notifyUrl
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", 
	    "projectId", "sponsorer", "projectName", "projectAmount", "startTime","projectPeriod", 
	    "reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	/**
	 * service+orderNo+ufxCustomerId+businessWay+respCode+respDesc+
	 * projectId+sponsorer+projectAmount+projectName+startTime+projectPeriod+
		reqExt+merPriv+returnUrl+notifyUrl
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "projectName", "startTime", "projectPeriod",
			"reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getProjectPeriod() {
		return projectPeriod;
	}

	public void setProjectPeriod(String projectPeriod) {
		this.projectPeriod = projectPeriod;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return requestParamNames
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 * 
	 * @param requestParamNames
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return responseParamNames
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 * 
	 * @param responseParamNames
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

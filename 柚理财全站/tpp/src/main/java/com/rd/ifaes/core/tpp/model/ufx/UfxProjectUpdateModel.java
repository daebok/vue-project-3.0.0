package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 项目更新，目前只做了项目状态更新，其他后续需要再添加
 * 
 * @author wj
 * @version 1.0
 * @date 2015年12月12日 下午2:43:48 Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights
 *       Reserved 官方网站：www.erongdu.com 研发中心：rdc@erongdu.com
 *       未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxProjectUpdateModel extends UfxBaseModel {
	
	/**
	 * 项目名称，浙商必传
	 */
	private String projectName;

	/**
	 * 更新类型：01：状态
	 */
	private String changeType;

	/**
	 * 更新状态： 0：开标、1：投标中、2：还款中、3：已还款、4：结束 -1：流标
	 * 兴业只用到：2,4,-1
	 */
	private String projectState;

	/**
	 * 用户类型，01：个人，02：企业
	 */
	private String userType;
	
	/**
	 * 项目开始时间
	 */
	private String startTime;
	
	/**
	 * 项目期限，单位天，浙商必传
	 */
	private String projectPeriod;
	
	/**
	 * 项目状态： 0：开标、1：投标中、2：还款中、3：已还款、4：结束 -1：流标
	 */
	public static final String PROJECT_STATE_STARTED 	= "0";
	public static final String PROJECT_STATE_TENDER		= "1";
	public static final String PROJECT_STATE_REPAY		= "2";
	public static final String PROJECT_STATE_REPAY_OVER	= "3";
	public static final String PROJECT_STATE_OVER		= "4";
	public static final String PROJECT_STATE_OVERFLOW	= "-1";
	

	public UfxProjectUpdateModel() {
		super();
		this.setService(UfxConstant.PROJECT_UPDATE);
		this.setNotifyUrl("/ufx/projectUpdate/notify.html");
	}

	/**
	 * 请求参数数组
	 */
	/**
	 * ervice+orderNo+ufxCustomerId+businessWay+
        projectId+projectName+amount+sponsorer+changeType+
        projectState+userType+startTime+projectPeriod +reqExt+merPriv +notifyUrl
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", 
	    "projectId", "projectName", "amount", "sponsorer","changeType", 
	    "projectState", "userType","startTime","projectPeriod", "reqExt", "merPriv",
		"notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	/**
	 * service+orderNo+
	 * ufxCustomerId+businessWay+respCode+respDesc+
	 * projectId+projectName+amount+
      sponsorer+changeType+projectState+startTime+projectPeriod+reqExt+merPriv
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "projectName", "amount", 
			"sponsorer", "changeType", "projectState", "startTime", "projectPeriod",
			"reqExt", "merPriv", "signInfo" };

	/**
	 * 获取changeType
	 * 
	 * @return changeType
	 */
	public String getChangeType() {
		return changeType;
	}

	/**
	 * 设置changeType
	 * 
	 * @param changeType
	 */
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	/**
	 * 获取projectState
	 * 
	 * @return projectState
	 */
	public String getProjectState() {
		return projectState;
	}

	/**
	 * 设置projectState
	 * 
	 * @param projectState
	 */
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	/**
	 * 获取userType
	 * 
	 * @return userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * 设置userType
	 * 
	 * @param userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
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

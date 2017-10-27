package com.rd.ifaes.core.tpp.model.ufx;

import java.util.Arrays;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 投资失败退款 Model
 * @author wujing
 * @version 1.0
 * @date 2015年12月3日 下午8:20:41
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxInvestFailModel extends UfxBaseModel {
	
	/**
	 * 投资流水号
	 */
	private String investNo;
	
	/**
	 * 投资日期
	 */
	private String investDate;
	
	/**
	 * 投资冻结流水号
	 */
	private String freezeNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "sponsorer",
			"projectAmount", "userCustId", "amount", "investNo", "investDate",
			"freezeNo", "reqExt", "merPriv", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "userCustId", "amount",
			"investNo", "investDate", "freezeNo", "reqExt", "merPriv",
			"signInfo" };
	
	public UfxInvestFailModel() {
		super();
 		this.setService(UfxConstant.INVEST_FAIL);
		this.setNotifyUrl("/ufx/investFail/notify.html"); 
	}
	
	/**  
	 * 获取投资流水号  
	 * @return investNo  
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**  
	 * 设置投资流水号  
	 * @param investNo  
	 */
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	/**  
	 * 获取投资日期  
	 * @return investDate  
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**  
	 * 设置投资日期  
	 * @param investDate  
	 */
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	/**  
	 * 获取投资冻结流水号  
	 * @return freezeNo  
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**  
	 * 设置投资冻结流水号  
	 * @param freezeNo  
	 */
	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
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

	@Override
	public String toString() {
		return "UfxInvestFailModel [investNo=" + investNo + ", investDate=" + investDate + ", freezeNo=" + freezeNo
				+ ", requestParamNames=" + Arrays.toString(requestParamNames) + ", responseParamNames="
				+ Arrays.toString(responseParamNames) + "]";
	}
	
}

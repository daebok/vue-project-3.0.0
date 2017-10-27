package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 生利宝（汇付） Model
 * @author ld
 * @version 1.0
 * @date 2015年12月22日 下午1:20:41
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxFssTransModel extends UfxBaseModel{

	/**
	 * 交易类型
	 */
	private String transType;
	
	/**
	 * 订单日期 格式为 YYYYMMDD
	 */
	private String ordDate;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "userCustId", "reqExt", "merPriv",
			"returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "amount", "ordDate", "transType", "reqExt",
			"merPriv", "signInfo" };

	public UfxFssTransModel(){
		super();
/*		this.setService(UfxConstant.FssTrans);
		this.setReturnUrl(Global.getValue("web_url") + "/ufx/fssTrans/return.html");
		this.setNotifyUrl(Global.getValue("web_url") + "/ufx/fssTrans/notify.html");*/
	}

	/**
	 * 获取交易类型
	 * @return transType
	 */
	public String getTransType() {
		return transType;
	}

	/**
	 * 设置交易类型
	 * @param transType
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}

	/**
	 * 获取订单日期格式为YYYYMMDD
	 * @return ordDate
	 */
	public String getOrdDate() {
		return ordDate;
	}

	/**
	 * 设置订单日期格式为YYYYMMDD
	 * @param ordDate
	 */
	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
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

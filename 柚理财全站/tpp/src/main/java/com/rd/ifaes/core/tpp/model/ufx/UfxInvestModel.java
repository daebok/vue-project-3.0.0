package com.rd.ifaes.core.tpp.model.ufx;

import java.util.Arrays;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 投资model
 * @author xx
 * @version 1.0
 * @date 2015年11月12日 上午11:01:30
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxInvestModel extends UfxBaseModel {
		
	/**
	 * 代金券金额
	 */
	private String voucher;
	
	/**
	 * 投资流水号
	 */
	private String investNo;
	
	/**
	 * 投资日期
	 */
	private String investDate;
	/**
	 * 是否冻结金额 1：冻结  0：不冻结  默认值 1
	 */
	private String frzFlag;
	/**
	 * 投标冻结流水号
	 */
	private String freezeNo;
	
	/**
	 * tpptrade 对应的orderNo
	 */
	private String tppOrderNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "sponsorer",
			"projectAmount", "userCustId", "amount", "voucher", "reqExt",
			"merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "userCustId", "amount",
			"voucher", "reqExt", "merPriv", "investNo", "investDate",
			"freezeNo", "signInfo" };
	
	public UfxInvestModel() {
		super();
 		this.setService(UfxConstant.INVEST);
		this.setReturnUrl("/ufx/invest/return.html");
		this.setNotifyUrl("/ufx/invest/notify.html"); 
	}

	
	/**
	 * 变现投资构造方法
	 */
	public UfxInvestModel(String flag) {
		super();
 		this.setService(UfxConstant.INVEST);
		this.setReturnUrl("/ufx/"+flag+"/return.html");
		this.setNotifyUrl("/ufx/"+flag+"/notify.html"); 
	}
	/**
	 * 获取voucher
	 * @return voucher
	 */
	public String getVoucher() {
		return voucher;
	}

	/**
	 * 设置voucher
	 * @param voucher
	 */
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	/**
	 * 获取投资流水号
	 * 
	 * @return
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**
	 * 设置投资流水号
	 * 
	 * @param investNo
	 */
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	/**
	 * 获取投资日期
	 * 
	 * @return
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置投资日期
	 * 
	 * @param investDate
	 */
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}
    /**
     *获取 是否冻结金额 1：冻结 0：不冻结 默认值 1
     */
	public String getFrzFlag() {
		return frzFlag;
	}
	 /**
     *设置 是否冻结金额 1：冻结 0：不冻结 默认值 1
     */
	public void setFrzFlag(String frzFlag) {
		this.frzFlag = frzFlag;
	}

	/**
	 * 获取投标冻结流水号
	 * 
	 * @return
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**
	 * 设置投标冻结流水号
	 * 
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

	public String getTppOrderNo() {
		return tppOrderNo;
	}


	public void setTppOrderNo(String tppOrderNo) {
		this.tppOrderNo = tppOrderNo;
	}


	@Override
	public String toString() {
		return "UfxInvestModel [voucher=" + voucher + ", investNo=" + investNo + ", investDate=" + investDate
				+ ", frzFlag=" + frzFlag + ", freezeNo=" + freezeNo + ", requestParamNames="
				+ Arrays.toString(requestParamNames) + ", responseParamNames=" + Arrays.toString(responseParamNames)
				+ "]";
	}
	
	
}

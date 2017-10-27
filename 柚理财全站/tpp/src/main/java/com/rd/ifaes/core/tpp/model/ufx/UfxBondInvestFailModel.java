package com.rd.ifaes.core.tpp.model.ufx;


/**
 * 债权投资失败资金退回
 * @author ctt
 * @version 2.3.0.0
 * @date 2016年3月11日 下午2:33:16
 * Copyright 杭州融都科技股份有限公司 P2P  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxBondInvestFailModel extends UfxBaseModel {
	
	/**
	 * 债权ID
	 */
	private String bondId;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "bondId", "userCustId", "amount",
			"orderNo", "reqExt", "merPriv", "notifyUrl", "signInfo" };
			

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "amount", "orderNo", "reqExt", "merPriv", "signInfo" };

	public UfxBondInvestFailModel() {
		super();
/*		this.setService(UfxConstant.BOND_INVEST_FAIL);
		this.setReturnUrl(Global.getValue("web_url")+"/ufx/bondInvestFail/return.html");
		this.setNotifyUrl(Global.getValue("web_url")+"/ufx/bondInvestFail/notify.html");*/
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



	/** 
	 * 获取债权ID
	 * @return bondId
	 */
	public String getBondId() {
		return bondId;
	}



	/** 
	 * 设置债权ID
	 * @param bondId
	 */
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}

	
}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.data;

/**
 * 多张银行卡信息
 * @version 3.0
 * @author Administrator
 * @date 2017年4月28日
 */
public class UsrInfExt{
	/**
	 * 开户行
	 */
	private String capCorg;
	
	/**
	 * 卡号
	 */
	private String capCrdNo;
	
	/**
	 * 绑定手机号
	 */
	private String mblNo;
	
	/**
	 * 姓名
	 */
	private String useName;
	public String getCapCorg() {
		return capCorg;
	}

	public void setCapCorg(String capCorg) {
		this.capCorg = capCorg;
	}

	public String getCapCrdNo() {
		return capCrdNo;
	}

	public void setCapCrdNo(String capCrdNo) {
		this.capCrdNo = capCrdNo;
	}

	public String getMblNo() {
		return mblNo;
	}

	public void setMblNo(String mblNo) {
		this.mblNo = mblNo;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}
}

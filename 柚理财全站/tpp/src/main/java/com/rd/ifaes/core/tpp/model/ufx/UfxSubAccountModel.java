package com.rd.ifaes.core.tpp.model.ufx;

import java.math.BigDecimal;

public class UfxSubAccountModel {

	/**
	 * 子账户类型
	 */
	private String acctType;
	
	/**
	 * 子账户账户号
	 */
	private String subAcctId;
	
	/**
	 * 子账户可用余额
	 */
	private BigDecimal avlBal;
	
	/**
	 * 子账户账户余额
	 */
	private BigDecimal acctBal;
	
	/**
	 * 子账户冻结金额
	 */
	private BigDecimal frzBal;

	public UfxSubAccountModel() {
		super();
	}

	public UfxSubAccountModel(String acctType, String subAcctId, BigDecimal avlBal,
			BigDecimal acctBal, BigDecimal frzBal) {
		super();
		this.acctType = acctType;
		this.subAcctId = subAcctId;
		this.avlBal = avlBal;
		this.acctBal = acctBal;
		this.frzBal = frzBal;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getSubAcctId() {
		return subAcctId;
	}

	public void setSubAcctId(String subAcctId) {
		this.subAcctId = subAcctId;
	}

	public BigDecimal getAvlBal() {
		return avlBal;
	}

	public void setAvlBal(BigDecimal avlBal) {
		this.avlBal = avlBal;
	}

	public BigDecimal getAcctBal() {
		return acctBal;
	}

	public void setAcctBal(BigDecimal acctBal) {
		this.acctBal = acctBal;
	}

	public BigDecimal getFrzBal() {
		return frzBal;
	}

	public void setFrzBal(BigDecimal frzBal) {
		this.frzBal = frzBal;
	}
}

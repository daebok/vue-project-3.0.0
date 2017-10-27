package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

public class AccountLogItemModel {

	/**
	 * 账户类别 
	 */ 
	private String	accountType;
	/**
	 * 状态说明
	 */
	private String accountTypeStr;
	/**
	 * 添加时间
	 */
	private Date createTime;
	/**
	 * 交易金额
	 */
	private BigDecimal	money;	
	/**
	 * 资金名称
	 */
	private String funName;
	/**
	 * 资金正负说明
	 */
	private String moneyStr;
	/** 
	 * 备注 
	 */ 
	private String	remark;		 	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountTypeStr() {
		return accountTypeStr;
	}
	public void setAccountTypeStr(String accountTypeStr) {
		this.accountTypeStr = accountTypeStr;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	public String getMoneyStr() {
		return moneyStr;
	}
	public void setMoneyStr(String moneyStr) {
		this.moneyStr = moneyStr;
	}

	
}

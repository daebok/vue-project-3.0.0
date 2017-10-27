package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectCollectionModel {
	/**
	 * IP
	 */
	private String	addIp;	
	/**
	 * 本金
	 */
	private BigDecimal	capital;
	 /**
	  *  待收类型  0普通待收;1债权转让
	  */ 
	private String	collectionType;	
	 /**
	  * 预计还款时间 
	  */ 
	private Date	repayTime;		
	 /**
	  * 利息 
	  */ 
	private BigDecimal	interest;
	/**
	 * 回款状态
	 */
	private String repayedAmountStr;
	/**
	 * 期数
	 */ 
	private String	periodsStr;	
	 /**
	  * 已还金额(本金+利息+加息) 
	  */ 
	private BigDecimal	repayedAmount;
	/**
	 * 还款状态标识符：1：未到期 2：已逾期 3：已还
	 */
	private String repayedStatus;

	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	public BigDecimal getCapital() {
		return capital;
	}
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}
	public String getCollectionType() {
		return collectionType;
	}
	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}
	public Date getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getRepayedAmountStr() {
		return repayedAmountStr;
	}
	public void setRepayedAmountStr(String repayedAmountStr) {
		this.repayedAmountStr = repayedAmountStr;
	}
	public String getPeriodsStr() {
		return periodsStr;
	}
	public void setPeriodsStr(String periodsStr) {
		this.periodsStr = periodsStr;
	}
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	public String getRepayedStatus() {
		return repayedStatus;
	}
	public void setRepayedStatus(String repayedStatus) {
		this.repayedStatus = repayedStatus;
	}	
	
}

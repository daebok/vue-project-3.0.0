package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 每期待收汇总
 * @author lh
 * @version 3.0
 * @since 2016-7-26
 *
 */
public class PeriodCollectionModel {
	
	private Date		repayTime;		 /* 预计还款时间 */ 
	private BigDecimal	payment;		 /* 预还金额 */ 
	private BigDecimal	capital;		 /* 本金 */ 
	private BigDecimal	interest;		 /* 利息 */ 
	private BigDecimal	manageFee;		 /* 利息管理费 */
	private BigDecimal borrowManageFee; /** 借款人管理**/
	
	public Date getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public BigDecimal getCapital() {
		return capital;
	}
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getManageFee() {
		return manageFee;
	}
	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}
	public BigDecimal getBorrowManageFee() {
		return borrowManageFee;
	}
	public void setBorrowManageFee(BigDecimal borrowManageFee) {
		this.borrowManageFee = borrowManageFee;
	}
	@Override
	public String toString() {
		return "PeriodCollectionModel [repayTime=" + repayTime + ", payment=" + payment + ", capital=" + capital + ", interest="
				+ interest + ", manageFee=" + manageFee + ", borrowManageFee=" + borrowManageFee + "]";
	}
	
}

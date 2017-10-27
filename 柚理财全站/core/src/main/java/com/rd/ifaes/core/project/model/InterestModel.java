package com.rd.ifaes.core.project.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 利息计算MODEL
 * @author lh
 * @version 3.0
 * @since 2016-7-23
 *
 */
public class InterestModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 还款方式
	 */
	private String repayStyle;
	/**
	 * 借款期限类型： 0月标 1天标
	 */
	private String timeType;
	/**
	 * 借款总额
	 */
	private BigDecimal account;
	/**
	 * 年化利率
	 */
	private BigDecimal yearApr;
	/**
	 * 加息利率
	 */
	private BigDecimal addApr;
	
	/**
	 * 开始计息日 
	 */
	private Date interestTime;
	/**
	 * 期数 
	 */
	private int periods;
	/**
	 * 利息管理费
	 */
	private BigDecimal manageFee;	
	/**
	 * 固定还款日
	 */
	private Integer fixedRepayDay;
	
	
	public String getRepayStyle() {
		return repayStyle;
	}
	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getYearApr() {
		return yearApr;
	}
	public void setYearApr(BigDecimal yearApr) {
		this.yearApr = yearApr;
	}
	
	public BigDecimal getAddApr() {
		return addApr;
	}
	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	public int getPeriods() {
		return periods;
	}
	public void setPeriods(int periods) {
		this.periods = periods;
	}
	public BigDecimal getManageFee() {
		return manageFee;
	}
	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}
	public Integer getFixedRepayDay() {
		return fixedRepayDay;
	}
	public void setFixedRepayDay(Integer fixedRepayDay) {
		this.fixedRepayDay = fixedRepayDay;
	}
	@Override
	public String toString() {
		return "InterestModel [repayStyle=" + repayStyle + ", timeType=" + timeType + ", account=" + account
				+ ", yearApr=" + yearApr + ", addApr=" + addApr + ", interestTime=" + interestTime + ", periods="
				+ periods + ", manageFee=" + manageFee + ", fixedRepayDay=" + fixedRepayDay + "]";
	}
	
	
}

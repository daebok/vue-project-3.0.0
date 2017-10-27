package com.rd.ifaes.mobile.model.account.log;

import java.util.Date;

/**
 * @author shenliang
 * 用户回款记录
 */
public class AccountInvestRepayPlanItemModel {
	
	private String uuid;
	
	/**
	 * 标的名称
	 */
	private String name;
	
	/**
	 * 年利率
	 */
	private double apr;
	
	/**
	 * 本息
	 */
	private double totalCapital;
	
	/**
	 * 计息起息日
	 */
	private Date startDate;
	
	/**
	 * 还款日
	 */
	private Date repaymentTime;
	
	/**
	 * 本金
	 */
	private double capital;
	
	/**
	 * 利息
	 */
	private double interest;
	
	/**
	 * 第几期
	 */
	private String period;
	
	
	/**
	 * 总期数
	 */
	private String allPeriod;
	
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getApr() {
		return apr;
	}
	
	public void setApr(double apr) {
		this.apr = apr;
	}
	
	public double getTotalCapital() {
		return totalCapital;
	}
	
	public void setTotalCapital(double totalCapital) {
		this.totalCapital = totalCapital;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getRepaymentTime() {
		return repaymentTime;
	}
	
	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}
	
	public double getCapital() {
		return capital;
	}
	
	public void setCapital(double capital) {
		this.capital = capital;
	}
	
	public double getInterest() {
		return interest;
	}
	
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getAllPeriod() {
		return allPeriod;
	}

	public void setAllPeriod(String allPeriod) {
		this.allPeriod = allPeriod;
	}
	
}


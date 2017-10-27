package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectToBondModel {

	/**
	 * 有效投资金额
	 */
	private BigDecimal amount;
	/**
	 * 加息利息
	 */
	private BigDecimal raiseInterest;
	public BigDecimal getRaiseInterest() {
		return raiseInterest;
	}
	public void setRaiseInterest(BigDecimal raiseInterest) {
		this.raiseInterest = raiseInterest;
	}
	/**
	 * 原借款标预期年化
	 */
	private BigDecimal apr;
	/**
	 * 计息时间 
	 */
	private Date  interestTime;
	/**
	 * 最后还款时间
	 */
	private Date lastRepayTime;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 获取实时剩余期限（当前时间到最后的还款日中间的时间天数）
	 */
	private int projectRealRemainDays;
	/**
	 *  获取总期限（计息日到最后的还款日中间的时间天数）
	 */
	private int projectTotalRemainDays;
	/**
	 *  否校验过定向密码
	 */
	private  Boolean pwdHasVerify; 
	/**
	 * 已还金额
	 */
	private BigDecimal	repayedAmount;
	 /**
	  * 已还利息 
	  */ 
	private BigDecimal	repayedInterest;		
	/**
	 * 投资项目uuid
	 */
	private String uuid;
	/**
	 * 下期还款时间
	 */
	private Date nextRepayDate;
	/**
	 * 项目id
	 */
	private String	projectId;
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	public Date getLastRepayTime() {
		return lastRepayTime;
	}
	public void setLastRepayTime(Date lastRepayTime) {
		this.lastRepayTime = lastRepayTime;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getProjectRealRemainDays() {
		return projectRealRemainDays;
	}
	public void setProjectRealRemainDays(int projectRealRemainDays) {
		this.projectRealRemainDays = projectRealRemainDays;
	}
	public int getProjectTotalRemainDays() {
		return projectTotalRemainDays;
	}
	public void setProjectTotalRemainDays(int projectTotalRemainDays) {
		this.projectTotalRemainDays = projectTotalRemainDays;
	}
	public Boolean getPwdHasVerify() {
		return pwdHasVerify;
	}
	public void setPwdHasVerify(Boolean pwdHasVerify) {
		this.pwdHasVerify = pwdHasVerify;
	}
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}
	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getNextRepayDate() {
		return nextRepayDate;
	}
	public void setNextRepayDate(Date nextRepayDate) {
		this.nextRepayDate = nextRepayDate;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	
}

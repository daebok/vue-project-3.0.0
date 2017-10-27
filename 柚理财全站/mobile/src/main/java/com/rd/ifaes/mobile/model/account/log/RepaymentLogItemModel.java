package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

public class RepaymentLogItemModel {
	/**
	 * 应还本金
	 */
	private BigDecimal	capital;	
	/**
	 * 预计还款时间
	 */ 
	
	private Date	repayTime;		
	/**
	 * 还款状态
	 */
	private String  statusStr;
	/**
	 * 期数
	 */
	private String periodsStr;
	/**
	 * 第几期
	 */
	private Integer	period;
	/**
	 * 总期数
	 */
	private Integer periods;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 还款状态  0未还；1已还  -1（前面还有期数没有还款，不能进行还款操作）
	 */ 
	private String	status;	
	/**
	 * 应还利息
	 */
	private BigDecimal	interest;
	/**
	 * 逾期罚息
	 */
	private BigDecimal	lateInterest;
	/**
	 * 实际还款时间
	 */
	private Date realRepayTime;
	/**
	 * 还款计划-类型
	 */
	private String repaymentTypeStr;
	/* 预还金额 */
	private BigDecimal	payment;	
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 状态备注
	 */
	private String statusTypeStr;
	public BigDecimal getCapital() {
		return capital;
	}
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}
	public Date getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getPeriodsStr() {
		return periodsStr;
	}
	public void setPeriodsStr(String periodsStr) {
		this.periodsStr = periodsStr;
	}
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
	public Integer getPeriods() {
		return periods;
	}
	public void setPeriods(Integer periods) {
		this.periods = periods;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getLateInterest() {
		return lateInterest;
	}
	public void setLateInterest(BigDecimal lateInterest) {
		this.lateInterest = lateInterest;
	}
	public Date getRealRepayTime() {
		return realRepayTime;
	}
	public void setRealRepayTime(Date realRepayTime) {
		this.realRepayTime = realRepayTime;
	}
	public String getRepaymentTypeStr() {
		return repaymentTypeStr;
	}
	public void setRepaymentTypeStr(String repaymentTypeStr) {
		this.repaymentTypeStr = repaymentTypeStr;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getStatusTypeStr() {
		return statusTypeStr;
	}
	public void setStatusTypeStr(String statusTypeStr) {
		this.statusTypeStr = statusTypeStr;
	}
	
		 
}
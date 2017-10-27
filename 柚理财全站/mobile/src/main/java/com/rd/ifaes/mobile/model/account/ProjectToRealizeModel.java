package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectToRealizeModel {

	/**
	 * 投资金额
	 * */
	private BigDecimal amount;
	/**
	 * 预期收益
	 */
	private BigDecimal	interest;
	/**
	 * 剩余期限
	 */
	private int remainDays;
	
	/**
	 * 项目名称
	 */
	private String projectName;
		
	/**
	 * 投资项目uuid（对应变现projectInvestId）
	 */
	private String uuid;
	/**
	 * 下期还款时间
	 */
	private Date nextRepayTime;
	/**
	 * 项目id
	 */
	private String	projectId;
	 /** 
	  * 变现标识
	  */
	private String	realizeFlag;		 
	public String getRealizeFlag() {
		return realizeFlag;
	}
	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public int getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getNextRepayTime() {
		return nextRepayTime;
	}
	public void setNextRepayTime(Date nextRepayTime) {
		this.nextRepayTime = nextRepayTime;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
}

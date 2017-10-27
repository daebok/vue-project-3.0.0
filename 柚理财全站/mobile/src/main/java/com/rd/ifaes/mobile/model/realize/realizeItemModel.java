package com.rd.ifaes.mobile.model.realize;

import java.math.BigDecimal;

/**
 * @author LGX
 * 变现列表model
 */
public class realizeItemModel {
	/** 
	 * 项目ID 
	 */
	private String	projectId;	
	/** 
	 * 投标ID 
	 */
	private String	investId;	
	/** 
	 * 借款期限
	 */
	protected Integer timeLimit; 
	/**
	 * 剩余可投金额
	 */
	private Double remainAccount;
	/** 
	 * 项目总额 
	 */
	protected BigDecimal account; 
	/** 
	 * 最低起投金额 
	 */
	private BigDecimal lowestAccount; 
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}
	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	/** 
	 * 借款期限类型： 0月标 1天标 
	 */
	protected String timeType; 
	/**  
	 * 状态: 0 新增 1发布待审 2 发布审核成功(待售) 3发布审核失败4 募集中 5 满额待审（已售）6 成立审核成功（还款中）8 还款中87 逾期中（还款中）88坏账（还款中） 90部分还款（还款中） 9还款成功（已完成）91逾期还款（已完成）   
     */
	protected String status; 
	/** 
	 * 年利率 
	 */
	protected BigDecimal apr; 
	/** 
	 * 项目名称 
	 */
	protected String projectName; 
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	public String getProjectId() {
		return projectId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Double getRemainAccount() {
		return remainAccount;
	}
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}

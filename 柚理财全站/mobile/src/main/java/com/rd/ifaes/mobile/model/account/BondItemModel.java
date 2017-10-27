package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

public class BondItemModel {
	/**
	 *原资产预期年化(预期年化收益率)
	 */
	private BigDecimal apr;
	/**
	 *折溢价率  
	 */
	private BigDecimal	bondApr;
	/**
	 * 债权截止日期
	 */
	private Date	bondEndTime;
	
	/**
	 * 债权金额
	 */
	private BigDecimal	bondMoney;

	/**
	 * 债权名称
	 */
	private String bondName;
	/**
	 * 债权转让编号
	 */
	private String	bondNo;
	/**
	 * 转让价格   计算得来 
	 */
	private BigDecimal bondPrice;
	/**
	 * 转让成功时间    相当于 successTime 前端展示
	 */
	private Date successTime;
	/**
	 * 添加时间
	 */
	private Date	createTime;
    /**
	 * 投标ID
	 */
	private String	investId;

	 /**
     * 手续费   相当于 bondFee 前端展示
     */
    private BigDecimal manageFee;
 
    /**
	 * 项目ID
	 */
	private String	projectId;
	/**
	 * 项目名称
	 */
	private String projectName;
	 /**
     * 实收金额
     */
    private BigDecimal receivedMoney;

	/**
	 * 债权人
	 */
	private String	userId;
	/**
	 * 债权uuid
	 */
	private String uuid;
	/**
	 * 债权协议url
	 */
	private String BondProtocolUrl;
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	public BigDecimal getBondApr() {
		return bondApr;
	}
	public void setBondApr(BigDecimal bondApr) {
		this.bondApr = bondApr;
	}
	public Date getBondEndTime() {
		return bondEndTime;
	}
	public void setBondEndTime(Date bondEndTime) {
		this.bondEndTime = bondEndTime;
	}

	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	
	public String getBondName() {
		return bondName;
	}
	public void setBondName(String bondName) {
		this.bondName = bondName;
	}
	public String getBondNo() {
		return bondNo;
	}
	public void setBondNo(String bondNo) {
		this.bondNo = bondNo;
	}
	public BigDecimal getBondPrice() {
		return bondPrice;
	}
	public void setBondPrice(BigDecimal bondPrice) {
		this.bondPrice = bondPrice;
	}
	public Date getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	
	public BigDecimal getManageFee() {
		return manageFee;
	}
	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public BigDecimal getReceivedMoney() {
		return receivedMoney;
	}
	public void setReceivedMoney(BigDecimal receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBondProtocolUrl() {
		return BondProtocolUrl;
	}
	public void setBondProtocolUrl(String bondProtocolUrl) {
		BondProtocolUrl = bondProtocolUrl;
	}
	
	
}

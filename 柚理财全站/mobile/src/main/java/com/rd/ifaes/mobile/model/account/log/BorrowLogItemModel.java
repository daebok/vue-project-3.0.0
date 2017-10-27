package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

public class BorrowLogItemModel {
	/**
	 * 借款金额
	 */
	private BigDecimal account; 
	/**
	 * 已投金额(募集金额)
	 */
	private BigDecimal accountYes; 
	 /**
	  * 借款年利率
	  */
	private BigDecimal apr;
	/**
	 * 借款标标识(1是，借款标， 0否，即是理财产品）
	 */
	private String borrowFlag; 
	/**
	 * 添加时间
	 */
	private Date createTime; 
	/**
	 * 进度条
	 */
	private BigDecimal scales;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 借款状态标识
	 */
	private String status;
	/**
	 * 状态
	 */
	private String statusStr;
	/**
	 * 借款期限
	 */
	private String timeLimitStr;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 *借款人下载协议
	 */
	private String ProtocolUrl;
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getAccountYes() {
		return accountYes;
	}
	public void setAccountYes(BigDecimal accountYes) {
		this.accountYes = accountYes;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	public String getBorrowFlag() {
		return borrowFlag;
	}
	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getScales() {
		return scales;
	}
	public void setScales(BigDecimal scales) {
		this.scales = scales;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimeLimitStr() {
		return timeLimitStr;
	}
	public void setTimeLimitStr(String timeLimitStr) {
		this.timeLimitStr = timeLimitStr;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProtocolUrl() {
		return ProtocolUrl;
	}
	public void setProtocolUrl(String protocolUrl) {
		ProtocolUrl = protocolUrl;
	}
	
}

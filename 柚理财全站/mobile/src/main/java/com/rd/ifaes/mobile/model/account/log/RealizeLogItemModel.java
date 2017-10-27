package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

public class RealizeLogItemModel {
	/**
	 *变现金额
	 */
	private BigDecimal account; 
	
	/**
	 * 变现利率
	 */
	private BigDecimal apr;
	
	/**
	 * 变现申请时间
	 */
	private Date createTime;

	/**
	 * 投标ID
	 */
	private String	investId;

	/**
	 * 进度
	 */
	private BigDecimal scales;
	/**
	 * 变现完成时间
	 */
	private Date reviewTime; 
	/**
	 * 产品名称
	 */
	private String projectName;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 项目ID
	 */
	private String	projectId;
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
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
	public BigDecimal getScales() {
		return scales;
	}
	public void setScales(BigDecimal scales) {
		this.scales = scales;
	}
	public Date getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	public String getUuid() {
		return uuid;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	
}
package com.rd.ifaes.mobile.model.project;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LGX 项目列表
 */
public class projectIndexItemRecordModel {
	/**
	 *  项目id 
	 */
	private String projectId;
	/**
	 *  项目总额
	 */
	protected BigDecimal account;
	/**
	 *  最高投资总额 
	 */
	protected BigDecimal mostAccount;
	/** 
	 * 开售时间 
	 */
	protected Date saleTime;
	/**
	 *  是否可变现: 1 可变现 0 不可变现，默认 0
	 */
	private String realizeUseful;
	/**  
	 * 加息年利率 
	 */
	private BigDecimal addApr;
	/** 
	 * 是否可以债权转让 1 可以，0不可以
	 */
	private String bondUseful;
	/**  
	 * 新手标标识： 1新手专享 0 普通 ，默认：0 
	 */
	private String novice;
	/**  
	 * 定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0)
	 */
	private String specificSale;
	/** 
	 *  定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名
	 */
	private String specificSaleRule; 

	public String getSpecificSaleRule() {
		return specificSaleRule;
	}

	public void setSpecificSaleRule(String specificSaleRule) {
		this.specificSaleRule = specificSaleRule;
	}
	/** 
	 *  借款期限类型： 0月标 1天标
	 */
	private String timeType; 
	/** 
	 *  借款期限 
	 */
	private Integer timeLimit; 
	/** 
	 *  最低起投金额 
	 */
	private BigDecimal lowestAccount; 
	/** 
	 *  剩余可投金额 
	 */
	private Double remainAccount;
	/** 
	 *  项目名称
	 */
	private String projectName; 
	/** 
	 *  年利率 
	 */
	private BigDecimal apr; 
	/** 
	 *  是否开售 
	 */
	private String ifSale; 
	public Date getTimeNow() {
		return timeNow;
	}
	public void setTimeNow(Date timeNow) {
		this.timeNow = timeNow;
	}
	/** 
	 *  当前时间 
	 */
	private Date timeNow;
	public String getIfSale() {
		return ifSale;
	}
	public void setIfSale(String ifSale) {
		this.ifSale = ifSale;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getMostAccount() {
		return mostAccount;
	}
	public void setMostAccount(BigDecimal mostAccount) {
		this.mostAccount = mostAccount;
	}
	public Date getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	public String getRealizeUseful() {
		return realizeUseful;
	}
	public void setRealizeUseful(String realizeUseful) {
		this.realizeUseful = realizeUseful;
	}
	public String getBondUseful() {
		return bondUseful;
	}
	public void setBondUseful(String bondUseful) {
		this.bondUseful = bondUseful;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public BigDecimal getAddApr() {
		return addApr;
	}
	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}
	public String getNovice() {
		return novice;
	}
	public void setNovice(String novice) {
		this.novice = novice;
	}
	public String getSpecificSale() {
		return specificSale;
	}
	public void setSpecificSale(String specificSale) {
		this.specificSale = specificSale;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}
	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	public Double getRemainAccount() {
		return remainAccount;
	}
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
}

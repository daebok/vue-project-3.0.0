/**
 * 转入债权初始化信息
 */
package com.rd.ifaes.mobile.model.bond;

import java.math.BigDecimal;

/**
 * @author yoseflin
 * 转入债权初始化信息
 */
public class BondToBondModel {
	/**
	 * 防重复标识
	 */
	private String bondToken; 
	/**
	 * 一年？天
	 */
	private String daysOfYear;
	public String getDaysOfYear() {
		return daysOfYear;
	}
	public void setDaysOfYear(String daysOfYear) {
		this.daysOfYear = daysOfYear;
	}
	/**
	 *  项目id
	 */
	private String projectId; 
	/**
	 * 项目名称
	 */
	private String projectName; 
	/**
	 * 产品协议ID
	 */
	protected String protocolId;
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	 /**
	  * 利息管理费率
	  */
	private String interestManageRate;
	public String getInterestManageRate() {
		return interestManageRate;
	}
	public void setInterestManageRate(String interestManageRate) {
		this.interestManageRate = interestManageRate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 投标ID 
	 */
	private String	investId;	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	/**
	 *  项目总额总收益
	 */
	protected BigDecimal totalInterest; 
	/** 
	 * 项目总额实际支付
	 */
	protected BigDecimal  totalrealPayAmount;
	public BigDecimal getTotalInterest() {
		return totalInterest;
	}
	public void setTotalInterest(BigDecimal totalInterest) {
		this.totalInterest = totalInterest;
	}
	public String getOrderEnableTime() {
		return orderEnableTime;
	}
	public BigDecimal getTotalrealPayAmount() {
		return totalrealPayAmount;
	}
	public void setTotalrealPayAmount(BigDecimal totalrealPayAmount) {
		this.totalrealPayAmount = totalrealPayAmount;
	}
	public void setOrderEnableTime(String orderEnableTime) {
		this.orderEnableTime = orderEnableTime;
	}
	/**
	 * 订单有效时间
	 */
	private String orderEnableTime;
	public String getBondToken() {
		return bondToken;
	}
	public void setBondToken(String bondToken) {
		this.bondToken = bondToken;
	}
	/**
	 * 需要授权类型
	 */
	private String authorizeType;
	
	/**
	 * 用户可用余额
	 */
	private BigDecimal userMoney;
	
	/**
	 * 实际转让方式  转让金额方式:0 部分转让;1全额转让,默认0
	 */
	private String sellStyle;
	/**
	 * 债权总额
	 */
	private BigDecimal bondMostMoney;
	/**
	 * 起投金额
	 */
	private BigDecimal bondLowestMoney;
	/**
	 *折溢价率  
	 */
	private BigDecimal	bondApr;
	/**
	 * 可转让金额   计算得来 
	 */
	private Double remainMoney;
	public String getAuthorizeType() {
		return authorizeType;
	}
	public void setAuthorizeType(String authorizeType) {
		this.authorizeType = authorizeType;
	}
	public BigDecimal getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}
	public String getSellStyle() {
		return sellStyle;
	}
	public void setSellStyle(String sellStyle) {
		this.sellStyle = sellStyle;
	}
	public BigDecimal getBondMostMoney() {
		return bondMostMoney;
	}
	public void setBondMostMoney(BigDecimal bondMostMoney) {
		this.bondMostMoney = bondMostMoney;
	}
	public BigDecimal getBondLowestMoney() {
		return bondLowestMoney;
	}
	public void setBondLowestMoney(BigDecimal bondLowestMoney) {
		this.bondLowestMoney = bondLowestMoney;
	}
	public BigDecimal getBondApr() {
		return bondApr;
	}
	public void setBondApr(BigDecimal bondApr) {
		this.bondApr = bondApr;
	}
	public Double getRemainMoney() {
		return remainMoney;
	}
	public void setRemainMoney(Double remainMoney) {
		this.remainMoney = remainMoney;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	/**
	 *原资产预期年化(预期年化收益率)
	 */
	private BigDecimal apr;
	
	
}

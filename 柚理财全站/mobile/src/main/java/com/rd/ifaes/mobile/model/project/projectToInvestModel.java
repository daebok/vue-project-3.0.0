/**
 * 投标初始化模型
 */
package com.rd.ifaes.mobile.model.project;

import java.math.BigDecimal;
/**
 * @author yoseflin
 * 投标初始化模型
 */
public class projectToInvestModel {
	/** 
	 * 红包可用最大比例 
	 */
	protected BigDecimal redEnvelopeRate;
	/** 
	 * 最高投资份数 
	 */
	private Integer mostCopies; 
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
	 *  借款期限 
	 */
	protected Integer timeLimit; 
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	/** 
	 * 安全等级 
	 */
	private String riskLevel; 
	/** 
	 * 项目总额
	 */
	protected BigDecimal account; 
	/** 
	 * 项目总额总收益 
	 */
	protected BigDecimal totalInterest; 
	/** 
	 * 当前用户可投 
	 */
	protected BigDecimal userCanInvestAmount; 
	/** 
	 * 投资金额方式:0 部分投资;1全额投资,默认0 
	 */ 
	private String	buyStyle;		 
	public String getBuyStyle() {
		return buyStyle;
	}
	public void setBuyStyle(String buyStyle) {
		this.buyStyle = buyStyle;
	}
	/** 
	 * 项目名称 
	 */
	private String projectName; 
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/** 
	 * 防重复标识
	 */
	private String investToken; 
	public String getRedEnvelopeUseful() {
		return redEnvelopeUseful;
	}
	public void setRedEnvelopeUseful(String redEnvelopeUseful) {
		this.redEnvelopeUseful = redEnvelopeUseful;
	}
	public String getAdditionalRateUseful() {
		return additionalRateUseful;
	}
	public void setAdditionalRateUseful(String additionalRateUseful) {
		this.additionalRateUseful = additionalRateUseful;
	}
	/** 
	 * 递增金额：整数、以元为单位 
	 */
	private BigDecimal stepAccount; 
	/** 
	 * 红包可用标识: 1 红包可用 0 红包不可用 
	 */
	protected String redEnvelopeUseful; 
	/** 
	 * 加息劵可用标识:1可用 0不可用
	 */
	protected String additionalRateUseful; 
	/** 
	 * 是否有可用红包: 1有 0否
	 */
	protected String ifRedEnvelopestatus; 
	/** 
	 * 是否有可用加息券: 1有 0否
	 */
	protected String ifAdditionalRate; 
	public String getIfRedEnvelopestatus() {
		return ifRedEnvelopestatus;
	}
	public void setIfRedEnvelopestatus(String ifRedEnvelopestatus) {
		this.ifRedEnvelopestatus = ifRedEnvelopestatus;
	}
	public String getIfAdditionalRate() {
		return ifAdditionalRate;
	}
	public void setIfAdditionalRate(String ifAdditionalRate) {
		this.ifAdditionalRate = ifAdditionalRate;
	}
	public BigDecimal getStepAccount() {
		return stepAccount;
	}
	public void setStepAccount(BigDecimal stepAccount) {
		this.stepAccount = stepAccount;
	}
	public String getInvestToken() {
		return investToken;
	}
	public void setInvestToken(String investToken) {
		this.investToken = investToken;
	}
	/**
	 * 剩余可投金额
	 */
	private Double remainAccount;
	/**
	 * 用户可用资金
	 */
	private BigDecimal userMoney;
	/** 
	 * 年利率 
	 */
	private BigDecimal apr; 	
	/** 
	 * 销售规则 ：1 按金额 2 按份数，默认1 
	 */
	private String saleStyle; 
	/**
	 * 订单有效时间
	 */
	private String orderEnableTime;
	/** 
	 * 最低起投金额 
	 */
	private BigDecimal lowestAccount; 
	/** 
	 * 最高投资总额 
	 */
	private BigDecimal mostAccount; 
	 /** 
	  * 总份数 
	  */
	private Integer totalCopies;
	/** 
	 * 每份金额
	 */
	private BigDecimal copyAccount; 
	/** 
	 * 最低起投份数 
	 */
	private Integer lowestCopies; 
	/** 
	 * 加息年利率
	 */	
	private BigDecimal addapr;
	/**
	 * 还款方式
	 */
	private String  repayStyle;
	public BigDecimal getRedEnvelopeRate() {
		return redEnvelopeRate;
	}
	public void setRedEnvelopeRate(BigDecimal redEnvelopeRate) {
		this.redEnvelopeRate = redEnvelopeRate;
	}
	public Integer getMostCopies() {
		return mostCopies;
	}
	public void setMostCopies(Integer mostCopies) {
		this.mostCopies = mostCopies;
	}
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
    public String getSaleStyle() {
		return saleStyle;
	}
	public void setSaleStyle(String saleStyle) {
		this.saleStyle = saleStyle;
	}
	public BigDecimal getTotalInterest() {
		return totalInterest;
	}
	public void setTotalInterest(BigDecimal totalInterest) {
		this.totalInterest = totalInterest;
	}
	public BigDecimal getUserCanInvestAmount() {
		return userCanInvestAmount;
	}
	public void setUserCanInvestAmount(BigDecimal userCanInvestAmount) {
		this.userCanInvestAmount = userCanInvestAmount;
	}
	public String getOrderEnableTime() {
		return orderEnableTime;
	}
	public void setOrderEnableTime(String orderEnableTime) {
		this.orderEnableTime = orderEnableTime;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public Double getRemainAccount() {
		return remainAccount;
	}
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
	public BigDecimal getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}
	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}
	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}
	public BigDecimal getMostAccount() {
		return mostAccount;
	}
	public void setMostAccount(BigDecimal mostAccount) {
		this.mostAccount = mostAccount;
	}
	public Integer getTotalCopies() {
		return totalCopies;
	}
	public void setTotalCopies(Integer totalCopies) {
		this.totalCopies = totalCopies;
	}
	public BigDecimal getCopyAccount() {
		return copyAccount;
	}
	public void setCopyAccount(BigDecimal copyAccount) {
		this.copyAccount = copyAccount;
	}
	public Integer getLowestCopies() {
		return lowestCopies;
	}
	public void setLowestCopies(Integer lowestCopies) {
		this.lowestCopies = lowestCopies;
	}
	public BigDecimal getAddapr() {
		return addapr;
	}
	public void setAddapr(BigDecimal addapr) {
		this.addapr = addapr;
	}
	public String getRepayStyle() {
		return repayStyle;
	}
	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}


	
}

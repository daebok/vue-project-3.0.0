package com.rd.ifaes.mobile.model.realize;

import java.math.BigDecimal;
import java.util.Date;

/***
 * @author LGX
 * 变现详情model
 */
public class realizedetailModel {
	 /** 
	  * 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 
	  */
	protected String oldRepayStyle;
	/** 
	 * 借款期限类型： 0月标 1天标
	 */
	protected String oldTimeType; 
	/**
	 * 项目ID
	 */
	private String	projectId;	
	/**
	 * 产品协议ID
	 */
	protected String protocolId;
	 public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	/** 
	  * 借款期限 
	  */
	protected Integer oldTimeLimit;
	/** 
	 * 项目名称 
	 */
	protected String oldProjectName;
	/** 
	 * 下架时间
	 */
	private Date saleEndTime; 
	/**
	 * 被变现项目ID
	 */
	private String	oldProjectId;
	public String getOldProjectId() {
		return oldProjectId;
	}
	public void setOldProjectId(String oldProjectId) {
		this.oldProjectId = oldProjectId;
	}
	public Date getSaleEndTime() {
		return saleEndTime;
	}
	public void setSaleEndTime(Date saleEndTime) {
		this.saleEndTime = saleEndTime;
	}
	public String getIfSaleStatus() {
		return ifSaleStatus;
	}
	public void setIfSaleStatus(String ifSaleStatus) {
		this.ifSaleStatus = ifSaleStatus;
	}
	/**
	 *  投标id 
	 */
	protected String investId;
	/**
	 *  是否在募集期 0否 1是
	 */
	protected String ifSaleStatus; 
	/** 
	 * 年利率 
	 */
	protected BigDecimal oldApr; 
	/** 
	 * 起投金额
	 */
	protected BigDecimal oldInvestAmount; 
	public long getTimeNow() {
		return timeNow;
	}
	public void setTimeNow(long timeNow) {
		this.timeNow = timeNow;
	}
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	/** 
	 * 当前时间
	 */
	private long timeNow;
	public String getIsVouch() {
		return isVouch;
	}
	public void setIsVouch(String isVouch) {
		this.isVouch = isVouch;
	}
	public String getVouchName() {
		return vouchName;
	}
	public void setVouchName(String vouchName) {
		this.vouchName = vouchName;
	}
	/** 
	 * 是否担保( 1 担保，0 不担保） 
	 */
	protected String isVouch; 
	/** 
	 * 担保公司名称 
	 */
	protected String vouchName; 
	public String getBorrowFlag() {
		return borrowFlag;
	}
	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	/** 
	 * 借款标标识(1是，借款标， 0否，即是理财产品） 
	 */
	protected String borrowFlag; 
	/**
	 * 收款日
	 */
	protected String oldRepayTime; 
	/** 
	 * 投资金额方式:0 部分投资;1全额投资,默认0 
	 */ 
	private String	buyStyle;		 
	/**
	 * 等级弹窗说明 
	 */
	private String userLevelTitle;
	public String getUserLevelTitle() {
		return userLevelTitle;
	}
	public void setUserLevelTitle(String userLevelTitle) {
		this.userLevelTitle = userLevelTitle;
	}
	/**
	 * 当前用户风险承受能力
	 */
	private String userLevelName;
	/**
	 *  投资条件  不加 投资为
     */
	private String tenderConditionstr; 
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public String getTenderConditionstr() {
		return tenderConditionstr;
	}
	public void setTenderConditionstr(String tenderConditionstr) {
		this.tenderConditionstr = tenderConditionstr;
	}
	/**
	 * 是否需要提示
	 */
	private String isTipe;
	public String getIsTipe() {
		return isTipe;
	}
	public void setIsTipe(String isTipe) {
		this.isTipe = isTipe;
	}
	public String getAccounts() {
		return accounts;
	}
	public String getBuyStyle() {
		return buyStyle;
	}
	public void setBuyStyle(String buyStyle) {
		this.buyStyle = buyStyle;
	}
	public String getOldRepayStyle() {
		return oldRepayStyle;
	}
	public void setOldRepayStyle(String oldRepayStyle) {
		this.oldRepayStyle = oldRepayStyle;
	}
	public String getOldTimeType() {
		return oldTimeType;
	}
	public void setOldTimeType(String oldTimeType) {
		this.oldTimeType = oldTimeType;
	}
	public Integer getOldTimeLimit() {
		return oldTimeLimit;
	}
	public void setOldTimeLimit(Integer oldTimeLimit) {
		this.oldTimeLimit = oldTimeLimit;
	}
	public String getOldProjectName() {
		return oldProjectName;
	}
	public void setOldProjectName(String oldProjectName) {
		this.oldProjectName = oldProjectName;
	}
	public BigDecimal getOldApr() {
		return oldApr;
	}
	public void setOldApr(BigDecimal oldApr) {
		this.oldApr = oldApr;
	}
	public BigDecimal getOldInvestAmount() {
		return oldInvestAmount;
	}
	public void setOldInvestAmount(BigDecimal oldInvestAmount) {
		this.oldInvestAmount = oldInvestAmount;
	}
	public String getOldRepayTime() {
		return oldRepayTime;
	}
	public void setOldRepayTime(String oldRepayTime) {
		this.oldRepayTime = oldRepayTime;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getRealNameStatus() {
		return RealNameStatus;
	}
	public void setRealNameStatus(String realNameStatus) {
		RealNameStatus = realNameStatus;
	}
	/** 
	 * 项目名称 
	 */
	private String projectName; 
	public String getSpecificSaleRule() {
		return specificSaleRule;
	}
	public void setSpecificSaleRule(String specificSaleRule) {
		this.specificSaleRule = specificSaleRule;
	}
	/**
	 * 当specificSale为2或3，后台会根据当前登录用户再校验， 不匹配，该项内容为错误信息；匹配，该项为空
	 */
	private String specificSaleRule;
	/** 
	 * 实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过 
	 */ 
	private String RealNameStatus; 
	/**
	 * 是否可点击 
	 */
	private String isClick;
	public String getIsClick() {
		return isClick;
	}
	public void setIsClick(String isClick) {
		this.isClick = isClick;
	}
	public String getClickTitle() {
		return clickTitle;
	}
	public void setClickTitle(String clickTitle) {
		this.clickTitle = clickTitle;
	}
	/**
	 * 点击 文本
	 */
	private String clickTitle;
	/**
	 * 点击code
	 */
	private String clickCode;
	public String getClickCode() {
		return clickCode;
	}
	public void setClickCode(String clickCode) {
		this.clickCode = clickCode;
	}
	/** 
	 * 项目总额
	 */
	protected BigDecimal account; 
	/** 
	 * 项目总额  说明
	 */
	protected String accounts; 
	/** 
	 * 募集期:募集期时长，单位：天 
	 */
	protected Integer raiseTimeLimit; 
	public String getRaiseTimeLimits() {
		return raiseTimeLimits;
	}
	public void setRaiseTimeLimits(String raiseTimeLimits) {
		this.raiseTimeLimits = raiseTimeLimits;
	}
	/** 
	 * 募集期:募集期时长，单位：天 
	 */
	protected String raiseTimeLimits; 
	public Integer getRaiseTimeLimit() {
		return raiseTimeLimit;
	}
	public void setRaiseTimeLimit(Integer raiseTimeLimit) {
		this.raiseTimeLimit = raiseTimeLimit;
	}
	/** 
	 * 计息方式: 1、成立计息 2、 T+N计息
	 */
	protected String interestStyle; 
	public String getInterestStyle() {
		return interestStyle;
	}
	public void setInterestStyle(String interestStyle) {
		this.interestStyle = interestStyle;
	}
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	/** 
	 * 安全等级 
	 */
	private String riskLevel; 
	/** 
	 * 投资条件
	 */
	private String tenderCondition; 
	/**
	 * 还款方式
	 */
	private String  repayStyle;
	 /** 
	  * 年利率
	  */
	private BigDecimal apr;	
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getTenderCondition() {
		return tenderCondition;
	}
	public void setTenderCondition(String tenderCondition) {
		this.tenderCondition = tenderCondition;
	}
	public String getRepayStyle() {
		return repayStyle;
	}
	public void setRepayStyle(String repayStyle) {
		if(!"".equals(repayStyle)){
			if("1".equals(repayStyle))/** 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 */
				this.repayStyle = "等额本息";
			if("2".equals(repayStyle))
				this.repayStyle = "一次性还款";
			if("3".equals(repayStyle))
				this.repayStyle = "每月还息到期还本";
			if("4".equals(repayStyle))
				this.repayStyle = "等额本金";
		}
	}
/**	public BigDecimal getAddApr() {
		return addApr;
	}
	public void setAddapr(BigDecimal addApr) {
		this.addApr = addApr;
	}*/
	//private BigDecimal addApr; /** 加息年利率 */	
	public String getSpecificSale() {
		return specificSale;
	}
	public void setSpecificSale(String specificSale) {
		this.specificSale = specificSale;
	}
	/** 
	 * 定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0)
	 */
	private String	specificSale;		 
	/** 
	 * 借款期限类型： 0月标 1天标 
	 */
	private String timeType; 
	/** 
	 * 借款期限 
	 */
	private Integer timeLimit; 
	 /** 
	  * 最低起投金额
	  */
	private BigDecimal lowestAccount;
	/** 
	 * 最高投资总额
	 */
	private BigDecimal mostAccount; 
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
	public BigDecimal getMostAccount() {
		return mostAccount;
	}
	public void setMostAccount(BigDecimal mostAccount) {
		this.mostAccount = mostAccount;
	}
	public Double getRemainAccount() {
		return remainAccount;
	}
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
	/**
	 * 剩余可投金额
	 */
    private Double remainAccount;

}

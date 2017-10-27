package com.rd.ifaes.mobile.model.project;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LGX
 * 标详情model
 */
public class projectdetailModel {
	/**
	 * VIP等级限制
	 */
	private String  vipLevel; 
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
	 *  借款标标识(1是，借款标， 0否，即是理财产品） 
	 */
	protected String borrowFlag; 
	/** 
	 * 是否在募集期 0否 1是
	 */
	protected String ifSaleStatus; 
	public long getTimeNow() {
		return timeNow;
	}
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public String getIfSaleStatus() {
		return ifSaleStatus;
	}
	public void setIfSaleStatus(String ifSaleStatus) {
		this.ifSaleStatus = ifSaleStatus;
	}
	public String getBorrowFlag() {
		return borrowFlag;
	}
	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	public Date getSaleEndTime() {
		return saleEndTime;
	}
	public void setSaleEndTime(Date saleEndTime) {
		this.saleEndTime = saleEndTime;
	}
	public String getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}
	public void setTimeNow(long timeNow) {
		this.timeNow = timeNow;
	}
	public String getAccounts() {
		return accounts;
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
	/**
	 *  当前时间
	 */
	private long timeNow;
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
	 *  实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过
	 */ 
	private String RealNameStatus; 
	
	public String getUserLevelTitle() {
		return userLevelTitle;
	}
	public void setUserLevelTitle(String userLevelTitle) {
		this.userLevelTitle = userLevelTitle;
	}
	/**
	 * 是否可点击
	 */
	private String isClick;
	/**
	 * 等级弹窗文本说明 
	 */
	private String userLevelTitle;
	/**
	 * 当前用户风险承受能力
	 */
	private String userLevelName;
	public BigDecimal getUserInvestAmount() {
		return userInvestAmount;
	}
	public void setUserInvestAmount(BigDecimal userInvestAmount) {
		this.userInvestAmount = userInvestAmount;
	}
	/**
	 * 累计投资金额
	 */
	private BigDecimal userInvestAmount;
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
	/**
	 * 是否需要提示
	 */
	private String isTipe;
	public String getClickCode() {
		return clickCode;
	}
	public void setClickCode(String clickCode) {
		this.clickCode = clickCode;
	}
	/**
	 *  项目总额
	 */
	protected BigDecimal account; 
	/**
	 *  项目总额  说明
	 */
	protected String accounts; 
	public String getIsTipe() {
		return isTipe;
	}
	public void setIsTipe(String isTipe) {
		this.isTipe = isTipe;
	}
	/**
	 *  剩余开标时长
	 */
	protected long timeToStart;
	public long getTimeToStart() {
		return timeToStart;
	}
	public void setTimeToStart(long timeToStart) {
		this.timeToStart = timeToStart;
	}
	/**
	 *  销售规则 ：1 按金额 2 按份数，默认1 
	 */
	private String saleStyle; 
	/**
	 *  募集期:募集期时长，单位：天 
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
	public String getSaleStyle() {
		return saleStyle;
	}
	public void setSaleStyle(String saleStyle) {
		this.saleStyle = saleStyle;
	}
	/**
	 *  计息方式: 1、成立计息 2、 T+N计息
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
	 *  安全等级
	 */
	private String riskLevel; 
	public Date getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	/**
	 *  投资条件 
     */
	private String tenderCondition; 
	/**
	 *  投资条件  不加 投资为
     */
	private String tenderConditionstr; 
	public String getTenderConditionstr() {
		return tenderConditionstr;
	}
	public void setTenderConditionstr(String tenderConditionstr) {
		this.tenderConditionstr = tenderConditionstr;
	}
	/**
	 *  下架时间 
	 */
	private Date stopTime; 
	/** 
	 * 预售时间 
	 */
	private Date saleTime; 
	/**
	 *  销售结束时间 
	 */
	private Date saleEndTime; 
	/**
	 * 还款方式
	 */
	private String  repayStyle;
	/**
	 * 是否开售 1 是 0否
	 */
	private String  ifSale;
	
	public String getIfSale() {
		return ifSale;
	}
	public void setIfSale(String ifSale) {
		this.ifSale = ifSale;
	}
	/**
	 *  年利率
	 */
	private BigDecimal apr; 	
    public String getRealizeUseful() {
		return realizeUseful;
	}
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
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public String getRepayStyle() {
		return repayStyle;
	}
	public void setRepayStyle(String repayStyle,Integer fixedRepayDay) {
		if(!"".equals(repayStyle)){
			if("1".equals(repayStyle)){/* 还款方式: 1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金 */
				this.repayStyle = "等额本息";
				}else if("2".equals(repayStyle)){
				this.repayStyle = "一次性还款";
			}else if("3".equals(repayStyle)){
				if(fixedRepayDay>0){
				this.repayStyle = "每月还息到期还本(固定还款日"+fixedRepayDay+"日)";
			    }else{
				this.repayStyle = "每月还息到期还本";
			    }
			}else if("4".equals(repayStyle)){
				this.repayStyle = "等额本金";
			}else if("5".equals(repayStyle)){
				if(fixedRepayDay>0){
				this.repayStyle = "每季还息到期还本(固定还款日"+fixedRepayDay+"日)";
				}else{
				this.repayStyle = "每季还息到期还本";
			}
		}
		}
	}
	public void setRealizeUseful(String realizeUseful) {
		this.realizeUseful = realizeUseful;
	}
	/**
	 *  是否可变现: 1 可变现 0 不可变现，默认 0 
	 */
	private String realizeUseful; 
	public BigDecimal getAddApr() {
		return addApr;
	}
	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}
	/**
	 *  加息年利率
	 */	
	private BigDecimal addApr; 
	/**
	 *  是否可以债权转让 1 可以，0不可以
	 */
	 private String bondUseful; 
	 /**
	  *  新手标标识： 1新手专享 0 普通 ，默认：0 
	  */
	 private String novice; 
	 public String getBondUseful() {
		return bondUseful;
	}
	public void setBondUseful(String bondUseful) {
		this.bondUseful = bondUseful;
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
	/**
	 *  定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0) 
	 */
	private String	specificSale;		
	/**
	 *  借款期限类型： 0月标 1天标 
	 */
	private String timeType; 
	/**
	 *  是否担保( 1 担保，0 不担保）
	 */
	protected String isVouch; 
	/**
	 *  担保公司名称
	 */
	protected String vouchName; 
	
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
	 * 借款期限
	 */
	private Integer timeLimit; 
	/**
	 *  最低起投金额 
	 */
	private BigDecimal lowestAccount; 
	/**
	 *  最高投资总额 
	 */
	private BigDecimal mostAccount; 
	/**
	 *  总份数 
	 */
	private Integer totalCopies; 
	/**
	 *  每份金额 
	 */
	private BigDecimal copyAccount; 
	/**
	 *  最低起投份数 
	 */
	private Integer lowestCopies; 
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
	public Integer getMostCopies() {
		return mostCopies;
	}
	public void setMostCopies(Integer mostCopies) {
		this.mostCopies = mostCopies;
	}
	public Double getRemainAccount() {
		return remainAccount;
	}
	public void setRemainAccount(Double remainAccount) {
		this.remainAccount = remainAccount;
	}
	/**
	 *  最高投资份数 
	 */
	private Integer mostCopies; 
	/**
	 * 剩余可投金额
	 */
    private Double remainAccount;
	/**
	 * 是否已预约 1 是 0 否
	 */
	private BigDecimal  bespeak;

	/**
	 * 预约人数
	 */
	private Long  bespeaNum;
	public BigDecimal getBespeak() {
		return bespeak;
	}
	public void setBespeak(BigDecimal bespeak) {
		this.bespeak = bespeak;
	}
	public Long getBespeaNum() {
		return bespeaNum;
	}
	public void setBespeaNum(Long bespeaNum) {
		this.bespeaNum = bespeaNum;
	}

}

/**
 * banner模型
 */
package com.rd.ifaes.mobile.model.bond;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LGX banner模型
 */
public class bondItemdetailModel {

	/**
	 *  是否可点击
	 */
	private String isClick;
	
	/**
	 *  等级弹窗文本说明 
	 */
	private String userLevelTitle;
	
	/**
	 *  是否需要提示
	 */
	private String isTipe;
	
	/**
	 *  销售结束时间
	 */
	private Date saleEndTime; 
	
	/**
	 *  当前时间
	 */
	private long timeNow;
	
	/**
	 *  点击 文本 
	 */
	private String clickTitle;
	
	/**
	 *  点击code 
	 */
	private String clickCode;
	
	/**
	 * 债权名称
	 */
	private String bondName;

	public String getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}
	/** 
	 * 实际转让方式 转让金额方式:0 部分转让;1全额转让,默认0 
	 */
	private String sellStyle;
	/**
	 * 协议ID
	 */
	protected String protocolId;
	/**
	 * 原项目id
	 */
	private String oldProjectId;
	
	/**
	 * 计息方式
	 */
	private String RealNameStatus;

	/**
	 *  实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过 
	 */
	private String interestStyle;
	
	/**
	 * 还款方式
	 */
	private String repayStyle;
	
	/**
	 * 安全等级
	 */
	private String riskLevel;
	
	/**
	 * 安全等级说明
	 */
	private String riskLevels;
	
	/**
	 * 投资条件
	 */
	private String tenderCondition;

	/**
	 * 投资条件说明
	 */
	private String tenderConditions;
	
	/**
	 * 剩余期限 时间
	 */
	private Date dataTime;
	
	/**
	 * 债权状态:发布0， 转让完成3，自动撤回4，后台撤回5
	 */
	private String status;
	
	/**
	 * 剩余期限（天）
	 */
	private int remainDay;
	
	/**
	 * 剩余期限（天）
	 */
	private String remainDays;
	
	/**
	 * 债权总额
	 */
	private BigDecimal bondMostMoney;
	
	/**
	 * 债权总额说明
	 */
	private String bondMostMoneys;
	
	/**
	 * 起投金额
	 */
	private BigDecimal bondLowestMoney;

	/**
	 * 折溢价率
	 */
	private BigDecimal bondApr;
	
	/**
	 * 可转让金额 计算得来
	 */
	private Double remainMoney;
	
	/**
	 * 原资产预期年化(预期年化收益率)
	 */
	private BigDecimal apr;
	
	/**
	 * 是否为借贷产品
	 */
	private String borrowFlag;
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
	 * 当前用户风险承受能力
	 */
	private String userLevelName;
	public Date getSaleEndTime() {
		return saleEndTime;
	}

	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public void setSaleEndTime(Date saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public long getTimeNow() {
		return timeNow;
	}

	public void setTimeNow(long timeNow) {
		this.timeNow = timeNow;
	}

	public String getUserLevelTitle() {
		return userLevelTitle;
	}

	public void setUserLevelTitle(String userLevelTitle) {
		this.userLevelTitle = userLevelTitle;
	}

	public String getIsTipe() {
		return isTipe;
	}

	public void setIsTipe(String isTipe) {
		this.isTipe = isTipe;
	}

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

	public String getClickCode() {
		return clickCode;
	}

	public void setClickCode(String clickCode) {
		this.clickCode = clickCode;
	}

	public String getOldProjectId() {
		return oldProjectId;
	}

	public void setOldProjectId(String oldProjectId) {
		this.oldProjectId = oldProjectId;
	}

	public String getRealNameStatus() {
		return RealNameStatus;
	}

	public void setRealNameStatus(String realNameStatus) {
		RealNameStatus = realNameStatus;
	}

	public String getBondMostMoneys() {
		return bondMostMoneys;
	}

	public String getRiskLevels() {
		return riskLevels;
	}

	public void setRiskLevels(String riskLevels) {
		this.riskLevels = riskLevels;
	}

	public String getTenderConditions() {
		return tenderConditions;
	}

	public void setTenderConditions(String tenderConditions) {
		this.tenderConditions = tenderConditions;
	}

	public String getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}

	public void setBondMostMoneys(String bondMostMoneys) {
		this.bondMostMoneys = bondMostMoneys;
	}

	public String getSellStyle() {
		return sellStyle;
	}

	public void setSellStyle(String sellStyle) {
		this.sellStyle = sellStyle;
	}

	public BigDecimal getApr() {
		return apr;
	}

	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}

	public Double getRemainMoney() {
		return remainMoney;
	}

	public void setRemainMoney(Double remainMoney) {
		this.remainMoney = remainMoney;
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

	public String getInterestStyle() {
		return interestStyle;
	}

	public void setInterestStyle(String interestStyle) {
		this.interestStyle = interestStyle;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
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

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public String getBondName() {
		return bondName;
	}

	public void setBondName(String bondName) {
		this.bondName = bondName;
	}

	public BigDecimal getBondApr() {
		return bondApr;
	}

	public int getRemainDay() {
		return remainDay;
	}

	public void setRemainDay(int remainDay) {
		this.remainDay = remainDay;
	}

	public void setBondApr(BigDecimal bondApr) {
		this.bondApr = bondApr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBorrowFlag() {
		return borrowFlag;
	}

	public void setBorrowFlag(String borrowFlag) {
		this.borrowFlag = borrowFlag;
	}
	
}

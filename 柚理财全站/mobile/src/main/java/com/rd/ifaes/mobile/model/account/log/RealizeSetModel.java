package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;

public class RealizeSetModel {
    /**
     * 变现期限
     */
	private int timeLimit;
	/**
	 * 所持资产
	 */
	private BigDecimal waitAmount;
	/**
	 * 预期收益
	 */
	private BigDecimal waitInterest;
	/**
	 * 每年的天数，用于还款时计息的计算
	 */
	private String daysOfYear;
	/**
	 * 变现利率下限值
	 */
	private BigDecimal realizeRateMin;
	/**
	 *  变现利率上限值
	 */
	private BigDecimal realizeRateMax;
	/**
	 * 主键:UUID
	 */
	private String uuid;
	private String warmTips;
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public BigDecimal getWaitAmount() {
		return waitAmount;
	}
	public void setWaitAmount(BigDecimal waitAmount) {
		this.waitAmount = waitAmount;
	}
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}
	public String getDaysOfYear() {
		return daysOfYear;
	}
	public void setDaysOfYear(String daysOfYear) {
		this.daysOfYear = daysOfYear;
	}
	public BigDecimal getRealizeRateMin() {
		return realizeRateMin;
	}
	public void setRealizeRateMin(BigDecimal realizeRateMin) {
		this.realizeRateMin = realizeRateMin;
	}
	public BigDecimal getRealizeRateMax() {
		return realizeRateMax;
	}
	public void setRealizeRateMax(BigDecimal realizeRateMax) {
		this.realizeRateMax = realizeRateMax;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getWarmTips() {
		return warmTips;
	}
	public void setWarmTips(String warmTips) {
		this.warmTips = warmTips;
	}
	
	
}

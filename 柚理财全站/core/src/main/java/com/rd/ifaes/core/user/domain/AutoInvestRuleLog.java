package com.rd.ifaes.core.user.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:AutoInvestRuleLog
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public class AutoInvestRuleLog extends BaseEntity<AutoInvestRuleLog> {
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal	amountDayMax;		 /* 每日最大投资金额 */ 
	private String	repayStyles;		 /* 还款方式 */ 
	private String	monthType;		 /* 是否可投月期限 */ 
	private Integer	monthLimitMin;		 /* 月期限最小值 */ 
	private Integer	monthLimitMax;		 /* 月截至期限 */ 
	private String	dayType;		 /* 是否可投天期限 */ 
	private Integer	dayLimitMin;		 /* 天期限最小值 */ 
	private Integer	dayLimitMax;		 /* 天期限最大值 */ 
	private BigDecimal	aprMin;		 /* 投资收益最小值 */ 
	private String	realizeUseful;		 /*  是否可投变现产品 0 不可投1可投，默认0 */ 
	private String	bondUseful;		 /*  是否可投债权转让0 不可投1可投，默认0 */ 
	private String	status;		 /* 状态 0.关闭 1开启 */ 
	private Date	createTime;		 /* 添加时间 */ 
	
	private int redType;//红包类型 1最早到期日 2最大金额
	
	private int increaseType;// 加息券类型 1最早到期日 2最大金额

	//其他自定义属性
	

	// Constructor
	public AutoInvestRuleLog() {
	}

	/**
	 * full Constructor
	 */
	public AutoInvestRuleLog(String uuid, BigDecimal amountDayMax, String repayStyles, String monthType, Integer monthLimitMin, Integer monthLimitMax, String dayType, Integer dayLimitMin, Integer dayLimitMax, BigDecimal aprMin, String realizeUseful, String bondUseful, String status, Date createTime) {
		setUuid(uuid);
		this.amountDayMax = amountDayMax;
		this.repayStyles = repayStyles;
		this.monthType = monthType;
		this.monthLimitMin = monthLimitMin;
		this.monthLimitMax = monthLimitMax;
		this.dayType = dayType;
		this.dayLimitMin = dayLimitMin;
		this.dayLimitMax = dayLimitMax;
		this.aprMin = aprMin;
		this.realizeUseful = realizeUseful;
		this.bondUseful = bondUseful;
		this.status = status;
		this.createTime = createTime;
	}

	public BigDecimal getAmountDayMax() {
		return amountDayMax;
	}

	public void setAmountDayMax(BigDecimal amountDayMax) {
		this.amountDayMax = amountDayMax;
	}

	public String getRepayStyles() {
		return repayStyles;
	}

	public void setRepayStyles(String repayStyles) {
		this.repayStyles = repayStyles;
	}

	public String getMonthType() {
		return monthType;
	}

	public void setMonthType(String monthType) {
		this.monthType = monthType;
	}

	public Integer getMonthLimitMin() {
		return monthLimitMin;
	}

	public void setMonthLimitMin(Integer monthLimitMin) {
		this.monthLimitMin = monthLimitMin;
	}

	public Integer getMonthLimitMax() {
		return monthLimitMax;
	}

	public void setMonthLimitMax(Integer monthLimitMax) {
		this.monthLimitMax = monthLimitMax;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public Integer getDayLimitMin() {
		return dayLimitMin;
	}

	public void setDayLimitMin(Integer dayLimitMin) {
		this.dayLimitMin = dayLimitMin;
	}

	public Integer getDayLimitMax() {
		return dayLimitMax;
	}

	public void setDayLimitMax(Integer dayLimitMax) {
		this.dayLimitMax = dayLimitMax;
	}

	public BigDecimal getAprMin() {
		return aprMin;
	}

	public void setAprMin(BigDecimal aprMin) {
		this.aprMin = aprMin;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getRedType() {
		return redType;
	}

	public void setRedType(int redType) {
		this.redType = redType;
	}

	public int getIncreaseType() {
		return increaseType;
	}

	public void setIncreaseType(int increaseType) {
		this.increaseType = increaseType;
	}

	@Override
	public String toString() {
		return "AutoInvestRuleLog [amountDayMax=" + amountDayMax + ", repayStyles=" + repayStyles + ", monthType=" + monthType
				+ ", monthLimitMin=" + monthLimitMin + ", monthLimitMax=" + monthLimitMax + ", dayType=" + dayType
				+ ", dayLimitMin=" + dayLimitMin + ", dayLimitMax=" + dayLimitMax + ", aprMin=" + aprMin + ", realizeUseful="
				+ realizeUseful + ", bondUseful=" + bondUseful + ", status=" + status + ", createTime=" + createTime
				+ ", redType=" + redType + ", increaseType=" + increaseType + "]";
	}

}

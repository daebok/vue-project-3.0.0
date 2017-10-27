package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 还款统计
 * @version 3.0
 * @author fxl
 * @date 2017年3月1日
 */
public class StatisticRepay extends BaseEntity<StatisticRepay> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日期
	 */
	private Date recordDate;
	
	/**
	 * 还款状态 0未还 1 已还
	 */
	private String repayStatus;
	
	/**
	 * 借款类型
	 */
	private String borrowType;
	
	/**
	 * 借款性质
	 */
	private String borrowNature;
	
	/**
	 * 总额
	 */
	private BigDecimal	payment;
	
	/**
	 * 本金
	 */
	private BigDecimal	capital;
	
	/**
	 * 利息
	 */
	private BigDecimal	interest;
	
	/**
	 * 笔数
	 */
	private BigDecimal	repayCount;

	
	// Constructor
	public StatisticRepay() {
	}

	/**
	 * full Constructor
	 */
	public StatisticRepay(Date recordDate, String repayStatus, String borrowType, String borrowNature, BigDecimal payment, BigDecimal capital, BigDecimal interest, BigDecimal repayCount) {
		this.recordDate = recordDate;
		this.repayStatus = repayStatus;
		this.borrowType = borrowType;
		this.borrowNature = borrowNature;
		this.payment = payment;
		this.capital = capital;
		this.interest = interest;
		this.repayCount = repayCount;
	}
	
	/**
	 * 获取属性recordDate的值
	 * @return recordDate属性值
	 */
	public Date getRecordDate() {
		return recordDate;
	}

	/**
	 * 设置属性recordDate的值
	 * @param  recordDate属性值
	 */
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * 获取属性repayStatus的值
	 * @return repayStatus属性值
	 */
	public String getRepayStatus() {
		return repayStatus;
	}

	/**
	 * 设置属性repayStatus的值
	 * @param  repayStatus属性值
	 */
	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	/**
	 * 获取属性borrowType的值
	 * @return borrowType属性值
	 */
	public String getBorrowType() {
		return borrowType;
	}

	/**
	 * 设置属性borrowType的值
	 * @param  borrowType属性值
	 */
	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	/**
	 * 获取属性borrowNature的值
	 * @return borrowNature属性值
	 */
	public String getBorrowNature() {
		return borrowNature;
	}

	/**
	 * 设置属性borrowNature的值
	 * @param  borrowNature属性值
	 */
	public void setBorrowNature(String borrowNature) {
		this.borrowNature = borrowNature;
	}

	/**
	 * 获取属性payment的值
	 * @return payment属性值
	 */
	public BigDecimal getPayment() {
		return payment;
	}

	/**
	 * 设置属性payment的值
	 * @param  payment属性值
	 */
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	/**
	 * 获取属性capital的值
	 * @return capital属性值
	 */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * 设置属性capital的值
	 * @param  capital属性值
	 */
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	/**
	 * 获取属性interest的值
	 * @return interest属性值
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * 设置属性interest的值
	 * @param  interest属性值
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * 获取属性repayCount的值
	 * @return repayCount属性值
	 */
	public BigDecimal getRepayCount() {
		return repayCount;
	}

	/**
	 * 设置属性repayCount的值
	 * @param  repayCount属性值
	 */
	public void setRepayCount(BigDecimal repayCount) {
		this.repayCount = repayCount;
	}
	
	@Override
	public String toString() {
		return "StatisticRepay [" + "uuid=" + uuid + ", recordDate=" + recordDate + ", repayStatus=" + repayStatus + ", borrowType=" + borrowType + ", borrowNature=" + borrowNature + ", payment=" + payment + ", capital=" + capital + ", interest=" + interest + ", repayCount=" + repayCount +  "]";
	}
	
}

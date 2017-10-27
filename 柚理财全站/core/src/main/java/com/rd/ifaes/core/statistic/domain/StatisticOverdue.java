package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 逾期统计
 * @version 3.0
 * @author fxl
 * @date 2017年3月10日
 */
public class StatisticOverdue extends BaseEntity<StatisticOverdue> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日期
	 */
	private Date recordDate;
	
	/**
	 * 统计类型：1-人数，2-金额，3-笔数
	 */
	private String statType;
	
	/**
	 * 总数
	 */
	private BigDecimal totalCount;

	/**
	 * 第一级 90天以下
	 */
	private BigDecimal firstSeries;
	
	/**
	 * 第二级 90-180
	 */
	private BigDecimal secondSeries;
	
	/**
	 * 第三级 180天以上
	 */
	private BigDecimal thirdSeries;

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
	 * 获取属性statType的值
	 * @return statType属性值
	 */
	public String getStatType() {
		return statType;
	}

	/**
	 * 设置属性statType的值
	 * @param  statType属性值
	 */
	public void setStatType(String statType) {
		this.statType = statType;
	}

	/**
	 * 获取属性totalCount的值
	 * @return totalCount属性值
	 */
	public BigDecimal getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置属性totalCount的值
	 * @param  totalCount属性值
	 */
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获取属性firstSeries的值
	 * @return firstSeries属性值
	 */
	public BigDecimal getFirstSeries() {
		return firstSeries;
	}

	/**
	 * 设置属性firstSeries的值
	 * @param  firstSeries属性值
	 */
	public void setFirstSeries(BigDecimal firstSeries) {
		this.firstSeries = firstSeries;
	}

	/**
	 * 获取属性secondSeries的值
	 * @return secondSeries属性值
	 */
	public BigDecimal getSecondSeries() {
		return secondSeries;
	}

	/**
	 * 设置属性secondSeries的值
	 * @param  secondSeries属性值
	 */
	public void setSecondSeries(BigDecimal secondSeries) {
		this.secondSeries = secondSeries;
	}

	/**
	 * 获取属性thirdSeries的值
	 * @return thirdSeries属性值
	 */
	public BigDecimal getThirdSeries() {
		return thirdSeries;
	}

	/**
	 * 设置属性thirdSeries的值
	 * @param  thirdSeries属性值
	 */
	public void setThirdSeries(BigDecimal thirdSeries) {
		this.thirdSeries = thirdSeries;
	}
	
	
}

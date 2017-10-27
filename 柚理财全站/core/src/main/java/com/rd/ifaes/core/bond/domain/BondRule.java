/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.bond.model.BondRuleModel;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 债权规则类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
public class BondRule extends BaseEntity<BondRule> {

	private static final long serialVersionUID = 1L;
	/** 添加时间 */
	private Date createTime;
	/** 1 开启 0未开启 默认1,未开启不允许发布债权 */
	private String ruleStatus;
	/** 起息持有天数 默认 0 不开启此规则 */
	private Integer holdDays;
	/** 还款到期剩余天数,默认0 不开启此规则 */
	private Integer remainDays;
	/** 本期还款剩余天数 0 不开启此规则 */
	private Integer periodRemainDays;
	/** 转让时效（单位：小时，到时间自动撤回） */
	private Integer deadline;
	/** 转让起投金额倍数 */
	private Integer sellMultiple;
	/** 转让金额方式:0 部分转让;1全额转让,默认0 */
	private String sellStyle;
	/** 受让金额方式:0 部分受让;1全额受让,默认0 */
	private String buyStyle;
	/** 单笔受让最小金额 */
	private double buyAmountMin;
	/** 单笔受让最大金额 */
	private double buyAmountMax;
	/** 折溢价率下限值 */
	private double discountRateMin;
	/** 折溢价率上限值 */
	private double discountRateMax;
	/** 收费方式 0不收费 1固定金额 2 固定比例; 默认1 */
	private String feeStyle;
	/** 收费起步金额 */
	private double feeInitiateAmount;
	/** 固定金额手续费 */
	private double feeFixed;
	/** 转让金额百分比收取手续费（默认为0，单位是%） */
	private double feeRate;
	/** 单笔手续费上限值（单位:元） */
	private double feeSingleMax;

	// 其他自定义属性
	/**
	 * 构造器
	 */
	public BondRule() {
		super();
	}

	/**
	 * full Constructor
	 */
	public BondRule(final String uuid, final Date createTime,
			final String ruleStatus, final Integer holdDays,
			final Integer remainDays, final Integer periodRemainDays,
			final Integer deadline, final Integer sellMultiple,
			final String sellStyle, final String buyStyle,
			final double buyAmountMin, final double buyAmountMax,
			final double discountRateMin, final double discountRateMax,
			final String feeStyle, final double feeInitiateAmount,
			final double feeFixed, final double feeRate,
			final double feeSingleMax) {
		super();
		setUuid(uuid);
		this.createTime = createTime;
		this.ruleStatus = ruleStatus;
		this.holdDays = holdDays;
		this.remainDays = remainDays;
		this.periodRemainDays = periodRemainDays;
		this.deadline = deadline;
		this.sellMultiple = sellMultiple;
		this.sellStyle = sellStyle;
		this.buyStyle = buyStyle;
		this.buyAmountMin = buyAmountMin;
		this.buyAmountMax = buyAmountMax;
		this.discountRateMin = discountRateMin;
		this.discountRateMax = discountRateMax;
		this.feeStyle = feeStyle;
		this.feeInitiateAmount = feeInitiateAmount;
		this.feeFixed = feeFixed;
		this.feeRate = feeRate;
		this.feeSingleMax = feeSingleMax;
	}

	/**
	 * 复制属性方法
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param model
	 * @return
	 */
	public BondRule instance(final BondRuleModel model) {
		final BondRule rule = new BondRule();
		BeanUtils.copyProperties(model, rule);
		return rule;
	}

	/**
	 * 复制属性
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param rule
	 * @return
	 */
	public BondRuleModel instanceModel(final BondRule rule) {
		final BondRuleModel model = new BondRuleModel();
		BeanUtils.copyProperties(rule, model);
		return model;
	}

	/**
	 * 重写添加方法
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		this.discountRateMax = BigDecimalUtils.round(BigDecimal.valueOf(this.discountRateMax), Constant.INT_ONE).doubleValue();
		this.discountRateMin = BigDecimalUtils.round(BigDecimal.valueOf(this.discountRateMin), Constant.INT_ONE).doubleValue();
		super.preInsert();
	}

	/**
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return
	 */
	public String getRuleStatus() {
		return ruleStatus;
	}

	/**
	 * @param ruleStatus
	 */
	public void setRuleStatus(final String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	/**
	 * @return
	 */
	public Integer getHoldDays() {
		return holdDays;
	}

	/**
	 * @param holdDays
	 */
	public void setHoldDays(final Integer holdDays) {
		this.holdDays = holdDays;
	}

	/**
	 * @return
	 */
	public Integer getRemainDays() {
		return remainDays;
	}

	/**
	 * @param remainDays
	 */
	public void setRemainDays(final Integer remainDays) {
		this.remainDays = remainDays;
	}

	/**
	 * @return
	 */
	public Integer getPeriodRemainDays() {
		return periodRemainDays;
	}

	/**
	 * @param periodRemainDays
	 */
	public void setPeriodRemainDays(final Integer periodRemainDays) {
		this.periodRemainDays = periodRemainDays;
	}

	/**
	 * @return
	 */
	public Integer getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline
	 */
	public void setDeadline(final Integer deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return
	 */
	public Integer getSellMultiple() {
		return sellMultiple;
	}

	/**
	 * @param sellMultiple
	 */
	public void setSellMultiple(final Integer sellMultiple) {
		this.sellMultiple = sellMultiple;
	}

	/**
	 * @return
	 */
	public String getSellStyle() {
		return sellStyle;
	}

	/**
	 * @param sellStyle
	 */
	public void setSellStyle(final String sellStyle) {
		this.sellStyle = sellStyle;
	}

	/**
	 * @return
	 */
	public String getBuyStyle() {
		return buyStyle;
	}

	/**
	 * @param buyStyle
	 */
	public void setBuyStyle(final String buyStyle) {
		this.buyStyle = buyStyle;
	}

	/**
	 * @return
	 */
	public double getBuyAmountMin() {
		return buyAmountMin;
	}

	/**
	 * @param buyAmountMin
	 */
	public void setBuyAmountMin(final double buyAmountMin) {
		this.buyAmountMin = buyAmountMin;
	}

	/**
	 * @return
	 */
	public double getBuyAmountMax() {
		return buyAmountMax;
	}

	/**
	 * @param buyAmountMax
	 */
	public void setBuyAmountMax(final double buyAmountMax) {
		this.buyAmountMax = buyAmountMax;
	}

	/**
	 * @return
	 */
	public double getDiscountRateMin() {
		return discountRateMin;
	}

	/**
	 * @param discountRateMin
	 */
	public void setDiscountRateMin(final double discountRateMin) {
		this.discountRateMin = discountRateMin;
	}

	/**
	 * @return
	 */
	public double getDiscountRateMax() {
		return discountRateMax;
	}

	/**
	 * @param discountRateMax
	 */
	public void setDiscountRateMax(final double discountRateMax) {
		this.discountRateMax = discountRateMax;
	}

	/**
	 * @return
	 */
	public String getFeeStyle() {
		return feeStyle;
	}

	/**
	 * @param feeStyle
	 */
	public void setFeeStyle(final String feeStyle) {
		this.feeStyle = feeStyle;
	}

	/**
	 * @return
	 */
	public double getFeeInitiateAmount() {
		return feeInitiateAmount;
	}

	/**
	 * @param feeInitiateAmount
	 */
	public void setFeeInitiateAmount(final double feeInitiateAmount) {
		this.feeInitiateAmount = feeInitiateAmount;
	}

	/**
	 * @return
	 */
	public double getFeeFixed() {
		return feeFixed;
	}

	/**
	 * @param feeFixed
	 */
	public void setFeeFixed(final double feeFixed) {
		this.feeFixed = feeFixed;
	}

	/**
	 * @return
	 */
	public double getFeeRate() {
		return feeRate;
	}

	/**
	 * @param feeRate
	 */
	public void setFeeRate(final double feeRate) {
		this.feeRate = feeRate;
	}

	/**
	 * @return
	 */
	public double getFeeSingleMax() {
		return feeSingleMax;
	}

	/**
	 * @param feeSingleMax
	 */
	public void setFeeSingleMax(final double feeSingleMax) {
		this.feeSingleMax = feeSingleMax;
	}

	/**
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "BondRule [" + "uuid=" + uuid + ", createTime=" + createTime
				+ ", ruleStatus=" + ruleStatus + ", holdDays=" + holdDays
				+ ", remainDays=" + remainDays + ", periodRemainDays="
				+ periodRemainDays + ", deadline=" + deadline
				+ ", sellMultiple=" + sellMultiple + ", sellStyle=" + sellStyle
				+ ", buyStyle=" + buyStyle + ", buyAmountMin=" + buyAmountMin
				+ ", buyAmountMax=" + buyAmountMax + ", discountRateMin="
				+ discountRateMin + ", discountRateMax=" + discountRateMax
				+ ", feeStyle=" + feeStyle + ", feeInitiateAmount="
				+ feeInitiateAmount + ", feeFixed=" + feeFixed + ", feeRate="
				+ feeRate + ", feeSingleMax=" + feeSingleMax + "]";
	}
}

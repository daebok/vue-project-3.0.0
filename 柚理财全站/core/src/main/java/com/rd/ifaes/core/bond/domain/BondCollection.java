/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 债权待收类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
public class BondCollection extends BaseEntity<BondCollection> {

	private static final long serialVersionUID = 1L;
	/** 项目ID */
	private String projectId;
	/** 投标ID */
	private String investId;
	/** 投资人 */
	private String userId;
	/** 债权ID */
	private String bondId;
	/** 债权投资ID */
	private String bondInvestId;
	/** 还款ID */
	private String repaymentId;
	/** 已成功转出本金 */
	private BigDecimal bondCapital;
	/** 已成功转出利息 */
	private BigDecimal bondInterest;
	/** 待收本金 */
	private BigDecimal capital;
	/** 预计收款金额 */
	private BigDecimal collectionAmount;
	/** 预计收款时间 */
	private Date collectionTime;
	/** 实际收款金额 */
	private BigDecimal receivedAmount;
	/** 实际收款时间 */
	private Date receivedTime;
	/** 待收利息 */
	private BigDecimal interest;
	/** 预计天数 */
	private Integer lateDays;
	/** 逾期利息 */
	private BigDecimal lateInterest;
	/** 待收期数 */
	private byte period;
	/** 状态：0未还，1已还 */
	private String status;
	/** 添加时间 */
	private Date createTime;

	// 其他自定义属性

	/**
	 * Constructor
	 */
	public BondCollection() {
		super();
	}

	/**
	 * Constructor
	 */
	public BondCollection(final String bondId) {
		super();
		this.bondId = bondId;
	}

	/**
	 * @return
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * @param investId
	 */
	public void setInvestId(final String investId) {
		this.investId = investId;
	}

	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public String getBondId() {
		return bondId;
	}

	/**
	 * @param bondId
	 */
	public void setBondId(final String bondId) {
		this.bondId = bondId;
	}

	/**
	 * @return
	 */
	public String getBondInvestId() {
		return bondInvestId;
	}

	/**
	 * @param bondInvestId
	 */
	public void setBondInvestId(final String bondInvestId) {
		this.bondInvestId = bondInvestId;
	}

	/**
	 * @return
	 */
	public String getRepaymentId() {
		return repaymentId;
	}

	/**
	 * @param repaymentId
	 */
	public void setRepaymentId(final String repaymentId) {
		this.repaymentId = repaymentId;
	}

	/**
	 * @return
	 */
	public BigDecimal getBondCapital() {
		return bondCapital;
	}

	/**
	 * @param bondCapital
	 */
	public void setBondCapital(final BigDecimal bondCapital) {
		this.bondCapital = bondCapital;
	}

	/**
	 * @return
	 */
	public BigDecimal getBondInterest() {
		return bondInterest;
	}

	/**
	 * @param bondInterest
	 */
	public void setBondInterest(final BigDecimal bondInterest) {
		this.bondInterest = bondInterest;
	}

	/**
	 * @return
	 */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * @param capital
	 */
	public void setCapital(final BigDecimal capital) {
		this.capital = capital;
	}

	/**
	 * @return
	 */
	public BigDecimal getCollectionAmount() {
		return collectionAmount;
	}

	/**
	 * @param collectionAmount
	 */
	public void setCollectionAmount(final BigDecimal collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

	/**
	 * @return
	 */
	public Date getCollectionTime() {
		return collectionTime;
	}

	/**
	 * @param collectionTime
	 */
	public void setCollectionTime(final Date collectionTime) {
		this.collectionTime = collectionTime;
	}

	/**
	 * @return
	 */
	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	/**
	 * @param receivedAmount
	 */
	public void setReceivedAmount(final BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	/**
	 * @return
	 */
	public Date getReceivedTime() {
		return receivedTime;
	}

	/**
	 * @param receivedTime
	 */
	public void setReceivedTime(final Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	/**
	 * @return
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * @param interest
	 */
	public void setInterest(final BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * @return
	 */
	public Integer getLateDays() {
		return lateDays;
	}

	/**
	 * @param lateDays
	 */
	public void setLateDays(final Integer lateDays) {
		this.lateDays = lateDays;
	}

	/**
	 * @return
	 */
	public BigDecimal getLateInterest() {
		return lateInterest;
	}

	/**
	 * @param status
	 */
	public void setLateInterest(final BigDecimal lateInterest) {
		this.lateInterest = lateInterest;
	}

	/**
	 * @return
	 */
	public byte getPeriod() {
		return period;
	}

	/**
	 * @param status
	 */
	public void setPeriod(final byte period) {
		this.period = period;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final String status) {
		this.status = status;
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
	 * 重寫toString
	 */
	@Override
	public String toString() {
		return "BondCollection [" + "uuid=" + uuid + ", projectId=" + projectId
				+ ", investId=" + investId + ", userId=" + userId + ", bondId="
				+ bondId + ", bondInvestId=" + bondInvestId + ", repaymentId="
				+ repaymentId + ", bondCapital=" + bondCapital
				+ ", bondInterest=" + bondInterest + ", capital=" + capital
				+ ", collectionAmount=" + collectionAmount
				+ ", collectionTime=" + collectionTime + ", receivedAmount="
				+ receivedAmount + ", receivedTime=" + receivedTime
				+ ", interest=" + interest + ", lateDays=" + lateDays
				+ ", lateInterest=" + lateInterest + ", period=" + period
				+ ", status=" + status + ", createTime=" + createTime + "]";
	}

	/**
	 * 重写PreInsert方法
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		super.preInsert();
	}

}

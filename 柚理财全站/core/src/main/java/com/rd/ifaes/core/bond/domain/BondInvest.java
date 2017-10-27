/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.bond.model.BondInvestModel;

/**
 * 债权投资类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
public class BondInvest extends BaseEntity<BondInvest> {

	private static final long serialVersionUID = 1L;
	/**
	 * 待支付状态
	 */
	private static final String STATUSINIT = BondInvestEnum.STATUS_INIT
			.getValue();

	/** 债权ID */
	private String bondId;
	/** 项目ID */
	private String projectId;
	/** 债权投资订单号 */
	private String investOrderNo;
	/** 投标ID */
	private String investId;
	/** 投资流水号 */
	private String investNo;
	/** 投资日期 */
	private String investDate;
	/** 受让人 */
	private String userId;
	/** 投资金额 */
	private BigDecimal amount;
	/** 债权人支付手续费 */
	private BigDecimal bondFee;
	/** 提前付息 */
	private BigDecimal payedInterest;
	/** 已收金额 */
	private BigDecimal receivedAccount;
	/** 状态   受让状态：待支付 0  受让状态：受让成功  1 受让状态：受让失败  2   受让状态：超时取消 3  重新支付 投资作废  4 */
	private String status;
	/** 添加时间 */
	private Date createTime;
	/** 添加IP */
	private String addIp;
	/** 再次转让时bond_invest对应的在原始标下的投资id*/
	private String projectInvestId;
	/**
	 * 投资渠道 1 PC  2 APP 3 微信
	 */
	private String investChannel;
	// 其他自定义属性
	/**
	 * Constructor
	 */
	public BondInvest() {
		super();
	}

	/**
	 * full Constructor
	 */
	public BondInvest(final String uuid) {
		super();
		setUuid(uuid);
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
	public String getInvestNo() {
		return investNo;
	}

	/**
	 * @param investNo
	 */
	public void setInvestNo(final String investNo) {
		this.investNo = investNo;
	}

	/**
	 * 获取属性investDate的值
	 * 
	 * @return investDate属性值
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置属性investDate的值
	 * 
	 * @param investDate属性值
	 */
	public void setInvestDate(final String investDate) {
		this.investDate = investDate;
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
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 */
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return
	 */
	public BigDecimal getPayedInterest() {
		return payedInterest;
	}

	/**
	 * @param payedInterest
	 */
	public void setPayedInterest(final BigDecimal payedInterest) {
		this.payedInterest = payedInterest;
	}

	/**
	 * @return
	 */
	public BigDecimal getReceivedAccount() {
		return receivedAccount;
	}

	/**
	 * @param receivedAccount
	 */
	public void setReceivedAccount(final BigDecimal receivedAccount) {
		this.receivedAccount = receivedAccount;
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
	 * @return
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * @param addIp
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * @return the bondFee
	 */
	public BigDecimal getBondFee() {
		return bondFee;
	}

	/**
	 * @param bondFee
	 *            the bondFee to set
	 */
	public void setBondFee(final BigDecimal bondFee) {
		this.bondFee = bondFee;
	}

	/**
	 * 获取属性investOrderNo的值
	 * 
	 * @return investOrderNo属性值
	 */
	public String getInvestOrderNo() {
		return investOrderNo;
	}

	/**
	 * 设置属性investOrderNo的值
	 * 
	 * @param investOrderNo属性值
	 */
	public void setInvestOrderNo(final String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}
	
	
	/**
	 * 获取属性projectInvestId的值
	 * @return projectInvestId属性值
	 */
	public String getProjectInvestId() {
		return projectInvestId;
	}

	/**
	 * 设置属性projectInvestId的值
	 * @param  projectInvestId属性值
	 */
	public void setProjectInvestId(String projectInvestId) {
		this.projectInvestId = projectInvestId;
	}

	/**
	 * 获取属性investChannel的值
	 * @return investChannel属性值
	 */
	public String getInvestChannel() {
		return investChannel;
	}

	/**
	 * 设置属性investChannel的值
	 * @param  investChannel属性值
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}

	/**
	 * 复制属性
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BondInvestModel instance() {
		final BondInvestModel model = new BondInvestModel();
		BeanUtils.copyProperties(this, model);
		return model;
	}

	/**
	 * 重写toString方法
	 */
	@Override
	public String toString() {
		return "BondInvest [" + "uuid=" + uuid + ", bondId=" + bondId
				+ ", projectId=" + projectId + ", investId=" + investId
				+ ", investNo=" + investNo + ", investDate=" + investDate
				+ ", userId=" + userId + ", amount=" + amount
				+ ", payedInterest=" + payedInterest + ", receivedAccount="
				+ receivedAccount + ", status=" + status + ", createTime="
				+ createTime + ", addIp=" + addIp + "]";
	}

	/**
	 * 重写preInsert方法
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		this.status = STATUSINIT;
		super.preInsert();
	}
}

/**

 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.bond.model.BondModel;

/**
 * 债权实体类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月26日
 */
public class Bond extends BaseEntity<Bond> {

	private static final long serialVersionUID = 1L;
	/** 债权名称 */
	private String bondName;
	/** 项目ID */
	private String projectId;
	/** 投标ID */
	private String investId;
	/** 债权人 */
	private String userId;
	/** 折溢价率 */
	private BigDecimal bondApr;
	/** 已售债权利息 */
	private BigDecimal soldInterest;
	/** 已支付手续费 */
	private BigDecimal bondFee;
	/** 债权金额 */
	private BigDecimal bondMoney;
	/** 已售债权本金 */
	private BigDecimal soldCapital;
	/** 债权状态:发布0， 转让完成3，自动撤回4，后台撤回5 */
	private String status;
	/** 债权有效时长 */
	private Integer limitHours;
	/** 债权截止日期 */
	private Date bondEndTime;
	/** 债权转让编号 */
	private String bondNo;
	/** 开始期数 */
	private Integer startPeriod;
	/** 添加时间 */
	private Date createTime;
	/** 债权规则ID */
	private String ruleId;
	/** 债权起投金额 */
	private BigDecimal bondLowestMoney;
	/** 债权最高投资金额 */
	private BigDecimal bondMostMoney;
	/** 剩余期限（天） */
	private int remainDays;
	/** 转让成功时间*/
	private Date successTime;
	/**实际转让方式*/
	private String sellStyle;
	/**债权投资阶段：1 在投;2、满标(或无剩余可投)*/
	private Integer stage;
	/**债权协议ID*/
	private String protocolId;
	// 其他自定义属性

	private String userName;
	
	private String realName;
	
	/**
	 * Constructor
	 */
	public Bond() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param uuid
	 */
	public Bond(final String uuid) {
		super();
		this.uuid = uuid;
	}

	/**
	 * 复制属性
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public Bond instance() {
		final Bond bond = new Bond();
		BeanUtils.copyProperties(this, bond);
		return bond;
	}

	/**
	 * 复制属性
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @return
	 */
	public BondModel prototype() {
		final BondModel bondModel = new BondModel();
		BeanUtils.copyProperties(this, bondModel);
		return bondModel;
	}

	/**
	 * 获取属性bondName的值
	 * 
	 * @return bondName属性值
	 */
	public String getBondName() {
		return bondName;
	}

	/**
	 * 设置属性bondName的值
	 * 
	 * @param bondName属性值
	 */
	public void setBondName(final String bondName) {
		this.bondName = bondName;
	}

	/**
	 * 获取属性projectId的值
	 * 
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置属性projectId的值
	 * 
	 * @param projectId属性值
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取属性investId的值
	 * 
	 * @return investId属性值
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * 设置属性investId的值
	 * 
	 * @param investId属性值
	 */
	public void setInvestId(final String investId) {
		this.investId = investId;
	}

	/**
	 * 获取属性userId的值
	 * 
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * 
	 * @param userId属性值
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性bondApr的值
	 * 
	 * @return bondApr属性值
	 */
	public BigDecimal getBondApr() {
		return bondApr;
	}

	/**
	 * 设置属性bondApr的值
	 * 
	 * @param bondApr属性值
	 */
	public void setBondApr(final BigDecimal bondApr) {
		this.bondApr = bondApr;
	}

	/**
	 * 获取属性soldInterest的值
	 * 
	 * @return soldInterest属性值
	 */
	public BigDecimal getSoldInterest() {
		return soldInterest;
	}

	/**
	 * 设置属性soldInterest的值
	 * 
	 * @param soldInterest属性值
	 */
	public void setSoldInterest(final BigDecimal soldInterest) {
		this.soldInterest = soldInterest;
	}

	/**
	 * 获取属性bondFee的值
	 * 
	 * @return bondFee属性值
	 */
	public BigDecimal getBondFee() {
		return bondFee;
	}

	/**
	 * 设置属性bondFee的值
	 * 
	 * @param bondFee属性值
	 */
	public void setBondFee(final BigDecimal bondFee) {
		this.bondFee = bondFee;
	}

	/**
	 * 获取属性bondMoney的值
	 * 
	 * @return bondMoney属性值
	 */
	public BigDecimal getBondMoney() {
		return bondMoney;
	}

	/**
	 * 设置属性bondMoney的值
	 * 
	 * @param bondMoney属性值
	 */
	public void setBondMoney(final BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}

	/**
	 * 获取属性soldCapital的值
	 * 
	 * @return soldCapital属性值
	 */
	public BigDecimal getSoldCapital() {
		return soldCapital;
	}

	/**
	 * 设置属性soldCapital的值
	 * 
	 * @param soldCapital属性值
	 */
	public void setSoldCapital(final BigDecimal soldCapital) {
		this.soldCapital = soldCapital;
	}

	/**
	 * 获取属性status的值
	 * 
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置属性status的值
	 * 
	 * @param status属性值
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获取属性limitHours的值
	 * 
	 * @return limitHours属性值
	 */
	public Integer getLimitHours() {
		return limitHours;
	}

	/**
	 * 设置属性limitHours的值
	 * 
	 * @param limitHours属性值
	 */
	public void setLimitHours(final Integer limitHours) {
		this.limitHours = limitHours;
	}

	/**
	 * 获取属性bondEndTime的值
	 * 
	 * @return bondEndTime属性值
	 */
	public Date getBondEndTime() {
		return bondEndTime;
	}

	/**
	 * 设置属性bondEndTime的值
	 * 
	 * @param bondEndTime属性值
	 */
	public void setBondEndTime(final Date bondEndTime) {
		this.bondEndTime = bondEndTime;
	}

	/**
	 * 获取属性sellStyle的值
	 * @return sellStyle属性值
	 */
	public String getSellStyle() {
		return sellStyle;
	}

	/**
	 * 设置属性sellStyle的值
	 * @param  sellStyle属性值
	 */
	public void setSellStyle(String sellStyle) {
		this.sellStyle = sellStyle;
	}

	/**
	 * 获取属性bondNo的值
	 * 
	 * @return bondNo属性值
	 */
	public String getBondNo() {
		return bondNo;
	}

	/**
	 * 设置属性bondNo的值
	 * 
	 * @param bondNo属性值
	 */
	public void setBondNo(final String bondNo) {
		this.bondNo = bondNo;
	}

	/**
	 * 获取属性startPeriod的值
	 * 
	 * @return startPeriod属性值
	 */
	public Integer getStartPeriod() {
		return startPeriod;
	}

	/**
	 * 设置属性startPeriod的值
	 * 
	 * @param startPeriod属性值
	 */
	public void setStartPeriod(final Integer startPeriod) {
		this.startPeriod = startPeriod;
	}

	/**
	 * 获取属性createTime的值
	 * 
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置属性createTime的值
	 * 
	 * @param createTime属性值
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取属性ruleId的值
	 * 
	 * @return ruleId属性值
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * 设置属性ruleId的值
	 * 
	 * @param ruleId属性值
	 */
	public void setRuleId(final String ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * 获取属性bondLowestMoney的值
	 * 
	 * @return bondLowestMoney属性值
	 */
	public BigDecimal getBondLowestMoney() {
		return bondLowestMoney;
	}

	/**
	 * 设置属性bondLowestMoney的值
	 * 
	 * @param bondLowestMoney属性值
	 */
	public void setBondLowestMoney(final BigDecimal bondLowestMoney) {
		this.bondLowestMoney = bondLowestMoney;
	}

	/**
	 * 获取属性bondMostMoney的值
	 * 
	 * @return bondMostMoney属性值
	 */
	public BigDecimal getBondMostMoney() {
		return bondMostMoney;
	}

	/**
	 * 设置属性bondMostMoney的值
	 * 
	 * @param bondMostMoney属性值
	 */
	public void setBondMostMoney(final BigDecimal bondMostMoney) {
		this.bondMostMoney = bondMostMoney;
	}

	/**
	 * 获取属性remainDays的值
	 * 
	 * @return remainDays属性值
	 */
	public int getRemainDays() {
		return remainDays;
	}

	/**
	 * 设置属性remainDays的值
	 * 
	 * @param remainDays属性值
	 */
	public void setRemainDays(final int remainDays) {
		this.remainDays = remainDays;
	}

	/**
	 * 获取属性successTime的值
	 * @return successTime属性值
	 */
	public Date getSuccessTime() {
		return successTime;
	}

	/**
	 * 设置属性successTime的值
	 * @param  successTime属性值
	 */
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	
	
	/**
	 * 获取属性stage的值
	 * @return stage属性值
	 */
	public Integer getStage() {
		return stage;
	}

	/**
	 * 设置属性stage的值
	 * @param  stage属性值
	 */
	public void setStage(Integer stage) {
		this.stage = stage;
	}

	/**
	 * 获取属性protocolId的值
	 * @return protocolId属性值
	 */
	public String getProtocolId() {
		return protocolId;
	}

	/**
	 * 设置属性protocolId的值
	 * @param  protocolId属性值
	 */
	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	/**
	 * 重写toString 方法
	 */
	@Override
	public String toString() {
		return "Bond [" + "uuid=" + uuid + ", projectId=" + projectId
				+ ", investId=" + investId + ", userId=" + userId
				+ ", bondApr=" + bondApr + ", soldInterest=" + soldInterest
				+ ", status=" + status + ", bondMoney=" + bondMoney
				+ ", soldCapital=" + soldCapital + ", limitHours=" + limitHours
				+ ", bondEndTime=" + bondEndTime + ", bondNo=" + bondNo
				+ ", startPeriod=" + startPeriod + ", createTime=" + createTime
				+ "]";
	}

	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 重写preInsert 方法
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		this.setSoldCapital(BigDecimal.ZERO);
		this.setSoldInterest(BigDecimal.ZERO);
		this.setBondFee(BigDecimal.ZERO);
		this.setStage(Integer.parseInt(BondEnum.STAGE_INVESTING.getValue()));
		super.preInsert();
	}
}

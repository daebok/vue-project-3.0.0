package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:StatisticEarn
 * 
 * @author xhf
 * @version 3.0
 * @date 2017-3-14
 */
public class StatisticEarn extends BaseEntity<StatisticEarn> {
	
	private static final long serialVersionUID = 1L;
	private String	userId;		     /* 用户ID */ 
	private Date	statDate;		 /* 统计日期 */ 
	private BigDecimal	amount;		 /* 金额 */ 
	private String	accountType;     /* 资金类型 */ 

	// Constructor
	public StatisticEarn() {
	}
	
	public StatisticEarn(Date statDate) {
		super();
		this.statDate = statDate;
	}

	public StatisticEarn(String userId, Date statDate, BigDecimal amount,
			String accountType) {
		super();
		this.userId = userId;
		this.statDate = statDate;
		this.amount = amount;
		this.accountType = accountType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "StatisticEarn [" + "uuid=" + uuid + ", userId=" + userId + ", statDate=" + statDate + ", amount=" + amount +  "]";
	}
}

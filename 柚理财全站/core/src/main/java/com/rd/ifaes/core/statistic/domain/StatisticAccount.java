package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:StatisticAccount
 * 
 * @author xhf
 * @version 3.0
 * @date 2017-3-6
 */
public class StatisticAccount extends BaseEntity<StatisticAccount> {
	
	private static final long serialVersionUID = 1L;
	private Date statDate;		 /* 统计日期 */ 
	private BigDecimal useMoney;	     /* 可用余额 */ 
	private String relativeRatio;		 /* 环比值 */ 

	// Constructor
	public StatisticAccount() {
	}

	/**
	 * full Constructor
	 */
	public StatisticAccount(String uuid, Date statDate, BigDecimal useMoney, String relativeRatio) {
		this.uuid = uuid;
		this.statDate = statDate;
		this.useMoney = useMoney;
		this.relativeRatio = relativeRatio;
	}

	public StatisticAccount(Date statDate) {
		super();
		this.statDate = statDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public String getRelativeRatio() {
		return relativeRatio;
	}

	public void setRelativeRatio(String relativeRatio) {
		this.relativeRatio = relativeRatio;
	}

	@Override
	public String toString() {
		return "StatisticAccount [" + "uuid=" + uuid + ", statDate=" + statDate + ", useMoney=" + useMoney + ", relativeRatio=" + relativeRatio +  "]";
	}
}

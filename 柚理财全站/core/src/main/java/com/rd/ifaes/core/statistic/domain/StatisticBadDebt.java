package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:StatisticBadDebt
 * 
 * @author xhf
 * @version 3.0
 * @date 2017-3-9
 */
public class StatisticBadDebt extends BaseEntity<StatisticBadDebt> {
	
	private static final long serialVersionUID = 1L;
	private Date	statDate;		 /* 代收日期 */ 
	private String	projectId;		 /* 代收订单ID */ 
	private BigDecimal	amount;		 /* 代收金额 */ 
	private String	isVouch;		 /* 是否担保借款，1-是，0-否 */ 
	private String	isMortgage;		 /* 是否抵押借款，1-是，0-否 */ 
	private String	isRealize;		 /* 是否变现借款，1-是，0-否 */ 

	// Constructor
	public StatisticBadDebt() {
	}
	
	public StatisticBadDebt(Date statDate) {
		super();
		this.statDate = statDate;
	}

	/**
	 * full Constructor
	 */
	public StatisticBadDebt(String uuid, Date statDate, String projectId, BigDecimal amount, String isVouch, String isMortgage, String isRealize) {
		this.uuid = uuid;
		this.statDate = statDate;
		this.projectId = projectId;
		this.amount = amount;
		this.isVouch = isVouch;
		this.isMortgage = isMortgage;
		this.isRealize = isRealize;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getIsVouch() {
		return isVouch;
	}

	public void setIsVouch(String isVouch) {
		this.isVouch = isVouch;
	}

	public String getIsMortgage() {
		return isMortgage;
	}

	public void setIsMortgage(String isMortgage) {
		this.isMortgage = isMortgage;
	}

	public String getIsRealize() {
		return isRealize;
	}

	public void setIsRealize(String isRealize) {
		this.isRealize = isRealize;
	}

	@Override
	public String toString() {
		return "StatisticBadDebt [" + "uuid=" + uuid + ", statDate=" + statDate + ", projectId=" + projectId + ", amount=" + amount + ", isVouch=" + isVouch + ", isMortgage=" + isMortgage + ", isRealize=" + isRealize +  "]";
	}
}

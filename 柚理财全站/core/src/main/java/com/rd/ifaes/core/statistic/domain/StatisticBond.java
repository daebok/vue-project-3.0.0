package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:StatisticBond
 * 
 * @author xhf
 * @version 3.0
 * @date 2017-3-2
 */
public class StatisticBond extends BaseEntity<StatisticBond> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户ID */ 
	private Date	bondDate;		 /* 转让日期 */ 
	private String	bondInvestId;		 /* 转让投资订单ID */ 
	private BigDecimal	bondAmount;		 /* 转让金额 */ 
	private int	bondTimeLimit;		 /* 债权期限，以天为单位，1个月30天，1年365天 */ 
	private String isVouch;          /* 是否担保：1-是，0-否*/
	private String isMortgage;       /* 是否抵押：1-是，0-否*/
	 
	// Constructor
	public StatisticBond() {
	}

	public StatisticBond(Date bondDate) {
		this.bondDate = bondDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getBondDate() {
		return bondDate;
	}

	public void setBondDate(Date bondDate) {
		this.bondDate = bondDate;
	}

	public String getBondInvestId() {
		return bondInvestId;
	}

	public void setBondInvestId(String bondInvestId) {
		this.bondInvestId = bondInvestId;
	}

	public BigDecimal getBondAmount() {
		return bondAmount;
	}

	public void setBondAmount(BigDecimal bondAmount) {
		this.bondAmount = bondAmount;
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

	public int getBondTimeLimit() {
		return bondTimeLimit;
	}

	public void setBondTimeLimit(int bondTimeLimit) {
		this.bondTimeLimit = bondTimeLimit;
	}

	@Override
	public String toString() {
		return "StatisticBond [uuid=" + uuid + ", userId=" + userId
				+ ", bondDate=" + bondDate + ", bondInvestId=" + bondInvestId
				+ ", bondAmount=" + bondAmount + ", bondTimeLimit="
				+ bondTimeLimit + ", isVouch=" + isVouch + ", isMortgage="
				+ isMortgage + "]";
	}

}

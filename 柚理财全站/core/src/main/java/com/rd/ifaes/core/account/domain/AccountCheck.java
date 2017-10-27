package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;

import com.rd.ifaes.common.entity.BaseEntity;

public class AccountCheck extends BaseEntity<AccountCheck> {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户UserID
	 */
	private String userId;

	/**
	 * 第三方ID
	 */
	private String tppUserCustId;

	/**
	 * 第三方可用
	 */
	private BigDecimal tppUseMoney;

	/**
	 * 第三方冻结
	 */
	private BigDecimal tppNoUseMoney;

	/**
	 * 系统可用
	 */
	private BigDecimal useMoney;

	/**
	 * 系统冻结
	 */
	private BigDecimal noUseMoney;
	
	/**
	 * 可用差额
	 */
	private BigDecimal useMoneyDiff;
	
	/**
	 * 不可以差额
	 */
	private BigDecimal noUseMoneyDiff;
	
	//其他变量

	/**
	 * 每页条数
	 */
	public static final int PAGE_NUM = 100;
	
	private String userName;
	
	private String realName;

	public String getTppUserCustId() {
		return tppUserCustId;
	}

	public void setTppUserCustId(String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getTppUseMoney() {
		return tppUseMoney;
	}

	public void setTppUseMoney(BigDecimal tppUseMoney) {
		this.tppUseMoney = tppUseMoney;
	}

	public BigDecimal getTppNoUseMoney() {
		return tppNoUseMoney;
	}

	public void setTppNoUseMoney(BigDecimal tppNoUseMoney) {
		this.tppNoUseMoney = tppNoUseMoney;
	}

	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public BigDecimal getNoUseMoney() {
		return noUseMoney;
	}

	public void setNoUseMoney(BigDecimal noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	public BigDecimal getUseMoneyDiff() {
		return useMoneyDiff;
	}

	public void setUseMoneyDiff(BigDecimal useMoneyDiff) {
		this.useMoneyDiff = useMoneyDiff;
	}

	public BigDecimal getNoUseMoneyDiff() {
		return noUseMoneyDiff;
	}

	public void setNoUseMoneyDiff(BigDecimal noUseMoneyDiff) {
		this.noUseMoneyDiff = noUseMoneyDiff;
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

}

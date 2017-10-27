/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.account.model;

import java.math.BigDecimal;

import com.rd.account.domain.Account;

/**
 * 账户修改信息传送数据
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月9日
 */
public class AccountModel extends Account {

	private static final long serialVersionUID = 8646920761960971466L;

	/**
	 * 默认构造方法
	 */
	public AccountModel() {
		super();
	}

	/**
	 * @param userId 用户ID
	 * @param accountCode 账户编号
	 * @param useMoney 可用金额的变动金额
	 * @param noUseMoney 不可用金额的变动金额
	 */
	public AccountModel(String userId, String accountCode, BigDecimal useMoney, BigDecimal noUseMoney) {
		this.setUserId(userId);
		this.setAccountCode(accountCode);
		this.setUseMoney(useMoney);
		this.setNoUseMoney(noUseMoney);
	}
	
	/**
	 * @param userId 用户ID
	 * @param accountCode 账户编号
	 * @param useMoney 可用金额的变动金额
	 * @param noUseMoney 不可用金额的变动金额
	 * @param cashFeeMark 提现收费标志金额
	 */
	public AccountModel(String userId, String accountCode, BigDecimal useMoney, BigDecimal noUseMoney, BigDecimal cashFeeMark) {
		this.setUserId(userId);
		this.setAccountCode(accountCode);
		this.setUseMoney(useMoney);
		this.setNoUseMoney(noUseMoney);
		this.setCashFeeMark(cashFeeMark);
	}

	/**
	 * @param uuid uuID
	 * @param userId 用户ID
	 * @param accountCode 账户编号
	 * @param useMoney 可用金额的变动金额
	 * @param noUseMoney 不可用金额的变动金额
	 */
	public AccountModel(String uuid, String userId, String accountCode, BigDecimal useMoney, BigDecimal noUseMoney) {
		this.setUuid(uuid);
		this.setUserId(userId);
		this.setAccountCode(accountCode);
		this.setUseMoney(useMoney);
		this.setNoUseMoney(noUseMoney);
	}
	
}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.model;

import java.math.BigDecimal;

import com.rd.ifaes.core.user.domain.User;

/**
 * 其他业务处理 异步类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年12月7日
 */
public class BondInvestOtherModel {
	
	/**
	 * 债权投资人
	 */
	private User investUser;
	/**
	 * 债权投资金额
	 */
	private BigDecimal tenderMoney;
	/**
	 * 债权投资ID
	 */
	private String uuid;
	/**
	 * 债权标链接信息
	 */
	private String info;

	/**
	 * 获取属性investUser的值
	 * @return investUser属性值
	 */
	public User getInvestUser() {
		return investUser;
	}

	/**
	 * 设置属性investUser的值
	 * @param  investUser属性值
	 */
	public void setInvestUser(User investUser) {
		this.investUser = investUser;
	}

	/**
	 * 获取属性tenderMoney的值
	 * @return tenderMoney属性值
	 */
	public BigDecimal getTenderMoney() {
		return tenderMoney;
	}

	/**
	 * 设置属性tenderMoney的值
	 * @param  tenderMoney属性值
	 */
	public void setTenderMoney(BigDecimal tenderMoney) {
		this.tenderMoney = tenderMoney;
	}

	/**
	 * 获取属性uuid的值
	 * @return uuid属性值
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * 设置属性uuid的值
	 * @param  uuid属性值
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 获取属性info的值
	 * @return info属性值
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * 设置属性info的值
	 * @param  info属性值
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}

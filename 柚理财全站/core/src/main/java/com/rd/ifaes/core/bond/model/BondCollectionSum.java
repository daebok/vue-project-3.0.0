/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.model;

import java.math.BigDecimal;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 债权已还本金和已还利息  字段属性
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年12月5日
 */
public class BondCollectionSum extends BaseEntity<BondCollectionSum>{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -7982896892081659732L;

	/**
	 * 已还本金
	 */
	private BigDecimal repayedCapital;
	
	/**
	 * 已还利息
	 */
	private BigDecimal repayedInterest;
	
	/**
	 * 无参构造器
	 */
	public  BondCollectionSum(){
		
	}
	
	/**
	 * 有参构造器
	 * @param repayedCapital
	 * @param repayedInterest
	 */
	public BondCollectionSum(final BigDecimal repayedCapital,final BigDecimal repayedInterest){
		this.repayedCapital = repayedCapital;
		this.repayedInterest = repayedInterest;
	}
	/**
	 * 获取属性repayedCapital的值
	 * @return repayedCapital属性值
	 */
	public BigDecimal getRepayedCapital() {
		return repayedCapital;
	}

	/**
	 * 设置属性repayedCapital的值
	 * @param  repayedCapital属性值
	 */
	public void setRepayedCapital(BigDecimal repayedCapital) {
		this.repayedCapital = repayedCapital;
	}

	/**
	 * 获取属性repayedInterest的值
	 * @return repayedInterest属性值
	 */
	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}

	/**
	 * 设置属性repayedInterest的值
	 * @param  repayedInterest属性值
	 */
	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	
	
}

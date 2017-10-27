package com.rd.ifaes.common.dict;

/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 *  债权投资状态值
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月1日
 */
public enum BondInvestEnum implements BaseEnum<String, String> {
	/**
	 * 受让状态：待支付 0 
	 */
	STATUS_INIT("待支付", "0"),
	/**
	 * 受让状态：受让成功  1
	 */
	STATUS_COMPLETE("受让成功", "1"),
	/**
	 * 受让状态：受让失败  2
	 */
	STATUS_FAIL("受让失败", "2"),
	/**
	 * 受让状态：超时取消 3
	 */
	STATUS_OVERTIME("超时取消","3"),
	/**
	 * 重新支付 投资作废  4 
	 */
	STATUS_DELETE("投资作废","4"),
	
	/**
	 * 位置参数--已受让列表
	 */
	LOCATION_BOUGHTBONDLIST("已受让列表","boughtBondList");

	// 中文名称
	private String name;
	// 值
	private String value;

	private BondInvestEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 获取label
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * 根据VALUE比较
	 * 
	 * @param obj
	 *            目标值
	 * @return
	 */
	public boolean eq(Object obj) {
		return this.value.equals(obj);
	}
}

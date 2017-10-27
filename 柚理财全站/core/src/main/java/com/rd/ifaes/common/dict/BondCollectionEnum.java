package com.rd.ifaes.common.dict;

/**
 *  互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * 	Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 *  债权待还枚举类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月1日
 */
public enum BondCollectionEnum implements BaseEnum<String, String> {
	STATUS_NOT_REPAY("未还", "0"), STATUS_PAID("已还", "1");

	// 中文名称
	private String name;
	// 值
	private String value;

	private BondCollectionEnum(String name, String value) {
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

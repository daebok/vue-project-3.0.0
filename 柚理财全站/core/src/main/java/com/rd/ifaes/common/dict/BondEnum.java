/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.dict;

/**
 * 债权的相关参数值
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月29日
 */
public enum BondEnum implements BaseEnum<String, String>{
	/**
	 * 筛选条件--收益方式类型
	 */
	BOND_TYPE_REPAYSTYLETYPE("收益方式类型","repayStyle"),
	/**
	 * 筛选条件--预期年化类型
	 */
	BOND_TYPE_APRTYPE("预期年化类型","aprType"),
	/**
	 * 筛选条件--剩余期限类型
	 */
	BOND_TYPE_REMAINDAYSTYPE("剩余期限类型","remainDaysType"),
	/**
	 * 筛选条件--转让金额类型
	 */
	BOND_TYPE_BONDMONEYTYPE("转让金额类型","bondMoneyType"),
	/**
	 * 债权阶段  1 投资中  
	 */
	STAGE_INVESTING("债权投资阶段","1"),	
	/**
	 * 债权阶段    2 满标（剩余可投为0）
	 */
	STAGE_EMPTING("债权投资阶段","2"),	
	/**
	 * 债权转让状态值   转让中状态  0 
	 */
	STATUS_PUBLISH("转让中状态","0"),
	/**
	 * 债权转让状态值    转让完成状态  3
	 */
	STATUS_COMPLETE("转让完成状态","3"),
	/**
	 * 自动撤回
	 */
	STATUS_CANCLE_AUTO("自动撤回状态","4"),
	/**
	 * 手动前台撤回
	 */
	STATUS_CANCLE_WEB("手动前台撤回状态","5"),
	/**
	 * 手动后台撤回
	 */
	STATUS_CANCLE_MANGE("手动后台撤回状态","6"),
	
	/**
	 * 位置参数--可转让列表
	 */
	LOCATION_ABLEBONDLIST("可转让列表","ableBondList"),
	
	/**
	 * 位置参数--转让中列表
	 */
	LOCATION_SELLINGBONDLIST("转让中列表","sellingBondList"),

	/**
	 * 位置参数--已转让列表
	 */
	LOCATION_SOLDBONDLIST("已转让列表","soldBondList"),
	
	/**
	 * 位置参数--债权专区
	 */
	LOCATION_BONDINDEX("债权专区","bondIndex"),
	
	/**
	 *  位置参数--后台债权列表
	 */
	LOCATION_MANAGEBONDINDEX("后台债权列表","manageBondIndex");
	
	
	
	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private BondEnum(String name, String value) {
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
	  * @param obj 目标值
	  * @return
	  */
	public boolean eq(Object obj) {
		return this.value.equals(obj);
	}

}

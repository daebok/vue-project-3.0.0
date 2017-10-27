package com.rd.ifaes.common.dict;

/**
 * 	互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 *  Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 *  
 *  债权规则枚举类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月23日
 */
public enum BondRuleEnum  implements BaseEnum<String, String>{

	/**
	 * 债权规则配置  默认值  0 
	 */
	RULE_DEFAULT_VALUE("默认值","0"),
	/**
	 * 债权规则开启    1
	 */
	RULE_STATAS_ON("债权规则开启","1"),
	/**
	 * 债权规则关闭   0 
	 */
	RULE_STATAS_OFF("债权规则关闭","0"),
	/**
	 * 选择待收持有天数限制   1 开启限制  
	 */ 
	RULE_IS_HONDDAYS("选择待收持有天数限制","1"),
	/**
	 * 选择到期日前天数限制   1 开启限制 
	 */
	RULE_IS_REMAINSDAYS("选择到期日前天数限制","1"),
	/**
	 * 选择本期到期日前限制   1 开启限制 
	 */
	RULE_IS_PERIODREMAINSDAYS("选择本期到期日前限制","1"),
	/**
	 * 一次性全额转让
	 */
	RULE_SELL_ALL("一次性全额转让","1"),
	/**
	 * 部分转让
	 */
	RULE_SELL_PART("部分转让","0"),
	/**
	 * 一次性全额受让   
	 */
	RULE_BUY_ALL("一次性全额受让","1"),
	/**
	 * 部分受让
	 */
	RULE_BUY_PART("部分受让","0"),
	/**
	 * 固定金额手续费
	 */
	RULE_FEE_STYLE_FIXED("固定金额手续费","1"),
	/**
	 * 比例手续费
	 */
	RULE_FEE_STYLE_RATE("比例手续费","2"),
	/**
	 * 免手续费
	 */
	RULE_FEE_STYLE_FREE("免手续费","0");
	

	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private BondRuleEnum(String name, String value) {
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

package com.rd.ifaes.common.dict;

/**
 *  互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * 	Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月11日
 */
public enum UfxRepaymentModelEnum implements BaseEnum<String, String>{
	/**
	 * 还款类型  01：融资人还款   
	 */
	REPAY_TYPE_ONE("融资人还款 ","01"),	
	
	/**
	 * 还款类型  02：：商户平台代偿    
	 */
	REPAY_TYPE_TWO("商户平台代偿  ","02"),	

	/**
	 * 还款类型  03：：融资人还代偿给商户平台 
	 */
	REPAY_TYPE_THREE("融资人还代偿给商户平台 ","03"),
	
	/**
	 * 还款类型  04：：担保方代偿     
	 */
	REPAY_TYPE_FOUR("担保方代偿 ","04"),
	
	/**
	 * 还款类型  05：：融资人还代偿给担保方,   
	 */
	REPAY_TYPE_FIVE("融资人还代偿给担保方 ","05"),

	/**
	 * 还款类型  06：债权还款 
	 */
	REPAY_TYPE_SIX("债权还款   ","06"),
	
	/**
	 * 资金流向 01：项目资金转入       02：项目资金转出 03：项目资金无密转入 
	 */
	FUND_FLOW_ONE("项目资金转入","01"),
	
	/**
	 * 资金流向 01：项目资金转入       02：项目资金转出 03：项目资金无密转入 
	 */
	FUND_FLOW_TWO("项目资金转出","02"),
	
	/**
	 * 资金流向 01：项目资金转入       02：项目资金转出 03：项目资金无密转入 
	 */
	FUND_FLOW_THREE("项目资金无密转入 ","03");
	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private UfxRepaymentModelEnum(String name, String value) {
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

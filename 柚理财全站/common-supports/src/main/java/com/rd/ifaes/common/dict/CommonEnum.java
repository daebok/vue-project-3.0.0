package com.rd.ifaes.common.dict;

/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 *  常见的常量枚举类型
 *  
 * @version 3.0
 * @author FangJun
 * @date 2016年7月26日
 */
public enum CommonEnum implements BaseEnum<String, String> {
	
	 /**
     *   是 , 1
     */
     YES("是","1"),
     /**
      * 否 , 0
      */
     NO("否", "0" );

  //字典项的中文名称
	private String name;
	//字典项的值
	private String value;

	private CommonEnum(String name,String value){
		this.name = name;
		this.value = value;
	}
	/**
	 * 获取label
	 * @return
	 */
	@Override
	public String getName(){
		return this.name;
	}

	/**
	 * 获取值
	 * @return
	 */
	@Override
	public String getValue(){
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
package com.rd.ifaes.core.statistic.model;

/**
 * data model
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public class DataModel{

	private String name;
	
	private Object value;

	
	public DataModel(){
		
	}
	
	public DataModel(String name,Object value){
		this.name = name;
		this.value = value;
	}
	/**
	 * 获取属性name的值
	 * @return name属性值
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置属性name的值
	 * @param  name属性值
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取属性value的值
	 * @return value属性值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置属性value的值
	 * @param  value属性值
	 */
	public void setValue(Object value) {
		this.value = value;
	}


}

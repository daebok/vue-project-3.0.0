package com.rd.ifaes.common.rabbit.model;

/**
 * MQ基类
 * @version 3.0
 * @author lh
 * @date 2016年8月6日
 */
public class MqBaseModel {
	
	/**
	 * 操作
	 */
	protected String operate;
	
	/**
	 * 无参构造方法
	 */
	public MqBaseModel() {
		super();
	}

	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

}

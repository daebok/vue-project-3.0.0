package com.rd.ifaes.common.dict;
/**
 * 日志枚举类
 * @author lh
 * @version 3.0
 * @since 2016-9-21
 *
 */
public enum LogEnum implements BaseEnum<String, String> {
     /*-------------状态 ----------------- */
	/**
	 * 投资状态：待支付，0
	 */
	LOG_TYPE_SYS_ACCESS("系统访问日志","1");
	
	

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private LogEnum(String name, String value) {
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

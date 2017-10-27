package com.rd.ifaes.common.dict;

/**
 * 变现枚举类
 * @version 3.0
 * @author fxl
 * @date 2016年12月22日
 */
public enum RealizeEnum implements BaseEnum<String, String> {
	
	/**
	 * 变现冻结状态：未冻结，0
	 */
	FREEZE_STATUS_NEW("未冻结", "0"),
	/**
	 * 变现冻结状态：已冻结，1
	 */
	FREEZE_STATUS_FREEZE("已冻结", "1"),
	/**
	 * 变现冻结状态：已解冻，2
	 */
	FREEZE_STATUS_UNFREEZE("已解冻", "2"),
	/**
	 * 变现冻结状态：已撤销，4
	 */
	FREEZE_STATUS_CANCEL("已撤销", "4");

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private RealizeEnum(String name, String value) {
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

package com.rd.ifaes.common.dict;

/**
 * 字典类型常量
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
public enum DictDataEnum implements BaseEnum<String, String> {
	/**
	 *  lotteryType
	 */
	LOTTERY_TYPE_REDENVELOPE("红包", "1"),
	LOTTERY_TYPE_RATECOUPON("加息券", "2"),
	LOTTERY_TYPE_INTEGRAL("积分", "3");
	
	
	
	/**
	 *  字典值的中文名称
	 */
	private String name;
	
	/**
	 *  字典项的值
	 */
	private String value;

	private DictDataEnum(final String name, final String value) {
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

}

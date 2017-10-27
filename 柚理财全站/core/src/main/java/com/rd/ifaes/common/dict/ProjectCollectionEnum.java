package com.rd.ifaes.common.dict;
/**
 * 待收枚举
 * @author lh
 * @version 3.0
 * @since 2016-7-20
 *
 */
public enum ProjectCollectionEnum implements BaseEnum<String, String> {

	STATUS_NOT_REPAY("未还", "0"), 
	STATUS_PAID("已还", "1"),
	COLLECTION_TYPE_COMMON("普通待收","0"),
	COLLECTION_TYPE_BOND("转让人待收","1"),
	COLLECTION_TYPE_INVEST("受让人待收","2"),
	COLLECTION_TYPE_REMOVE("作废待收","3"); 
	// 中文名称
	private String name;
	// 值
	private String value;

	private ProjectCollectionEnum(String name, String value) {
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

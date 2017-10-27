package com.rd.ifaes.common.dict;

/**
 * 用户枚举
 * @version 3.0
 * @author fxl
 * @date 2017年3月20日
 */
public enum UserEnum implements BaseEnum<String, String> {

	/**
	 * 实名未认证 0
	 */
	REAL_NAME_STATUS_WAIT("实名未认证","0"),
	/**
	 * 实名认证通过 1
	 */
	REAL_NAME_STATUS_SUCC("实名认证通过","1"),
	/**
	 * 实名认证未通过 -1
	 */
	REAL_NAME_STATUS_FAIL("实名认证未通过","-1"),
	/**
	 * 用户类型：普通用户 0
	 */
	USER_TYPE_NORMAL("普通用户","0"),
	/**
	 * 用户类型：投资人 1
	 */
	USER_TYPE_INVESTOR("投资人","1"),
	/**
	 * 用户类型：借款人 2
	 */
	USER_TYPE_BORROWER("借款人","2"),
	/**
	 * 注册渠道：PC，1
	 */
	REGISTER_CHANNEL_PC("PC","1"),
	/**
	 * 注册渠道：APP，2
	 */
	REGISTER_CHANNEL_APP("APP","2"),
	/**
	 * 注册渠道：微信，3
	 */
	REGISTER_CHANNEL_WECHAT ("微信","3");
	
	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private UserEnum(String name, String value) {
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

package com.rd.ifaes.common.dict;

/**
 * 还款枚举类
 * 
 * @author lh
 * @version 3.0
 * @since 2016-7-26
 *
 */
public enum RepaymentEnum implements BaseEnum<String, String> {
	// ---------------还款状态--------------------------------------------------------
	/**
	 *  本期还款状态：未还,0
	 */
	STATUS_NOT_REPAY("未还", "0"),
	/**
	 *  本期还款状态：已还,1
	 */
	STATUS_REPAID("已还", "1"),
	/**
	 *  本期还款状态：未到还款时间
	 */
	STATUS_NOT_NEED_REPAY("未到还款时间", "-1"),
	/**
	 *  垫付已收款
	 */
	STATUS_REPAY_YES("已收款", "1"),
	/**
	 *  垫付未收款
	 */
	STATUS_REPAY_NO("未收款", "0"),
	// ---------------还款类型--------------------------------------------------------
	/**
	 *  还款类型：正常还款,1
	 */
	REPAY_TYPE_NORMAL("正常还款", "1"),
	/**
	 *  还款类型：担保垫付,2
	 */
	REPAY_TYPE_GUARANTEE("担保垫付", "2"),
	/**
	 *  还款类型：已还垫付,3
	 */
	REPAY_TYPE_REPAYED_GUARANTEE("已还垫付", "3"),
	/**
	 *  还款类型：逾期还款,4
	 */
	REPAY_TYPE_LATE("逾期还款", "4"),
	/**
	 *  逾期垫付状态：未垫付,0
	 */
	STATUS_OVERDUE_ADVANCE_NO("未垫付", "0"),
	/**
	 *  逾期垫付状态：已垫付,1
	 */
	STATUS_OVERDUE_ADVANCE_YES("已垫付", "1")
	;

	// 中文名称
	private String name;
	// 值
	private String value;

	private RepaymentEnum(String name, String value) {
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
	 * @param obj 目标值
	 * @return
	 */
	public boolean eq(Object obj) {
		return this.value.equals(obj);
	}
}

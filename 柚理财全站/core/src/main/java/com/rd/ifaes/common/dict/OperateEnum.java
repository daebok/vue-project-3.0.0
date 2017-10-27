package com.rd.ifaes.common.dict;

/**
 * 
 * 运营管理中的枚举定义
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public enum  OperateEnum implements BaseEnum<String, String>{
	/**
	 * 规则状态 : 禁用，0
	 */
	STATUS_FORBIDDEN("禁用", "0"),
	/**
	 * 规则状态 : 启用，1
	 */
	STATUS_SERVICE("启用", "1"),
	/**
	 * 奖励发放类型红包，1
	 */
	AWARD_TYPE_RED("奖励发放类型红包", "1"),
	/**
	 * 奖励发放类型加息券，2
	 */
	AWARD_TYPE_RATE("奖励发放类型加息券", "2"),

	/**
	 * 固定金额-红包
	 */
	GRANT_TYPE_FIXED_AMOUNT("固定金额", "1"),
	/**
	 * 固定比例-红包
	 */
	GRANT_TYPE_FIXED_RATE("固定比例", "2"),
	/**
	 * 金额满返-红包
	 */
	GRANT_TYPE_INTERVAL_AMOUNT("金额满返", "3"),
	/**
	 * 比例满返
	 */
	GRANT_TYPE_INTERVAL_RATE("比例满返", "4"),
	/**
	 * 积分发放规则 规定值增长
	 */
	GRANT_TYPE_FIXEDL_UP("固定值增长", "5"),
	/**
	 * 固定值 -加息券
	 */
	GRANT_TYPE_FIXED_UPARP_RATE("固定值", "1"),
	/**
	 * 金额满返-加息券
	 */
	GRANT_TYPE_INTERVAL_AMOUNT_RATE("金额满返", "2"),
	/**
	 * 状态（加息券，红包） 未使用
	 */
	STATUS_NO_USE("未使用", "0"),
	/**
	 * 状态（加息券，红包） 已经使用
	 */
	STATUS_USE("已经使用", "1"),
	/**
	 * 状态（加息券，红包） 已经过期
	 */
	STATUS_EXPIRED("已经过期", "2"),
	/**
	 * 状态（加息券，红包） 已经作废
	 */
	STATUS_CANCEL("已经作废", "3"),

	/**
	 * 活动名称 注册送好礼
	 */
	ACTIVITYPLAN_REGISTER("注册送好礼", "register"),
	/**
	 * 活动名称 首次投资奖励
	 */
	ACTIVITYPLAN_FIRST_TENDER("首次投资奖励", "first_tender"),
	/**
	 * 活动名称 投资奖励
	 */
	ACTIVITYPLAN_TENDER_SUC("投资奖励", "tender_suc"),
	/**
	 * 活动名称 好友注册奖励
	 */
	ACTIVITYPLAN_INVITE_REGISTER("好友注册奖励", "invite_register"),
	/**
	 * 活动名称 一级好友首投奖励
	 */
	ACTIVITYPLAN_FIRST_INVITE_FIRST_TENDER("一级好友首投奖励", "first_invite_first_tender"),
	/**
	 * 活动名称 二级好友首投奖励
	 */
	ACTIVITYPLAN_SECOND_INVITE_FIRST_TENDER("二级好友首投奖励", "second_invite_first_tender"),
	/**
	 * 活动名称 一级好友投资奖励
	 */
	ACTIVITYPLAN_FIRST_INVITE_TENDER_SUC("一级好友投资奖励", "first_invite_tender_suc"),
	/**
	 * 活动名称 二级好友投资奖励
	 */
	ACTIVITYPLAN_SECOND_INVITE_TENDER_SUC("二级好友投资奖励", "second_invite_tender_suc"),
	/**
	 * 活动名称 自定义
	 */
	ACTIVITYPLAN_CUSTOM("自定义", "custom"),
	/**
	 * 活动名称 手动定向发放
	 */
	ACTIVITYPLAN_SELECT_USERS("手动定向发放", "select_users"),
	/**
	 * 活动名称 vip等级特权
	 */
	ACTIVITYPLAN_VIP_LEVEL("vip等级特权", "vip_level"),
	/**
	 * 活动名称 积分商城
	 */
	ACTIVITYPLAN_SCORE_SHOP("积分商城", "score_shop"),
	/**
	 * 用户vip成长 增加
	 */
	VIP_GROWTH_OPT_ADD("增加", "1"),
	/**
	 * 用户vip成长 减少
	 */
	VIP_GROWTH_OPT_SUB("减少", "0"),
	/**
	 * 用户积分 增加
	 */
	SCORE_OPT_ADD("增加", "1"),
	/**
	 * 用户积分 减少
	 */
	SCORE_OPT_SUB("减少", "0"),
	/**
	 * 礼包类型 生日礼包
	 */
	GIFT_TYPE_BIRTHDAY("生日礼包", "0"),
	/**
	 * 礼包类型 专享礼包
	 */
	GIFT_TYPE_EXCLUSIVE("专享礼包", "1");

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private OperateEnum(String name, String value) {
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

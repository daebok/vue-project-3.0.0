package com.rd.ifaes.common.dict;

/**
 * All rights Reserved, Designed By QianPengZhan   
 * @Title:  RiskEnum.java   
 * @Package com.rd.ifaes.common.dict   
 * @date:   2016年7月12日 下午4:45:20   
 * @version V3.0  
 * 积分枚举类
 * @author wyw
 *
 */
public enum ScoreEnum implements BaseEnum<String, String>{
	SCORE_USER_REGISTER("注册成功","score_user_register"),
	
	SCORE_USER_LOGIN("用户登录成功","score_user_login"),
	
	SCORE_EMAIL("邮箱认证成功","score_email"),
	
	SCORE_PHONE("手机认证成功","score_phone"),
	
	SCORE_REALNAME("实名认证成功","score_realname"),
	
	SCORE_INVEST_SUCCESS("投资成功","score_invest_success"),
	
	SCORE_BOND_INVEST_SUCCESS("债权投资成功","score_bond_invest_success"),
	
	SCORE_EXCHANGE("积分兑换商品","score_exchange"),
	
	SCORE_LOTTERY("积分抽奖","score_lottery"),
	
	SCORE_EXCHANGE_VERIFY_SUCCESS("积分兑换商品审核成功","score_exchange_verify_success"),
	
	SCORE_EXCHANGE_VERIFY_FAILD("积分兑换商品审核失败","score_exchange_verify_faild");
	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private ScoreEnum(String name, String value) {
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

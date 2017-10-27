package com.rd.ifaes.common.dict;
/**
 * 投资相关字典数据（状态等）
 * @author  FangJun
 * @date 2016年7月15日
 */
public enum InvestEnum implements BaseEnum<String, String> {
     /*-------------状态 ----------------- */
	/**
	 * 投资状态：待支付，0
	 */
	STATUS_NEW("待支付","0"),
	/**
	 * 投资状态：投资成功，1
	 */
	STATUS_SUCCESS("投资成功","1"),
	/**
	 * 投资状态：投资失败，2
	 */
	STATUS_FAIL("投资失败","2"),
	/**
	 * 投资状态：退款处理中，3
	 */
	STATUS_REFUND("退款处理中","3"),
	/**
	 * 投资状态： 投资作废 4
	 */
	STATUS_DELETE("投资作废", "4"),
	/**
	 * 投资状态： 超时取消 5
	 */
	STATUS_TIMEOUT("超时取消", "5"),
	/**
	 * 投资状态： 订单失效 6，待支付订单再次支付操作使用
	 */
	STATUS_CANCEL("订单失效", "6"),
	//---------------投资债权标识--------------------------------------------------------
	/**
	 * 未债权发布
	 */
	BOND_FLAG_NORMAL("未债权发布","0"),
	/**
	 * 已债权发布
	 */
	BOND_FLAG_PUBLISH("已债权发布","1"),
	//---------------投资债权类型--------------------------------------------------------
	/**
	 * 普通投资
	 */
	INVEST_STYLE_NORMAL("普通投资","0"),
	/**
	 * 转让投资
	 */
	INVEST_STYLE_SELL("转让投资","1"),
	/**
	 * 受让投资
	 */
	INVEST_STYLE_BUY("受让投资","2"),
	//---------------投资变现标识--------------------------------------------------------
	/**
	 * 已变现投资,1
	 */
	REALIZE_FLAG_TENDER ("已变现投资", "1"),
	/**
	 * 未变现投资,0
	 */
	REALIZE_FLAG_NORMAL ("未变现投资", "0"),
	
	  /*-------------投资方式 ----------------- */
	/**
	 * 投资方式：手动投标，0
	 */
	INVEST_TYPE_WEB("手动投标","0"),
	/**
	 * 投资方式：自动投标，1
	 */
	INVEST_TYPE_AUTO("自动投标","1"),
	/*-------------投资渠道 ----------------- */
	/**
	 * 投资渠道：PC，1
	 */
	INVEST_CHANNEL_PC("PC","1"),
	/**
	 * 投资渠道：APP，2
	 */
	INVEST_CHANNEL_APP("APP","2"),
	/**
	 * 投资渠道：微信，3
	 */
	INVEST_CHANNEL_WECHAT ("微信","3"),
	/**
	 * 按照期数排序
	 */
	INVEST_PAGE_SORT_PERIOD("按照期数排序","period");
	
	

	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private InvestEnum(String name, String value) {
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

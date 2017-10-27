package com.rd.ifaes.common.constant;

public final class JxbankConstant {
	
	/**
	 * 证件类型	A	2	M	01-身份证（18位）
	 * 20-组织机构代码25-企业社会信用代码
	 */
	public static final String JXBANK_ID_TYPE_ONE ="01";

	public static final String JXBANK_ID_TYPE_TWO ="20";

	public static final String JXBANK_ID_TYPE_FIVE ="25";
	
	/**
	 * acctUse	账户用途	A	5	M	00000-普通账户
		10000-红包账户（只能有一个）
		01000-手续费账户（只能有一个）
		00100-担保账户
	 */
	public static final String JXBANK_ACCT_USE_ZERO = "00000";
	public static final String JXBANK_ACCT_USE_ONE = "10000";
	public static final String JXBANK_ACCT_USE_TWO = "01000";
	public static final String JXBANK_ACCT_USE_THREE = "00100";
	
	/**
	 * currency	交易币种	A	3	M	156-人民币
	 */
	public static final String JXBANK_CURRENCY_RMB = "156";
	
	/**
	 * 0-所有（默认）1-当前有效的绑定卡
	 */

	public static final String JXBANK_STATE_ONE = "1";
	/**
	 * 1-当前有效的绑定卡
	 */
	public static final String JXBANK_STATE_ZERO = "0";
	
	/**
	 * 不填默认为1
		1-适用于 增强类的接口
		2-适用于 金运通通道充值
	 */
	public static final String JXBANK_REQTYPE_TWO = "2";
	/**
	 * 1-适用于 增强类的接口
	 */
	public static final String JXBANK_REQTYPE_ONE = "1";
	
	/**
	 * 1-适用于 增强类的接口
	 */
	public static final String JXBANK_OPTION_MODIFY = "1";
	
	/**
	 * 0-到期与本金一起归还
	 */
	public static final String JXBANK_INTTYPE_ONE = "0";
	
	/**
	 * 1-每月固定日期支付
	 */
	public static final String JXBANK_INTTYPE_FIX = "1";

	/**
	 * 2-每月不确定日期支付
	 */
	public static final String JXBANK_INTTYPE_NOTFIX = "2";
	
	/**
	 * 是否冻结金额1-冻结
	 */
	public static final String JXBANK_FRZFLAG_FREEZE = "1";
	
	/**
	 * 是否冻结金额0-不冻结
	 */
	public static final String JXBANK_FRZFLAG_UNFREEZE = "0";
}

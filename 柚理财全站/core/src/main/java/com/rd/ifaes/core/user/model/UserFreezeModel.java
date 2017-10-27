package com.rd.ifaes.core.user.model;

import com.rd.ifaes.core.user.domain.UserFreeze;


public class UserFreezeModel extends UserFreeze {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户冻结功能状态---开启1
	 */
	public static final int STATUS_USER_FREEZE_OPEN = 1;
	
	/**
	 * 用户冻结功能状态---关闭0
	 */
	public static final int STATUS_USER_FREEZE_CLOSE = 0;
	
	/**
	 * 用户冻结点---充值--recharge
	 */
	public static final String STATUS_USER_FREEZE_RECHARGE = "recharge";
	
	/**
	 * 用户冻结点---提现-cash
	 */
	public static final String STATUS_USER_FREEZE_CASH = "cash";
	
	/**
	 * 用户冻结点---投资--invest
	 */
	public static final String STATUS_USER_FREEZE_INVEST= "invest";
	/**
	 * 用户冻结点---变现--realize
	 */
	public static final String STATUS_USER_FREEZE_REALIZE= "realize";
	/**
	 * 用户冻结点---债权转让--bond
	 */
	public static final String STATUS_USER_FREEZE_BOND = "bond";
	/**
	 * 用户冻结点---借款--loan
	 */
	public static final String STATUS_USER_FREEZE_LOAN = "loan";
	
	/**
	 * 担保用户冻结--担保（审核提交） -vouch
	 */
	public static final String STATUS_USER_FREEZE_VOUCH = "vouch";
}

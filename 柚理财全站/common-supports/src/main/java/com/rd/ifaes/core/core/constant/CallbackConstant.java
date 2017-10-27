package com.rd.ifaes.core.core.constant;

/**
 * 同步回调常量类
 * @version 3.0
 * @author lh
 * @date 2016年8月15日
 */
public class CallbackConstant {
	private CallbackConstant() {
	}
	
	/**充值-图形类型*/
	public static final String RECHARGE_PICTYPE = "type_cz";
	/**充值-请求类型*/
	public static final String RECHARGE_REQUEST_TYPE = "充值";
	/**充值-请求名*/
	public static final String RECHARGE_REQUEST_NAME = "我的账户>充值提现";
	/**充值-请求跳转地址*/
	public static final String RECHARGE_REQUEST_URL = "/member/recharge/index.html?type=recharge";
	/**充值-请求跳转地址*/
	public static final String RECHARGE_VOUCH_REQUEST_URL = "/member/recharge/vouchIndex.html?type=recharge";
	/**充值-按钮名称*/
	public static final String RECHARGE_BUTTON_NAME = "开始投资";
	
	/**提现-图形类型*/
	public static final String CASH_PICTYPE = "type_tx";
	/**提现-请求类型*/
	public static final String CASH_REQUEST_TYPE = "提现";
	/**提现-请求名*/
	public static final String CASH_REQUEST_NAME = "我的账户>充值提现";
	/**提现-请求跳转地址*/
	public static final String CASH_REQUEST_URL = "/member/recharge/index.html?type=cash";
	/**提现-请求跳转地址*/
	public static final String CASH_VOUCH_REQUEST_URL = "/member/recharge/vouchIndex.html?type=cash";
	
	/**修改存管密码-图形类型*/
	public static final String BINDPASS_PICTYPE = "type_bp";
	/**修改存管密码-请求类型*/
	public static final String BINDPASS_REQUEST_TYPE = "修改存管密码";
	/**修改存管密码-请求名*/
	public static final String BINDPASS_REQUEST_NAME = "我的账户>账户概览";
	/**修改存管密码-请求跳转地址*/
	public static final String BINDPASS_REQUEST_URL = "/member/account/main.html";
	
	
	/**开户-图形类型*/
	public static final String REGISTER_PICTYPE = "type_kh";
	/**开户-请求类型*/
	public static final String REGISTER_REQUEST_TYPE = "开户";
	/**开户-请求名*/
	public static final String REGISTER_REQUEST_NAME = "账户管理>基本信息";
	/**开户-请求跳转地址*/
	public static final String REGISTER_REQUEST_URL = "/member/security/realNameIdentify.html";
	/**开户-请求跳转地址-渤海银行*/
	public static final String REGISTER_REQUEST_CBHB_URL = "/member/baseInfo/index.html";
	
	/**授权-图形类型*/
	public static final String AUTH_SIGN_PICTYPE = "type_sq";
	/**授权-请求类型*/
	public static final String AUTH_SIGN_REQUEST_TYPE = "业务授权";
	/**授权-请求名*/
	public static final String AUTH_SIGN_REQUEST_NAME = "账户管理>基本信息";
	/**授权-请求跳转地址*/
	public static final String AUTH_SIGN_REQUEST_URL = "/member/baseInfo/index.html";
	
	/**绑卡-图形类型*/
	public static final String BANK_CARD_PICTYPE = "type_bk";
	/**绑卡-请求类型*/
	public static final String BANK_CARD_REQUEST_TYPE = "绑卡";
	public static final String BANK_CARD_REQUEST_TYPE_UN_BIND = "解绑";
	/**绑卡-请求名*/
	public static final String BANK_CARD_REQUEST_NAME = "账户管理>银行卡";
	/**绑卡-请求跳转地址*/
	public static final String BANK_CARD_REQUEST_URL = "/member/bankCard/list.html";
	
	/**修改银行卡-图形类型*/
	public static final String BIND_CARD_PICTYPE = "type_bk";
	/**修改银行卡-请求类型*/
	public static final String BIND_CARD_REQUEST_TYPE = "绑卡";
	public static final String BIND_CARD_REQUEST_TYPE_UN_BIND = "解绑";
	/**修改银行卡-请求名*/
	public static final String BIND_CARD_REQUEST_NAME = "账户管理>银行卡";
	/**修改银行卡-请求跳转地址*/
	public static final String BIND_CARD_REQUEST_URL = "/member/bankCard/list.html";
	
	/**修改手机号-图形类型*/
	public static final String BIND_MOBILE_PICTYPE = "type_bk";
	/**修改手机号-请求类型*/
	public static final String BIND_MOBILE_REQUEST_TYPE = "修改手机号";
	/**修改手机号-请求名*/
	public static final String BIND_MOBILE_REQUEST_NAME = "账户管理>基本信息";
	/**修改手机号-请求跳转地址*/
	public static final String BIND_MOBILE_REQUEST_URL = "/member/baseInfo/index.html";
	
	
	/**支付-投资、转让、变现-图形类型*/
	public static final String PAY_PICTYPE = "type_zf";
	/**支付-投资、转让、变现-请求类型*/
	public static final String PAY_REQUEST_TYPE = "支付";
	/**支付-投资、转让、变现-请求名*/
	public static final String PAY_REQUEST_NAME = "我的资产>我的投资";
	/**支付-投资、转让、变现-请求跳转地址*/
	public static final String PAY_BUTTON_NAME = "继续投此产品";
	
	
	/**错误-图形类型*/
	public static final String ERROR_PICTYPE = "type_error";
	
}

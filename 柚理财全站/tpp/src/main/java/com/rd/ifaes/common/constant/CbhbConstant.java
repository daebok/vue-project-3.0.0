package com.rd.ifaes.common.constant;

import com.rd.ifaes.core.core.util.ConfigUtils;


/**
 * 常用变量类
 * 
 * @author ZhangBiao
 * @date 2017-2-21
 * @version 3.0
 */
public final class CbhbConstant {
	/**
	 * 异步回调回调
	 */
	public static final String CBHB_RESPONSE_TYPE_NOTIFY ="3";
	/**
	 * 同步回调
	 */
	public static final String CBHB_RESPONSE_TYPE_RETURN ="2";
	/**
	 * 实时回调
	 */
	public static final String CBHB_RESPONSE_TYPE_CURRENT ="1";
	
	/**
	 * 渤海银行证书私钥密码
	 */
	public static final String CBHB_PRIVATE_KEY = "cbhb_private_key";
	/**
	 * 渤海银行标识
	 */
	public static final String CBHB_TPP_NAME = "cbhb";
	
	/**
	 * 处理--响应
	 */
	public static final String CBHB_HANDLE_TYPE_RESPONSE= "0";
	/**
	 * 处理--请求
	 */
	public static final String CBHB_HANDLE_TYPE_REQUEST = "1";
	/**
	 * 渤海银行 银行卡 枚举类行
	 */
	public static final String CBHB_BANK_CARD = "cbhb_bank_card";
	/**
	 * 渤海银行交易状态枚举类型
	 */
	public static final String CBHB_TRANSSTAT =  "cbhb_transStat";
	/**
	 * 渤海银行商户号ID
	 */
	public static final String CBHB_CUSTOMID ="cbhb_customerId";
	/**
	 * 涉及到异步回调结果通知的接口时，存管系统会以HTTP方式回调，商户系统接收到回调请求需要返回接收500成功
	 */
	public static final String CBHB_SUCCESS_CODE = "SUCCESS";
	/**
	 * 成功的返回码
	 */
	public static final String CBHB_CODE_SUCCESS = "000000";
	/**
	 * 渤海银行 --后台方式提交 超时时间  300s
	 */
	public static final int CBHB_TIME_OUT = 300000;
	/**
	 * 对应GBK
	 */
	public static final String CHAR_SET = "00";
	/**
	 * 常用字符类型0
	 */
	public static final String STRING_ZERO = "0";
	/**
	 * 常用字符类型1
	 */
	public static final String STRING_ONE = "1";
	/**
	 * 常用字符类型2
	 */
	public static final String STRING_TWO = "2";
	/**
	 * 常用字符类型100
	 */
	public static final String STRING_ONE_HUNDRED = "100";
	
	/**
	 * 证件类型：身份证
	 */
	public static final String CARD_TYPE = "00";
	
	/**
	 * 版本类型
	 */
	public static final String VERSION_NO = "2.0";
	
	/**
	 * 字符类型：utf-8
	 */
	public static final String CHAR_SET_UTF8 = "02";
	
	/**
	 * 签名方式  RSA 
	 */
	public static final String SIGN_TYPE_RSA  ="RSA";

	/**
	 * 签名方式  MD5 
	 */
	public static final String SIGN_TYPE_MD5  ="MD5";
	/**
	 * 消息类型：放款（FTP&后台方式）
	 */
	public static final String BIZ_TYPE_FILE_RELEASE ="FileRelease";
	/**
	 * 消息类型：批量撤销（FTP&后台方式）
	 */
	public static final String BIZ_TYPE_BAT_INVEST_CANCLE ="BatInvestCancle";
	/**
	 * 消息类型:投标撤销
	 */
	public static final String BIZ_TYPE_INVEST_CANCLE = "InvestCancle";
	/**
	 * 消息类型:用户提现页面方式响应返回类型
	 */
	public static final String BIZ_TYPE_DRAWINGS_RESULT = "DrawingsResult";
	/**
	 * 消息类型:用户提现页面方式请求类型
	 */
	public static final String BIZ_TYPE_DRAWINGS = "Drawings";
	/**
	 * 消息类型:修改手机号
	 */
	public static final String BIZ_TYPE_BIND_PASS = "BindPass";
	/**
	 * 消息类型:修改手机号
	 */
	public static final String BIZ_TYPE_BIND_MOBILE_WEB = "BindMobileNo";
	/**
	 * 消息类型:修改绑定银行卡
	 */
	public static final String BIZ_TYPE_BIND_CAED_WEB = "BindCardWeb";
	
	/**
	 * 消息类型:新用户注册
	 */
	public static final String BIZ_TYPE_REAL_NAME_WEB = "RealNameWeb";
	
	/**
	 * 消息类型:用户信息查询
	 */
	public static final String BIZ_TYPE_QUERY_USER_INF = "QueryUserInf";
	
	/**
	 * 消息类型:用户余额查询
	 */
	public static final String BIZ_TYPE_QUERY_BALANCE = "QueryBalance";
	
	/**
	 * 消息类型:商户自主提现
	 */
	public static final String BIZ_TYPE_MERC_WITHDRAW = "MercWithdraw";
	
	/**
	 * 消息类型:还款（不含收取担保费）
	 */
	public static final String BIZ_TYPE_FILE_REPAYMENT = "FileRepayment";
	
	/**
	 * 消息类型:用户充值
	 */
	public static final String BIZ_TYPE_WEB_RECHARGE = "WebRecharge";
	
	/**
	 * 消息类型:用户投标
	 */
	public static final String BIZ_TYPE_BACK_INVEST = "BackInvest";
	
	/**
	 * 消息类型:建标
	 */
	public static final String BIZ_TYPE_CREATE_BID = "CreateBid";
	
	/**
	 * 消息类型:商户账户充值
	 */
	public static final String BIZ_TYPE_MERC_RECHARGE = "MercRecharge";
	
	/**
	 * 消息类型:红包申请通知
	 */
	public static final String BIZ_TYPE_EXPER_CAP = "ExperCap";
	
	/**
	 * 消息类型:存量用户注册
	 */
	public static final String BIZ_TYPE_EXIST_USER_REGISTER = "ExistUserRegister";
	
	/**
	 * 消息类型:实时红包
	 */
	public static final String BIZ_TYPE_EXPER_BONUS = "ExperBonus";
	
	/**
	 * 消息类型:资金转出
	 */
	public static final String BIZ_TYPE_UN_HOLD_AMT = "UnHoldAmt";
	
	/**
	 * 消息类型:资金转出冻结
	 */
	public static final String BIZ_TYPE_HOLD_AMT = "HoldAmt";
	
	/**
	 * 消息类型:商户账户查询
	 */
	public static final String BIZ_TYPE_QUERY_MERCHANT_ACCTS = "QueryMerchantAccts";
	
	/**
	 * 消息类型:交易状态查询
	 */
	public static final String BIZ_TYPE_QUERY_TRANS_STAT = "QueryTransStat";
	
	/**
	 * 消息类型：动态口令申请
	 */
	public static final String BIZ_TYLE_SEND_UAP_MSG ="sendUapMsg";
	
	/**
	 * 渤海银行测试环境提交地址
	 */
	public static final String CBHB_SUBMIT_URL = "cbhb_submit_url";
	
	/**
	 * 商户号
	 */
	public static final String PARTNER_ID = ConfigUtils.getValue(CBHB_CUSTOMID);
	/**
	 * 公共变量
	 */
	private CbhbConstant() {

	}

}

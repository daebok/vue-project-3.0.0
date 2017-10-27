package com.rd.ifaes.core.core.constant;

/**
 * 队列常量类
 * @version 3.0
 * @author lh
 * @date 2016年8月6日
 */
public final class MqConstant {
	
	private MqConstant() {
	}
	
	/**
	 * 默认交换机名称
	 */
	public static final String DEFAULT_EXCHANGE_NAME = "rd-mq-exchange";
	
	/******************************** 队列通道key ********************************/
	/**
	 * 队列通道key：日志
	 */
	public static final String ROUTING_KEY_LOG    = "queue_log_key";
	/**
	 * 队列通道key：消息
	 */
	public static final String ROUTING_KEY_MESSAGE = "queue_message_key";
	/**
	 * 队列通道key：自动投资
	 */
	public static final String ROUTING_KEY_AUTO_INVEST = "queue_auto_invest_key";
	/**
	 * 队列通道key：投资
	 */
	public static final String ROUTING_KEY_INVEST = "queue_invest_key";
	/**
	 * 队列通道key：放款
	 */
	public static final String ROUTING_KEY_LOAN = "queue_loan_key";
	/**
	 * 队列通道key：还款
	 */
	public static final String ROUTING_KEY_REPAY = "queue_repay_key";
	/**
	 * 队列通道key：用户
	 */
	public static final String ROUTING_KEY_USER = "queue_user_key";
	/**
	 * 队列通道key：取现
	 */
	public static final String ROUTING_KEY_CASH = "queue_cash_key";
	/**
	 * 队列通道key：第三方交易
	 */
	public static final String ROUTING_KEY_TRADE = "queue_trade_key";
	/**
	 * 队列通道key：第三方交易--放款
	 */
	public static final String ROUTING_KEY_TRADE_LOAN = "queue_trade_loan_key";
	/**
	 * 队列通道key：第三方交易--还款
	 */
	public static final String ROUTING_KEY_TRADE_REPAY = "queue_trade_repay_key";
	/**
	 * 队列通道key：第三方交易--项目撤销
	 */
	public static final String ROUTING_KEY_TRADE_INVESTFAIL = "queue_trade_investFail_key";
	/**
	 * 队列通道key：协议
	 */
	public static final String ROUTING_KEY_PROTOCOl = "queue_protocol_key";
	
	/**
	 * 队列通道key：活动方案
	 */
	public static final String ROUTING_KEY_ACTPLAN = "queue_actplan_key";
	/**
	 * 队列通道key：账户日志
	 */
	public static final String ROUTING_KEY_ACCOUNT_LOG = "queue_account_log_key";
	/**
	 * 队列通道key：提现免手续费标记日志
	 */
	public static final String ROUTING_KEY_CASH_FEE_MARK_LOG = "queue_cash_fee_mark_log_key";
	
	
	/******************************** 操作  ********************************/
	/**
	 * 操作：投资
	 */
	public static final String OPERATE_INVEST_SUCCESS = "investSuccess";
 
	/**
	 * 操作：超时后支付成功
	 */
	public static final String OPERATE_INVEST_TIMEOUT = "InvestTimeout";
	
	/**
	 * 操作：投资支付失败
	 */
	public static final String OPERATE_INVEST_PAY_FAIL = "investPayFail";
	
	/**
	 * 操作：投资失败接口（investFail)
	 */
	public static final String OPERATE_INVEST_FAIL = "investFail";
	
	/**
	 * 操作：放款接口（loan ) 
	 */
	public static final String OPERATE_LOAN = "loan";
	/**
	 * 操作：还款接口 ( repayment )
	 */
	public static final String OPERATE_REPAY = "repay";
	/**
	 * 操作：还垫付款接口 ( repayBail )
	 */
	public static final String OPERATE_REPAY_BAIL = "repayBail";
	/**
	 * 操作：还款结束批量结束债权 (batchCreditEnd)
	 */
	public static final String OPERATE_BATCH_CREDIT_END = "batchCreditEnd";
	
	/**
	 * 操作：开始自动投资业务
	 */
	public static final String OPERATE_START_AUTO_INVEST = "startAutoInvest";

	/**
	 * 操作：债权投资
	 */
	public static final String OPERATE_BOND_INVEST = "bondInvest";
	
	/**
	 * 操作：债权投资失败
	 */
	public static final String OPERATE_BOND_INVEST_FAIL ="bondInvestFail";
	/**
	 * 操作：债权投资异步业务处理
	 */
	public static final String OPERATE_BOND_INVEST_OTHER ="bondInvestOther";
	/**
	 * 操作：转让后更新bond_invest和bond_collection的investId;
	 */
	public static final String OPERATE_UPDATE_INVEST_ID ="updateInvestId";
	
	
	/**
	 * 操作：个人注册
	 */
	public static final String OPERATE_REGISGER = "regisger";
	
	/**
	 * 操作：企业注册
	 */
	public static final String OPERATE_COMPANY_REGISGER = "companyRegisger";
	
	/**
	 * 操作：对比用户资金
	 */
	public static final String OPERATE_USER_COMPARE_ACCOUNT = "userCompareAccount";
	/**
	 * 操作：第三方调度任务预警
	 */
	public static final String TPP_WARN = "tpp_warn";
	
	/**
	 * 操作：提现-同步异步回调
	 */
	public static final String OPERATE_CASH = "cash";
	
	/**
	 * 操作：提现-异步对账回调
	 */
	public static final String OPERATE_CASH_BANK_RETURN = "cashBankReturn";
	
	/**
	 * 操作：取现复核-异步回调
	 */
	public static final String OPERATE_CASH_AUDIT = "cashAudit";

	/**
	 * 操作：充值成功
	 */
	public static final String OPERATE_RECHARGE_SUCCESS = "rechargeSuccess";
	
	/**
	 * 操作：充值失败
	 */
	public static final String OPERATE_RECHARGE_FAIL = "rechargeFail";
	
	/**
	 * 操作：第三方交易
	 */
	public static final String OPERATE_TPP_TASK = "tppTask";
	
	/**
	 * 操作：生成债权协议
	 */
	public static final String BUILD_BOND_PROTOCOL = "build_bond_protocol";
	
	/**
	 * 操作：生成借款协议
	 */
	public static final String BUILD_PROTOCOL = "build_protocol";
	/**
	 * 活动大类：注册 --送积分
	 */
	public static final String OPERATE_ACTPLAN_REGISTER_SCORE = "1_3";
	/**
	 * 活动大类：注册 -- 注册好友送好礼--送加息券
	 */
	public static final String OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_RATECOUPON = "1_2_2";
	/**
	 * 活动大类：注册 -- 注册好友送好礼--送红包
	 */
	public static final String OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_REDPACKET = "1_2_1";
	
	/**
	 * 活动大类：注册 -- 注册送好礼--送加息券
	 */
	public static final String OPERATE_ACTPLAN_REGISTER_GIFT_RATECOUPON = "1_1_2";
	
	/**
	 * 活动大类：注册 -- 注册送好礼--送红包
	 */
	public static final String OPERATE_ACTPLAN_REGISTER_GIFT_REDPACKET = "1_1_1";
	/**
	 * 活动大类：注册
	 */
	public static final String OPERATE_ACTPLAN_REGISTER = "1";
	
	/**
	 * 活动大类：首投
	 */
	public static final String OPERATE_ACTPLAN_INVEST_FIRST = "2" ;
	
	/**
	 * 活动大类：投资
	 */
	public static final String OPERATE_ACTPLAN_INVEST = "3" ;
	
	/**
	 * 活动大类：平台自定义活动
	 */
	public static final String OPERATE_ACTPLAN_CUSTOM = "4" ;
	
	/**
	 * 活动大类：登录--积分
	 */
	public static final String OPERATE_ACTPLAN_LOGIN = "5";
	
	/**
	 * 活动大类：其他
	 */
	public static final String OPERATE_ACTPLAN_OTHER = "99" ;
	
	/**
	 * 活动方案：批量用户红包发放
	 */
	public static final String OPERATE_ACTPLAN_BATCH_USER_RED_GRANT = "batch_user_red_grant";
	
	/**
	 * 操作：用户投资记录
	 */
	public static final String OPERATE_USER_INVEST_NUM = "userInvestNum";

}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 * 借贷资源标识
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月2日
 */
public final class LoanResource {
	
	private LoanResource() {
	}
	/**
	 * 类别不存在
	 */
	public static final String PROJECTTYPE_NOT_EXISTS = "projectType.not.exists";
	/**
	 * 协议无效
	 */
	public static final String PROTOCOL_STATUS_NOT ="protocol_status_not_valid";
	/**
	 * 项目ID为空
	 */
	public static final String PROJECT_ID_IS_NULL = "web.invest.projectIdIsNull";
	/**
	 * 投资记录未找到
	 */
	public static final String INVEST_NOT_FOUND = "web.invest.notFound";
	/**
	 * 投资状态已改变,请刷新页面
	 */
	public static final String INVEST_STATUS_NOTRIGHT = "invest.status.notright";
	/**
	 * 投资人不正确
	 */
	public static final String INVEST_USER_NOTRIGHT = "invest.user.notright";
	
	/**
	 *风险承受能力不足
	 */
	public static final String INVEST_RISK_LEVEL_TOO_LOW = "invest.riskLevel.tooLow";
	
	/**
	 * 项目不存在
	 */
	public static final String PROJECT_NOT_EXISTS = "web.project.notExists";

	/**
	 * 项目可投金额不足
	 */
	public static final String PROJECT_ACCOUNT_NOT_ENOUGH = "core.project.account.notEnoughError";

	/**
	 * 项目已筹满
	 */
	public static final String PROJECT_AMOUNT_FULL = "core.project.amountFullError";
	/**
	 * 项目筹集结束
	 */
	public static final String PROJECT_NOT_RAISING = "core.project.isNotRaising";
	/**
	 * 项目定向密码错误
	 */
	public static final String SALE_PASS_ERROR = "core.project.salePasswordError";
	/**
	 * 项目定向销售VIP等级错误:仅VIP{0}以上会员可投资
	 */
	public static final String SALE_RULE_VIP_ERROR = "project.saleVipLevel.msg";
	/**
	 * 未到项目筹集时间
	 */
	public static final String INVEST_BEFORE_SALE_TIME = "core.project.investBeforeSaleTime";
	/**
	 *  项目筹集时间已结束
	 */
	public static final String INVEST_AFTER_SALE_END = "core.project.investAfterSaleEnd";
	/**
	 * 投资金额不合法
	 */
	public static final String INVEST_AMOUNT_IS_NOT_VAILD = "web.invest.amountIsNotVaild";
	/**
	 * 投资金额小于起投金额
	 */
	public static final String INVEST_AMOUNT_LESS_LOWEST = "core.invest.amountLessLowest";
	/**
	 * 剩余可投较少，需要全部购买
	 */
	public static final String INVEST_NOT_EQ_REMAIN_AMOUNT= "core.invest.notEqRemainAmount";
	/**
	 * 投资金额大于最高投资金额
	 */
	public static final String INVEST_AMOUNT_GREATER_MOST = "core.invest.amountGreaterMost";
	/**
	 * 投资自己的借款
	 */
	public static final String INVEST_SELF_BORROW = "core.invest.investSelfBorrow";
	/**
	 * 重复投新手标
	 */
	public static final String INVEST_NOVICE_AGAIN = "core.invest.investNoviceAgain";
	/**
	 * 定向邮箱错误
	 */
	public static final String INVEST_NOVICE_SPECIFIC_SALE_MAIL = "core.invest.SpecificSale.mail";
	/**
	 * 投资金额不匹配递增金额
	 */
	public static final String INVEST_AMOUNT_NOT_MATCH_STEP = "core.invest.mountNotMatchStepError";
	/**
	 * 投资项目不可使用加息劵
	 */
	public static final String INVEST_CANNOT_USE_RATECOUPON="core.rateCoupon.cannotUseError";
	/**
	 * 投资使用加息劵不存在
	 */
	public static final String INVEST_RATECOUPON_NOT_EXISTS= "core.rateCoupon.cannotUseError";
	/**
	 * 投资使用非本人的加息劵
	 */
	public static final String INVEST_USE_OTHER_RATECOUPON= "core.rateCoupon.useByOtherError";
	/**
	 * 投资使用无效加息劵：过期、已使用或限制项目类别
	 */
	public static final String INVEST_USE_INVALID_RATECOUPON=  "core.rateCoupon.useError";
	/**
	 * 投资项目不可使用红包
	 */
	public static final String INVEST_CANNOT_USE_REDENVELOPE="core.redEnvelope.cannotUseError";
	/**
	 * 投资使用红包不存在
	 */
	public static final String INVEST_REDENVELOPE_NOT_EXISTS= "core.redEnvelope.noExistsError";
	/**
	 * 投资使用非本人的红包
	 */
	public static final String INVEST_USE_OTHER_REDENVELOPE= "core.redEnvelope.useByOtherError";
	/**
	 * 投资使用无效红包：过期、已使用或限制项目类别
	 */
	public static final String INVEST_USE_INVALID_REDENVELOPE= "core.redEnvelope.useError";
	/**
	 * 单笔投资使用红包过多
	 */
	public static final String INVEST_USE_TOO_MUCH_REDENVELOPE="core.redEnvelope.useTooMuchError";
	/**
	 * 投资实际支付不可为零
	 */
	public static final String INVEST_REAL_AMOUNT_NOT_EQ_ZERO="invest.realAmount.eqZeroError";
	/**
	 * 投资订单号不可为空
	 */
	public static final String INVEST_ORDER_NO_NOT_EMPTY = "invest.orderNo.notEmpty";
	/**
	 * 用户未支付投资订单过多
	 */
	public static final String INVEST_UNPAY_TOO_MUCH = "invest.unpay.tooMuch";
	/**
	 * 投资回调重复处理
	 */
	public static final String  INVEST_ORDER_HAS_HANDLED  = "invest.order.hasHandled";
	/**
	 * 投资订单超时时间设置错误
	 */
	public static final String INVEST_TIMEOUT_TIME_ERROR = "invest.timeout.timeError";
	/**
	 * 投资订单超时处理，订单状态错误
	 */
	public static final String INVEST_TIMEOUT_HANDLE_STATUS_ERROR = "invest.timeoutHandle.statusError";
	/**
	 * 投资订单冻结流水号为空错误
	 */
	public static final String INVEST_FREEZE_NO_EMPTY_ERROR = "core.invest.hasNotFreezeNo";
	/**
	 * 项目状态已不在募集中
	 */
	public static final String PROJECT_STATUS_NEQ_RAISING = "project.status.neq.raising";
	/**
	 * 该期借款不存在！
	 */
	public static final String REPAYMENT_NOEXISTS = "core.repayment.noexists";
	/**
	 * 该期借款已经还款,请不要重复操作！
	 */
	public static final String REPAYMENT_HASREPAID = "core.repayment.hasRepaid";
	/**
	 * 存在转让中的借款，不能进行还款操作
	 */
	public static final String REPAYMENT_HASBOND = "core.repayment.hasBond";
	/**
	 * 该项目本期前还有尚未还款的借款
	 */
	public static final String REPAYMENT_HASUNPAYRECORDS = "core.repayment.hasUnpayRecords";
	/**
	 *还款失败，可用金额不足
	 */
	public static final String REPAYMENT_USEMONEY_NOT_ENOUGH= "core.repayment.useMoneyNotEnough";
	/**
	 *预约成功，提示用户
	 */
	public static final String INVEST_BESPEAK_SUCCESS= "invest.bespeak.success";
	/**
	 *查询三方账户账户失败
	 */
	public static final String TPP_QUERY_ACCOUNT_FAIL= "tpp.account.queryFail";
}

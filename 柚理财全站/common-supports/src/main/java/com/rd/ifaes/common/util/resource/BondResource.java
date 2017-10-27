/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 * 债权模块资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月23日
 */
public class BondResource {
	public BondResource() {
		
	}
	/**
	 * 债权标待收不存在
	 */
	public static final String BOND_COLLECTION_IS_NOT_EXISTS = "bond.message.collection.exists";
	/**
	 * 请勿重复点击提交发布债权
	 */
	public static final String BOND_INSERT_REPEAT = "bond.message.insert.repeat";
	/**
	 * 勾选还款日限制距离还款日必须大于0
	 */
	public static final String BOND_REMAINDAYS_ZERO = "bond.message.remainDays.zero";
	/**
	 * 勾选到期日限制距离本期还款日必须大于0
	 */
	public static final String BOND_PERIODDAYS_ZERO = "bond.message.perioddays.zero";
	/**
	 * 勾选起息日限制持有天数必须大于0
	 */
	public static final String BOND_HOlDDAYS_ZERO = "bond.message.holddays.zero";
	/**
	 * 折溢价率的最小值不能小于-100
	 */
	public static final String BOND_DISRATE_MIN_LIMIT = "bond.message.disrate.min.limit";
	/**
	 * 折溢价率的最大值不能大于100
	 */
	public static final String BOND_DISRATE_MAX_LIMIT = "bond.message.disrate.max.limit";
	/**
	 * 您的债权溢价率超出了限制范围，不允许转让
	 */
	public static final String BOND_MAXAPR_LOW_RULE = "bond.message.maxApr.low.rule";
	/**
	 * 剩余期限天数小于等于0天,禁止发布转让
	 */
	public static final String BOND_DAYS_LOW_ZERO ="bond.message.bondDay.low.zero";
	/**
	 * 等额本金或者等额本息还款方式下必须全额受让
	 */
	public static final String BOND_INVEST_MUST_BUY_ALL ="bond.message.invest.must.buy.all";
	/**
	 * 该笔债权标存在待支付的订单，不允许撤回
	 */
	public static final String BOND_INVEST_INIT_STATUS_EXISTS ="bond.message.invest.status_init.exists";
	/**
	 * 折溢价率的小数部分不能大于等于2位
	 */
	public static final String BOND_DISRATE_VALIDNUM_LIMIT = "bond.message.disrate.validnum.limit";
	/**
	 * 折溢价率的整数部分不能大于2位
	 */
	public static final String BOND_DISRATE_NUM_LIMIT = "bond.message.disrate.num.limit";
	/**
	 * 债权协议不存在
	 */
	public static final String BOND_PROTOCOL_IS_NOT_EXISTS = "bond.message.bond.protocol.is.not.exists";
	/**
	 * 折溢价率必须是数字
	 */
	public static final String BOND_DISRATE_IS_MUST_NUM = "bond.message.bondrule.disrate.is.must.num";
	/**
	 * 非待支付状态,不能支付
	 */
	public static final String BOND_INVEST_PAY_STATUS = "bond.message.invest.is.pay.status";
	/**
	 * 债权已经投资满额,不能撤回!
	 */
	public static final String BOND_INVEST_HAS_OK = "bond.message.invest.has.ok";
	/**
	 * 折溢价率不能大于最大折溢价率
	 */
	public static final String BOND_APR_UP_MAX = "bond.message.bondAprUpMax";
	/**
	 * 折溢价率不能小于最小折溢价率
	 */
	public static final String BOND_APR_LO_MIN = "bond.message.bondAprLoMin";
	/**
	 * 债权投资金额有误
	 */
	public static final String  BOND_INVEST_ACCOUNTERROR=  "bond.message.bondInvestAccountError";
	/**
	 * 投资金额大于债权最高投资金额
	 */
	public static final String  BOND_INVEST_MOSTLIMIT=  "bond.message.bondInvestMostLimitError";
	/**
	 * 投资金额小于债权起投金额
	 */
	public static final String BOND_INVEST_LOWESTLIMIT=  "bond.message.bondInvestLowestLimitError";
	/**
	 * 剩余债权小于2倍的最小投资金额，则需要全部受让
	 */
	public static final String  BOND_INVEST_DOUBLELOWESTLIMIT=   "bond.message.bondInvest.doubleLowestLimitError";
	/**
	 * 一次性全额受让必须全部购买
	 */
	public static final String  BOND_INVEST_RULEBUYALLLIMIT=  "bond.message.bondInvest.ruleBuyAllLimitError";
	/**
	 * 债权投资金额不足
	 */
	public static final String  BOND_INVEST_ACCOUNTNOTENOUGH = "bond.message.bondInvestAccountNotEnough";
	/**
	 * 投资用户不存在
	 */
	public static final String  BOND_INVESTUSER_ISNULL =  "bond.message.investUserIsNull";
	/**
	 * 原始标投资记录不存在
	 */
	public static final String  BOND_PROJECTINVEST_ISNULL =  "bond.message.projectInvestIsNull" ;
	/**
	 * 原始标投资记录标识不存在
	 */
	public static final String   BOND_PROJECTINVEST_IDISNULL= "bond.message.projectInvestIdIsNull";
	/**
	 * 原始标借款人不存在
	 */
	public static final String   BOND_BORROWUSER_NULL = "bond.message.borrowUserIsNull";
	/**
	 * 原始标借款人不能投资
	 */
	public static final String  BOND_BORROWUSER_NOTINVEST =  "bond.message.borrowUserNotInvestError" ;
	/**
	 * 债权投资不能是发布债权的人
	 */
	public static final String  BOND_INVEST_NOTMIME= "bond.message.bondInvestUserIsMineError";
	/**
	 * 债权表状态不是发布中状态
	 */
	public static final String BOND_STATSU_ISNOTPUBLISH =  "bond.message.bondStatusIsNotPublishError";
	/**
	 * 借款标不是还款中状态,不能发布转让
	 */
	public static final String NOT_REPAY_START = "bond.message.notRepayStart";
	/**
	 * 收费起步金额 不能为空
	 */
	public static final String BOND_RULE_FEE_INITIATEAMOUNT_NOT_EXISTS = "bond.message.rule.feeInitiateAmount.notExists";
	
	/**
	 * 单笔手续费上限值不能为空
	 */
	public static final String BOND_RULE_FEEBUYFMAX_NOT_EXISTS= "bond.message.rule.feeBuyMax.notExists";
	
	/**
	 * 固定金额手续费金额不能为空
	 */
	public static final String BOND_RULE_FEE_FIXD_NOT_EXISTS = "bond.message.rule.feeFixd.notExists";
	
	/**
	 * 比例手续费比例值不能为空
	 */
	public static final String BOND_RULE_FEE_RATE_NOT_EXISTS = "bond.message.rule.feeRate.notExists";
	
	/**
	 * 汇付限制,比例手续费比例值不能超过{}%
	 */
	public static final String BOND_RULE_FEE_RATE_OVER_LIMIT = "bond.message.rule.feeRate.overLimit";
	
	/**
	 * 折溢价率下限不能超过折溢价率上限
	 */
	public static final String BOND_RULE_BONDAPR_MIN_OVER_MAX = "bond.message.rule.bondAprMinOverMax";
	
	/**
	 * 折溢价率上限、下限均不能为空
	 */
	public static final String BOND_RULE_BONDAPR_MIN_MAX_NOT_EXISTS = "bond.message.rule.bondAprMinMaxNotExists";
	
	/**
	 * 若选择部分受让,必须填写单笔受让最低限制金额!
	 */
	public static final String BOND_RULE_LOWEST_BUYACCOUNT_NOT_EXISTS = "bond.message.rule.lowestBuyAccountNotExists";
	
	/**
	 * 若选择部分转让,必须填写最低转让金额大于等于单笔受让最低金额的倍率!
	 */
	public static final String BOND_RULE_LOWEST_SELL_UPPER_MITY = "bond.message.rule.lowestSellUpperMity";
	
	/**
	 * UFX对接汇付第三方接口不允许债权部分受让,请选择一次性全额受让
	 */
	public static final String BOND_RULE_UPDATE_TPP_BUY_LIMIT = "bond.message.rule.tppBuyLimit";
	
	/**
	 * UFX对接汇付第三方接口不允许债权部分转让,请选择一次性全额转让
	 */
	public static final String BOND_RULE_UPDATE_TPP_SELL_LIMIT = "bond.message.rule.tppSellLimit";
	
	/**
	 * 受让记录不存在
	 */
	public static final String  BOND_INVEST_NOT_EXISTS = "bond.message.bondInvest.notExists";
	
	/**
	 * 受让记录标识不存在
	 */
	public static final String BOND_INVEST_ID_NOT_EXISTS = "bond.message.bondInvestId.notExists";
	/**
	 * 投资超投
	 */
	public static final String BOND_INVEST_ACCOUNT_NOT_ENOUGH =  "bond.message.bondInvestAccountNotEnough";
	
	/**
	 * 放款未结束
	 */
	public static final String BOND_INVEST_LOAN_NOT_SUCCESS = "bond.message.bondInvest.loan.not.success";
	
	/**
	 * 未支付的订单数过多
	 */
	public static final String BOND_INVEST_UNPAY_TOOMUCH = "bond.message.bondInvest.unpay.tooMuch";
	/**
	 * 汇付限制，只能一次性全部转让
	 */
	public static final String BOND_UFX_HUIFU_LIMIT = "bond.message.huifulimit";
	/**
	 * 债权发布已经处理了,请勿多次提交处理!
	 */
	public static final String BOND_INVEST_INSERT_HAS_HANDLE ="bond.message.bondInsertHasHandle";
	/**
	 * 债权投资订单已经处理了
	 */
	public static final String BOND_INVEST_ORDER_NO_HAS_HANDLE ="bond.message.bondInvestOrderHasHandle";
	/**
	 * 债权投资订单号不能为空
	 */
	public static final String BOND_INVEST_ORDER_NO_NOT_EMPTY = "bond.message.bondInvestOrderNoNotEmpty";
	/**
	 * 回调验签失败
	 */
	public static final String TPP_NOTIFY_SIGN_FAIL = "tpp.notify.sign.fail";
	/**
	 * 收益计算错误
	 */
	public static final String BOND_INTEREST_AMOUNT_NULL = "bond.message.bondInterest.amountIsNull";
	/**
	 * 债权不存在
	 */
	public static final String BOND_NOT_EXISTS = "bond.message.bondNotExists";
	/**
	 * 债权已经售完，不可撤回
	 */
	public static final String BOND_IS_SALE_OK = "bond.message.bondIsSaleOkError";
	/**
	 * 债权已经撤销,不能重复处理
	 */
	public static final String BOND_IS_CANCLE = "bond.message.bondIsCancleError";
	/**
	 * 债权ID不能为空
	 */
	public static final String BOND_ID_NOT_EXISTS = "bond.message.bondIdNotExists";
	/**
	 * 债权发布失败
	 */
	public static final String  BOND_PUBISH_FAIL = "bond.message.bondPublishFailError";
	/**
	 * 项目类型不存在
	 */
	public static final String PROJECT_TYPE_NOT_EXISTS = "bond.message.projectTypeNotExists";
	
	/**
	 * 转让金额小于最低转让金额
	 */
	public static final  String BONDMONEY_LESS_LOWEST = "bond.message.bondMoneyLessLowsetMoneyError";
	
	/**
	 * 借款标不存在
	 */
	public static final String BORROW_NOT_EXISTS = "bond.message.borrowNotExists";
	
	/**
	 * 可转让列表标识不能为空
	 */
	public static final String PROJECT_INVEST_ID_NOT_EXISTS = "bond.message.projectInvest.idNotExists";
	
	/**
	 * 可转让投资记录对象不存在
	 */
	public static final String PROJECT_INVEST_NOT_EXISTS = "bond.message.projectInvest.isNotExists";
	
	/**
	 * 债权规则不存在
	 */
	public static final String BOND_RULE_NOT_EXISTS = "bond.message.bondRuleNotExists";
	
	/**
	 * 债权规则标识不存在
	 */
	public static final String BOND_RULE_ID_NOT_EXISTS = "bond.message.bondRule.idNotExists";
	
	/**
	 * 债权规则尚未配置
	 */
	public static final String BOND_RULE_STATUS_NOT_FOND = "bond.message.bondRuleNotFondError";
	
	/**
	 * 债权规则尚未开启
	 */
	public static final String BOND_RULE_STATUS_NOT_START = "bond.message.bondRuleNotStartError";
	
	/**
	 * 持有待收天数不足，不能发布债权
	 */
	public static final String BOND_RULE_HOLDDAY_NOT_ENOUGH = "bond.message.holdDayNotEnoughError";
	
	/**
	 *距离到期还款日天数不足，不能发布债权
	 */
	public static final String BOND_RULE_REMAINDAY_NOT_ENOUGH= "bond.message.remainDayNotEnoughError";
	
	/**
	 * 距离本期还款日到期天数不足，不能发布债权
	 */
	public static final String BOND_RULE_PERIODREMAINDAY_NOT_ENOUGH= "bond.message.periodRemainDayNotEnoughError";
	
	/**
	 * 原始标不存在
	 */
	public static final String PROJECT_NOT_EXISTS = "bond.message.project.notExists";
	
	/**
	 * 债权标对应原始标标识不能为空
	 */
	public static final String PROJECT_ID_NOT_EXISTS = "bond.message.project.idIsNull";
	/**
	 * 请输入小数点为2位的有效数字
	 */
	public static final String INPUT_MONEY_NOT_MONEY = "bond.message.intput.not.money";
	
	/**
	 * 转让次数已满，不可再继续转让
	 */
	public static final String BOND_COUNT_OVER = "bond.message.bondCountOverError";
	
	/**
	 * 原始标发布时不允许转让
	 */
	public static final String  BOND_NOT_TRANSFER_ABLE = "bond.message.bondNotTransferableError";
	
	/**
	 * 此笔债权已经发布,请勿重新发布
	 */
	public static final String  BOND_ALREADY_RELEASED = "bond.message.bondAlreaduReleasedError";
	
	
	/**
	 * 异常回显页面
	 */
	public static final String BONG_RETURN_PAGE = "/bond/index.html";
	
}

package com.rd.ifaes.core.core.constant;


/**
 * 资源常量类
 * @version 3.0
 * @author lh
 * @date 2016年8月10日
 */
public class ResourceConstant {
	
	private ResourceConstant() {
	}
	
	/************************************操作提示************************************/
	/**
	 * 操作成功
	 */
	public static final String GLOBAL_MSG_SUCCESS = "global.msg.success";
	/**
	 * 操作失败
	 */
	public static final String GLOBAL_MSG_FAIL = "global.msg.fail";
	/**
	 * 非法操作
	 */
	public static final String GLOBAL_MSG_ILLEGAL = "global.msg.illegal";
	/**
	 * 无操作记录
	 */
	public static final String GLOBAL_NO_RECORD = "global.noRecord";
	/**
	 * 记录不存在或无效
	 */
	public static final String GLOBAL_RECORD_NO_EXISTS = "global.record.noExists";
	/**
	 * 该状态下不可操作
	 */
	public static final String GLOBAL_STATUS_ERROR = "global.statusError";
	
	/**
	 *未指定删除的记录
	 */
	public static final String GLOBAL_DEL_NO_RECORD = "global.del.noRecord";
	
	/**
	 * global.code.exists
	 */
	public static final String GLOBAL_CODE_EXISTS = "global.code.exists";
	/**
	 * 验证码不能为空
	 */
	public static final String VALID_CODE_NOTEMPTY="common.message.validCode.notEmpty";
	/**
	 * 验证码错误
	 */
	public static final String VALID_CODE_ERROR="common.message.validCodeError";
	/**
	 * 表单重复提交错误
	 */
	public static final String REPEAT_SUBMIT_ERROR="common.form.repeatSubmitError";
	/**
	 * 返回
	 */
	public static final String COMMON_GO_BACK="common.go.back";	
	/******************* 队列问题 ********************************************/
	/**
	 *队列服务设置错误
	 */
	public static final String MQ_CONNECT_ERROR="global.mq.connectError";
	
	
	/************************************状态提示************************************/
	/**
	 * 请点击跳转通过实名认证
	 */
	public static final String GLOBAL_REAL_NAME_STATUS_ERROR = "global.realName.statusError";
	/**
	 * 请点击跳转通过授权认证
	 */
	public static final String GLOBAL_AUTH_SIGN_STATUS_ERROR = "global.authSign.statusError";
	/**
	 * 邮箱已绑定,请勿重复操作
	 */
	public static final String GLOBAL_EMAIL_STATUS_ERROR  = "global.email.statusError";
	/**
	 * 修改的邮箱不能与原邮箱相同
	 */
	public static final String EMAIL_CANNOTBE_SAME  = "email.cannotbe.same";
	/************************************DOMAIN************************************/
	
	/**
	 * 该还款方式不支持按日计息
	 */
	public static final String CALCULATOR_DAYREPAY_NOTSUPPORT = "calculator.dayRepay.notSupport";
	
	/**
	 * 该还款方式与投资期限类型不匹配
	 */
	public static final String CALCULATOR_REPAY_STYLE_ERROR = "calculator.repay.style.error";
	/**
	 * 投资期限为必填项
	 */
	public static final String PERIOD_NOT_EMPTY = "period.not.empty";
	/**
	 * 投资金额为必填项
	 */
	public static final String PRINCIPAL_NOT_EMPTY = "principal.not.empty";
	/**

	
	/************************************DOMAIN************************************/
	
	/**
	 * 固定还款日的区间应该在1-31之间
	 */
	public static final String FIXED_REPAY_DAY_ERROR="fixed.repay.day.error";
	
	/**
	 * 计息方式不能为空
	 */
	public static final String PROJECT_INTEREST_STYLE_NOT_EMPTY = "project.interestStyle.notEmpty";
	/**
	 * 还款方式不能为空
	 */
	public static final String PROJECT_REPAY_STYLE_NOT_EMPTY = "project.repayStyle.notEmpty";
	/**
	 * 期限类型不能为空
	 */
	public static final String PROJECT_TIME_TYPE_NOT_EMPTY = "project.timeType.notEmpty";

	/**
	 * 最低起投金额不能为空
	 */
	public static final String PROJECT_LOWEST_ACCOUNT_NOT_EMPTY = "project.lowestAccount.notEmpty";
	/**
	 * 最低起投金额不能小于{0}元
	 */
	public static final String PROJECT_LOWEST_ACCOUNT_LT_ONE_ERROR = "project.lowestAccount.ltOneError";
	/**
	 * 最低起投金额不能超过借款(产品)总额
	 */
	public static final String PROJECT_LOWEST_ACCOUNT_GT_ACCOUNT_ERROR = "project.lowestAccount.gtAccountError";
	/**
	 * 最低起投金额不能超过最高投资总额
	 */
	public static final String PROJECT_LOWEST_ACCOUNT_GT_MOST_ACCOUNT_ERROR = "project.lowestAccount.gtMostAccountError";
	/**
	 * 最高投资总额不能超过借款(产品)总额
	 */
	public static final String PROJECT_MOST_ACCOUNT_GT_ACCOUNT_ERROR = "project.mostAccount.gtAccountError";
	/**
	 * 固定还款日不能大于{0}天
	 */
	public static final String PROJECT_FIXED_REPAY_DAY_GT_DAYSOFMONTH = "project.fixedRepayDay.gtDaysOfMonth";
	
	/**
	 * 风险等级不能为空
	 */
	public static final String PROJECT_RISK_LEVEL_NOT_EMPTY = "project.riskLevel.notEmpty";
	/**
	 * 年利率不能为空
	 */
	public static final String PROJECT_APR_NOTEMPTY = "project.apr.notEmpty";
	/**
	 * 年利率范围错误
	 */
	public static final String PROJECT_APR_RANGE_ERROR = "project.apr.rangeError";
	/**
	 * 年利率最多保留两位小数
	 */
	public static final String PROJECT_APR_REMAINTWODECIMAL = "project.apr.remainTwoDecimal";
	
	/**
	 * 加息利率不能超过{0}%
	 */
	public static final String PROJECT_ADD_APR_RANGE_ERROR = "project.addapr.rangeError";
	/**
	 * 加息率和加息券不能叠加使用
	 */
	public static final String PROJECT_ADD_APR_ADDITIONALRATEUSEFUL_SUPERPOSITION = "project.addapr.additionalRateUseful.superposition";
	/**
	 * 所属类别不能为空
	 */
	public static final String PROJECT_PROJECT_TYPE_ID_NOT_EMPTY = "project.projectTypeId.notEmpty";
	/**
	 * 选择类别不存在或已删除
	 */
	public static final String PROJECT_PROJECT_TYPE_ID_NOT_EXISTS = "project.projectTypeId.noExists";
	/**
	 * 审核备注不能为空
	 */
	public static final String PROJECT_VERIFY_REMARK_NOT_EMPTY  = "project.verify.remark.not.empty";
	/**
	 * 审核备注长度错误
	 */
	public static final String PROJECT_VERIFY_REMARK_LENGTH_ERROR  = "project.verify.remark.lengthError";
	/**
	 * 审核项目不是担保项目
	 */
	public static final String PROJECT_VERIFY_NOT_VOUCH  = "project.verify.notVouchError";
	/**
	 * 当前用户不是该项目担保人
	 */
	public static final String PROJECT_VERIFY_NOT_VOUCH_USER  = "project.verify.notVouchUserError";
	
	/**
	 * 上架渠道不能为空
	 */
	public static final String PROJECT_SALE_CHANNEL_NOT_EMPTY = "project.saleChannel.notEmpty";
	/**
	 * 上架时间不能为空
	 */
	public static final String PROJECT_SHOW_TIME_NOT_EMPTY = "project.showTime.notEmpty";
	/**
	 * 销售时间不能为空
	 */
	public static final String PROJECT_SALE_TIME_NOT_EMPTY = "project.saleTime.notEmpty";
	/**
	 * 上架时间晚于销售时间
	 */
	public static final String PROJECT_SHOW_TIME_GT_SALE_TIME = "project.showTime.gtSaleTime";
	
	/**
	 * 产品销售规则不可为空
	 */
	public static final String PROJECT_SPECIFIC_SALE_RULE= "project.specificSaleRule.notEmpty";
	/**
	 *  红包可用最大比例数值错误（0<rate<=100）
	 */
	public static final String PROJECT_RED_ENVELOPE_RATE_ERROR= "project.redEnvelopeRate.rangeError";
	
	/**
	 * 已上架产品记录不可修改
	 */
	public static final String PROJECT_SHOW_EDIT_ERROR= "project.show.editError";
	
	/**
	 * 期限类型错误（每季还息到期还本，必须为月）
	 */
	public static final String PROJECT_TIME_TYPE_QUARTER_ERROR = "project.timeType.quarterError";
	/**
	 * 期限数值错误（每季还息到期还本，必须为3的整数倍）
	 */
	public static final String PROJECT_TIME_LIMIT_QUARTER_ERROR = "project.timeLimit.quarterError";
	/**
	 * 期限不能超过?天
	 */
	public static final String PROJECT_TIME_LIMIT_OUTOFDAYS = "project.timeLimit.outOfDays";
	/**
	 * 期限不能超过?个月
	 */
	public static final String PROJECT_TIME_LIMIT_OUTOFMONTHS = "project.timeLimit.outOfMonths";
	
	/**
	 * 利率过大或者期限过长导致利息超过本金
	 */
	public static final String PROJECT_INTEREST_MUSTLT_CAPITAL = "project.interest.mustlt.capital";
	
	/**
	 * 资金账户不能为空
	 */
	public static final String PRODUCT_USER_ID_NOT_EMPTY = "product.userId.notEmpty";
	/**
	 * 产品金额不能为空
	 */
	public static final String PROJECT_ACCOUNT_NOTEMPTY = "project.account.notEmpty";
	/**
	 * 产品金额范围错误
	 */
	public static final String PRODUCT_ACCOUNT_RANGE_ERROR = "product.account.rangeError";
	/**
	 * 每份金额不能小于等于0
	 */
	public static final String PRODUCT_COPY_ACCOUNT_LE_ZERO = "product.copyAccount.leZero";
	/**
	 * 每份金额不能大于产品总额
	 */
	public static final String PRODUCT_COPY_ACCOUNT_GT_ACCOUNT = "product.copyAccount.gtAccount";
	/**
	 * 每份金额必须能被产品总额整除
	 */
	public static final String PRODUCT_COPY_ACCOUNT_UNDIVISIBLE_BY_ACCOUNT = "product.copyAccount.undivisibleByAccount";
	/**
	 * 起投份数不能高于项目总份数
	 */
	public static final String PRODUCT_LOWEST_COPIES_GT_TOTAL_COPIES = "product.lowestCopies.gtTotalCopies";
	/**
	 * 起投份数不能高于项目总份数
	 */
	public static final String PRODUCT_MOST_COPIES_GT_TOTAL_COPIES = "product.mostCopies.gtTotalCopies";
	/**
	 * 产品详情不能为空
	 */
	public static final String PRODUCT_CONTENT_NOT_EMPTY = "product.content.notEmpty";
	
	/**
	 * 天标的还款方式只能为一次性还款
	 */
	public static final String PRODUCT_TIME_TYPE_DAY_ONETIME_REPAYMENT_ONLY = "product.timeTypeDay.oneTimeRepaymentOnly";
	
	/**
	 * 借款期限为1的还款方式只能为一次性还款
	 */
	public static final String PRODUCT_TIME_LIMIT_ONETIME_REPAYMENT_ONLY = "product.timeLimit.oneTimeRepaymentOnly";
	
	/**
	 * 借款人不存在
	 */
	public static final String PRODUCT_BORROWER_NOEXISTS = "product.borrower.noExists";
	/**
	 * 借款人未通过实名认证
	 */
	public static final String PRODUCT_BORROWER_NOAUTH = "project.borrower.noAuth";
	
	/**
	 * 借款人用户类型不明确
	 */
	public static final String PRODUCT_BORROWER_USERNATURE_NOTCLEAR = "project.borrower.userNature.notClear";
	
	/**
	 * 产品期限不能为空
	 */
	public static final String PRODUCT_TIMELIMIT_NOTEMPTY = "product.timeLimit.notEmpty";
	/**
	 * 企业缺少统一社会信用代码或者营业执照编号
	 */
	public static final String PRODUCT_COMPANY_CREDITCODE_BUSSINESSCODE_NOTEMPTY = "product.company.creditCode.businessCode.notEmpty";
	
	/**
	 * 项目[{0}]没有成功的投资记录
	 */
	public static final String PROJECT_HAS_NOTSUCCESSINVEST = "core.project.hasNotSuccessInvest";
	/**
	 * 项目还款金额为0不能进行还款操作
	 */
	public static final String PROJECT_REPAYMENT_MONEY_ZERO = "project.repayment.money.zero";
	/**
	 * 项目还款计划生成失败
	 */
	public static final String PROJECT_REPAYMENT_CREATE_FAIL = "project.repayment.create.fail";
	/**
	 *不支持即息理财
	 */
	public static final String PROJECT_NOSUPPORT_INTEREST_FINANCING= "project.interestFinancing.noSupport";
	/**
	 * 借款名称长度范围错误
	 */
	public static final String BORROW_PROJECT_NAME_LENGTH_ERROR = "borrow.projectName.lengthError";
	/**
	 * 借款详情不能为空
	 */
	public static final String BORROW_CONTENT_NOT_EMPTY = "borrow.content.notEmpty";
	/**
	 * 借款方不能为空
	 */
	public static final String BORROW_USER_ID_NOT_EMPTY = "borrow.userId.notEmpty";
	/**
	 * 借款用途不能为空
	 */
	public static final String BORROW_BORROW_USE_NOT_EMPTY = "borrow.borrowUse.notEmpty";
	/**
	 * 借款用途错误
	 */
	public static final String BORROW_BORROW_USE_ERROR = "borrow.borrowUse.error";
	/**
	 * 还款方式错误
	 */
	public static final String BORROW_REPAY_STYLE_ERROR = "borrow.repayStyle.error";
	/**
	 * 期限类型不正确
	 */
	public static final String PROJECT_TIMETYPE_ERROR = "project.timeType.error";
	/**
	 * 担保机构不存在
	 */
	public static final String BORROW_VOUCH_ERROR = "borrow.vouch.error";
	/**
	 * 借款期限不能为空
	 */
	public static final String BORROW_TIME_LIMIT_NOT_EMPTY = "borrow.timeLimit.notEmpty";
	/**
	 * 借款期限类型只能是天或者月
	 */
	public static final String BORROW_TIME_LIMIT_TYPE_ONLY_DAY_OR_MONTH = "borrow.timeLimitType.onlyDayOrMonth";
	/**
	 * 借款金额范围错误
	 */
	public static final String BORROW_ACCOUNT_RANGE_ERROR = "borrow.account.rangeError";
	/**
	 * 募集期不能为空
	 */
	public static final String BORROW_RAISE_TIME_LIMIT_NOT_EMPTY = "borrow.raiseTimeLimit.notEmpty";
	/**
	 * 募集期范围错误
	 */
	public static final String BORROW_RAISE_TIME_LIMIT_RANGE_ERROR = "borrow.raiseTimeLimit.rangeError";
	/**
	 * 借款名称不能为空
	 */
	public static final String BORROW_PROJECTNAME_NOTEMPTY = "borrow.projectName.notEmpty";
	/**
	 * 借款名称不能超过？个字符
	 */
	public static final String BORROW_PROJECTNAME_OUTOFRANGE = "borrow.projectName.outOfRange";
	/**
	 * 借款详情不能超过？个字符
	 */
	public static final String BORROW_CONTENT_OUTOFRANGE = "borrow.content.outOfRange";
	/**
	 * 产品详情不能超过？个字符
	 */
	public static final String PRODUCT_CONTENT_OUTOFRANGE = "product.content.outOfRange";
	/**
	 * 产品名称不能为空
	 */
	public static final String PRODUCT_PROJECTNAME_NOTEMPTY = "product.projectName.notEmpty";
	/**
	 * 产品名称不能超过？个字符
	 */
	public static final String PRODUCT_PROJECTNAME_OUTOFRANGE = "product.projectName.outOfRange";
	/**
	 * 请设置锁定期
	 */
	public static final String PRODUCT_MUST_SET_LOCKTIME = "product:must:set:locktime";
	/**
	 * 递增金额必须为整数
	 */
	public static final String PROJECT_STEPACCOUNT_MUSTINT = "project.stepAccount.mustInt";
	/**
	 * 递增金额需小于等于最低起投金额
	 */
	public static final String PROJECT_STEPACCOUNT_GT_LOWEST = "project.stepAccount.gtLowestAccount";
	/**
	 * 最低起投金额和递增金额的总和不能超过借款金额
	 */
	public static final String BORROW_STEPACCOUNT_MUSTLT_ACCOUNT = "borrow.stepAccount.mustlt.account";
	/**
	 * 最低起投金额和递增金额的总和不能超过产品金额
	 */
	public static final String PRODUCT_STEPACCOUNT_MUSTLT_ACCOUNT = "product.stepAccount.mustlt.account";
	/**
	 * 最低起投金额和递增金额的总和不能超过最高投资总额
	 */
	public static final String PROJECT_STEPACCOUNT_MUSTLT_MOSTACCOUNT = "project.stepAccount.mustlt.mostAccount";
	
	/**
	 * 金额必须为整数
	 */
	public static final String PROJECT_ACCOUNT_MUSTINT = "project.account.mustInt";

	/**
	 * 定向密码格式错误（6位数字）
	 */
	public static final String PROJECT_PASS_FORMAT_ERROR= "project.password.formatError";
	
	/**
	 * 定向销售邮箱域名不能为空
	 */
	public static final String PROJECT_SALE_MAIL_NOTEMPTY= "project.saleMail.notEmpty";
	
	/**
	 * 定向销售邮箱域名格式错误（如：*.com,*.com)
	 */
	public static final String PROJECT_SALE_MAIL_FORMAT_ERROR= "project.saleMail.formatError";
	/**
	 * 定向销售邮箱域名限制的提示: 仅定向用户可投资
	 */
	public static final String PROJECT_SALE_MAIL_MSG= "project.saleMail.msg";
	/**
	 * 定向销售VIP不能为空
	 */
	public static final String PROJECT_SALE_VIP_LEVEL_NOTEMPTY= "project.saleVipLevel.notEmpty";
	/**
	 * 定向销售VIP格式错误（数字）
	 */
	public static final String PROJECT_SALE_VIP_LEVEL_FORMAT_ERROR= "project.saleVipLevel.formatError";
	/**
	 * 定向销售VIP提示 ，如： 仅VIP1以上会员可投资
	 */
	public static final String PROJECT_SALE_VIP_LEVEL_MSG= "project.saleVipLevel.msg";
	/**
	 * T+N计息时起始计息天数不能为空
	 */
	public static final String PROJECT_INTERESTSTYLE_TNDAYS_NOTEMPTY= "project.interestStyle.tndays.notEmpty";
	/**
	 * 起始计息天数不能小于0且不大于7
	 */
	public static final String PROJECT_INTERESTSTARTDAYS_RANGE_ERROR= "project.interestStartDays.rangeError";
	/**
	 * 起始计息天数不能超过产品期限
	 */
	public static final String PROJECT_INTERESTSTARTDAYS_MUSTLT_TIMELIMIT= "project.interestStartDays.mustLt.timeLimit";
	
	/**
	 * 类别不存在
	 */
	public static final String PROJECT_PROJECTTYPE_NOEXISTS= "project.projectType.noExists";
	/**
	 * 一级类别的排序不能重复
	 */
	public static final String PROJECT_TYPE_SORT_CANNOT_REPEAT = "project.type.sort.cannot.repeat";
	
	/**
	 * 分类标识已存在
	 */
	public static final String PROJECT_TYPE_NID_HAS_EXISTED = "project.type.nid.has.existed";
	
	/**
	 * 没有对应的产品记录
	 */
	public static final String PROJECT_NORECORD= "project.noRecord";
	
	/**
	 * 借款资料不能为空
	 */
	public static final String BORROW_BORROWDATA_NOT_EMPTY = "borrow.borrowData.notEmpty";

	/**
	 * 身份证号已存在
	 */
	public static final String IDCARD_IS_EXIST = "idcard.is.exist";
	
	/**
	 * 身份证号不能为空
	 */
	public static final String IDCARD_IS_EMPTY = "idcard.is.empty";
	
	/**
	 * 身份证号错误，请重新输入
	 */
	public static final String IDCARD_IS_ERROR = "idcard.is.error";
	
	/**
	 * 该组织机构代码已被使用
	 */
	public static final String ORG_CODE_IS_USED = "org.code.is.used";
	
	/**
	 * 该社会信用代码已被使用
	 */
	public static final String CREDIT_CODE_IS_USED = "credit.code.is.used";
	
	/**
	 * 该营业执照注册号已被使用
	 */
	public static final String BUSSINESS_CODE_IS_USED = "bussiness.code.is.used";
	
	/**
	 * 第三方商户号重复
	 */
	public static final String TPP_USER_CUST_ID_REPEAT = "tpp.userCustId.is.repeat";
	
	/**
	 * 创建账户签章失败
	 */
	public static final String CREATE_ACCOUNT_SIGN_FAIL = "create.account.sign.fail";
	
	/************************************自动投资************************************/
	/**
	 * 您需开通第三方资金托管账户，才能开启自动投资
	 */
	public static final String AUTO_INVEST_RULE_LOG_NEED_REAL_NAME = "autoInvestRuleLog.need.realName";
	/**
	 * 去开通
	 */
	public static final String AUTO_INVEST_RULE_LOG_OPEN_REAL_NAME = "autoInvestRuleLog.open.realName";
	/**
	 * 您需开通业务授权，才能开启自动投资
	 */
	public static final String AUTO_INVEST_RULE_LOG_NEED_AUTH_SIGN = "autoInvestRuleLog.need.authSign";
	/**
	 * 去授权
	 */
	public static final String AUTO_INVEST_RULE_LOG_OPEN_AUTH_SIGN = "autoInvestRuleLog.open.authSign";
	/**
	 * 您需完成风险承受能力评估，才能开启自动投资
	 */
	public static final String AUTO_INVEST_RULE_LOG_NEED_RISK = "autoInvestRuleLog.need.risk";
	/**
	 * 立即评估
	 */
	public static final String AUTO_INVEST_RULE_LOG_GO_RISK = "autoInvestRuleLog.go.risk";
	/**
	 * 立即充值
	 */
	public static final String AUTO_INVEST_RULE_LOG_GO_RECHARGE = "autoInvestRuleLog.go.recharge";
	/**
	 * 账户可用余额达{0}元才能开启自动投资，请充值
	 */
	public static final String AUTO_INVEST_RULE_LOG_USE_MONEY_ERROR = "autoInvestRuleLog.useMoney.error";
	/**
	 * 单日最高投资金额不能为空
	 */
	public static final String AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_NOT_EMPTY = "autoInvestRuleLog.amountDayMax.notEmpty";
	/**
	 * 单日最高投资金额只能为正整数
	 */
	public static final String AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_FORMAT_ERROR = "autoInvestRuleLog.amountDayMax.formatError";
	/**
	 * 单日最高投资金额不能超过账户可用余额{0}元
	 */
	public static final String AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_RANGE_ERROR = "autoInvestRuleLog.amountDayMax.rangeError";
	/**
	 * 账户单日最高投资金额不能小于最低自动投资限制额度{0}元
	 */
	public static final String AUTO_INVEST_RULE_LOG_INVEST_MIN_RANGE_ERROR = "autoInvestRuleLog.investMin.rangeError";
	/**
	 * 请至少填写一个投资期限区间
	 */
	public static final String AUTO_INVEST_RULE_LOG_TIME_TYPE_NOT_EMPTY = "autoInvestRuleLog.timeType.notEmpty";
	/**
	 * 月范围最大值不能小于最小值
	 */
	public static final String AUTO_INVEST_RULE_LOG_MONTH_LIMIT_RANGE_ERROR = "autoInvestRuleLog.monthLimit.rangeError";
	/**
	 * 月范围值错误
	 */
	public static final String AUTO_INVEST_RULE_LOG_MONTH_RANGE_ERROR = "autoInvestRuleLog.month.rangeError";
	/**
	 * 天范围最大值不能小于最小值
	 */
	public static final String AUTO_INVEST_RULE_LOG_DAY_LIMIT_RANGE_ERROR = "autoInvestRuleLog.dayLimit.rangeError";
	/**
	 * 天范围值错误
	 */
	public static final String AUTO_INVEST_RULE_LOG_DAY_RANGE_ERROR = "autoInvestRuleLog.day.rangeError";
	/**
	 * 请填写投资收益最低值
	 */
	public static final String AUTO_INVEST_RULE_LOG_APR_MIN_NOT_EMPTY = "autoInvestRuleLog.aprMin.notEmpty";
	/**
	 * 自动投资配置成功，将在5分钟内生效
	 */
	public static final String AUTO_INVEST_RULE_LOG_SUCCESS = "autoInvestRuleLog.success";
	/**
	 * 请至少选择一种收益方式
	 */
	public static final String AUTO_INVEST_RULE_LOG_REPAY_STYLES_NOT_EMPTY = "autoInvestRuleLog.repayStyles.notEmpty";
	
	/**
	 * 投资收益范围值错误
	 */
	public static final String AUTO_INVEST_RULE_LOG_APR_MIN_RANGE_ERROR = "autoInvestRuleLog.aprMin.rangeError";
	
	/**
	 * 投资收益最多保留两位小数
	 */
	public static final String AUTO_INVEST_RULE_LOG_APR_MIN_ERROR = "autoInvestRuleLog.aprMin.error";
	
	/**
	 * 您的可用小于此产品最小限额
	 */
	public static final String AUTO_INVEST_USEMONEY_LTLOWESTACCOUNT="autoInvest.useMoney.ltLowestAccount";
	
	/**
	 * 您的可用不足
	 */
	public static final String AUTO_INVEST_USEMONEY_NOTENOUGH="autoInvest.useMoney.notEnough";
	
	/**
	 * 自动投资成功
	 */
	public static final String AUTO_INVEST_SUCCESS="autoInvest.success";
	/**
	 * 您已达到单日最高自动投资金额
	 */
	public static final String AUTO_INVEST_INVESTMONEY_OVERLIMIT="autoInvest.investMoney.overLimit";
	/**
	 * 自动投资所剩额度已不足两倍最底投资额度
	 */
	public static final String AUTO_INVEST_REMAINACCOUNT_LTDOUBLELOWESTACCOUNT="autoInvest.remainAccount.ltDoubleLowestAccount";
	/**
	 * 投资金额范围须在{0}到{1}之间
	 */
	public static final String INVEST_ACCOUNT_RANGEERROR="invest.account.rangeError";
	
	/************************************自动投资************************************/
	
	/************************************前台借款************************************/
	
	/**
	 * 担保用户不能申请借款
	 */
	public static final String USER_NATURE_ERROR ="user.nature.error";
	/**
	 * 姓名不能超过10个汉字
	 */
	public static final String BORROW_BESPEAK_CONTACT_NAME_LENGTH_ERROR = "borrowBespeak.contactName.lengthError";
	/**
	 * 请填写您的姓名
	 */
	public static final String BORROW_BESPEAK_CONTACT_NAME_EMPTYMSG = "borrowBespeak.contactName.emptyMsg";
	/**
	 * 性别不能为空
	 */
	public static final String BORROW_BESPEAK_SEX_NOTEMPTY = "borrowBespeak.sex.notEmpty";
	/**
	 * 性别类型错误
	 */
	public static final String BORROW_BESPEAK_SEX_ERROR = "borrowBespeak.sex.error";
	/**
	 * 手机号码不能为空
	 */
	public static final String USER_MOBILE_NOTEMPTY = "user.mobile.notEmpty";
	/**
	 * 	请填写借款期限
	 */
	public static final String BORROW_BESPEAK_LIMIT_TIME_EMPTYMSG = "borrowBespeak.limitTime.emptyMsg";
	/**
	 * 	借款期限错误
	 */
	public static final String BORROW_BESPEAK_LIMIT_TIME_ERROR = "borrowBespeak.limitTime.error";
	/**
	 * 请填写借款金额
	 */
	public static final String BORROW_BESPEAK_MONEY_NOT_EMPTY = "borrowBespeak.money.notEmpty";
	/**
	 * 请输入正整数的借款金额
	 */
	public static final String BORROW_BESPEAK_MONEY_FORMAT_ERROR = "borrowBespeak.money.formatError";
	/**
	 * 请输入大于等于{0}元，小于等于{1}元的借款金额
	 */
	public static final String BORROW_BESPEAK_MONEY_RANGE_ERROR = "borrowBespeak.money.rangeError";
	/**
	 * 请选择所在城市
	 */
	public static final String BORROW_BESPEAK_ZONE_NOT_EMPTY = "borrowBespeak.zone.notEmpty";
	/**
	 * 所在城市不存在
	 */
	public static final String BORROW_BESPEAK_ZONE_ERROR = "borrowBespeak.zone.error";
	/**
	 * 您的借款申请已经提交成功，我们的工作人员会在24小时内审核您的需求，并尽快与您取得联系！
	 */
	public static final String BORROW_BESPEAK_SUCCESS = "borrowBespeak.success";
	/**
	 * 您的借款申请已保存，请确认信息
	 */
	public static final String BORROW_WEB_CONFIRM = "borrow.web.confirm";
	/**
	 * 您的借款申请已提交，请耐心等待审核！
	 */
	public static final String BORROW_WEB_SUCCESS = "borrow.web.success";
	/**
	 * 请选择预约借款
	 */
	public static final String BORROW_BESPEAK_NOT_CHANGE = "borrowBespeak.notChange";
	/**
	 * 备注信息过长
	 */
	public static final String BORROW_BESPEAK_REMARK_LENGTH_ERROR = "borrowBespeak.remark.lengthError";
	/**
	 * 预约跟进已处理
	 */
	public static final String BORROW_BESPEAK_STATUS_ERROR = "borrowBespeak.status.error";
	/************************************前台借款************************************/
	
	/************************************校验 start************************************/
	/**
	 * 默认密码不能为空
	 */
	public static final String USER_PWD_ISEMPTY = "user.pwd.isEmpty";
	/**
	 * 默认密码格式错误
	 */
	public static final String USER_PWD_MANAGE_ERROR = "user.pwd.manage.error";
	/**
	 * 该手机已被注册，请更换手机
	 */
	public static final String USER_MOBILE_REPEAT = "user.mobile.repeat";
	/**
	 * 手机号码不能为空
	 */
	public static final String USER_MOBILE_ISEMPTY = "user.mobile.isEmpty";
	/**
	 * 手机号格式错误
	 */
	public static final String USER_MOBILE_ERROR = "user.mobile.error";
	/**
	 * 新手机号码不能与原有手机号码相同
	 */
	public static final String USER_MOBILE_NEED_NEW = "user.mobile.need.new";
	/**
	 * 更新手机绑定状态失败
	 */
	public static final String UPDATE_MOBILE_STATUS_FAIL = "update.mobile.status.fail";
	/**
	 * 更新邮箱绑定状态失败
	 */
	public static final String UPDATE_EMAIL_STATUS_FAIL = "update.email.status.fail";
	/**
	 * 第三方商户号过长
	 */
	public static final String USER_TPP_USER_CUST_LENGHT_ERROR = "user.tppUserCust.lenghtError";
	/**
	 * 企业地址长度错误
	 */
	public static final String USER_ADDRESS_LENGHT_ERROR = "user.address.lenghtError";
	/**
	 * 企业固定电话长度错误
	 */
	public static final String USER_TELEPHONE_LENGHT_ERROR = "user.telephone.lenghtError";
	/**
	 * 企业固定电话格式错误
	 */
	public static final String USER_TELEPHONE_FORMAT_ERROR = "user.telephone.formatError";
	/**
	 * 企业名称不能为空
	 */
	public static final String USER_COMPANY_NAME_ISEMPTY = "user.companyName.isEmpty";
	/**
	 * 企业名称长度要大于{0}小于等于{1}
	 */
	public static final String USER_COMPANY_NAME_LENGHT_ERROR = "user.companyName.lenghtError";
	/**
	 * 联系人姓名长度要大于{0}小于等于{1}
	 */
	public static final String USER_CONTACTS_LENGHT_ERROR = "user.contacts.lenghtError";
	/**
	 * 联系邮箱格式错误
	 */
	public static final String USER_EMAIL_ERROR = "user.email.error";
	/**
	 * 织机构代码长度错误
	 */
	public static final String USER_ORG_CODE_LENGHT_ERROR = "user.orgCode.lenghtError";
	/**
	 * 营业执照注册号格式错误
	 */
	public static final String USER_BUSSINESS_CODE_LENGHT_ERROR = "user.bussinessCode.lenghtError";
	/**
	 * 税务登记证号格式错误
	 */
	public static final String USER_TAXREGNO_LENGHT_ERROR = "user.taxRegNo.lenghtError";
	/**
	 * 用户手机未认证
	 */
	public static final String USER_MOBILE_NOT_AUTH ="user.mobile.notAuth";
	/**
	 * 密码找回方式有误
	 */
	public static final String PASS_RETRIEVE_TYPE_ERROR = "password.retrieve.type.error";
	/**
	 * 新老密码相同!
	 */
	public static final String RETRIEVE_PASS_NOT_CHANGE = "retrieve.password.not.change";
	/**
	 * 为了您的资金安全，请您开通第三方资金托管账户！
	 */
	public static final String USER_TPP_STATUS_NOT_OPEN ="user.tppStatus.notOpen";
	
	/**
	 * 为了您的资金安全，请您开通第三方资金托管账户！
	 */
	public static final String USER_REAL_NAME_STATUS ="user.realName.status";
	
	/**
	 * 开通托管账户
	 */
	public static final String USER_REAL_NAME_GO_OPEN ="user.realName.go.open";
	
	/**
	 * 您需完成业务授权，才能申请借款
	 */
	public static final String USER_AUTH_SIGN_STATUS_ERROR ="user.authSign.status.error";
	
	/**
	 * 马上开通
	 */
	public static final String USER_REAL_NAME_OPEN ="user.realName.open";
	
	/**
	 * 为方便托管方在本平台进行相关资金操作，请您进行业务授权！
	 */
	public static final String USER_AUTH_SIGN_STATUS ="user.authSign.status";
	
	/**
	 * 马上授权
	 */
	public static final String USER_AUTH_SIGN_OPEN ="user.authSign.open";
	
	/**
	 * 用户未进行风险评估
	 */
	public static final String USER_RISK_LEVEL_IS_EMPTY ="user.riskLevel.isEmpty";
	
	/**
	 * 公司名称不合规则： 长度为2—30个字符 ，由中文、（）组成
	 */
	public static final String USER_COMPANYNAME_IRREGULAR="user.companyName.irregular";
	/**
	 * 组织机构代码不合规则: 9位数字（或大写拉丁字母）
	 */
	public static final String USER_ORGCODE_IRREGULAR="user.orgCode.irregular";
	
	/***********************************变现 start************************************/
	/**
	 * 变现原投资未找到
	 */
	public static final String REALIZE_INVEST_NOT_FOUND = "realize.invest.not.found";
	/**
	 * 请先阅读协议并勾选
	 */
	public static final String REALIZE_PROTOCOL_NOT_CHECK = "realize.protocol.not.check";
	/**
	 * 必须全额变现
	 */
	public static final String REALIZE_MUST_FULLREALIZE = "realize.must.fullrealize";
	/**
	 * 变现金额不能小于最小变现金额{0}元
	 */
	public static final String REALIZE_NOT_LESS_SELLAMOUNTMIN = "realize.not.less.sellAmountMin";
	/**
	 * 变现金额小于最小变现金额{0}元时,必须全额变现
	 */
	public static final String REALIZE_LESS_MUST_FULLREALIZE = "realize.less.must.fullrealize";
	/**
	 * 变现金额不得大于最大可变现额
	 */
	public static final String REALIZE_NOT_ABOVE_MOSTMONEY = "realize.not.above.mostmoney";
	/**
	 * 变现原项目未找到
	 */
	public static final String REALIZE_ORGPROJECT_NOT_FOUNT = "realize.orgproject.not.fount";
	/**
	 * 变现项目未找到
	 */
	public static final String REALIZE_PROJECT_NOT_FOUNT = "realize.project.not.fount";
	/**
	 * 该项目不是变现项目
	 */
	public static final String REALIZE_PROJECT_NOT_REALIZE = "realize.project.not.realize";
	/**
	 * 变现项目正在放款中
	 */
	public static final String REALIZE_IS_LOANING = "realize.is.loaning";
	/**
	 * 变现项目已经还款
	 */
	public static final String REALIZE_IS_REPAYED = "realize.is.repayed";
	/**
	 * 变现项目撤销处理中
	 */
	public static final String REALIZE_IS_CANCELLING = "realize.is.cancelling";
	/**
	 * 变现项目不在成立待审状态
	 */
	public static final String REALIZE_IS_NOT_RAISEEND = "realize.is.not.raiseend";
	/**
	 * 变现项目不在还款中状态
	 */
	public static final String REALIZE_IS_NOT_REPAYING = "realize.is.not.repaying";
	/**
	 * 变现项目不在募集状态
	 */
	public static final String REALIZE_IS_NOT_RAISING = "realize.is.not.raising";
	/**
	 * 变现项目正在发布中,请稍后再试
	 */
	public static final String REALIZE_IS_PUBLISHING = "realize.is.publishing";
	/**
	 * 还款方式不正确
	 */
	public static final String REALIZE_REPAYSTYLE_ISNOT_RIGHT = "realize.repaystyle.isnot.right";
	/**
	 * 投资记录冻结流水号为空
	 */
	public static final String INVEST_FREEZENO_ISEMPTY = "invest.freezeno.isempty";
	/**
	 * 自己不能投自己发布的借款
	 */
	public static final String CANOT_INVEST_SELF_REALIZE = "canot.invest.self.realize";
	/**
	 * 变现已满额
	 */
	public static final String REALIZE_IS_FULL = "realize.is.full";
	/**
	 * 变现还款信息异常
	 */
	public static final String REALIZE_REPAY_ISNOT_RIGHT = "realize.repay.isnot.right";
	/**
	 * 原产品未还款
	 */
	public static final String REALIZE_ORGPROJECT_NOT_REPAYED = "realize.orgproject.not.repayed";
	/**
	 * 原产品正在还款中,请稍后再试
	 */
	public static final String REALIZE_ORGPROJECT_IS_REPAYING = "realize.orgproject.is.repaying";
	/**
	 * 变现借款人不正确
	 */
	public static final String REALIZE_BORROWER_NOT_RIGHT = "realize.borrower.not.right";
	/**
	 * 变现还款人不正确
	 */
	public static final String REALIZE_REPAYER_NOT_RIGHT = "realize.repayer.not.right";
	/**
	 * 当前还款方式不支持变现！ 
	 */
	public static final String REALIZE_NOWREPAYMENTSTYLE_NOT = "realize.nowRepaymentStyle.not";
	/**
	 * 期限未设置的产品不支持变现！
	 */
	public static final String REALIZE_NOTLIMITTIME_NOT = "realize.notLimitTime.not";
	/**
	 * 有固定还款日的产品不支持变现！
	 */
	public static final String REALIZE_FIXEDREPAYMENTTIME_NOT = "realize.fixedRepaymentTime.not";
	/**
	 * 募集结束时间必须大于发布时间
	 */
	public static final String RAISEENDTIME_MUST_GT_ISSUETIME = "raiseendtime.must.gt.issuetime";
	/**
	 * 距离到期日必须大于0
	 */
	public static final String REMAINDAYS_MUST_GT_ZERO = "remaindays.must.gt.zero";
	/**
	 * 部分变现最低变现金额不能为空
	 */
	public static final String SELL_AMOUNTMIN_IS_EMPTY = "sell.amountmin.is.empty";
	/**
	 * 部分投资最小投资金额不能为空
	 */
	public static final String BUY_AMOUNTMIN_IS_EMPTY = "buy.amountmin.is.empty";
	/**
	 * 部分投资最小投资金额必须大于等于1
	 */
	public static final String BUY_AMOUNTMIN_MUST_GT_ONE = "buy.amountmin.must.gt.one";
	/**
	 * 部分变现最大投资金额不能为空
	 */
	public static final String BUY_AMOUNTMAX_IS_EMPTY = "buy_amountmax_is_empty";
	/**
	 * 变现利率下限值不能高于变现利率上限值
	 */
	public static final String REALIZE_REALIZERATEMIN_MUSTLT_REALIZERATEMAX = "realize.realizeRateMin.mustlt.realizeRateMax";
	/**
	 * 变现利率范围错误
	 */
	public static final String REALIZE_APR_RANGE_ERROR = "realize.apr.rangeError";
	/**
	 * 变现利率最多保留两位小数
	 */
	public static final String REALIZE_APR_REMAINTWODECIMAL = "realize.apr.remainTwoDecimal";
	/**
	 * 存在逾期借贷，请还款后再进行变现操作
	 */
	public static final String HAS_OVERDUE_PROJECT = "has.overdue.project";
	/**
	 * 变现借款本金必须大于1元
	 */
	public static final String REALIZE_CAPITAL_MUST_GT_ONE= "realize.capital.must.gt.one";
	
	
	/***********************************变现 end *************************************/
	
	/***********************************登录 start************************************/
	/**
	 * 登录成功
	 */
	public static final String LOGIN_SUCCESS = "login.success";
	/**
	 * 账号已被锁定，请您联系客服
	 */
	public static final String LOGIN_USER_IS_LOCK = "login.fail.for.user.is.lock";
	/**
	 * 用户名不存在
	 */
	public static final String LOGIN_USER_NAME_ERROR = "login.fail.for.userName.error";
	/**
	 * 登录失败，图形验证码错误
	 */
	public static final String LOGIN_CODE_IS_ERROR = "login.fail.for.code.is.error"; 
	/**
	 * 登录失败，用户名或密码错误
	 */
	public static final String LOGIN_NAME_OR_PASS_IS_ERROR = "login.fail.for.name.or.password.is.error"; 
	/**
	 * 用户名或密码错误，您还可输入2次
	 */
	public static final String LOGIN_FAIL_WITH_TWO_CHANCE = "login.fail.with.two.chance"; 
	/**
	 * 还有最后一次重试机会
	 */
	public static final String LOGIN_FAIL_WITH_LAST_CHANCE = "login.fail.with.last.chance";
	
	/***********************************登录 end**************************************/
	
	/***********************************用户信息 start**********************************/
	/**
	 * 用户名由6-20位字符组成，由英文字母、英文字母加数字组成
	 */
	public static final String USER_NAME_ERROR = "user.userName.error";
	/**
	 * 用户名重复，请更换用户名
	 */
	public static final String USER_NAME_REPEAT = "user.userName.repeat";
	
	/**
	 * 当前用户未登录
	 */
	public static final String USER_NOT_LOGIN = "user.not.login";

	/**
	 * 用户不存在
	 */
	public static final String USER_NOT_EXIST = "user.not.exist";

	/**
	 * 企业开户，私有域不能为空
	 */
	public static final String COMPANY_REGISTER_MERPRIV_NOT_EXIST = "company.register.merPriv.not.exist";
	
	/**
	 * 返回个人中心
	 */
	public static final String COME_BACK_TO_USER_CENTER = "come.back.to.user.center";
	
	/**
	 * 开户成功
	 */
	public static final String USER_TPP_REGISTER_SUCCESS = "user.tpp.register.success";
	
	/**
	 * 开户失败
	 */
	public static final String USER_TPP_REGISTER_FAIL = "user.tpp.register.fail";
	
	/**
	 * 回调验签失败
	 */
	public static final String TPP_NOTIFY_SIGN_FAIL = "tpp.notify.sign.fail";
	/**
	 *  撤销投资
	 */
	public static final String TPP_INVESTFAIL_FAIL = "tpp.investFail.failError";
	
	/**
	 * 订单超时
	 */
	public static final String ORDER_IS_TIMEOUT = "order.is.timeout";
	
	/**
	 * 订单不存在
	 */
	public static final String ORDER_NOT_EXIST = "order.not.exist";
	
	/**
	 * 系统异常，业务处理失败
	 */
	public static final String ORDER_HANDLE_SYSTEM_EXCEPTION = "order.handle.system.exception";
	
	/***********************************充值 start**********************************/
	/**
	 * 充值成功
	 */
	public static final String RECHARGE_SUCCESS = "recharge.success";
	
	/**
	 * 充值失败
	 */
	public static final String RECHARGE_FAIL = "recharge.fail";
	
	/**
	 * 充值金额不能为空，且必须大于零
	 */
	public static final String RECHARGE_MONEY_POSITIVE = "recharge.money.positive";
	
	/**
	 * 充值金额格式错误，最多输入两位小数！
	 */
	public static final String RECHARGE_MONEY_ERROR = "recharge.money.error";
	
	/**
	 * 充值金额不能小于{0}元！
	 */
	public static final String RECHARGE_MONEY_MIN = "recharge.money.min";
  
	/**
	 * 充值金额不能大于{0}元！
	 */
	public static final String RECHARGE_MONEY_MAX = "recharge.money.max";
	
	/**
	 * 更改充值记录失败！
	 */
	public static final String RECHARGE_UPDATE_FAIL = "recharge.update.fail";
	
	/***********************************充值 end**********************************/
	/***********************************冻结 start********************************/
	/**
	 * 用户充值功能已被锁定，请联系客服人员。
	 */
	public static final String RECHARGE_IS_FREEZE = "recharge.is.freeze";
	/**
	 * 用户提现功能已被锁定，请联系客服人员。
	 */
	public static final String CASH_IS_FREEZE = "cash.is.freeze";
	/**
	 * 您的账户借款功能已被冻结，不能申请借款，请联系平台客服
	 */
	public static final String PRODUCT_BORROWER_LOAN_FREEZE = "project.borrower.loan.freeze";
	/**
	 * 用户投资功能已冻结
	 */
	public static final String USER_FREEZE_INVEST_FREEZED ="user.freeze.investFreezed";
	/**
	 * 变现功能已被锁定，请联系客服人员。
	 */
	public static final String REALIZE_IS_FREEZE ="realize.is.freeze";
	/**
	 * 债权转让功能已被锁定，请联系客服人员。
	 */
	public static final String BOND_IS_FREEZE ="bond.is.freeze";
	
	/**
	 * 担保功能已被锁定，请联系客服人员。
	 */
	public static final String VOUCH_IS_FREEZE ="vouch.is.freeze";
	
	/***********************************冻结 end************************************/
	/***********************************提现 start**********************************/
	
	/**
	 * 提现失败
	 */
	public static final String CASH_FAIL = "cash.fail";
	
	/**
	 * 提现成功
	 */
	public static final String CASH_SUCCESS = "cash.success";
	
	/**
	 * 提现审核失败
	 */
	public static final String CASH_AUDIT_FAIL = "cash.audit.fail";
	
	/**
	 * 该提现记录已经被审核处理过，请刷新页面重试
	 */
	public static final String CASH_HAS_AUDITED = "cash.has.audited";
	
	/**
	 * 请先选择提现银行卡
	 */
	public static final String CASH_NEED_BANK_CARD = "cash.need.bank.card";
	
	/**
	 * 提现金额不能为空，且必须大于零
	 */
	public static final String CASH_MONEY_POSITIVE = "cash.money.positive";
	
	/**
	 * 提现金额格式错误，最多输入两位小数！
	 */
	public static final String CASH_MONEY_ERROR = "cash.money.error";
	
	/**
	 * 单笔提现金额不能小于{0}元！
	 */
	public static final String CASH_SINGLE_MIN_MONEY = "cash.single.min.money";
	
	/**
	 * 提现金额不能大于您的可用余额！
	 */
	public static final String CASH_MAX_USE_MONEY = "cash.max.use.money";
	
	/**
	 * 提现失败，每天最多只能提现{0}次！
	 */
	public static final String CASH_DAY_TIME_LIMIT = "cash.day.time.limit";
	
	/**
	 * 提现失败，您今天提现已达限额{0}元！
	 */
	public static final String CASH_DAY_MAX_MONEY = "cash.day.max.money";
	
	/**
	 * 提现失败，您今天还可提现{0}元！
	 */
	public static final String CASH_DAY_REMAIN_MONEY = "cash.day.remain.money";
	
	/**
	 * 更新提现记录失败！
	 */
	public static final String CASH_UPDATE_FAIL = "cash.update.fail";
	
	/**
	 * 抱歉，您的提现已超额！
	 */
	public static final String CASH_MONEY_OVER_TPP_MOENY = "cash.over.tpp.money";
	
	/***********************************提现 end**********************************/
	
	/**
	 * 图片路径不能为空
	 */
	public static final String PIC_PATH_EMPTY = "picPath.is.empty";
	
	/**
	 * 真实姓名：请输入2-10个字符的真实姓名，仅为中文
	 */
	public static final String USER_REALNAME_ERROR = "user.realName.error";
	
	/**
	 * 身份证：请输入15或18位的身份证号，仅限数字和大写X
	 */
	public static final String USER_IDNO_ERROR = "user.idNo.error";
	
	/**
	 * 企业名称：请输入2-30位的公司名称，由中文、（）组成
	 */
	public static final String COMPANY_NAME_ERROR = "company.name.error";
	
	/**
	 * 注册地区不能为空
	 */
	public static final String COMPANY_ZONE_EMPTY = "company.zone.empty";
	
	/**
	 * 办公地点不能为空
	 */
	public static final String COMPANY_OFFICE_ZONE_EMPTY = "company.office.zone.empty";
	
	/**
	 * 营业执照号：请输入正确的营业执照注册号
	 */
	public static final String BUSSINESS_CODE_ERROR = "bussiness.code.error";
	
	/**
	 * 组织机构代码：请输入9位组织机构代码
	 */
	public static final String ORG_CODE_ERROR = "org.code.error";
	
	/**
	 * 请输入长度为18位的社会统一信用代码
	 */
	public static final String CREDIT_CODE_ERROR = "credit.code.error";
	
	/**
	 * 您的企业开户申请正在审核中，请不要重复提交开户请求
	 */
	public static final String COMPANY_TPP_REG_AUDITING = "company.tpp.reg.auditing";
	
	/**
	 * 您的企业开户申请已通过
	 */
	public static final String COMPANY_TPP_REG_SUCCESS = "company.tpp.reg.success";
	
	/**
	 * 您的提现操作过于频繁，请稍后再试
	 */
	public static final String CASH_IS_FREQUENCY = "cash.is.frequency";
	
	/**
	 * 账户余额不足，可用余额加预冻结资金小于提现金额
	 */
	public static final String CASH_ACCOUNT_ERROR = "cash.account.error";
	
	/**
	 * 该提现记录正在审核处理中，请不要重复操作
	 */
	public static final String CASH_IS_DOING = "cash.audit.is.doing";
	
	/**
	 * 资金托管方处理失败
	 */
	public static final String ACCOUNT_TPP_FAIL = "account.tpp.fail";
	
	/**
	 * 取现复核发生错误异常，审核不通过但第三方处理失败。
	 */
	public static final String CASH_AUDIT_TPP_ERROR = "cash.audit.tpp.error";
	
	/**
	 * 证明资料类型不能为空
	 */
	public static final String QUALIFICATION_TYPE_EMPTY= "qualification.can.not.be.empty";
	
	/**
	 * 请至少上传一份认证资料
	 */
	public static final String QUALIFICATION_LEAST_ONE = "qualification.picPaths.at.least.for.one";
	
	/**
	 * 请上传资质材料
	 */
	public static final String QUALIFICATION_PIC_PATHS_EMPTY = "qualification.picPaths.empty";
	
	/**
	 * 认证图片最多上传[{0}]张
	 */
	public static final String QUALIFICATION_REACH_MAX = "qualification.picPaths.reach.max";
	
	/**
	 * 证明资料正在待审核，无法上传新的证明资料
	 */
	public static final String QUALIFICATION_IS_AUDITING = "qualification.is.auditing";
	
	/**
	 * 请先选择一条记录
	 */
	public static final String NEED_SELECT_ONE_RECORD = "need.to.select.one.record";
	/***********************************VIP start*********************************/
	/**
	 * 所需成长值需大于上级成长封顶值:{0}
	 */
	public static final String GROWTHVALUE_MUST_GT_UPLEVEL = "growthvalue.must.gt.uplevel";
	/**
	 * 当前有VIP等级大于0的用户 无法修改或删除VIP等级
	 */
	public static final String USER_VIP_COUNT_GT_ZERO = "user.vip.count.gt.zero";
	/**
	 * 所需成长封顶值需小于下级成长值:{0}
	 */
	public static final String GROWTHVALUE_MUST_LT_DOWNLEVEL = "growthvalue.must.lt.downlevel";
	/**
	 * 成长封顶值需大于所需成长值
	 */
	public static final String GROWTHLIMITVALUE_MUST_GT_GROWTHVALUE = "growthlimitvalue.must.gt.growthvalue";
	/***********************************VIP end***********************************/
	
	/***********************************运营 begin xxb 20160927**********************************/
	/**
	 * 固定期红包发放最大有效期提示
	 */
	public static final String REDENVELOPE_MAX_PERIOD_ERROR = "redenvelope.max.period.error";
	/**
	 * 固定期红包发放最大有效期提示
	 */
	public static final String RATECOUPON_MAX_PERIOD_ERROR = "rateCoupon.max.period.error";
	/**
	 * 手动定向发放活动已被禁用
	 */
	public static final String ACTIVITY_GRANT_BAN = "activity_grant_ban";
	
	/**
	 * 下发红包最小金额	
	 */
	public static final String REDENVELOPE_GRANTMIN_NOTEMPTY = "redenvolopeRule.grantmin.notEmpty";
	/**
	 * 下发红包最大金额	
	 */
	public static final String REDENVELOPE_GRANTMAX_NOTEMPTY = "redenvolopeRule.grantmax.notEmpty";
	/**
	 * 下发红包最小金额不能超最大金额
	 */
	public static final String REDENVELOPE_GRANT_AMOUNT_ERROR = "redenvolopeRule.grant.amount.error";
	
	/**
	 * 下发红包发放开始时间为必填项
	 */
	public static final String REDENVELOPE_GRANT_BEGINTIME_NOTEMPTY = "redenvolopeRule.grant.begintime.notempty";
	
	/**
	 * 下发红包发放结束时间为必填项
	 */
	public static final String REDENVELOPE_GRANT_ENDTIME_NOTEMPTY = "redenvolopeRule.grant.endtime.notempty";
	
	/**
	 * 下发红包结束时间不能小于开始时间
	 */
	public static final String REDENVELOPE_GRANT_TIME_ERROR = "redenvolopeRule.grant.time.error";
	
	/**
	 * 使用条件不能为空
	 */
	public static final String USE_PROJECT_TYPE_NOTEMPTY = "use.project.type.notempty";
	
	/**
	 * 有效期不能为空
	 */
	public static final String 	USE_VALIDDAY_NOTEMPTY = "use.validday.notempty";
	
   /**
    *  下发红包金额	
	 */
	public static final String REDENVELOPE_GRANTAMOUNT_NOTEMPTY = "redenvolopeRule.grantamount.notEmpty";
	/**
	* 下发红包发放比例为必填项
	 */
	public static final String REDENVELOPE_GRANTRATE_NOTEMPTY = "redenvolopeRule.grantrate.notEmpty";
	/**
	
	/**
	 * 投资最小金额	
	 */
	public static final String REDENVELOPE_TENDERMIN_NOTEMPTY = "redenvolopeRule.tendermin.notEmpty";
	/**
	 * 投资最大金额	
	 */
	public static final String REDENVELOPE_TENDERMAX_NOTEMPTY = "redenvolopeRule.tendermax.notEmpty";
	/**
	 * 投资最小金额不能超最大金额
	 */
	public static final String REDENVELOPE_TENDER_AMOUNT_ERROR = "redenvolopeRule.tender.amount.error";
	/**
	 * 发放数量需为{0}倍数
	 */
	public static final String REDENVELOPE_REDNUM_UNDIVISIBLE_TOTALNUM = "redenvelope.rednum.undivisible.totalnum";
	/**
	 * 规则数量不需大于0
	 */
	public static final String REDENVELOPE_NUM_MUST_GT_ZERO = "redenvelope.num.must.gt.zero";
	
	/**
	 * 红包作废状态错误
	 */
	public static final String REDENVELOPE_CANCEL_STATUS_ERROR = "redenvelope.cancel.status.error";
	
	/**
	 * 加息券作废状态错误
	 */
	public static final String RATECOUPON_CANCEL_STATUS_ERROR = "ratecoupon.cancel.status.error";
	
	/**
	 * 请至少选择一项礼包福利！
	 */
	public static final String USER_GIFT_AWARD_NOT_NULL = "user_gift_award_not_null";
	/**
	 * {0}类别已关联生效的红包规则,请重新选择！
	 */
	public static final String PORJECT_TYPE_IS_ASSOCIATED = "porject.type.is.associated";
	
	/***********************************运营 end**********************************/

	/***********************************密保 start**********************************/
	/**
	 * 密保问题不能重复
	 */
	public static final String SECURITYANSWER_IS_REPEAT = "securityanswer_is_repeat";
	/**
	 * 密保设置失败
	 */
	public static final String SECURITYANSWER_SAVE_ERROR = "securityanswer.save.error";
	/**
	 * 密保答案设置过长
	 */
	public static final String SECURITYANSWER_LENGTH_ERROR = "securityanswer.length.error";
	/***********************************密保 end**********************************/

	/***********************************后台用户 start**********************************/
	/**
	 * 后台用户不存在
	 */
	public static final String OPERATOR_EMPTY = "operator.empty";
	/**
	 * 原密码错误
	 */
	public static final String OPERATOR_OLD_PWD_ERROR = "operator.oldPwd.error";
	/**
	 * 您的初始密码输入错误，如遗忘请联系平台
	 */
	public static final String OPERATOR_FIRST_OLD_PWD_ERROR = "operator.first.oldPwd.error";
	/**
	 * 新密码与原密码不能相同
	 */
	public static final String OPERATOR_NEWPWD_AGAIN_ERROR = "operator.newPwd.again.error";
	/**
	 * 新密码与初始密码不能相同
	 */
	public static final String OPERATOR_FIRST_NEWPWD_AGAIN_ERROR = "operator.first.newPwd.again.error";
	/**
	 * 两次密码输入不一致
	 */
	public static final String OPERATOR_PWD_AGAIN_ERROR = "operator.pwd.again.error";
	/**
	 * 密码格式错误
	 */
	public static final String OPERATOR_PWD_ERROR = "operator.pwd.error";
	/***********************************后台用户 end**********************************/

	
	/**
	 * 记录已审核
	 */
	public static final String RECORD_IS_VERIFIED = "record.is.verified";
	
	/**
	 * 问题一的密保答案错误！
	 */
	public static final String ANSWER_FIRST_ERROR = "answer.first.is.error";
	
	/**
	 * 问题二的密保答案错误！
	 */
	public static final String ANSWER_TWO_ERROR = "answer.two.is.error";
	
	/**
	 * 问题三的密保答案错误！
	 */	
	public static final String ANSWER_THREE_ERROR = "answer.three.is.error";
	
	/**
	 * 答案不能为空
	 */
	public static final String ANSWER_IS_EMPTY = "answer.can.not.be.empty";
	
	/**
	 * 答案错误
	 */
	public static final String ANSWER_IS_ERROR = "answer.is.error";
	
	/**
	 * 用户未设置过该密保问题
	 */
	public static final String PWD_QUESTION_IS_NOT_EXIST = "pwd.question.is.not.exist";
	
	/**
	 * 请先选择认证方式
	 */
	public static final String IDENTIFY_METHOD_NEED_SELECTED = "identify.method.need.selected";
	
	/**
	 * 该用户非后台添加，不可修改
	 */
	public static final String USER_NOT_CREATE_BY_MANAGE = "user.not.create.by.manage";
	
	/**
	 * 银行卡号不能为空
	 */
	public static final String BANK_CARDID_EMPTY = "bank.cardId.can.not.be.empty";
	
	/**
	 * 银行标识不能为空
	 */
	public static final String BANK_CODE_EMPTY = "bank.code.can.not.be.empty";
	
	/**
	 * 请阅读并同意服务协议！
	 */
	public static final String PROTOCOL_SHOULD_BE_AGREE = "protocol.should.be.agree";
	
	/**
	 * 用户名不能为空！
	 */
	public static final String USER_NAME_EMPTY = "user.name.can.not.be.empty";
	
	/**
	 * 密码不能为空！
	 */
	public static final String USER_PWD_EMPTY = "user.password.can.not.be.empty";	
	
	/**
	 * 手机号码格式错误!
	 */
	public static final String MOBILE_STYLE_ERROR = "user.register.mobile.style.error";
	
	/**
	 * 邮箱格式不正确
	 */
	public static final String EMAIL_FORMAT_ERROR = "common.email.format.error";
	
	/**
	 * 邮箱已经被使用
	 */
	public static final String EMAIL_IS_USED = "common.email.is.used";
	
	/**
	 * 邮箱不能为空
	 */
	public static final String EAMIL_IS_EMPTY = "common.email.can.not.be.empty";
	
	/**
	 * 邮箱超出最大长度限制
	 */
	public static final String EMAIL_OVER_MAX_LENGTH = "common.email.over.max.length";
	
	/**
	 * 原密码不能为空
	 */
	public static final String OLD_PWD_IS_EMPTY = "old.pwd.can.not.be.empty";
	
	/**
	 * 新密码不能为空
	 */
	public static final String NEW_PWD_IS_EMPTY = "new.pwd.can.not.be.empty";
	
	/**
	 * 确认密码不能为空
	 */
	public static final String CONFIRM_PWD_IS_EMPTY = "confirm.pwd.can.not.be.empty";
	
	/**
	 * 新密码格式错误
	 */
	public static final String NEW_PWD_FORMAT_ERROR = "new.pwd.format.error";
	
	
	/*************************红包 加息券  start********************************/
	/**
	 * 规则发放对象为空
	 */
	public static final String TO_USER_IS_NULL="to.user.is.null";
	
	/**
	 * 规则被禁用
	 */
	public static final String RULE_IS_BANED="rule.is.baned";
	
	/**
	 * 规则不在发放期内
	 */
	public static final String RULE_OVER_GRANT_TIME="rule.over.grant.time";
	
	/**
	 * 规则不在有效期内
	 */
	public static final String RULE_OVER_VALIDITY_TIME="rule.over.validity.time";
	
	/**
	 * 剩余发放数量不足
	 */
	public static final String REMAINING_RULE_NUM_INSUFFICIENT="remaining.rule.num.insufficient";
	
	/**
	 * 发放数量最大值为1000000
	 */
	public static final String GRANT_TOTAL_NUM_ERROR="grant.total.num.error";
	
	/**
	 * 规则名称的长度在2-15个字符之间
	 */
	public static final String RULE_NAME_ERROR="rule.name.error";
	
	/**
	 * 礼包名称的长度在2-15个字符之间
	 */
	public static final String GIFT_NAME_ERROR="gift.name.error";
	
	/**
	 * 发放规则不能存在为空的数值
	 */
	public static final String RULE_DETAIL_EMPTY_ERROR="rule.detail.empty.error";
	
	/**
	 * 投资金额为整数
	 */
	public static final String TENDER_AMOUNT_INTEGER_ERROR="tender.amount.integer.error";
	
	/**
	 * 发放数量为整数
	 */
	public static final String GRANT_QUANTITY_INTEGER_ERROR="grant.quantity.integer.error";
	/**
	 * 有效期必须大于发放开始时间
	 */
	public static final String EXPIRETIME_MUST_GT_STARTTIME="expiretime.must.gt.starttime";
	
	/**
	 * 自定义活动红包只可获取一次
	 */
	public static final String AWARD_ONLY_ONCE="award.only.once";
	
	/**
	 * 只有禁用的规则才可删除，请仔细检查
	 */
	public static final String ONLY_BAN_RULE_CAN_DELETE="only.ban.rule.can.delete";
	
	/*************************红包 加息券  end********************************/
	
	/**
	 * 预警订单类型-提现 
	 */
	public static final String ORDER_TYPE_CASH = "order.type.cash";
	
	/*************************字典 start********************************/
	/**
	 * 类型名称长度不能超过40
	 */
	public static final String TYPE_NAME_OVER_LENGTH="type.name.over.length";
	/**
	 * 类型标识长度不能超过64
	 */
	public static final String DICT_TYPE_OVER_LENGTH="dict.type.over.length";
	/**
	 * 字典名称长度不能超过64
	 */
	public static final String ITEM_NAME_OVER_LENGTH="item.name.over.length";
	/**
	 * 字典值长度不能超过128
	 */
	public static final String ITEM_VALUE_OVER_LENGTH="item.value.over.length";
	/**
	 * 描述信息长度不能超过512
	 */
	public static final String REMARK_OVER_LENGTH="remark.over.length";
	/*************************字典 end********************************/
	
	/*************************参数表 start********************************/
	/**
	 * 参数值为必填项且输入长度不允许超过{0}!
	 */
	public static final String CONFIG_VALUE_MAX_LENTH ="config.value.max.lenth";
	
	/*************************参数表 end********************************/
	
	/*************************后台用户 start********************************/
	public static final String SYS_USER_LOGINNAME_LENGTHERROR = "sys.user.loginName.lengthError";
	public static final String SYS_USER_REALNAME_LENGTHERROR = "sys.user.realName.lengthError";
	
	
	/*************************后台用户 end********************************/
	
	
	/**
	 * 描述信息长度不能超过256
	 */
	public static final String CUSTOMER_REMARK_OVER_LENGTH="customer.remark.over.length";
	
	/**
	 * 下架原因长度不能超过128
	 */
	public static final String BORROW_STOP_REMARK_OVER_LENGTH="borrow.stop.remark.over.length";
	
	
	/**
	 * 排序值不能为空
	 */
	public static final String SORT_VALUE_NOT_NULL="sort.value.not.null";
	
	/**
	 * 该角色下还有操作员，请删除操作员后再删除角色
	 */
	public static final String DELETE_ROLE_MUST_HAS_NO_OPERATES="delete.role.must.has.no.operates";
	
	/**
	 * 角色名称
	 */
	public static final String ROLE_NAME_ERROR = "role.name.error";
	
	/**
	 * 资源名称的长度须在5到100之间
	 */
	public static final String RESOURCE_RESNAME_LENTHERROR="resource.resName.lengthError";
	
	/**
	 * 资源值的长度须在5到100之间
	 */
	public static final String RESOURCE_RESTEXT_LENTHERROR="resource.resText.lengthError";
	
	/**
	 * 系统默认角色:超级管理员、在线客服、经理人不可删除，请确认删除的角色中不包含以上类别
	 */
	public static final String ROLE_DELETE_ERROR="role.delete.error";
	/**
	 * 标识已存在
	 */
	public static final String SECTION_CODE_HAS_EXIST="section.code.has.exist";
	
	/**
	 * 导出记录不能超过10000条，请分批次导出
	 */
	public static final String EXPORT_NUM_ERROR="export.num.error";
	
	/**
	 * 账号已被锁定，请您联系客服
	 */
	public static final String ACCOUNT_IS_LOCKED = "account.is.locked";
	
	/**
	 * 文章详情不能为空
	 */
	public static final String ARTICLE_CONTENT_NOT_NULL="article.content.not.null";
	
	/**
	 * 存在正在生效的规则！
	 */
	public static final String ACTIVITY_PLAN_BAN_ERROR="activity.plan.ban.error";
	
	/**
	 * 用户礼包：{0}正在使用，规则禁用失败！
	 */
	public static final String RULE_BAN_ERROR_FOR_GIFT="rule_ban_error_for_gift";
	
	/**
	 * VIP等级奖励：{0}正在使用，规则禁用失败！
	 */
	public static final String RULE_BAN_ERROR_FOR_VIP_LEVEL="rule_ban_error_for_vip_level";
	
	
	/***********************账户中心 ***********************/
	public static final String USER_IS_EXIST="user.is.exist";
	
	public static final String USER_ADD_FAILD="user.add.faild";
	
	public static final String USER_ID_IS_NOT_NULL="user.id.is.not.null";
	
	public static final String USER_ACCOUNTCODE_IS_NOT_NULL="user.accountcode.is.not.null";
	
	public static final String USER_UPDATE_FAILD= "user.update.faild";
	
	public static final String MONEY_NOT_LESS_THAN_ZERO="money.not.less.than.zero";
	
	public static final String TRADENO_NOT_NULL="tradeNo.not.null";
	
	public static final String CHANGE_MONEY_NOT_NULL="change.money.not.null";
	
	public static final String USE_MONEY_NO_USE_MONEY_CANT_INCREASE_TOGETHER="use.money.no.use.money.cant.increase.together";
	
	public static final String USE_MONEY_NO_USE_MONEY_CANT_DECREASE_TOGETHER="use.money.no.use.money.cant.decrease.together";
	
	public static final String ACCOUNT_AND_LOG_NOT_NULL = "account.and.log.not.null";
	
	public static final String ACCOUNT_AND_LOG_NOT_EQUAL ="account.and.log.not.equal";
	
	public static final String ACCOUNT_AND_LOG_NOT_MATCH = "account.and.log.not.match";
	
	/*************** 积分商城 start  ********************/
	/**
	 * 上架中的商品不可修改库存量
	 */
	public static final String SCORE_GOODS_STORE_STATUS_ERROR = "score.goods.store.status.error";
	
	/**
	 * 修改库存不能小于冻结加已售之和
	 */
	public static final String GOODS_STORE_MUSTGT_FREEZE_AND_SOLD = "goods.store.mustgt.freeze.and.sold";
	/**
	 * 商品数量不足
	 */
	public static final String SCORE_GOODS_NUM_NONE = "score.goods.num.none";
	/**
	 * 用户积分不足
	 */
	public static final String SCORE_NOT_ENOUGH = "score.not.enough";
	/**
	 * 跟新商品库存出错
	 */
	public static final String UPDATE_GOODS_STORE_ERROR = "update.goods.store.error";
	
	/**
	 * 申请兑换商品备注
	 */
	public static final String EXCHANGE_GOODS_APPLY_REMARK = "exchange.goods.apply.remark";
	/**
	 * 兑换商品备注
	 */
	public static final String EXCHANGE_GOODS_REMARK = "exchange.goods.remark";
	/**
	 * 兑换商品失败备注
	 */
	public static final String EXCHANGE_GOODS_FAILD_REMARK = "exchange.goods.faild.remark";
	/**
	 * 超过商品最大兑换数量
	 */
	public static final String EXCHANGE_GOODS_LIMIT_ERROR = "exchange.goods.limit.error";
	/**
	 * 请至少上传一张商品图片
	 */
	public static final String GOODS_PIC_NOT_NULL = "goods.pic.not.null";
	/**
	 * 该商品已下架!
	 */
	public static final String GOODS_IS_OVER_STOP_SALE = "goods.is.over.stop.sale";
	/**
	 * 审核通过和已上架的商品不可修改!
	 */
	public static final String GOODS_UPDATE_ERROR = "goods.update.error";
	/**
	 * 恭喜你,兑换成功啦！
	 */
	public static final String GOODS_EXCHANGE_SUCCESS = "goods.exchange.success";
	/**
	 * 请先填写收货地址
	 */
	public static final String RECEIVE_NOT_NULL = "receive.not.null";
	/**
	 * 收货地址数量已达上限，上限数量为{}
	 */
	public static final String RECEIVE_NUM_MAX = "receive.num.max";
	
	/**
	 * 当前收货地址已发生变动，请重新确认收货地址！
	 */
	public static final String RECEIVING_ADDRESS_IS_ERROR = "receiving.address.is.error";
	
	/**
	 * 该兑换商品不存在
	 */
	public static final String SCORE_GOODS_IS_NOT_EXIST = "score.goods.is.not.exist";
	
	/**
	 * 使用积分不能小于0
	 */
	public static final String SCORE_CANNOT_LT_ZERO = "score.cannot.lt.zero";
	
	/**
	 * 兑换留言过长
	 */
	public static final String SCORE_RECEIVE_REMARK_TOO_LONG = "score.receive.remark.too.long";
	
	/*************** 积分商城 end  ********************/
	
		/**
	 * 部分调度任务尚未收到回调且时间间隔过短，请稍后重试
	 */
	public static final String TPPTRADE_DISPATCHTASK_TIMEINTERVAL_TOOSHORT = "tppTrade.dispatchTask.timeInterval.tooShort";
	/**
	 * 用户红包发放操作过于频繁
	 */
	public static final String USER_RED_GRANT_TOO_OFTEN="userRed.grant.tooOften";
	
	/**
	 * 查询开始日期和结束日期都不能为空
	 */
	public static final String STAT_QUERY_DATE_IS_EMPTY = "statistic.query.date.is.empty";
	
    /**
     * 查询开始日期不能大于结束日期
     */
	public static final String STAT_QUERY_DATE_IS_ERROR = "statistic.query.date.is.error";
	
	/**
	 * 对比日期不能为空
	 */
	public static final String STAT_COMPARE_DATE_IS_EMPTY = "statistic.compare.date.is.empty";
	/**
	 * 信用额度提示
	 */
	public static final String USER_CREDIT_BORROW_DEDUCT = "user.credit.borrow.deduct";//信用贷扣除信用额度提示{0}信用贷名称，{1}扣除的信用额度
	public static final String USER_CREDIT_INSUFFICIENT = "user.credit.insufficient";//信用额度不足提示，{0}用户名,{1}需要的信用额度
	public static final String USER_CREDIT_BORROW_RETURN = "user.credit.borrow.return";//退换信用贷额度提示{0}信用贷名称，{1}用户名，{2}退换的信用额度

	/*----提现免手续费金额提示语  start----*/
	//充值成功，提现免手续费的金额上浮{0}元
	public static final String CASH_FEE_MARK_LOG_RECHARGE = "cash.fee.mark.log.recharge";
	//提现处理中，提现需要手续费的金额下浮{0}元
	public static final String CASH_FEE_MARK_LOG_CASH_DISPOSE = "cash.fee.mark.log.cash.dispose";
	//提现失败，提现需要收费的金额上浮{0}
	public static final String CASH_FEE_MARK_LOG_CASH_FAIL = "cash.fee.mark.log.cash.fail";
	public static final String CASH_FEE_MARK_LOG_INVEST = "cash.fee.mark.log.invest";
	public static final String CASH_FEE_MARK_LOG_BOND = "cash.fee.mark.log.bond";
	/*----提现免手续费金额提示语  end----*/
	
	/*********积分抽奖start*******/
	/**
	 * 积分抽奖概率状态错误
	 */
	public static final String SCORE_LOTTORY_STATUS_ERROR = "score.lottery.status.error";
	/**
	 * 积分抽奖备注
	 */
	public static final String SCORE_LOTTORY_SUCCESS_REMARK = "score.lottery.success.remark";

	/**********积分抽奖end******/
}

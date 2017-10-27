package com.rd.ifaes.core.core.constant;

/**
 * 系统日志常量
 * @version 3.0
 * @author fxl
 * @date 2016年10月22日
 */
public final class SysLogConstant {
	
	/******************** 操作方式 start***********/
	/** 查询  */
	public static final String  QUERY = "query";
	/** 查看  */
	public static final String  VIEW = "view";
	/** 新增  */
	public static final String  ADD = "add";
	/** 编辑  */
	public static final String  EDIT = "edit";
	/** 发放  */
	public static final String  GRANT = "grant";
	/** 删除  */
	public static final String  DEL = "del";
	/** 导出  */
	public static final String  EXPORT = "export";
	/** 下载  */
	public static final String  DOWNLOAD = "download";
	/** 执行  */
	public static final String  DO = "do";
	/** 冻结/解冻  */
	public static final String  FREEZE = "freeze";
	/** 登陆  */
	public static final String  LOGIN = "login";
	/** 审核  */
	public static final String  AUDIT = "audit";
	/** 上架  */
	public static final String  SALE = "sale";
	/** 下架  */
	public static final String  STOP = "stop";
	/** 成立审核  */
	public static final String  ESTABLISH = "establish";
	
	/******************** 操作方式 end***********/
	
	/******************** 操作内容 start***********/
	/** 账户列表  */
	public static final String  ACCOUNT_LIST = "account_list";
	/** 用户资金  */
	public static final String  USER_ACCOUNT = "user_account";
	/** 账户日志  */
	public static final String  ACCOUNTLOG_LIST = "accountlog_list";
	/** 平台账户日志  */
	public static final String  MERCHANT_LOG = "merchant_log";
	/** 平台充值  */
	public static final String  MERCHANT_RECHARGE = "merchant_recharge";
	/** 平台提现  */
	public static final String  MERCHANT_CASH = "merchant_cash";
	/** 平台给用户转账  */
	public static final String  MERCHANT_TRANSFER = "merchant_transfer";
	/** 平台账户间转账  */
	public static final String  SUB_MERCHANT_TRANSFER = "sub_merchant_transfer";
	/** 资金对账  */
	public static final String  ACCOUNT_CHECK = "account_check";
	/** 提现列表  */
	public static final String  CASH_LIST = "cash_list";
	/** 提现审核  */
	public static final String  CASH_AUDIT = "cash_audit";
	/** 提现人工核查  */
	public static final String  CASH_CHECK = "cash_check";
	/** 充值列表  */
	public static final String  RECHARGE_LIST = "recharge_list";
	/** 转让记录 */
	public static final String  BOND_INVEST_LIST = "bond_invest_list";
	/** 债权列表 */
	public static final String  BOND_LIST = "bond_list";
	/** 债权规则 */
	public static final String  BOND_RULE = "bond_rule";
	/** 文章 */
	public static final String  ARTICLE = "article";
	/** 栏目 */
	public static final String  SECTION = "section";
	/** 预约跟进 */
	public static final String  BORROW_BESPEAK = "borrow_bespeak";
	/** 借款列表 */
	public static final String  BORROW_LIST = "borrow_list";
	/** 借款 */
	public static final String  BORROW = "borrow";
	/** 借款人 */
	public static final String  BORROWER = "borrower";
	/** 借款资料 */
	public static final String  BORROW_PIC = "borrow_pic";
	/** 借款审核 */
	public static final String  BORROW_AUDIT = "borrow_audit";
	/** 借款上架 */
	public static final String  BORROW_SALE = "borrow_sale";
	/** 借款下架 */
	public static final String  BORROW_STOP = "borrow_stop";
	/** 借款成立审核 */
	public static final String  BORROW_ESTABLISH = "borrow_establish";
	/** 借款记录 */
	public static final String  BORROW_RECORD = "borrow_record";
	/** 贷后跟踪 */
	public static final String  BORROW_FOLLOW = "borrow_follow";
	/** 审核记录 */
	public static final String  VERIFY_LOG = "verify_log";
	/** 待收列表 */
	public static final String  COLLECTION_RECORD = "collection_record";
	/** 投资列表 */
	public static final String  INVEST_RECORD = "invest_record";
	/** 产品 */
	public static final String  PRODUCT = "product";
	/** 产品列表 */
	public static final String  PRODUCT_LIST = "product_list";
	/** 产品审核 */
	public static final String  PRODUCT_AUDIT = "product_audit";
	/** 产品上架 */
	public static final String  PRODUCT_SALE = "product_sale";
	/** 产品下架 */
	public static final String  PRODUCT_STOP = "product_stop";
	/** 产品成立审核 */
	public static final String  PRODUCT_ESTABLISH = "product_establish";
	/** 产品分类 */
	public static final String  PROJECT_TYPE = "project_type";
	/** 还款记录 */
	public static final String  REPAYMENT_RECORD = "repayment_record";
	/** 逾期记录 */
	public static final String  REPAYMENT_OVERDUE = "repayment_overdue";
	/** 垫付记录 */
	public static final String  REPAYMENT_ADVANCE = "repayment_advance";
	/** 催收 */
	public static final String  REPAY_URGE = "repay_urge";
	/** 坏账 */
	public static final String  REPAY_BAD_DEBT = "repay_bad_debt";
	/** 活动方案 */
	public static final String  ACTIVITY = "activity";
	/** 加息规则 */
	public static final String  RATE_RULE = "rate_rule";
	/** 红包规则 */
	public static final String  RED_RULE = "red_rule";
	/** 用户积分*/
	public static final String  USER_SCORE = "user_score";
	/** 用户积分记录 */
	public static final String  USER_SCORE_LOG = "user_score_log";
	/** 加息券 */
	public static final String  USER_RATE = "user_rate";
	/** 红包 */
	public static final String  USER_RED = "user_red";
	/** vip等级 */
	public static final String  VIP_LEVEL = "vip_level";
	/** 用户礼包 */
	public static final String  USER_GIFT = "user_gift";
	/** 用户vip */
	public static final String  USER_VIP = "user_vip";
	/** 协议 */
	public static final String  PROTOCOL = "protocol";
	/** 变现 */
	public static final String  REALIZE = "realize";
	/** 变现规则 */
	public static final String  REALIZE_RULE = "realize_rule";
	/** 风险评估答案 */
	public static final String  RISK_ANSWER = "risk_answer";
	/** 风险等级 */
	public static final String  RISK_LEVEL = "risk_level";
	/** 风险问卷 */
	public static final String  RISK_PAPER = "risk_paper";
	/** 风险问题 */
	public static final String  RISK_QUESTION = "risk_question";
	/** 风险评测记录 */
	public static final String  RISK_USER_LOG = "risk_user_log";
	/** 参数配置 */
	public static final String  CONFIG = "config";
	/** 数据字典*/
	public static final String  DICT = "dict";
	/** 操作日志*/
	public static final String  LOG = "log";
	/** 日志模板*/
	public static final String  LOG_TEMPLATE = "log_template";
	/** 首页信息 */
	public static final String  INDEX = "index";
	/** 用户登陆 */
	public static final String  USER_LOGIN = "user_login";
	/** 菜单 */
	public static final String  MENU = "menu";
	/** 消息记录 */
	public static final String  MESSAGE = "message";
	/** 消息模板 */
	public static final String  MESSAGE_TYPE = "message_type";
	/** 后台用户 */
	public static final String  OPERATOR = "operator";
	/** 角色关联 */
	public static final String  OPERATOR_ROLE = "operator_role";
	/** 组织机构  */
	public static final String  ORG = "org";
	/** 资源  */
	public static final String  RESOURCE = "resource";
	/** 角色 */
	public static final String  ROLE = "role";
	/** 角色权限 */
	public static final String  ROLE_PERMS = "role_perms";
	/** 意见反馈 */
	public static final String  OPINION = "opinion";
	/** 调度任务 */
	public static final String  TPP_TRADE = "tpp_trade";
	/** 投标对账 */
	public static final String  CBHB_INVEST_CHECK = "cbhb_invest_check";
	/** 充值对账 */
	public static final String  CBHB_PDD_CHECK = "cbhb_pdd_check";
	/** 提现对账 */
	public static final String  CBHB_WDC_CHECK = "cbhb_wdc_check";
	/** 实时红包对账 */
	public static final String  CBHB_EXP_CHECK = "cbhb_exp_check";
	/** 经纪人客户 */
	public static final String  OPERATOR_CUSTOMER = "operator_customer";
	/** 业务统计 */
	public static final String  SALE_STATISTICS = "sale_statistics";
	/** 企业用户 */
	public static final String  USER_COMPANY = "user_company";
	/** 好友邀请 */
	public static final String  USER_INVITE = "user_invite";
	/** 邀请奖励 */
	public static final String  USER_INVITE_AWARD = "user_invite_award";
	/** 用户 */
	public static final String  USER = "user";
	/** 用户列表 */
	public static final String  USER_LIST = "user_list";
	/** 资质认证*/
	public static final String  USER_QUALIFICATION = "user_qualification";
	/** 担保用户 */
	public static final String  VOUCH = "vouch";
	/** 担保机构 */
	public static final String  VOUCH_LIST = "vouch_list";
	/** 发放红包导入用户数据 */
	public static final String  GRANT_USER_RED_IMPORT_DATA="grant_user_red_import_data"; 
	/** 发放加息券导入用户数据 */
	public static final String  GRANT_RATE_IMPORT_DATA="grant_rate_import_data"; 
	/** 商品类别管理 */
	public static final String  GOODS_CATEGORY = "goods_category";
	/** 商品库存管理 */
	public static final String  GOODS_STORE = "goods_store";
	/** 商品管理 */
	public static final String  GOODS = "goods";
	/** 投资人数分析 */
	public static final String STATISTIC_INVEST_CUSTOMERS = "statistic_invest_customers";	
	/** 投资金额分析 */
	public static final String STATISTIC_INVEST_AMOUNT = "statistic_invest_amount";	
	/** 投资笔数分析 */
	public static final String STATISTIC_INVEST_TIMES = "statistic_invest_times";
	/** 投资地区分析 */
	public static final String STATISTIC_INVEST_AREA = "statistic_invest_area";	
	/** 投资情况分析 */
	public static final String STATISTIC_INVEST_CONDITION = "statistic_invest_condition";	
	/** 用户统计 */
	public static final String STATISTIC_USER = "statistic_user";
	/** 用户活跃度 */
	public static final String STATISTIC_USER_ACTIVE = "statistic_user_active";
	/** 用户画像 */
	public static final String STATISTIC_USER_PORTRAIT = "statistic_user_portrait";
	/** 借款人数分析 */
	public static final String STATISTIC_BORROWER_NUM = "statistic_borrower_num";
	/** 借款金额分析 */
	public static final String STATISTIC_BORROW_ACCOUNT = "statistic_borrow_account";
	/** 借款笔数分析 */
	public static final String STATISTIC_BORROW_NUM = "statistic_borrow_num";
	/** 借款对比 */
	public static final String STATISTIC_COMPARE_DATA = "statistic_compare_data";
	/** 统计借款列表 */
	public static final String STATISTIC_BORROW_LIST = "statistic_borrow_list";
	/** 借款地区 */
	public static final String STATISTIC_BORROW_AREA = "statistic_borrow_area";
	/** 借款情况 */
	public static final String STATISTIC_BORROW_INFO = "statistic_borrow_info";
	/** 待还分析 */
	public static final String STATISTIC_BORROW_NOTREPAY = "statistic_borrow_notrepay";
	/** 正常还款分析 */
	public static final String STATISTIC_BORROW_REPAID = "statistic_borrow_repaid";
	/** 逾期分析 */
	public static final String STATISTIC_BORROW_OVERDUE = "statistic_borrow_overdue";
	/** 出入金分析 */
	public static final String STATISTIC_ACCOUNT_CIRCULATE = "statistic_account_circulate";
	/** 用户余额分析 */
	public static final String STATISTIC_ACCOUNT_USEMONEY = "statistic_account_useMoney";
	/** 投资收益分析 */
	public static final String STATISTIC_ACCOUNT_EARN = "statistic_account_earn";
	/** 平台盈亏分析 */
	public static final String STATISTIC_ACCOUNT_PLANT = "statistic_account_plant";
	/** 活动运营分析 */
	public static final String STATISTIC_ACTIVITY = "statistic_actvity";
	/** 债权分析 */
	public static final String STATISTIC_BOND = "statistic_bond";
	/** 额度 */
	public static final String  CREDIT = "credit";
	/** 额度审核 */
	public static final String  CREDIT_AUDIT = "credit_audit";
	/** 额度成立审核 */
	public static final String  CREDIT_ESTABLISH = "credit_establish";
	/** 积分抽奖管理 */
	public static final String  SCORE_LOTTERY = "score_lottery";
	/** 积分抽奖记录 */
	public static final String  USER_SCORE_LOTTERY_RECORD = "user_score_lottery_record";
	/******************** 操作内容 end***********/
	/**
	 * 公共变量
	 */
	private SysLogConstant() {

	}

}

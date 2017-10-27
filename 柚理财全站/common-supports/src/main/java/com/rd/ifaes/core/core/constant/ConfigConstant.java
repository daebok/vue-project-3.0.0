package com.rd.ifaes.core.core.constant;

/**
 * 参数配置常量类
 * @version 3.0
 * @author lh
 * @date 2016年8月10日
 */
public class ConfigConstant {
	
	private ConfigConstant() {
	}
	/**
	 * 是否开通线上环境
	 */
	public static final String BOND_PREFIX = "bond_prefix";
	public static final String WEB_META_DESC ="meta_description";
	public static final String WEB_META_KEYWORDS = "meta_keywords";
	public static final String WEB_URL = "web_url";
	public static final String MANAGE_URL = "manage_url";
	
	
	public static final String MOBILE_URL = "mobile_url";
	public static final String UFX_URL = "ufx_url";
	public static final String IMAGE_SERVER_URL = "image_server_url";
	public static final String WEB_IMAGE_SERVER_URL = "web_image_server_url";
	/**
	 * 借款（产品）金额区间
	 */
	public static final String BORROW_LOWEST_ACCOUNT = "borrow_lowest_account";
	public static final String BORROW_MOST_ACCOUNT = "borrow_most_account";
	
	/**
	 * 借款（产品）年化利率区间
	 */
	public static final String BORROW_LOWEST_APR = "borrow_lowest_apr";
	public static final String BORROW_MOST_APR = "borrow_most_apr";
	
	/**
	 * 月均天数
	 */
	public static final String DAYS_OF_MONTH = "days_of_month";
	/**
	 * 年平均天数
	 */
	public static final String DAYS_OF_YEAR = "days_of_year";
	/**
	 * 项目最长年限
	 */
	public static final String PROJECT_TIME_LIMIT_MAX_YEAR = "project_time_limit_max_year";

	/**
	 * 加息利率最大值
	 */
	public static final String BORROW_MOST_ADD_APR = "borrow_most_add_apr";
	/**
	 * 最低起投金额
	 */
	public static final String BORROW_LOWEST_INVEST_ACCOUNT = "borrow_lowest_invest_account";
	
	/**
	 * 借款管理费费率
	 */
	public static final String BORROW_MANAGE_RATE = "borrow_manage_rate";
	/**
	 * 利息管理费费率
	 */
	public static final String INTEREST_MANAGE_RATE = "interest_manage_rate";
	/**
	 * 逾期费率
	 */
	public static final String OVERDUE_FEE = "overdue_fee";
	/**
	 * 逾期？天内不收逾期罚息
	 */
	public static final String FREE_OVERDUE_DAY = "free_overdue_day";
	/**
	 * 逾期利息划给用户比例
	 */
	public static final String LATE_INTEREST_RATE = "late_interest_rate";
	
	public static final String ACCOUNT_CODE = "account_code";
	
	public static final String WEB_NAME = "web_name";
	/**
	 * 每笔投资使用红包最大百分比
	 */
	public static final String INVEST_REDENVELOPE_RATE= "user_invest_redEnvelope_rate";
	/**
	 * 投资订单有效时间
	 */
	public static final String INVEST_TIMEOUT ="invest_timeout";
	
	/**
	 * 投资订单超时时长（分钟）
	 */
	public static final  int  INVEST_DEFAULT_ORDER_TIME_OUT=20;
	/**
	 * 债权投资订单超时时长（分钟）
	 */
	public static final  int  INVEST_DEFAULT_BOND_ORDER_TIME_OUT=10;
	/**
	 * 平台支付活动奖励给投资用户的结算账户
	 */
	public static final String MERCHANT_ACCOUNT ="merchant_account";
	
	/**
	 * 项目编号初始值
	 */
	public static final String PROJECT_NO_INIT_VALUE = "project_no_init_value";
	/**
	 * 债权项目编号初始值
	 */
	public static final String BOND_NO_INIT_VALUE = "bond_no_init_value";
	
	/**
	 * cfca密码
	 */
	public static final String CFC_PASS = "cfc_pwd";
	/**
	 * cfca url
	 */
	public static final String CFC_URL = "cfc_url";
	
	/**
	 * 红包最大有效期限
	 */
	public static final String REDENVELPE_MAX_AVAIL_PERIOD = "redenvelope_max_avail_period";
	/**
	 *加息劵最大有效期限
	 */
	public static final String RATECOUPON_MAX_AVAIL_PERIOD = "rateCoupon_max_avail_period";
	
	/**
	 * 新浪微博
	 */
	public static final String 	WEB_INDEX_SINA_MICROBLOG = "web_index_sina_microblog";
	
	/**
	 * QQ群
	 */
	public static final String 	WEB_INDEX_QQ = "web_index_qq";
	
	/**
	 * copyright
	 */
	public static final String 	WEB_INDEX_COPYRIGHT = "web_index_copyright";

	/**
	 * record_number
	 */
	public static final String 	WEB_RECORD_NUMBER = "web_record_number";
	/**
	 * VERIFY_CODE_TIME
	 */
	public static final String 	VERIFY_CODE_TIME = "verify_code_time";
	/**
	 * 邮件发送地址
	 */
	public static final String 	EMAIL_EMAIL = "email_email";
	
	/**
	 * 资金预警联系人
	 */
	public static final String ACCOUNT_WARN_CONTACTS = "account_warn_contacts";
	
	/**
	 * 开启RabbitMQ异步服务
	 */
	public static final String OPEN_RABBIT_MQ = "open_rabbitmq";
	/**
	 * 三方支付标识
	 */
	public static final String TPP_NAME="tppName";
	/**
	 * 产品展示开关
	 */
	public static final String OPEN_PRODUCT="open_product";
	
	/**
	 * UFX商户号
	 */
	public static final String UFX_CUSTOMERID = "ufx_customerId";
	/**
	 * TPP商户账户号
	 */
	public static final String TPP_ACCOUNT_ID = "tpp_account_id";
	/**
	 * TPP接入版本号
	 */
	public static final String TPP_VERSION = "tpp_version";
	
	/**
	 * 第三方转让手续费限制
	 */
	public static final String UFX_BOND_FEE_LIMIT  = "ufx_bond_fee_limit";
	
	/**
	 * 取现手续费是否平台垫付
	 */
	public static final String CASH_FEE_PLATFORM_ADVANCE="cash_fee_platform_advance";
	/**
	 * 取现手续费是否平台垫付每月次数
	 */
	public static final String CASH_FEE_PLATFORM_ADVANCE_MONTH_COUNT ="cash_fee_platform_advance_month_count";
	
	/**
	 * 客服热线
	 */
	public static final String CUSTOMER_HOTLINE = "customer_hotline";
	
	/**
	 * 当日用户最大未支付订单数
	 */
	public static final String INVEST_UNPAY_MAX = "invest_unpay_max";
	
	/**
	 *是否支持即息理财 1 支持，0 不支持，默认0
	 */
	public static final String  SUPPORT_INTEREST_FINANCING = "support_interest_financing";
	
	/**
	 * 投资/成立审核之后发放首投奖励 1 成立审核，0 投资，默认0
	 */
	public static final String  OPEN_FIRST_INVEST = "open_first_invest";
	
	/**
	 * SFTP相关配置
	 */
	public static final String FTP_HOST = "ftp_host";
	public static final String FTP_PORT = "ftp_port";
	public static final String FTP_USERNAME = "ftp_username";
	public static final String FTP_PASS = "ftp_password";
	public static final String FTP_KEYPATH = "ftp_keyPath";
	
	/**
	 * 平台商户私钥
	 */
	public static final String MERCHANT_PRIVATE_KEY="merchant_private_key";
	/**
	 * 平台商户公钥
	 */
	public static final String MERCHANT_PUBLIC_KEY="merchant_public_key";
	
	/**
	 * 三方API服务接口
	 * merSendSmsPwd,IdCodeUpdate,queryAndUpdatePhone,projectApply,projectUpdate,revokeProject,cashAudit,autoInvest,investFail,loans,repayment,
	 * merTransfer,merTransferCancel,bondInvestFail,fee,voucherLoan,freeze,unFreeze,balanceDeduct,
	 * queryBankCard,queryTransSeq,queryBalance,queryAduit,queryAccDetail,queryRegister,queryProject,queryTradeDetail,queryAuthentication,queryComAccInfo,queryComReg,
	 * queryRepayFreezeDetail,querySingleCreditTransfer,querySingleInvest,querySingleRepayFreeze,queryAccountDetail,queryCreditTransferDetail,queryEcardByIdNo,
	 * queryEcardByPhone,queryInvestDetail,queryOldTradeDetail,queryPassInfo,queryRegister,queryTransfer
	 * 
	 */
	public static final String TPP_API_SERVICES="tpp_api_services";
	/**
	 * 三方表单服务接口
	 * register,companyRegister,bindBankCard,unBindBankCard,authSign,login,recharge,cash,invest,creditTransfer,fssTrans,realName
	 */
	public static final String TPP_FORM_SERVICES="tpp_form_services";
	
	/**
	 * 是否线上
	 */
	public static final String CONFIG_ONLINE="config_online";
	
	/**
	 * HttpClient连接超时时间(毫秒)
	 */
	public static final String TPP_CONN_TIME = "tpp_conn_time";
	
	/**
	 * HttpClient数据传输超时时间(毫秒)
	 */
	public static final String TPP_DATA_TRANS = "tpp_data_trans";
	
	/**
	 * 生产环境路径
	 */
	public static final String TPP_URL = "tpp_url";
	/**
	 * 测试环境路径
	 */
	public static final String TPP_TEST_URL = "tpp_test_url";
	
	/**
	 * 是否开启vip功能
	 */
	public static final String VIP_OPEN =  "vip_open";
	
	/**
	 *  vip成长系数
	 */
	public static final String VIP_GROWTH_RATE = "vip_growth_rate";
	 
	/**
	 * 自动投资最小可用金额
	 */
	public static final String AUTO_INVEST_MIN_USEMONEY = "auto_invest_min_usemoney";
	
	/**
	 * 自动投资开关
	 */
	public static final String AUTO_INVEST = "auto_invest";
	/**
	 * 自动投资可投比例
	 */
	public static final String AUTO_INVEST_MAX_APR = "auto_invest_max_apr";
	/**
	 * VIP自动投资标识
	 */
	public static final String AUTO_INVEST_VIP_FLAG = "auto_invest_vip_flag";
	
	/**
	 * WEB_COMPANY
	 */
	public static final String WEB_COMPANY  = "web_company";
	
	/**
	 * 资源默认语言
	 */
	public static final String MESSAGE_RESOURCE_DEFAULT_LANGUAGE = "message_resource_default_language";
	
	/**
	 * 最多可设置的收货地址数量
	 */
	public static final String WEB_MAXRECEIVENUM  = "web_maxReceiveNum";
	
	/**
	 * 是否启用用户登录页面表单密码加密
	 */
	public static final String USER_LOGIN_FORM_ENCRYPT_ENABLE="user_login_form_encrypt_enable";
	/**
	 * 用户登录页面表单密码公钥
	 */
	public static final String USER_LOGIN_FORM_ENCRYPT_PUBLIC = "user_login_form_encrypt_public";

	/**
	 *用户登录页面表单密码加密私钥
	 */
	public static final String USER_LOGIN_FORM_ENCRYPT_PRIVATE = "user_login_form_encrypt_private";
	
	
	/**
	 * 是否开启任务中心
	 */
	public static final String  TASK_CENTER_ONLINE = "task_center_online";
	
	public static final String WEB_REFRESH_URLS = "web_refresh_urls";
	
	/**
	 * 机构代码
	 */
	public static final String  JXBANK_INST_CODE = "jxbank_inst_code";

	/**
	 * 银行代码
	 */
	public static final String JXBANK_BANK_CODE = "jxbank_bank_code";

	/**
	 * 私钥密码
	 */
	public static final String  JXBANK_PASS = "jxbank_pass";

	/**
	 * 访问环境的路径
	 */
	public static final String JXBANK_BASE_URL = "jxbank_base_url";
	
	/**
	 * 私钥名称
	 */
	public static final String JXBANK_PRIVATE_PASS = "jxbank_private_pass";
	
	/**
	 * 即信公钥名称
	 */
	public static final String JXBANK_JIXIN_PUBLIC_PASS = "jxbank_jixin_public_pass";
	
	/**
	 * 红包账户
	 */
	public static final String JXBANK_REDENVELOPE = "jxbank_redenvelope";
	
	/**
	 * 产品编号
	 */
	public static final String JXBANK_PRODUCT = "jxbank_product";
	
	/**
	 * 银行编号
	 */
	public static final String JXBANK_BANK_NUM = "jxbank_bank_num";
	/**
	 *积分抽奖需要的积分
	 */
	public static final String SCORE_LOTTERY_NUM = "score_lottery_num";
	/**
	 * 积分抽奖游戏规则
	 */
	public static final String SCORE_LOTTERY_GAME_RULE = "score_lottery_game_rule";
}

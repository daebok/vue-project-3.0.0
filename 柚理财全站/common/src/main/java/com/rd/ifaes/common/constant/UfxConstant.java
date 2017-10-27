package com.rd.ifaes.common.constant;

/**
 * ufx接口常量定义
 * @author zxc
 * @version 2.3.0.0
 * @date 2016年1月4日 上午11:51:49
 * Copyright 杭州融都科技股份有限公司 银行存管系统  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class UfxConstant {
	
	private UfxConstant() {
	}
	/**
	 * 三方支付标识-汇付
	 */
	public static final String UFX_TPP_NAME_CHINAPNR  = "chinapnr";
	
	/**
	 * ufx处理返回成功编码
	 */
	public static final String UFX_CODE_SUCCESS = "0000";
	
	/**
	 * ufx处理返回失败编码
	 */
	public static final String UFX_CODE_FAIL = "0001";
	
	/** 提现ufx变更处理 2016-05-27 start **/
	/**
	 * ufx处理返回处理中编码
	 */
	public static final String UFX_CODE_DOING = "0002";
	/** 提现ufx变更处理 2016-05-27 end **/
	
	/**
	 * ufx处理返回待审核编码
	 */
	public static final String UFX_CODE_VERIFY = "0003";
	
	/**
	 * 企业的证件编号，默认18个0
	 */
	public static final String UFX_MERCHANT_IDNO = "000000000000000000";
	
	/**
	 * 接收异步报文成功，回复UFX成功标识
	 */
	public static final String UFX_NOTIFY_RECEIVED_SUCCESS = "SUCCESS";
	
	/*****接口定义 *****/
	/**
	 * 开户
	 */
	public static final String REGISTER = "register";

	/**
	 * 企业开户
	 */
	public static final String COMPANY_REGISTER = "companyRegister";

	/**
	 * 登录
	 */
	public static final String USER_LOGIN = "userLogin";

	/**
	 * 授权
	 */
	public static final String AUTH_SIGN = "authSign";

	/**
	 * 绑卡
	 */
	public static final String BIND_BANK_CARD = "bindBankCard";

	/**
	 * 解绑银行卡
	 */
	public static final String UN_BIND_BANK_CARD = "unBindBankCard";

	/**
	 * 充值
	 */
	public static final String RECHARGE = "recharge";

	/**
	 * 取现
	 */
	public static final String CASH = "cash";
	
	/**
	 * 取现复核
	 */
	public static final String CASH_AUDIT = "cashAudit";

	/**
	 * 发布项目
	 */
	public static final String PROJECT_APPLY = "projectApply";

	/**
	 * 自动投标
	 */
	public static final String AUTO_INVEST = "autoInvest";
	
	/**
	 * 主动投资
	 */
	public static final String INVEST = "invest";
	
	/**
	 * 投资失败退款
	 */
	public static final String INVEST_FAIL = "investFail";
	
	/**
	 * 债权投资失败退款
	 */
	public static final String BOND_INVEST_FAIL = "bondInvestFail";

	/**
	 * 更新标的状态
	 */
	public static final String PROJECT_UPDATE = "projectUpdate";
	
	/**
	 * 撤销项目信息
	 */
	public static final String PROJECT_REVOKE = "revokeProject";


	/**
	 * 放款
	 */
	public static final String LOANS = "loans";

	/**
	 * 还款
	 */
	public static final String REPAYMENT = "repayment";

	/**
	 * 商户转账
	 */
	public static final String MERTRANSFER = "merTransfer";

	/**
	 * 资金转账接口：用户转账接口
	 */
	public static final String ACCOUNT_TRANSFER = "accountTransfer";

	/**
	 * 债权转让
	 */
	public static final String CREDIT_TRANSFER = "creditTransfer";
	
	/**
	 * 生利宝（汇付） 
	 */
	public static final String FSSTRANS = "fssTrans";
	
	/**
	 * 解冻
	 */
	public static final String UNFREEZE = "unFreeze";	
	
	/**
	 * 更新手机号
	 */
	public static final String UPDATE_PHONE = "updatePhone";
	
	/** ===========汇付转换双乾修改变动 start============== **/
	/**
	 * 手续费收取 
	 */
	public static final String FEE = "fee";
	
	/**
	 * 代金券红包放款 
	 */
	public static final String VOUCHER_LOAN = "voucherLoan";
	/** ===========汇付转换双乾修改变动 end============== **/
	
	/******************** 查询类接口 *******************/
	/**
	 * 余额查询
	 */
	public static final String QUERY_BALANCE = "queryBalance";
	
	/**
	 * 查询银行卡
	 */
	public static final String QUERY_BANK_CARD = "queryBankCard";
	
	/**
	 * 单笔交易查询
	 */
	public static final String QUERY_TRANSFER = "queryTransfer";
	
	/**
	 * 查询请求记录补单
	 */
	public static final String ADDITIONAL_ORDER = "additionalOrder";

	/******************** 充值-支付方式 *******************/
	/**
	 * 网银充值
	 */
	public static final String PAY_WAY_ONLINE = "0"; 
	/**
	 * 代扣充值
	 */
	public static final String PAY_WAY_WITHHOLD = "1"; 
	/**
	 * 快捷充值
	 */
	public static final String PAY_WAY_QUICK = "2";   
	/**
	 * 汇款充值
	 */
	public static final String PAY_WAY_REMIT = "3";  
	/**
	 * 企业网银充值
	 */
	public static final String PAY_WAY_COMPANY_ONLINE = "4"; 
	
	/******************** 企业开户-是否是担保账户 *******************/
	/**是担保账户**/
	public static final String GUAR_TYPE_Y = "Y";  
	/**非担保账户**/
	public static final String GUAR_TYPE_N = "N";  
}

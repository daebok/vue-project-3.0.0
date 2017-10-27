package com.rd.ifaes.common.constant;

/**
 * 
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 * 令牌标识相关常量
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月3日
 */
public final class TokenConstants {
	
	
	/**
	 * 债权转让提交token
	 */
	public static final String TOKEN_BOND_COMMIT = "commitBondToken";
	/**
	 * 添加债权规则token
	 */
	public static final String TOKEN_BONDRULE_ADD = "addBondRuleToken";
	/**
	 * 变现提交token
	 */
	public static final String TOKEN_REALIZE_COMMIT = "commitRealizeToken";
	/**
	 * 编辑答案
	 */
	public static final String TOKEN_ANSWER_EDIT ="editAnswerToken";
	/**
	 * 添加答案token
	 */
	public static final String TOKEN_ANSWER_ADD = "addAnswerToken";
	/**
	 * 编辑试卷QAtoken
	 */
	public static final String TOKEN_PAPERS_QA_EDIT = "editPapersQAToken";
	/**
	 * 添加试卷等级token
	 */
	public static final String TOKEN_PAPERSSCORE_ADD = "addPapersScoreToken";
	/**
	 * 添加单个问题token
	 */
	public static final String TOKEN_SINGLE_QUESTION_ADD ="addSingleQuestionToken";
	/**
	 * 添加整卷token
	 */
	public static final String TOKEN_ALLPAPERS_ADD ="addAllPapersToken";
	/**
	 * 编辑整卷token
	 */
	public static final String TOKEN_ALLPAPERS_EDIT = "editAllPapersToken";
	/**
	 * 编辑试卷token
	 */
	public static final String TOKEN_PAPERS_EDIT ="editPapersToken";
	/**
	 * 添加试卷token
	 */
	public static final String TOKEN_PAPERS_ADD = "addPapersToken";
	/**
	 * 单个添加试卷token
	 */
	public static final String TOKEN_PAPERS_ADDSINGLE ="addSinglePapersToken";
	/**
	 * 添加角色token
	 */
	public static final String  TOKEN_ROLE_ADD= "addRoleToken";
	/**
	 * 修改角色token
	 */
	public static final String TOKEN_ROLE_EDIT = "editRoleToken";
	/**
	 * 添加菜单token
	 */
	public static final String TOKEN_MENU_ADD = "addMenuToken";
	/**
	 * 修改菜单token
	 */
	public static final String TOKEN_MENU_EDIT = "editMenuToken";
	/**
	 * 添加栏目token
	 */
	public static final String TOKEN_SECTION_ADD = "addSectionToken";

	/**
	 * 添加产品类别token
	 */
	public static final String TOKEN_PROJECT_TYPE_ADD = "addProjectTypeToken";
	/**
	 * 修改产品类别token
	 */
	public static final String TOKEN_PROJECT_TYPE_EDIT = "editProjectTypeToken";

	/**
	 * 添加产品token
	 */
	public static final String TOKEN_PRODUCT_ADD = "addProductToken";

	/**
	 * 修改产品token
	 */
	public static final String TOKEN_PRODUCT_EDIT = "editProductToken";
	/**
	 * 产品发布审核token
	 */
	public static final String TOKEN_PRODUCT_PUBLISH_VERIFY = "publishVerifyProductToken";
	/**
	 * 产品上架token
	 */
	public static final String TOKEN_PRODUCT_SALE = "saleProductToken";
	/**
	 * 产品下架token
	 */
	public static final String TOKEN_PRODUCT_STOP = "stopProductToken";
	/**
	 * 产品成立审核token
	 */
	public static final String TOKEN_PRODUCT_ESTABLISH_VERIFY = "establishVerifyProductToken";
	/**
	 * 添加借款token
	 */
	public static final String TOKEN_BORROW_ADD = "addBorrowToken";

	/**
	 * 修改借款token
	 */
	public static final String TOKEN_BORROW_EDIT = "editBorrowToken";
	/**
	 * 借款审核token
	 */
	public static final String TOKEN_BORROW_VERIFY = "verifyBorrowToken";
	/**
	 * 借款上架token
	 */
	public static final String TOKEN_BORROW_SALE = "saleBorrowToken";
	/**
	 * 借款下架token
	 */
	public static final String TOKEN_BORROW_STOP = "stopBorrowToken";

	/**
	 * 借款成立审核token
	 */
	public static final String TOKEN_BORROW_ESTABLISH_VERIFY = "establishVerifyBorrowToken";
	
	/**
	 * 添加变现规则token
	 */
	public static final String  TOKEN_REALIZE_RULE_ADD = "addRealizeRuleToken";

	/**
	 * 新增个人用户token
	 */
	public static final String  TOKEN_ADD_USER_PERSON = "addUserPersonToken";
	
	/**
	 * 编辑个人用户token
	 */
	public static final String  TOKEN_EDIT_USER_PERSON = "editUserPersonToken";
	
	/**
	 * 新增企业用户token
	 */
	public static final String  TOKEN_ADD_USER_COMPANY = "addUserCompanyToken";
	
	/**
	 * 编辑企业用户token
	 */
	public static final String  TOKEN_EDIT_USER_COMPANY = "editUserCompanyToken";
	/**
	 * 新增担保用户token
	 */
	public static final String  TOKEN_ADD_USER_VOUCH = "addUserVouchToken";
	public static final String  TOKEN_EDIT_USER_VOUCH = "editUserVouchToken";/* 担保用户修改 */ 
	
	
	/* 经纪人模块相关token */ 
	public static final String  TOKEN_ADD_OPERATOR_CUSTOMER = "addOperatorCustomerToken";/* 经纪人客户新增 */ 
	public static final String  TOKEN_EDIT_OPERATOR_CUSTOMER = "editOperatorCustomerToken";/* 经纪人客户修改 */ 
	
	/**
	 * 立即预约 token校验
	 */
	public static final String  TOKEN_ADD_BORROW_BESPEAK = "borrowBespeakAddToken";
	/**
	 * 预约跟进编辑token
	 */
	public static final String  TOKEN_EDIT_BORROW_BESPEAK = "borrowBespeakEditToken";
	
	/**
	 * 前台借款-上传资料证明
	 */
	public static final String  TOKEN_WEB_BORROW_QUALIFICATION_ADD = "webBorrowQualificationAddToken";
	
	/**
	 * 前台借款-申请
	 */
	public static final String  TOKEN_WEB_BORROW_ADD = "webBorrowAddToken";
	
	/**
	 * 前台借款-确认
	 */
	public static final String  TOKEN_WEB_BORROW_DO_ADD = "webBorrowDoAddToken";
	
	/**
	 * 还款token
	 */
	public static final String  TOKEN_REPAY="repayToken";
	/**
	 * 投资token
	 */
	public static final String  TOKEN_INVEST="investToken";
	/**
	 * 发放红包token
	 */
	public static final String  TOKEN_GRANT_REDENVELOPE = "grantRedenvelopeToken";
	
	/**
	 * 增加红包规则token
	 */
	public static final String  TOKEN_ADD_RED_RULE = "redRuleAddToken";
	
	/**
	 * 发放加息券token
	 */
	public static final String  TOKEN_GRANT_RATECOUPON = "grantRateCouponToken";
	
	/**
	 * 增加红包规则token
	 */
	public static final String  TOKEN_ADD_RATE_RULE = "rateRuleAddToken";
	/**
	 * 意见反馈提交token
	 */
	public static final String TOKEN_ASSISTANT_COMMIT = "commitAssistantToken";
	
	/**
	 * 绑定手机token
	 */
	public static final String TOKEN_MOBILE_BIND = "mobileBindToken";
	
	/**
	 * 绑定邮箱token
	 */
	public static final String TOKEN_EMAIL_BIND = "emailBindToken";
	
	/**
	 * 提现复核token
	 */
	public static final String TOKEN_CASH_AUDIT = "cashAuditToken";
	
	/**
	 * 消息群发
	 */
	public static final String TOKEN_SEND_MESSAGE = "sendMessageToken";
	/**
	 * 用户积分发放
	 */
	public static final String TOKEN_GRANT_SCORE = "grantScoreToken" ;
	private TokenConstants() {
	}
}

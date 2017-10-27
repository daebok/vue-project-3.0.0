package com.rd.ifaes.core.core.constant;

/**
 * 消息模板常量类
 * 
 * @author lh
 * @version 3.0
 * @since 2016年6月22日
 */
public final class MessageConstant {

	/** 邮箱认证-发送认证链接 **/
	public static final String MESSAGE_EMAIL_ACTIVE = "get_email";
	/** 通过邮箱找回密码-发送验证码 **/
	public static final String MESSAGE_GET_PASS_EMAIL = "get_pwd_email";
	/** 通过手机找回密码-发送验证码 **/
	public static final String MESSAGE_GET_PASS_PHONE = "get_pwd_phone";
	/** 绑定邮箱-发送验证码 **/
	public static final String MESSAGE_BIND_EMAIL = "bind_email";
	/** 修改绑定邮箱-发送手机验证码 **/
	public static final String MESSAGE_MODIFY_EMAIL_PHONECODE = "modify_email_phoneCode";
	/** 修改绑定邮箱-发送邮箱验证码 **/
	public static final String MESSAGE_MODIFY_EMAIL_EMAILCODE = "modify_email_emailCode";
	/** 绑定手机-发送验证码 **/
	public static final String MESSAGE_BIND_PHONE = "bind_phone";
	/** 修改绑定手机-发送手机验证码 **/
	public static final String MESSAGE_MODIFY_PHONE_PHONECODE = "modify_phone_phoneCode";
	/** 修改绑定手机-发送邮箱验证码 **/
	public static final String MESSAGE_MODIFY_PHONE_EMAILCODE = "modify_phone_emailCode";
	/** 重置密保问题-发送手机验证码 **/
	public static final String MESSAGE_RESET_QUESTION_PHONECODE = "reset_question_phoneCode";
	/** 重置密保问题-发送邮箱验证码 **/
	public static final String MESSAGE_RESET_QUESTION_EMAILCODE = "reset_question_emailCode";
	/** 用户注册发送手机验证码 **/
	public static final String MESSAGE_USER_REGISTER_PHONECODE = "user_register_phoneCode";
	/** 用户还款发送手机验证码 **/
	public static final String  MESSAGE_USER_REPAY_PHONECODE = "user_repay_phoneCode";
	/** 担保用户垫付还款发送手机验证码 **/
	public static final String  MESSAGE_USER_ADVANCE_PHONECODE = "user_advance_phoneCode";
	/** 找回密码-发送手机验证码 **/
	public static final String  MESSAGE_RETRIEVE_PASS_PHONECODE = "retrieve_password_phoneCode";
	/** 找回密码-发送邮箱验证码 **/
	public static final String  MESSAGE_RETRIEVE_PASS_EMAILCODE = "retrieve_password_emailCode";
	/** 新借款标发布 **/
	public static final String NEW_PROJECT = "new_project";
	/** 借款标取消 **/
	public static final String PROJECT_CANCEL = "project_cancel";
	/** 后台初审通过 **/
	public static final String PROJECT_VERIFY_SUCC = "project_verify_succ";
	/** 后台初审不通过 **/
	public static final String PROJECT_VERIFY_FAIL = "project_verify_fail";
	/** 投标**/
	public static final String INVEST = "invest";
	/** 投标成功 **/
	public static final String INVEST_SUCC = "invest_succ";
	/** 投标失败 **/
	public static final String INVEST_FAIL = "invest_fail";
	/** 自动投标成功 **/
	public static final String AUTO_INVEST = "auto_invest";
	/** 成立审核通过 **/
	public static final String PROJECT_FULL_SUCC = "project_full_succ";
	/** 成立审核失败 **/
	public static final String PROJECT_FULL_FAIL = "project_full_fail";
	/** 代偿成功 **/
    public static final String COMPENSATE_SUCC = "compensate_succ";
	/** 收到投标奖励 **/
	public static final String RECEIVE_TENDER_AWARD = "receive_tender_award";
	/** 支付投标奖励 **/
	public static final String DEDUCT_PROJECTER_AWARD = "deduct_projecter_award";
	/** 借款人还款 **/
	public static final String RECEIVE_REPAY = "receive_repay";
	/** 还款提前通知 **/
    public static final String PROJECTER_REPAY_MESSAGE = "projecter_repay_message";
    /** 认证通过 **/
    public static final String CERTIFY_SUCC = "certify_succ";
    /** 认证未通过 **/
    public static final String CERTIFY_FAIL = "certify_fail";
    /** 债权转让成功出让人 **/
    public static final String BOND_SELL_SUCC = "bond_sell_succ";
    /** 债权转让投资成功 **/
    public static final String BOND_BUY_SUCC = "bond_buy_succ";
    /** 变现成功 **/
	public static final String REALIZE_FULL_SUCC = "realize_full_succ";
	/** 变现失败 **/
	public static final String REALIZE_FULL_FAIL = "realize_full_fail";
    
    /** 提现申请成功 **/
    public static final String CASH_APPLY_SUCC = "cash_apply_succ";
    /** 提现成功 **/
    public static final String CASH_SUCC = "cash_succ";
    /** 提现失败 **/
    public static final String CASH_FAIL = "cash_fail";
    /** 充值成功 **/
	public static final String RECHARGE_SUCC = "recharge_succ";
    /** 充值失败 **/
    public static final String RECHARGE_FAIL = "recharge_fail";
    
    /** 注册验证码 **/
    public static final String MESSAGE_REG = "message_reg";
    
    /** 操作员密码重置 **/
    public static final String MESSAGE_OPERATOR_PASS_RESET = "message_operator_password_reset";
    
    /** 预约投资提醒 **/
    public static final String PROJECT_INVEST_BESPEAK_REMIND = "project_invest_bespeak_remind";
    
    /** 自动预警 **/
    public static final String ACCOUNT_WARN = "account_warn";
    /** 资金对比异常预警提醒*/
    public static final String ACCOUNT_COMPARE_WARN = "account_compare_warn";
    /** TPP实时预警 **/
    public static final String TPP_WARN = "tpp_warn";
    
    public static final String PHONE_SEND = "phone_send";
    
    public static final String EMAIL_SEND = "email_send";
    /**兑换成功 **/
    public static final String DELIVER_SUCC = "deliver_succ";
    /** 催收提醒 **/
    public static final String URGE_REPAYMENT = "urge_repayment" ;
    /** 额度扣除 **/
    public static final String CREDIT_LETTER = "credit_letter" ;
    /** 额度申请 **/
    public static final String ADD_CREDIT_LETTER = "add_credit_letter" ;
    //end
    /**
     * sms: 1 短信
     */
	public static final String MESSAGE_SMS = "1";
	/**
	 * email:2 邮件
	 */
	public static final String MESSAGE_EMAIL = "2";
	/**
	 * letter:3 站内信
	 */
	public static final String MESSAGE_LETTER = "3";
	/**
	 * 发送成功
	 */
	public static final String SEND_STATUS_SUCCESS = "1";
	/**
	 * 发送失败
	 */
	public static final String SEND_STATUS_FAIL = "2";

	public static final String USER_ID_ADMIN = "1";

	/**
	 * 消息模板用变量
	 */
	private MessageConstant() {

	}

}

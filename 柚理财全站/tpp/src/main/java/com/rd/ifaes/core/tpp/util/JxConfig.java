package com.rd.ifaes.core.tpp.util;

public class JxConfig {
    /**
     * 存管的正确返回
     */
    public static String SUCCESS = "00000000";
    

    public static String DOWNLOAD_SUCCESS = "0000";
    
    /**
     * CE999028常规响应
     */
    public static String PROCESS = "CE999028";
    
    /**
     * T9903、CT990300、CE999999超时响应
     */
    public static String TIME_OUT_ONE = "CT9903";

    public static String TIME_OUT_TWO = "CT990300";

    public static String TIME_OUT_THREE = "CE999028";

    /**
     * 510000、502、504等异常响应
     */
    public static String ERROR_ONE = "510000";
    public static String ERROR_TWO = "502";
    public static String ERROR_THREE = "504";
    
    /**
     * 0:正常
     */
    public static String CASH_OR_FLAG_NORMAL = "0";

    /**
     * 1：已冲正/撤销
     */
    public static String CASH_OR_FLAG_ERROR = "1";
    
    /**
     * 平台默认错误，验签失败
     */
    public static String ERROR = "11111111";


    public static final String JXBANK_AUTH_OPEN = "1";
    public static final String JXBANK_AUTH_CLOSE = "0";

    //客户管理接口
    /**
     * 个人绑卡注册
     */
    public static String ACCOUNT_OPEN_PLUS = "accountOpenPlus";

    /**
     * 请求发送短信验证码
     */
    public static String SMS_CODE_APPLY = "smsCodeApply";

    /**
     * 绑定银行卡增强
     */
    public static String CARD_BIND_PLUS = "cardBindPlus";

    /**
     * 电子账户手机号修改增强
     */
    public static String MOBILE_MODIFY_PLUS = "mobileModifyPlus";

    /**
     * 密码重置
     */
    public static String PASSWORD_RESET = "passwordReset";

    /**
     * 密码重置增强
     */
    public static String PASSWORD_RESET_PLUS = "passwordResetPlus";

    /**
     * 解绑银行卡
     */
    public static String CARD_UNBIND = "cardUnbind";

    /**
     * 密码设置
     */
    public static String PASSWORD_SET = "passwordSet";

    //充值类接口

    /**
     * 联机绑定卡到电子账户充值
     */
    public static String DIRECT_RECHARGE_ONLINE = "directRechargeOnline";


    //投标类接口

    /**
     * 借款人标的登记
     */
    public static String DEBT_REGISTER = "debtRegister";

    /**
     * 借款人标的撤销
     */
    public static String DEBT_REGISTER_CANCEL = "debtRegisterCancel";

    /**
     * 投资人投标申请
     */
    public static String BID_APPLY = "bidApply";

    /**
     * 投资人自动投标签约
     */
    public static String AUTO_BID_AUTH = "autoBidAuth";

    /**
     * 投资人自动投标签约增强
     */
    public static String AUTO_BID_AUTH_PLUS = "autoBidAuthPlus";

    /**
     * 撤销自动投标签约
     */
    public static String AUTO_BID_AUTH_CANCEL = "autoBidAuthCancel";

    /**
     * 自动投标申请
     */
    public static String BID_AUTO_APPLY = "bidAutoApply";

    /**
     * 投标申请撤销
     */
    public static String BID_CANCEL = "bidCancel";

    /**
     * 投资人自动债权转让签约
     */
    public static String AUTO_CREDIT_INVEST_AUTH = "autoCreditInvestAuth";
    /**
     * 投资人自动债权转让签约增强
     */
    public static String AUTO_CREDIT_INVEST_AUTH_PLUS = "autoCreditInvestAuthPlus";
    /**
     * 撤销自动债权转让签约
     */
    public static String AUTO_CREDIT_INVEST_AUTH_CANCEL = "autoCreditInvestAuthCancel";


    //扣款类接口

    /**
     * 提现
     */
    public static String WITHDRAW = "withdraw";

    /**
     * 投资人购买债权
     */
    public static String CREDIT_INVEST = "creditInvest";

    /**
     * 红包发放
     */
    public static String VOUCHER_PAY = "voucherPay";

    /**
     * 红包发放撤销
     */
    public static String VOUCHER_PAY_CANCEL = "voucherPayCancel";

    /**
     * 红包发放隔日撤销
     */
    public static String VOUCHER_PAY_DELAY_CANCEL = "voucherPayDelayCancel";

    /**
     * 批次放款
     */
    public static String BATCH_LEND_PAY = "batchLendPay";

    /**
     * 批次还款
     */
    public static String BATCH_REPAY = "batchRepay";

    /**
     * 批次融资人还担保账户垫款
     */
    public static String batch_Repay_Bail = "batchRepayBail";

    /**
     * 批次结束债权
     */
    public static String BATCH_CREDIT_END = "batchCreditEnd";

    /**
     * 批次撤销
     */
    public static String BATCH_CANCEL = "batchCancel";

    /**
     * 批次担保账户代偿
     */
    public static String BATCH_BAIL_REPAY = "batchBailRepay";

    /**
     * 批次发红包
     */
    public static String BATCH_VOUCHER_PAY = "batchVoucherPay";

    //冻结类接口

    /**
     * 还款申请冻结资金
     */
    public static String BALANCE_FREEZE = "balanceFreeze";

    /**
     * 还款申请撤销资金解冻
     */
    public static String BALANCE_UNFREEZE = "balanceUnfreeze";


    //查询接口

    /**
     * 按证件号查询电子账号
     */
    public static String ACCOUNT_ID_QUERY = "accountIdQuery";

    /**
     * 绑卡关系查询
     */
    public static String CARD_BIND_DETAILS_QUERY = "cardBindDetailsQuery";

    /**
     * 企业账户查询
     */
    public static String CORPRATION_QUERY = "corprationQuery";

    /**
     * 电子账户余额查询
     */
    public static String BALANCE_QUERY = "balanceQuery";

    /**
     * 查询批次交易明细状态
     */
    public static String BATCH_DETAILS_QUERY = "batchDetailsQuery";

    /**
     * 查询批次发红包交易明细
     */
    public static String BATCH_VOUCHER_DETAILS_QUERY = "batchVoucherDetailsQuery";

    /**
     * 电子账户资金交易明细查询
     */
    public static String ACCOUNT_DETAILS_QUERY = "accountDetailsQuery";

    /**
     * 单笔资金类业务交易查询
     */
    public static String FUND_TRANS_QUERY = "fundTransQuery";
    
    /**
     * 投资人债权明细查询
     */
    public static String CREDIT_DETAILS_QUERY = "creditDetailsQuery";

    /**
     * 借款人标的信息查询
     */
    public static String DEBT_DETAILS_QUERY = "debtDetailsQuery";
    
    /**
     * 投资人购买债权查询
     */
    public static String CREDIT_INVEST_QUERY = "creditInvestQuery";
    
    /**
     * 投资人投标申请查询
     */
    public static String BID_APPLY_QUERY = "bidApplyQuery";
    
    /**
     * 投资人签约状态查询
     */
    public static String CREDIT_AUTH_QUERY = "creditAuthQuery";
    
    /**
     * 电子账户密码是否设置查询
     */
    public static String PASSWORD_SET_QUERY = "passwordSetQuery";
    
    /**
     * 2.7.16账户资金冻结明细查询
     */
    public static String FREEZE_DETAILS_QUERY = "freezeDetailsQuery";
    
    /**
     * 2.7.10查询批次状态
     */
    public static String BATCH_QUERY = "batchQuery";
}

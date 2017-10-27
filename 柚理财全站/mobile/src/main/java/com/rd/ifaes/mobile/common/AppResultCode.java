package com.rd.ifaes.mobile.common;

/**
 * App返回code常量类
 * 
 * @author Kunkka
 *
 */
public class AppResultCode { 
	/** 常规错误 */
    public static final int ERROR                       = 0x0000;
    /** 常规错误 - 需要特殊处理(弹窗提示 OR 不提示) */
    public static final int ERROR_SPECIAL               = 0x0001;
    /** 注册时提示已被使用 */
    public static final int USER_REGISTER_ERROR         = 0x0002;
    /** 登录时使用  --账户不存在，请重新输入或注册新账号 */
    public static final int USER_DOLOGIN_ERROR          = 0x0003;
    /** 错误 - 弹框上面只有返回按钮 选项 */
    public static final int ERROR_BACK                  = 0x0004;
    /** 开通托管账户 */
    public static final int USER_PAYMENT                = 0x6001;
    /** 风险评测 */
    public static final int USER_RISK                   = 0x6002;
    /** 绑定邮箱 */
    public static final int USER_EMAIL                  = 0x6003;
    /** 提示更新 */
    public static final int UPDATE_NORMAL               = 0x7001;
    /** 强制更新 */
    public static final int UPDATE_FORCED               = 0x7002;
    /** Token过期 - APP调用RefreshToken接口 */
    public static final int TOKEN_TIMEOUT               = 0x8000;
    /** RefreshToken过期 - APP提示需要登录，跳转到登录页面 */
    public static final int TOKEN_REFRESH_TIMEOUT       = 0x8001;
    /** Token不唯一 - APP提示被顶号，跳转到登录页面 */
    public static final int TOKEN_NOT_UNIQUE            = 0x8002;
    /** Token不存在 - APP提示需要登录，跳转到登录页面 */
    public static final int TOKEN_NOT_EXIT              = 0x8003;
   /* *//** Token从其他设备登录*//*
    public static final int TOKEN_NOT_OTHER_LOGIN       = 0x8004;*/
    /** 成功 */
    public static final int SUCCESS                     = 0x9999;
    /** 账户被锁定 需要退出*/
    public static final int USER_LOCK                   = 0x5001;
    /** 充值功能被冻结    */
    public static final int USER_FREEZE_RECHARGE        = 0x5002;
    /** 提现功能被冻结  */
    public static final int USER_FREEZE_CASH            = 0x5003;
    /** 投资功能被冻结  */
    public static final int USER_FREEZE_INVEST          = 0x5004;
    /** 变现能被冻结  */
    public static final int USER_FREEZE_REALIZE         = 0x5005;
    /** 债权功能被冻结  */
    public static final int USER_FREEZE_BOND            = 0x5006;
    /** 借款功能被冻结  */
    public static final int USER_FREEZE_LOAN            = 0x5007;
    
    }

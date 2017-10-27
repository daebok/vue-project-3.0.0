package com.rd.ifaes.common.dict;

/**
 * 缓存keys
 * @author lh
 * @version 3.0
 * @since 2016-8-25
 *
 */
public enum CacheKeys {


	/********************************** 投资相关 **********************************/
      PREFIX_INVEST_ORDER_NO_HANDLE_NUM("invest:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "投资回调处理计数:{key=投资:订单号:handle_num,val= 投资回调处理计数}")
	
    ,PREFIX_INVEST_FAIL_FREEZE_NO_HANDLE_NUM("investFail:freezeNo:%s:handle_num", ExpireTime.FIVE_MIN, "投资失败回调处理计数:{key=投资失败:冻结流水号:handle_num,val= 投资失败回调处理计数}")
    
	, PREFIX_USER_INVEST_UNPAY_NUM("invest:userCustId:%s:%s:unpay_num", ExpireTime.FIVE_MIN, "投资回调处理计数:{key=投资:userCustId:yyyymmdd:unpay_num,val= 投资回调处理计数}")

	, PREFIX_USER_INVEST_NUM("invest:userCustId:%s:invest_num", ExpireTime.FIVE_MIN, "投资记录计数:{key=投资:userCustId:invest_num,val= 投资记录计数}")
	
	, PREFIX_LOAN_ORDER_NO_HANDLE_NUM("loan:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "放款回调处理计数:{key=放款:订单号:handle_num,val=放款回调处理计数}")
	
	, PREFIX_REPAY_ORDER_NO_HANDLE_NUM("repay:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "还款回调处理计数:{key=还款:订单号:handle_num,val=还款回调处理计数}")

	, PREFIX_CREDIT_END_ORDER_NO_HANDLE_NUM("creditEnd:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "结束债权回调处理计数:{key=结束债权:订单号:handle_num,val=结束债权回调处理计数}")

	, PREFIX_REPAY_BAIL_ORDER_NO_HANDLE_NUM("repayBail:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "还垫付款回调处理计数:{key=还垫付款:订单号:handle_num,val=还垫付款回调处理计数}")

	, PREFIX_EXIST_USER_REGISTER_ORDER_NO_HANDLE_NUM("existUserRegister:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "渤海批量实名注册回调处理计数:{key=还款:订单号:handle_num,val=海批量实名注册回调处理计数}")
	/********************************** 债权相关 **********************************/
	, BOND_REMAIN_ACCOUNT("bond:bondNo:%s:remainAccount", ExpireTime.FIVE_MIN, "债权剩余可投金额")
	
	, PREFIX_BOND_INVEST_ORDER_NO_HANDLE_NUM("bondInvest:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "债权投资回调处理计数{key=投资：订单号:handle_num, val= 投资回调处理计数}")
	
	, PREFIX_BOND_LIST("bondList", ExpireTime.FIVE_MIN, "债权项目列表前缀")
	/**
	 * 债权发布处理计数{key=债权发布：原始标投资记录ID:hanlde_num,val=处理计数}
	 */
	,PREFIX_BOND_INSERT_HANDLE_NUM("bond:insert:%s:handle_num",ExpireTime.FIVE_MIN,"债权发布处理计数{key=债权发布：原始标投资记录ID:hanlde_num,val=处理计数}")
	
	/********************************** 提现相关 **********************************/
	, PREFIX_CASH_APPLY_HANDLE_NUM("cash:apply:userId:%s:handle_num", ExpireTime.FIVE_MIN, "用户提现计数:{key=提现:用户ID:handle_num,val= 提现处理计数}")
    , PREFIX_CASH_TPP_HANDLE_NUM("cash:tpp:orderNo:%s:%s:%s:handle_num", ExpireTime.FIVE_MIN, "提现回调处理计数:{key=提现:orderNo:respType:respCode:handle_num,val= 提现回调处理计数}")
    , PREFIX_CASH_AUDIT_HANDLE_NUM("cash:audit:cashNo:%s:handle_num", ExpireTime.FIVE_MIN, "用户提现审核计数:{key=提现审核:提现订单号:handle_num,val= 提现审核计数}")
    , PREFIX_CASH_AUDIT_TPP_HANDLE_NUM("cash:audit:tpp:orderNo:%s:%s:%s:handle_num", ExpireTime.FIVE_MIN, "提现复核回调处理计数:{key=提现:orderNo:respType:respCode:handle_num,val= 提现回调处理计数}")
	
    /********************************** 开户相关 **********************************/
    , PREFIX_TPP_REGISTER_HANDLE_NUM("tpp:register:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "用户第三方开户回调计数:{key=开户:用户ID:handle_num,val= 开户回调处理计数}")
    
    /********************************** 充值相关 **********************************/
    , PREFIX_RECHARGE_ORDER_NO_HANDLE_NUM("recharge:orderNo:%s:handle_num", ExpireTime.FIVE_MIN, "充值回调处理计数:{key=充值:订单号:handle_num,val= 充值回调处理计数}")
    
    /********************************** 回调订单相关 **********************************/
    , PREFIX_TPP_ORDER_HANDLE_KEY("tpp_orderNo_handle_key_%s", ExpireTime.FIVE_MIN, "回调订单处理标识")
    ;
	
	private final String key;
	private final Enum<ExpireTime> time;
	private final String desc;
	
	CacheKeys(String key,Enum<ExpireTime> time,String desc){
		this.key  = key;
		this.time = time;
		this.desc = desc;
	}
	
	/*
	 * 获得缓存key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 获得缓存失效时间
	 * @return
	 */
	public Enum<ExpireTime> getTime() {
		return time;
	}

	/**
	 * 获得缓存描述信息
	 * @return
	 */
	public String getDesc() {
		return desc;
	}
	
	
	
	
}

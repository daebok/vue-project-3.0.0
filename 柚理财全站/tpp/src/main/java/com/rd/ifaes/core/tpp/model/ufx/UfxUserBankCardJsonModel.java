package com.rd.ifaes.core.tpp.model.ufx;

/**
 *	查询银行卡信息Json结果Model
 */
public class UfxUserBankCardJsonModel {
	
	/**
	 * 是否允许解绑-允许 Y
	 */
	public static final String CAN_DISABLE = "Y";
	
	/**
	 * 是否允许解绑-不允许 N
	 */
	public static final String NO_CAN_DISABLE = "N";
	
	/**
	 * 绑卡状态-待绑定
	 */
	public static final String STATE_APPLY = "0";
	
	/**
	 * 绑卡状态-绑定成功
	 */
	public static final String STATE_SUCCESS = "1";
	
	/**
	 * 绑卡状态-绑定失败
	 */
	public static final String STATE_FAIL = "2";
	
	/**
	 * 是否是快捷卡-是 Y
	 */
	public static final String FAST_PAY_FLAG = "Y";
	
	/**
	 * 是否是快捷卡-否 N
	 */
	public static final String NO_FAST_PAY_FLAG = "N";
	
	
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 是否允许解绑
	 * Y：允许
	 * N：不允许
	 */
	private String canDisable;
	
	/**
	 * 绑卡状态  0-待绑定  1-绑定成功  2-绑定失败
	 */
	private String state;
	
	/**
	 * 是否快捷卡
	 * Y：快捷卡
	 * N：普通卡
	 */
	private String  fastPayFlag;

	/**  
	 * 获取银行卡号  
	 * @return cardId  
	 */
	public String getCardId() {
		return cardId;
	}

	/**  
	 * 设置银行卡号  
	 * @param cardId  
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**  
	 * 获取银行编码  
	 * @return bankCode  
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**  
	 * 设置银行编码  
	 * @param bankCode  
	 */
	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	/**  
	 * 获取银行名称  
	 * @return bankName  
	 */
	public String getBankName() {
		return bankName;
	}

	/**  
	 * 设置银行名称  
	 * @param bankName  
	 */
	public void setBankName(final String bankName) {
		this.bankName = bankName;
	}

	/**  
	 * 获取是否允许解绑
	 * Y：允许N：不允许  
	 * @return canDisable  
	 */
	public String getCanDisable() {
		return canDisable;
	}

	/**  
	 * 设置是否允许解绑
	 * Y：允许N：不允许  
	 * @param canDisable  
	 */
	public void setCanDisable(final String canDisable) {
		this.canDisable = canDisable;
	}

	/**
	 * 获取绑卡状态  0-待绑定  1-绑定成功  2-绑定失败
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置绑卡状态  0-待绑定  1-绑定成功  2-绑定失败
	 * @param state
	 */
	public void setState(final String state) {
		this.state = state;
	}

	/**
	 * @return the 是否快捷卡Y：快捷卡N：普通卡
	 */
	public String getFastPayFlag() {
		return fastPayFlag;
	}

	/**
	 * @param 是否快捷卡Y：快捷卡N：普通卡 
	 */
	public void setFastPayFlag(String fastPayFlag) {
		this.fastPayFlag = fastPayFlag;
	}
	
}

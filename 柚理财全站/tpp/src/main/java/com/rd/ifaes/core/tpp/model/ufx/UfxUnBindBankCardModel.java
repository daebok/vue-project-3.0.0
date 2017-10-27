package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 解绑银行卡
 * @author xhf
 * @version 3.0
 * @date 2016年6月26日下午2:48:58
 */
@SuppressWarnings("serial")
public class UfxUnBindBankCardModel extends UfxBaseModel {
	
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 商户用户号
	 */
	private String userId;
	
	/**
	 * 使用密码标识
	 */
	private String pwdFlag;
	
	/**
	 * 支付密码
	 */
	private String payPwd;
	
	/**
	 * 银行标识
	 */
	private String bankCode;
	
	/**
	 * 身份证号
	 */
	private String idNo;
	
	/**
	 * 姓名
	 */
	private String realName;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"userCustId", "cardId", "userId","pwdFlag","payPwd", "bankCode", "idNo", "realName", "reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "cardId", "idNo", "realName", "reqExt", "merPriv", "signInfo" };
	
	/**
	 * 构造函数
	 */
	public UfxUnBindBankCardModel(){
		super();
		this.setService(UfxConstant.UN_BIND_BANK_CARD);
		this.setReturnUrl("/ufx/unBindBankCard/return.html");
		this.setNotifyUrl("/ufx/unBindBankCard/notify.html");
	}

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
	 * 获取平台商户号
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置平台商户号
	 * @param userId the userId to set
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取银行标识
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * 设置银行标识
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 获取身份证号
	 * @return the idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置身份证号
	 * @param idNo the idNo to set
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取姓名
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置姓名
	 * @param realName the realName to set
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/** 
	 * 获取请求参数数组
	 * @return requestParamNames
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/** 
	 * 设置请求参数数组
	 * @param requestParamNames
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/** 
	 * 获取响应参数数组
	 * @return responseParamNames
	 */
	
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/** 
	 * 设置响应参数数组
	 * @param responseParamNames
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 
	 * 获取密码标识
	 * @return
	 */
	public String getPwdFlag() {
		return pwdFlag;
	}

	/**
	 * 
	 * 设置密码标识
	 * @param pwdFlag
	 */
	public void setPwdFlag(final String pwdFlag) {
		this.pwdFlag = pwdFlag;
	}

	/**
	 * 
	 * 获取支付密码
	 * @return
	 */
	public String getPayPwd() {
		return payPwd;
	}

	/**
	 * 
	 * 设置支付密码
	 * @return
	 */
	public void setPayPwd(final String payPwd) {
		this.payPwd = payPwd;
	}
	
}

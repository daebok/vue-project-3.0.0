package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 绑定银行卡model
 * @author ctt
 * @version 1.0
 * @date 2015年12月10日下午4:04:20
 */
@SuppressWarnings("serial")
public class UfxBindBankCardModel extends UfxBaseModel {

	/**
	 * 身份证号
	 */
	private String idNo;
	
	/**
	 * 户名
	 */
	private String realName;
	
	/** ===========汇付转换双乾修改变动 start============== **/
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 银行标识
	 */
	private String bankCode;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 密码
	 */
	private String payPwd;
	
	/** ===========汇付转换双乾修改变动 end============== **/
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "idNo", "realName", "userId", "returnUrl", "signInfo" };

	/** ===========汇付转换双乾修改变动 start============== **/
	// 加入:, "phone", "userId", "cardId", "bankCode", "payPwd"
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "idNo", "realName", "userId", "cardId", "bankCode", "signInfo" };
	/** ===========汇付转换双乾修改变动 end============== **/
	
	public UfxBindBankCardModel(){
		super();
		this.setService(UfxConstant.BIND_BANK_CARD);
		this.setReturnUrl("/ufx/bindBankCard/return.html");
	}

	/**
	 * 获取身份证号
	 * @return idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置身份证号
	 * @param idNo
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取户名
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置户名
	 * @param realName
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
	 * 获取银行卡号
	 * @return
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 设置银行卡号
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 获取银行编号
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * 
	 * 设置银行编号
	 */
	public void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 获取手机号
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * 获取支付密码
	 * @return
	 */
	public String getPayPwd() {
		return payPwd;
	}

	/**
	 * 设置支付密码
	 */
	public void setPayPwd(final String payPwd) {
		this.payPwd = payPwd;
	}
}

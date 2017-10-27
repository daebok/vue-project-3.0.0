package com.rd.ifaes.core.tpp.model.ufx;

import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 用户余额查询
 * @author xhf
 * @version 3.0
 * @date 2016年6月18日 下午2:50:03
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxQueryBalanceModel extends UfxBaseModel {
	
	/**
	 * 默认现金账户类型
	 */
	public static final String ACCOUNT_TYPE_CASH = "01";
	
	/**
	 * 费用账户类型
	 */
	public static final String ACCOUNT_TYPE_FEE = "01";
	
	/**
	 * 用户类型 - 个人用户
	 */
	public static final String USER_TYPE_PERSONAL = "01";
	
	/**
	 * 用户类型 - 企业用户
	 */
	public static final String USER_TYPE_COMPANY = "02";
	
	/**
	 * 账户类型
	 * 01现金账户，02费用账户
	 */
	private String accountType;
	
	/**
	 *用户类型
	 * 01个人，02企业
	 */
	private String userType;
	
	/**
	 * 用户身份证号码 个人用户必传
	 */
	private String idNo;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 注册时的返回的流水号，徽商查企业信息必传
	 */
	private String tradeNo;
	
	/**
	 * 用户在资金存管平台的交易密码 兴业查企业信息必传
	 */
	private String payPwd;

	/************************************* 响应 ************************************************/
	
	/**
	 * 可用余额，必填
	 */
	private String avlBal;
	
	/**
	 * 账户余额，必填
	 */
	private String acctBal;
	
	/**
	 * 冻结余额，必填
	 */
	private String frzBal;
	
	/**
	 * 可提现余额，根据接口情况返回
	 */
	private String withdrawBal;
	
	/**
	 * 子账户
	 * 说明：返回json，没有子账户返""
	 * 格式：[{"accountId":"MD0000001","avlBal":"0.00","acctBal":"0.00","frzBal":"0.00"}]
	 */
	private String subAccounts;
	
	/**
	 * 用户签订的协议，使用|分隔，例如1|2，协议类型参见签约接口类型 联动使用
	 */
	private String agreementList;
	
	/**
	 * 账户状态，必填。
	 *  -1：审核拒绝   00：注销   01：正常； 02：审核中； 03：未知； 04:待激活；05： 冻结；
	 *  06：挂失；07：尚未认证（双乾）； 08：快捷认证（双乾）； 09：其他认证（双乾）；10：已通过其他渠道开户(徽商)
	 */
	private String accountState;
	
	/**
	 * 证件类型
	 */
	private String certType;

	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"userCustId", "accountType","userType","idNo","userId","phone","realName","tradeNo","payPwd",
			"reqExt", "merPriv", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "avlBal", "acctBal", "frzBal", "withdrawBal", "subAccounts", "agreementList", "reqExt", "merPriv", "signInfo" };

	public UfxQueryBalanceModel() {
		super();
		this.setService(UfxConstant.QUERY_BALANCE);
	}
	
	@Override
	public void response(String resp){
		super.response(resp);
		JSONObject respJson = JSONObject.parseObject(resp);
		this.setSubAccounts(respJson.getString("subAccounts"));
		this.setAvlBal(respJson.getString("avlBal"));
		this.setAcctBal(respJson.getString("acctBal"));
		this.setFrzBal(respJson.getString("frzBal"));
		this.setWithdrawBal(respJson.getString("withdrawBal"));
		this.setMerPriv(respJson.getString("merPriv"));
	}

	/**  
	 * 获取可用余额  
	 * @return avlBal  
	 */
	public String getAvlBal() {
		return avlBal;
	}

	/**  
	 * 设置可用余额  
	 * @param avlBal  
	 */
	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}

	/**  
	 * 获取账户余额  
	 * @return acctBal  
	 */
	public String getAcctBal() {
		return acctBal;
	}

	/**  
	 * 设置账户余额  
	 * @param acctBal  
	 */
	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}

	/**  
	 * 获取冻结余额  
	 * @return frzBal  
	 */
	public String getFrzBal() {
		return frzBal;
	}

	/**  
	 * 设置冻结余额  
	 * @param frzBal  
	 */
	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
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
	public void setRequestParamNames(String[] requestParamNames) {
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
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取subAccounts
	 * @return subAccounts
	 */
	public String getSubAccounts() {
		return subAccounts;
	}

	/**
	 * 设置subAccounts
	 * @param subAccounts
	 */
	public void setSubAccounts(String subAccounts) {
		this.subAccounts = subAccounts;
	}

	/**
	 * 获取accountType
	 * @return accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * 设置accountType
	 * @param accountType
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * 获取用户身份证号码个人用户必传
	 * @return idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置用户身份证号码个人用户必传
	 * @param  idNo
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取手机号
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号
	 * @param  phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取真实姓名
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名
	 * @param  realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取注册时的返回的流水号，徽商查企业信息必传
	 * @return tradeNo
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置注册时的返回的流水号，徽商查企业信息必传
	 * @param  tradeNo
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取用户在资金存管平台的交易密码兴业查企业信息必传
	 * @return payPwd
	 */
	public String getPayPwd() {
		return payPwd;
	}

	/**
	 * 设置用户在资金存管平台的交易密码兴业查企业信息必传
	 * @param  payPwd
	 */
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	/**
	 * 获取可提现余额，根据接口情况返回
	 * @return withdrawBal
	 */
	public String getWithdrawBal() {
		return withdrawBal;
	}

	/**
	 * 设置可提现余额，根据接口情况返回
	 * @param  withdrawBal
	 */
	public void setWithdrawBal(String withdrawBal) {
		this.withdrawBal = withdrawBal;
	}

	/**
	 * 获取用户签订的协议，使用|分隔，例如1|2，协议类型参见签约接口类型联动使用
	 * @return agreementList
	 */
	public String getAgreementList() {
		return agreementList;
	}

	/**
	 * 设置用户签订的协议，使用|分隔，例如1|2，协议类型参见签约接口类型联动使用
	 * @param  agreementList
	 */
	public void setAgreementList(String agreementList) {
		this.agreementList = agreementList;
	}

	/**
	 * 获取账户状态，必填。-1：审核拒绝00：注销01：正常；02：审核中；03：未知；04:待激活；05：冻结；
	 * 06：挂失；07：尚未认证（双乾）；08：快捷认证（双乾）；09：其他认证（双乾）；10：已通过其他渠道开户(徽商)
	 * @return accountState
	 */
	public String getAccountState() {
		return accountState;
	}

	/**
	 * 设置账户状态，必填。-1：审核拒绝00：注销01：正常；02：审核中；03：未知；04:待激活；05：冻结；
	 * 06：挂失；07：尚未认证（双乾）；08：快捷认证（双乾）；09：其他认证（双乾）；10：已通过其他渠道开户(徽商)
	 * @param  accountState
	 */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	/**
	 * 获取证件类型
	 * @return certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * 设置证件类型
	 * @param  certType
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	
}

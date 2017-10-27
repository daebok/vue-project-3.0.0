package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 债权转让Model
 * 
 * @author yinliang UpdateBy QianPengZhan
 * @version 1.0
 * @date 2015年12月16日 上午11:00:54 Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights
 *       Reserved 官方网站：www.erongdu.com 研发中心：rdc@erongdu.com
 *       未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxCreditTransferModel extends UfxBaseModel {

	/**
	 * 转让人客户号 债权转让转让人在资金存管平台的账户
	 */
	private String assignorCustId;

	/**
	 * 债权转让金额 格式"#.00"
	 */
	private String assignAmount;

	/**
	 * 投资流水号 被转让债权的投资流水号
	 */
	private String investNo;

	/**
	 * 投资日期 被转让债权的投资日期 格式yyyyMMdd
	 */
	private String investDate;

	/**
	 * 投资（债权受让）订单号
	 */
	private String creditTransferInvestNo;

	/**
	 * 投资（债权受让）日期
	 */
	private String creditTransferInvestDate;

	/**
	 * 债权管理费 债权转让扣除转让人的管理费 格式"#.00"
	 */
	private String creditFee;

	/**
	 * 是否全部转让 必填 1 全部转让 0 部分转让
	 */
	private String portion;

	/**
	 * 用户号 商户平台用户唯一标识，如用户名，6~25半角字符 必填
	 */
	private String userId;

	/**
	 * 可选 身份证号码 受让人身份证号码 兴业必填
	 */
	private String idNo;

	/**
	 * 可选 到期后总金额 格式“#.00” 兴业必传
	 */
	private String endAmt;

	/**
	 * 年化率 可选字段 兴业必传 格式“#.00” 如果9.80 则表示 9.80%
	 */
	private String apr;

	/**
	 * 可选 收费方式 0 ：指定金额 1：同产品设置 默认0 徽商必传
	 */
	private String feeWay;

	/**
	 * 可选 监管协议编号 兴业必传
	 */
	private String superviseNo;

	/**
	 * 可选 授权码 债权投资授权码 徽商必传
	 */
	private String authCode;

	/**
	 * 可选 短信验证码 浙商必传
	 */
	private String smsCode;

	/**
	 * 可选 项目已还金额 汇付必传
	 */
	private String repaymentYesAccount;

	/**
	 * 可选 受让人手机号 浙商必传
	 */
	private String phone;

	/** 回调参数 */
	/**
	 * 新监管协议编号
	 */
	private String tradeNo;

	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "sponsorer",
			"projectAmount", "assignorCustId", "assignAmount", "investNo",
			"investDate", "userCustId", "amount", "creditFee", "portion",
			"userId", "idNo", "endAmt", "apr", "feeWay", "authCode",
			"superviseNo", "repaymentYesAccount", "phone", "reqExt", "merPriv",
			"returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "assignorCustId",
			"assignAmount", "investNo", "investDate", "userCustId", "amount",
			"creditFee", "creditTransferInvestNo", "creditTransferInvestDate",
			"reqExt", "merPriv", "signInfo" };
	
	/**
	 * 构造器
	 */
	public UfxCreditTransferModel() {
		super();
		this.setService(UfxConstant.CREDIT_TRANSFER);
		this.setReturnUrl("/ufx/bond/creditTransferReturn.html");
		this.setNotifyUrl("/ufx/bond/creditTransferNotfy.html");
	}

	/**
	 * 获取转让人客户号
	 * 
	 * @return assignorCustId
	 */
	public String getAssignorCustId() {
		return assignorCustId;
	}

	/**
	 * 设置转让人客户号
	 * 
	 * @param assignorCustId
	 */
	public void setAssignorCustId(final String assignorCustId) {
		this.assignorCustId = assignorCustId;
	}

	/**
	 * 获取债权金额
	 * 
	 * @return assignAmount
	 */
	public String getAssignAmount() {
		return assignAmount;
	}

	/**
	 * 设置债权金额
	 * 
	 * @param assignAmount
	 */
	public void setAssignAmount(final String assignAmount) {
		this.assignAmount = assignAmount;
	}

	/**
	 * 获取投资流水号被转让债权的投资流水号
	 * 
	 * @return investNo
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**
	 * 设置投资流水号被转让债权的投资流水号
	 * 
	 * @param investNo
	 */
	public void setInvestNo(final String investNo) {
		this.investNo = investNo;
	}

	/**
	 * 获取投资日期被转让债权的投资日期格式yyyyMMdd
	 * 
	 * @return investDate
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置投资日期被转让债权的投资日期格式yyyyMMdd
	 * 
	 * @param investDate
	 */
	public void setInvestDate(final String investDate) {
		this.investDate = investDate;
	}

	/**
	 * 获取投资（债权受让）订单号
	 * 
	 * @return creditTransferInvestNo
	 */
	public String getCreditTransferInvestNo() {
		return creditTransferInvestNo;
	}

	/**
	 * 设置投资（债权受让）订单号
	 * 
	 * @param creditTransferInvestNo
	 */
	public void setCreditTransferInvestNo(final String creditTransferInvestNo) {
		this.creditTransferInvestNo = creditTransferInvestNo;
	}

	/**
	 * 获取投资（债权受让）日期
	 * 
	 * @return creditTransferInvestDate
	 */
	public String getCreditTransferInvestDate() {
		return creditTransferInvestDate;
	}

	/**
	 * 设置投资（债权受让）日期
	 * 
	 * @param creditTransferInvestDate
	 */
	public void setCreditTransferInvestDate(final String creditTransferInvestDate) {
		this.creditTransferInvestDate = creditTransferInvestDate;
	}

	/**
	 * 获取债权管理费
	 * 
	 * @return creditFee
	 */
	public String getCreditFee() {
		return creditFee;
	}

	/**
	 * 设置债权管理费
	 * 
	 * @param creditFee
	 */
	public void setCreditFee(final String creditFee) {
		this.creditFee = creditFee;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return requestParamNames
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 * 
	 * @param requestParamNames
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return responseParamNames
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 * 
	 * @param responseParamNames
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取属性portion的值
	 * 
	 * @return portion属性值
	 */
	public String getPortion() {
		return portion;
	}

	/**
	 * 设置属性portion的值
	 * 
	 * @param portion属性值
	 */
	public void setPortion(final String portion) {
		this.portion = portion;
	}

	/**
	 * 获取属性userId的值
	 * 
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * 
	 * @param userId属性值
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性idNo的值
	 * 
	 * @return idNo属性值
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置属性idNo的值
	 * 
	 * @param idNo属性值
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取属性endAmt的值
	 * 
	 * @return endAmt属性值
	 */
	public String getEndAmt() {
		return endAmt;
	}

	/**
	 * 设置属性endAmt的值
	 * 
	 * @param endAmt属性值
	 */
	public void setEndAmt(final String endAmt) {
		this.endAmt = endAmt;
	}

	/**
	 * 获取属性apr的值
	 * 
	 * @return apr属性值
	 */
	public String getApr() {
		return apr;
	}

	/**
	 * 设置属性apr的值
	 * 
	 * @param apr属性值
	 */
	public void setApr(final String apr) {
		this.apr = apr;
	}

	/**
	 * 获取属性feeWay的值
	 * 
	 * @return feeWay属性值
	 */
	public String getFeeWay() {
		return feeWay;
	}

	/**
	 * 设置属性feeWay的值
	 * 
	 * @param feeWay属性值
	 */
	public void setFeeWay(final String feeWay) {
		this.feeWay = feeWay;
	}

	/**
	 * 获取属性superviseNo的值
	 * 
	 * @return superviseNo属性值
	 */
	public String getSuperviseNo() {
		return superviseNo;
	}

	/**
	 * 设置属性superviseNo的值
	 * 
	 * @param superviseNo属性值
	 */
	public void setSuperviseNo(final String superviseNo) {
		this.superviseNo = superviseNo;
	}

	/**
	 * 获取属性authCode的值
	 * 
	 * @return authCode属性值
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * 设置属性authCode的值
	 * 
	 * @param authCode属性值
	 */
	public void setAuthCode(final String authCode) {
		this.authCode = authCode;
	}

	/**
	 * 获取属性smsCode的值
	 * 
	 * @return smsCode属性值
	 */
	public String getSmsCode() {
		return smsCode;
	}

	/**
	 * 设置属性smsCode的值
	 * 
	 * @param smsCode属性值
	 */
	public void setSmsCode(final String smsCode) {
		this.smsCode = smsCode;
	}

	/**
	 * 获取属性repaymentYesAccount的值
	 * 
	 * @return repaymentYesAccount属性值
	 */
	public String getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	/**
	 * 设置属性repaymentYesAccount的值
	 * 
	 * @param repaymentYesAccount属性值
	 */
	public void setRepaymentYesAccount(final String repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	/**
	 * 获取属性phone的值
	 * 
	 * @return phone属性值
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置属性phone的值
	 * 
	 * @param phone属性值
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * 获取属性tradeNo的值
	 * 
	 * @return tradeNo属性值
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置属性tradeNo的值
	 * 
	 * @param tradeNo属性值
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
	}

}

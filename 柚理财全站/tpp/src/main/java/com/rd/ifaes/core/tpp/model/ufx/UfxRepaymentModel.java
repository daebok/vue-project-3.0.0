package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 还款model
 * @author xx
 * @version 1.0
 * @date 2015年12月7日 下午2:50:58
 * Copyright 杭州融都科技股份有限公司 UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxRepaymentModel extends UfxBaseModel {
	
	/**
	 * 订单日期
	 */
	private String orderDate;
	
	/**
	 * 还款类型，01融资人还款，02商户平台代偿，03融资人还代偿给商户平台，04担保方代偿，05融资人还代偿给担保方
	 */
	private String repayType;
	
	/**
	 * 还款资金流向，01项目资金转入，02项目资金转出，03项目资金无密转入
	 */
	private String fundFlow;
	
	/**
	 * 出账客户号
	 */
	private String outCustId;
	
	/**
	 * 入账客户号
	 */
	private String inCustId;
	
	/**
	 * 投资订单流水号
	 */
	private String investNo;
	
	/**
	 * 投资订单日期
	 */
	private String investDate;
	
	/**
	 * 手续费
	 */
	private String fee;
	
	/**
	 * 用户类型 01：个人 02：企业 默认 01<br>
		此处为资金变动方的账户类型，例如：还款转入时为借款人的账户类型，还款转出时，投资人的账户类型	联动必传
	 */
	private String userType;
	/**
	 * 商户平台用户号, 商户平台用户唯一标识，如用户名，6-25 位半角字符<br>
		项目资金转入时填写融资人客户 ID，转出时填写投资人客户 ID 兴业必传
	 */
	private String userId;
	/**
	 * 密码 <br>
	 * 用户在资金存管平台的交易密码， 项目资金转入时传融资人的，转出时传投资人的 兴业必传
	 */
	private String payPwd;
	/**
	 * 用户姓名<br>
	 * 用户真实姓名，项目资金转入时填写融资人姓名，转出时填写投资人姓名
	 */
	private String realName;
	/**
	 * 流水号, 该参数作废
	 */
	private String tradeNo;
	/**
	 * 总收益<br>
	 * 项目资金转出时必传，保留两位小数，格式为“#.00“
	 */
	private String sumIncome;
	/**
	 * 授权码<br>
	 * 还款时填写投资申请时返回的授权码 徽商必传
	 */
	private String authCode;
	/**
	 * 监管协议编号<br>
	 * 项目资金转入时填写发标时的监管协议编号，转出时填写投资时编号 兴业必传
	 */
	private String superviseNo;
	/**
	 * 结束标志<br>
	 * 标识是否最后一笔还款 Y：是 N：否 徽商必传
	 */
	private String endFlag;
	/**
	 * 批量放款<br>
	 * List 转化为 json 格式字符串， List 每一个对象都包含除参数列表中除 repaymentList 之外业务参数，徽商必传
	 */
	private String repaymentList;
	/**
	 * 投资人账户 汇付体验券还款/逾期利息还款时，还款类型repayType 应传01，
	 * 入账客户号inCustId 为商户ID，investCustId 必须传投资人第三方账户，其他场景investCustId 可不传
	 */
	private String investCustId;
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "repayType","fundFlow", 
			"outCustId", "inCustId", "projectId", "investNo", "investDate", "amount", "fee", 
			"userType",  "userId",  "payPwd", "realName",  "tradeNo",  "sumIncome",
			"authCode",  "superviseNo",  "endFlag",  "repaymentList",  "sponsorer","investCustId",
			"reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"repayType", "fundFlow", "outCustId", "inCustId", "projectId",
			"investNo", "investDate", "amount", "fee", 
			"tradeNo", "endFlag", "repaymentList",
			"reqExt", "merPriv", "signInfo" };
	
	
	public UfxRepaymentModel() {
		super();
		this.setService(UfxConstant.REPAYMENT);
		this.setReturnUrl("/ufx/repay/return.html");
		this.setNotifyUrl("/ufx/repay/notify.html");
	}
	
	/**  
	 * 获取订单日期  
	 * @return orderDate  
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**  
	 * 设置订单日期  
	 * @param orderDate  
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * 获取还款资金流向，01项目资金转入，02项目资金转出，03项目资金无密转入 
	 * @return fundFlow
	 */
	public String getFundFlow() {
		return fundFlow;
	}

	/**
	 * 设置还款资金流向，01项目资金转入，02项目资金转出，03项目资金无密转入
	 * @param fundFlow
	 */
	public void setFundFlow(String fundFlow) {
		this.fundFlow = fundFlow;
	}

	/**
	 * 获取还款类型，01融资人还款，02商户平台代偿，03融资人还代偿给商户平台，04担保方代偿，05融资人还代偿给担保方
	 * @return repayType
	 */
	public String getRepayType() {
		return repayType;
	}

	/**
	 * 设置还款类型，01融资人还款，02商户平台代偿，03融资人还代偿给商户平台，04担保方代偿，05融资人还代偿给担保方
	 * @param repayType
	 */
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	/**  
	 * 获取出账客户号  
	 * @return outCustId  
	 */
	public String getOutCustId() {
		return outCustId;
	}

	/**  
	 * 设置出账客户号  
	 * @param outCustId  
	 */
	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}

	/**  
	 * 获取入账客户号  
	 * @return inCustId  
	 */
	public String getInCustId() {
		return inCustId;
	}

	/**  
	 * 设置入账客户号  
	 * @param inCustId  
	 */
	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
	}

	/**  
	 * 获取投资订单流水号  
	 * @return investNo  
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**  
	 * 设置投资订单流水号  
	 * @param investNo  
	 */
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	/**  
	 * 获取投资订单日期  
	 * @return investDate  
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**  
	 * 设置投资订单日期  
	 * @param investDate  
	 */
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	/**  
	 * 获取手续费  
	 * @return fee  
	 */
	public String getFee() {
		return fee;
	}

	/**  
	 * 设置手续费  
	 * @param fee  
	 */
	public void setFee(String fee) {
		this.fee = fee;
	}
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getRealName() {
		return realName;
	}



	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getSumIncome() {
		return sumIncome;
	}

	public void setSumIncome(String sumIncome) {
		this.sumIncome = sumIncome;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getSuperviseNo() {
		return superviseNo;
	}

	public void setSuperviseNo(String superviseNo) {
		this.superviseNo = superviseNo;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getRepaymentList() {
		return repaymentList;
	}

	public void setRepaymentList(String repaymentList) {
		this.repaymentList = repaymentList;
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
	 * 投资人账户 
	 * 汇付体验券还款/逾期利息还款时，还款类型repayType 应传01，
	 * 入账客户号inCustId 为商户ID，investCustId 必须传投资人第三方账户，其他场景investCustId 可不传
	 * @return investCustId属性值
	 */
	public String getInvestCustId() {
		return investCustId;
	}

	/**
	 *投资人账户
	 * 汇付体验券还款/逾期利息还款时，还款类型repayType 应传01，
	 * 入账客户号inCustId 为商户ID，investCustId 必须传投资人第三方账户，其他场景investCustId 可不传
	 * @param  investCustId属性值
	 */
	public void setInvestCustId(String investCustId) {
		this.investCustId = investCustId;
	}
 
}
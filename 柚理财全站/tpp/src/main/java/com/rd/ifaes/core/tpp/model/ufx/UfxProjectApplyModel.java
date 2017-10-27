package com.rd.ifaes.core.tpp.model.ufx;

import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.LoanEnum;
/**
 * 发布项目model
 * @author ctt
 * @version 1.0
 * @date 2016年7月5日上午10:28:11
 * Copyright 杭州融都科技股份有限公司 资金存管系统UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxProjectApplyModel extends UfxBaseModel {
	
	/** 证件类型 - 身份证 */
	public static final String CART_TYPE_ID = "00";

	/**用户类型 - 个人*/
	public static final String USER_TYPE_PERSONAL ="01";
	/**用户类型 - 企业*/
	public static final String USER_TYPE_COMPANY ="02";
	
	/**
	 * 超投控制:00：允许 01：不允许
	 */
	public static final String CTRL_OVER_INVEST_YES = "00";
	public static final String CTRL_OVER_INVEST_NO  = "01";
	
	/**
	 * 项目类型:01：信用 02：抵押 ;03：债权转让 99：其他
	 */
	public static final String PROJECT_TYPE_OTHER  = "99";
	
	/**
	 * 项目产品类型
	 * 01：房贷类 02：车贷类
      03：收益权转让类 04：信用贷款类
      05：股票配资类 06：银行承兑汇票
      07：商业承兑汇票 08：消费贷款类
      09：供应链类 99：其他
	 */
	public static final String PROJECT_PRODUCT_TYPE_OTHER  = "99";
		
	/**
	 * 项目名称:M
	 */
	private String projectName;
	
	/**
	 * 年利率
	 */
	private String apr;
	
	/**
	 * 还款方式
	 * 1：等额本息
	   2：每月还息到期还本
	   3：一次性还款
	   04：其它
	      徽商可选值 01,02,03	      
	   本平台：  1月等额本息 2一次性还款 3 每月还息到期还本 4 等额本金
	 */
	private String repaymentType;
	
	/**
	 * 应还款金额
	 */
	private String repaymentAmount;
	
	/**
	 * 应还款日期
	 */
	private String repaymentTime;
	
	/**
	 * 项目开始时间yyyyMMddhhmmss
	 */
	private String startTime;
	
	/**
	 * 项目结束时间yyyyMMddhhmmss
	 */
	private String endTime;
	
	/**
	 * 项目所在区域代码:M
	 */
	private String projectArea;
	
	/**
	 * 用户类型：01：个人，02：企业
	 */
	private String userType;
	
	/**
	 * 真实姓名或者借款企业名称
	 */
	private String realName;
	
	/**
	 * 发起人手机号
	 */
	private String phone;
	
	/**
	 * 项目用途 :汇付必传
	 */
	private String projectUse;
	
	/**
	 * 商户平台用户唯一标识，如用户名6-25 位半角字符
	 */
	private String userId;
	
	
	/**
	 * 电子账户密码 兴业必传
	 */
	private String payPwd;
	
	/**
	 * 超投控制:00：允许 01：不允许
		只用于联动， 此字段针对某个项目是否需要控制,
		只有商户开通了超投传入此字段才有效
	 */
	private String ctrlOverInvest;
	
	/**
	 * 担保人账户  
	 */
	private String guarantorCustId;
	
	/**
	 * 名义借款人资金账户
	 */
	private String nominalCustId;
	
	/**
	 * 处理流水号 :有对应查询接口的时候使用 ,兴业必传返回监管协议号
	 */
	private String tradeNo;
	
	/**
	 * 监管协议编号
	 */
	private String superviseNo;
	
	/**
	 * 请求参数名称
	 */
	//service+orderNo+
	//ufxCustomerId+businessWay+projectId+projectName+
	//sponsorer+realName+phone+projectUse+projectArea+
	//projectAmount+apr+repaymentType+repaymentAmount+
	//repaymentTime+startTime+endTime+userType+
	//userId+payPwd+reqExt+merPriv+notifyUrl
	
	/**
	 * service+orderNo+
	 * ufxCustomerId+businessWay+projectId+projectName+
	 * sponsorer+realName+phone+projectUse+projectArea+
	 * projectAmount+apr+repaymentType+repaymentAmount+
	 * repaymentTime+startTime+endTime+userType+
	 * userId+payPwd+ctrlOverInvest+guarantorCustId+nominalCustId+
	 * reqExt+merPriv+notifyUr
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "projectName",
			"sponsorer", "realName", "phone", "projectUse", "projectArea", 
			"projectAmount", "apr", "repaymentType", "repaymentAmount", 
			"repaymentTime", "startTime", "endTime", "userType", 
			"userId", "payPwd","ctrlOverInvest", "guarantorCustId", "nominalCustId",//added in 16/7/5
			"reqExt", "merPriv", "notifyUrl", "signInfo" };
	
	/**
	 * 响应参数名称
	 */
	//"tradeNo", "superviseNo",
	private String[] responseParamNames = new String[] { 
			"service", "orderNo", "ufxCustomerId", "businessWay", "respCode", 
			"respDesc", "projectId", "projectName", "sponsorer", "projectArea", 
			"projectAmount", "apr", "repaymentType", "repaymentAmount", "repaymentTime", 
			"startTime", "endTime", "tradeNo", "superviseNo", "reqExt", "merPriv", "signInfo"
			};

	public UfxProjectApplyModel(){
		super();
		this.setService(UfxConstant.PROJECT_APPLY);
		this.setNotifyUrl("/ufx/projectApply/notify.html");
		this.setReturnUrl("/ufx/projectApply/return.html");
	}
	
	/**
	 * 还款类型转换
	 * @param style
	 * @return
	 */
	private String conventRepayStyle(String style){
		if(LoanEnum.STYLE_INSTALLMENT_REPAYMENT.getValue().equals(style)){
			return "01";
		}else if(LoanEnum.STYLE_ONETIME_REPAYMENT.getValue().equals(style)){
			return "03";
		}else if(LoanEnum.STYLE_MONTHLY_INTEREST.getValue().equals(style)){
			return "02";
		}
		return "04";
	}
	
	@Override
	public void response(String resp) {
		super.response(resp);
		JSONObject json = JSONObject.parseObject(resp);
		this.setProjectId(json.getString("projectId"));
	}

	/**  
	 * 获取项目名称  
	 * @return projectName  
	 */
	public String getProjectName() {
		return projectName;
	}

	/**  
	 * 设置项目名称  
	 * @param projectName  
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**  
	 * 获取年利率  
	 * @return apr  
	 */
	public String getApr() {
		return apr;
	}

	/**  
	 * 设置年利率  
	 * @param apr  
	 */
	public void setApr(String apr) {
		this.apr = apr;
	}

	/**  
	 * 获取还款方式  
	 * @return repaymentType  
	 */
	public String getRepaymentType() {
		return repaymentType;
	}

	/**  
	 * 设置还款方式  
	 * @param repaymentType  
	 */
	public void setRepaymentType(String repaymentType) {
		this.repaymentType =conventRepayStyle(repaymentType);
	}

	/**  
	 * 获取应还款金额  
	 * @return repaymentAmount  
	 */
	public String getRepaymentAmount() {
		return repaymentAmount;
	}

	/**  
	 * 设置应还款金额  
	 * @param repaymentAmount  
	 */
	public void setRepaymentAmount(String repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}

	/**  
	 * 获取应还款日期  
	 * @return repaymentTime  
	 */
	public String getRepaymentTime() {
		return repaymentTime;
	}

	/**  
	 * 设置应还款日期  
	 * @param repaymentTime  
	 */
	public void setRepaymentTime(String repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	/**  
	 * 获取项目开始时间yyyyMMddhhmmss  
	 * @return startTime  
	 */
	public String getStartTime() {
		return startTime;
	}

	/**  
	 * 设置项目开始时间yyyyMMddhhmmss  
	 * @param startTime  
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**  
	 * 获取项目结束时间yyyyMMddhhmmss  
	 * @return endTime  
	 */
	public String getEndTime() {
		return endTime;
	}

	/**  
	 * 设置项目结束时间yyyyMMddhhmmss  
	 * @param endTime  
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**  
	 * 获取项目所在地  
	 * @return projectArea  
	 */
	public String getProjectArea() {
		return projectArea;
	}

	/**  
	 * 设置项目所在地  
	 * @param projectArea  
	 */
	public void setProjectArea(String projectArea) {
		this.projectArea = projectArea;
	}


	/**
	 * 获取用户类型
	 * @return userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * 设置用户类型
	 * @param userType
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**  
	 * 获取请求参数名称  
	 * @return requestParamNames  
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**  
	 * 设置请求参数名称  
	 * @param requestParamNames  
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**  
	 * 获取响应参数名称  
	 * @return responseParamNames  
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**  
	 * 设置响应参数名称  
	 * @param responseParamNames  
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProjectUse() {
		return projectUse;
	}

	public void setProjectUse(String projectUse) {
		this.projectUse = projectUse;
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

	public String getCtrlOverInvest() {
		return ctrlOverInvest;
	}

	public void setCtrlOverInvest(String ctrlOverInvest) {
		this.ctrlOverInvest = ctrlOverInvest;
	}

	public String getGuarantorCustId() {
		return guarantorCustId;
	}

	public void setGuarantorCustId(String guarantorCustId) {
		this.guarantorCustId = guarantorCustId;
	}

	public String getNominalCustId() {
		return nominalCustId;
	}

	public void setNominalCustId(String nominalCustId) {
		this.nominalCustId = nominalCustId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getSuperviseNo() {
		return superviseNo;
	}

	public void setSuperviseNo(String superviseNo) {
		this.superviseNo = superviseNo;
	}
	
	
}

package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 企业开户model
 * @author xhf
 * @version 3.0
 * @date 2016年06月16日上午9:56:06
 */
@SuppressWarnings("serial")
public class UfxCompanyRegisterModel extends UfxBaseModel {
	
	/**
	 * 企业开户成功
	 */
	public static final String REG_SUCCESS = "Y";
	
	/**
	 * 企业开户失败
	 */
	public static final String REG_FAIL = "F";

	/**
	 * 企业开户-审核拒绝
	 */
	public static final String REG_REJECT = "R";
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 企业名称
	 */
	private String companyName;
	
	/**
	 * 营业执照号
	 */
	private String bussinessCode;
	
	/**
	 *  担保账户类型   Y:是    N:否
	 */
	private String guarType = "N";
	
	/**
	 * 组织机构代码
	 */
	private String orgCode;
	
	/**
	 * 对公账户
	 */
	private String userAccId;
	
	/**
	 * 密码
	 */
	private String payPwd;
	
	/**
	 * 审核状态 I-- 初始   T-- 提交   P-- 审核中   R-- 审核拒绝   F-- 开户失败    K-- 开户中   Y-- 开户成功
	 */
	private String auditStat;
	
	/**
	 * 审核状态描述
	 */
	private String auditDesc;
	
	/**
	 * 流水号，查询企业开户结果时需要使用 徽商必传
	 */
	private String tradeNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay", 
			"userId", "mobile", "email", "companyName", "bussinessCode", "guarType", "orgCode", "reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId","businessWay","respCode", "respDesc", 
			"userCustId", "userAccId", "auditStat", "auditDesc", "tradeNo", "payPwd", "reqExt", "merPriv", "signInfo" };

	/**
	 * 构造函数
	 */
	public UfxCompanyRegisterModel() {
		super();
		this.setService(UfxConstant.COMPANY_REGISTER);
		this.setReturnUrl("/ufx/companyRegister/return.html");
		this.setNotifyUrl("/ufx/companyRegister/notify.html");
	}
	
	/** 
	 * 获取企业名称
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/** 
	 * 设置企业名称
	 * @param companyName
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	/** 
	 * 获取营业执照号
	 * @return bussinessCode
	 */
	public String getBussinessCode() {
		return bussinessCode;
	}

	/** 
	 * 设置营业执照号
	 * @param bussinessCode
	 */
	public void setBussinessCode(final String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	/** 
	 * 获取页面通知地址
	 * @return guarType
	 */
	public String getGuarType() {
		return guarType;
	}

	/** 
	 * 设置页面通知地址
	 * @param guarType
	 */
	public void setGuarType(final String guarType) {
		this.guarType = guarType;
	}

	/** 
	 * 获取审核状态I--初始T--提交P--审核中R--审核拒绝F--开户失败K--开户中Y--开户成功
	 * @return auditStat
	 */
	public String getAuditStat() {
		return auditStat;
	}

	/** 
	 * 设置审核状态I--初始T--提交P--审核中R--审核拒绝F--开户失败K--开户中Y--开户成功
	 * @param auditStat
	 */
	public void setAuditStat(final String auditStat) {
		this.auditStat = auditStat;
	}

	/** 
	 * 获取审核状态描述
	 * @return auditDesc
	 */
	public String getAuditDesc() {
		return auditDesc;
	}

	/** 
	 * 设置审核状态描述
	 * @param auditDesc
	 */
	public void setAuditDesc(final String auditDesc) {
		this.auditDesc = auditDesc;
	}

	/** 
	 * 获取手机号
	 * @return mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/** 
	 * 设置手机号
	 * @param mobile
	 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取邮箱
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * 设置邮箱
	 */
	public void setEmail(final String email) {
		this.email = email;
	}
	
	/**
	 * 获得组织机构代码
	 * @return
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * 设置组织机构代码
	 * @param orgCode
	 */
	public void setOrgCode(final String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * 获得交易流水号
	 * @return
	 */
	public String getTradeNo() {
		return tradeNo;
	}
    
	/**
	 * 设置交易流水号
	 * @param tradeNo
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
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
	 * 获得对公账户
	 */
	public String getUserAccId() {
		return userAccId;
	}

	/**
	 * 设置对公账号
	 */
	public void setUserAccId(final String userAccId) {
		this.userAccId = userAccId;
	}

	/**
	 * 获得支付密码
	 * @return
	 */
	public String getPayPwd() {
		return payPwd;
	}

	/**
	 * 设置支付密码
	 * @param payPwd
	 */
	public void setPayPwd(final String payPwd) {
		this.payPwd = payPwd;
	}


}

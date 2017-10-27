package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 开户model
 * @author xhf
 * @version 3.0
 * @date 2016年06月16日 下午7:56:39
 */
@SuppressWarnings("serial")
public class UfxRegisterModel extends UfxBaseModel {
		
	/**
	 * 用户ID,必须保证在其平台下唯一性 
	 */
	private String userId;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 用户邮箱
	 */
	private String email;
	
	/**
	 * 身份证号码
	 */
	private String idNo;
	
	/**
	 * 性别   M：男    F：女
	 */
	private String sex;	
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"userId", "realName", "phone", "email", "sex", "idNo", "reqExt", "merPriv", "returnUrl", "notifyUrl", "signInfo" };
	
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userId", "realName", "phone", "email", "idNo", "reqExt", "merPriv","userCustId", "userAccId", "signInfo" };

	public UfxRegisterModel() {
		super();
		this.setService(UfxConstant.REGISTER);
		this.setReturnUrl("/ufx/register/return.html");
		this.setNotifyUrl("/ufx/register/notify.html");
	}
	
	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取真实姓名
	 * 
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名
	 * 
	 * @param realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取手机号码
	 * 
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机号码
	 * 
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取用户邮箱
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置用户邮箱
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取身份证号码
	 * 
	 * @return
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置身份证号码
	 * 
	 * @param idNo
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获得性别
	 * @return
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别   M：男    F：女
	 * @param sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

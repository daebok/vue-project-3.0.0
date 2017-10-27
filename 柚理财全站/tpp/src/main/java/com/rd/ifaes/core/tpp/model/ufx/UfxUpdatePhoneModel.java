package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 修改手机号model
 * @author xhf
 * @version 3.0
 * @date 2016年7月28日 下午2:58:44
 */
@SuppressWarnings("serial")
public class UfxUpdatePhoneModel extends UfxBaseModel {

	/**
	 * 密码标识    0：不使用密码 1：使用消费密码 2：使用查询密码   默认0
	 */
	private String pwdFlag;
	
	/**
	 * 密码，pwdFlag 不为0 时必传
	 */
	private String pwd;
	
	/**
	 * 原手机号，必填
	 */
	private String oldPhone;
	
	/**
	 * 新手机号码，必填
	 */
	private String phone;
	
	/**
	 * 短信验证码，通过sendSms 接口获取 浙商必传
	 */
	private String smsCode;
	
    /**
     * 备注	
     */
	private String remark;
	
	/**
	 * 请求方式： 01 api 请求方式 其它方式不填
	 */
	private String reqWay;
	
	/**********响应***********************/
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 账号对应的证件号码
	 */
	private String idNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "pwdFlag", "pwd", "oldPhone","phone", "remark", "returnUrl", "reqWay", "smsCode", "reqExt", "merPriv", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay","respCode", "respDesc",
			"userCustId", "realName", "oldPhone", "phone", "idNo", "remark", "reqExt", "merPriv", "signInfo" };

	
    /**
     * 构造函数
     */
	public UfxUpdatePhoneModel() {
		super();
		this.setService(UfxConstant.UPDATE_PHONE);
	}

	/**
	 * 获取密码标识0：不使用密码1：使用消费密码2：使用查询密码默认0
	 * @return pwdFlag
	 */
	public String getPwdFlag() {
		return pwdFlag;
	}

	/**
	 * 设置密码标识0：不使用密码1：使用消费密码2：使用查询密码默认0
	 * @param  pwdFlag
	 */
	public void setPwdFlag(String pwdFlag) {
		this.pwdFlag = pwdFlag;
	}

	/**
	 * 获取密码，pwdFlag不为0时必传
	 * @return 密码，pwdFlag不为0时必传
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * 设置密码，pwdFlag不为0时必传
	 * @param  密码，pwdFlag不为0时必传
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * 获取原手机号，必填
	 * @return oldPhone
	 */
	public String getOldPhone() {
		return oldPhone;
	}

	/**
	 * 设置原手机号，必填
	 * @param  oldPhone
	 */
	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	/**
	 * 获取新手机号码，必填
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置新手机号码，必填
	 * @param  phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取短信验证码，通过sendSms接口获取浙商必传
	 * @return smsCode
	 */
	public String getSmsCode() {
		return smsCode;
	}

	/**
	 * 设置短信验证码，通过sendSms接口获取浙商必传
	 * @param  smsCode
	 */
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	/**
	 * 获取备注
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * @param  remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取请求方式：01api请求方式其它方式不填
	 * @return reqWay
	 */
	public String getReqWay() {
		return reqWay;
	}

	/**
	 * 设置请求方式：01api请求方式其它方式不填
	 * @param  reqWay
	 */
	public void setReqWay(String reqWay) {
		this.reqWay = reqWay;
	}

	/**
	 * 获取响应
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置响应
	 * @param  realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 获取账号对应的证件号码
	 * @return idNo
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置账号对应的证件号码
	 * @param  idNo
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取响应
	 * @return respCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * 设置响应
	 * @param  respCode
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * 获取应答返回码中文描述（返回状态为原始订单的处理状态）
	 * @return respDesc
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * 设置应答返回码中文描述（返回状态为原始订单的处理状态）
	 * @param  respDesc
	 */
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
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
	 * @param  requestParamNames
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
	 * @param  responseParamNames
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

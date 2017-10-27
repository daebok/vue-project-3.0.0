package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.2.4密码重置增强
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxPasswordResetPlusModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String idType; //证件类型	A	2	M	01-身份证（18位）
	private String idNo; //证件号码	A	18	M	
	private String name; //姓名	A	60	M	
	private String mobile; //手机号	A	12	M	
	private String retUrl; //前台跳转链接	A	200	C	交易后台跳转的前台URL
	private String notifyUrl; //后台通知链接	A	200	M	【响应参数】会返回到该URL，平台收到后请返回“success”
	private String lastSrvAuthCode; //前导业务授权码	A	60	M	通过请求发送短信验证码接口获取
	private String smsCode; //短信验证码	A	6	M	手机接收到短信验证码

    
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "idType", "idNo", "name",
    		"mobile", "retUrl", "notifyUrl", "lastSrvAuthCode", "smsCode"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId"};

	public JxPasswordResetPlusModel() {
		super();
		super.setTxCode(JxConfig.PASSWORD_RESET_PLUS);
		setRetUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/resetPayPwd/return.html");
		setNotifyUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/resetPayPwd/notify.html");
		setSummitUrl(getBaseUrl() + "/p2p/page/mobile/plus");
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getLastSrvAuthCode() {
		return lastSrvAuthCode;
	}

	public void setLastSrvAuthCode(String lastSrvAuthCode) {
		this.lastSrvAuthCode = lastSrvAuthCode;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String[] getDirectRequestParamNames() {
		return directRequestParamNames;
	}

	public void setDirectRequestParamNames(String[] directRequestParamNames) {
		this.directRequestParamNames = directRequestParamNames;
	}

	public String[] getDirectResponseParamNames() {
		return directResponseParamNames;
	}

	public void setDirectResponseParamNames(String[] directResponseParamNames) {
		this.directResponseParamNames = directResponseParamNames;
	}

}

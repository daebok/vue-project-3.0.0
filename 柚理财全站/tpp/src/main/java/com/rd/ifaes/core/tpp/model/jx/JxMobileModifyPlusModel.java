package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.2.7电子账户手机号修改增强
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxMobileModifyPlusModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String option; //选项	A	1	M	1-修改
	private String mobile; //新手机号	A	12	M	
	private String lastSrvAuthCode; //前导业务授权码	A	60	M	通过请求发送短信验证码接口获取
	private String smsCode; //短信验证码	A	6	M	手机接收到短信验证码

	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "option", "mobile", "lastSrvAuthCode",
    		"smsCode"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId"};
	
    public JxMobileModifyPlusModel() {
		super();
		super.setTxCode(JxConfig.MOBILE_MODIFY_PLUS);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxAutoBidAuthPlusModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String txAmount; //交易金额	N	12,2	M	单笔投标金额的上限
	private String totAmount; //总交易金额	N	12,2	M	自动投标总金额上限（不算已还金额）
	private String forgotPwdUrl; //忘记密码跳转	A	200	C	忘记密码的跳转URL
	private String retUrl; //前台跳转链接	A	200	C	交易后台跳转的前台URL
	private String notifyUrl; //后台通知链接	A	200	M	【响应参数】会返回到该URL，平台收到后请返回“success”
	private String lastSrvAuthCode; //前导业务授权码	A	60	M	通过请求发送短信验证码接口获取
	private String smsCode; //短信验证码	A	6	M	手机接收到短信验证码
	
	private String name; //姓名	A	60	C	
    
	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "totAmount", 
    		"forgotPwdUrl", "retUrl", "notifyUrl", "lastSrvAuthCode", "smsCode"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "txAmount", "name"};

	public JxAutoBidAuthPlusModel() {
		super();
		super.setTxCode(JxConfig.AUTO_BID_AUTH_PLUS);
		setSummitUrl(getBaseUrl() + "/p2p/page/mobile/plus");
		setRetUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/autoBidAuthPlus/return.html");
		setForgotPwdUrl(ConfigUtils.getValue("notify_url")+ "/member/security/modifyTransactionPwd.html");
		setNotifyUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/autoBidAuthPlus/notify.html");
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getTotAmount() {
		return totAmount;
	}

	public void setTotAmount(String totAmount) {
		this.totAmount = totAmount;
	}

	public String getForgotPwdUrl() {
		return forgotPwdUrl;
	}

	public void setForgotPwdUrl(String forgotPwdUrl) {
		this.forgotPwdUrl = forgotPwdUrl;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

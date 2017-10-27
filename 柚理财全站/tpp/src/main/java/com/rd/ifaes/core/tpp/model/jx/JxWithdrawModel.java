package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.5.1提现
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxWithdrawModel extends JxBaseModel {

	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String idType; //证件类型	A	2	M	01-身份证（18位）
	private String idNo; //证件号码	A	18	M	
	private String name; //姓名	A	60	M	
	private String mobile; //手机号	A	12	M	
	private String cardNo; //银行卡号	A	19	M	绑定银行卡号
	private String txAmount; //交易金额	N	12,2	M	提现金额
	private String txFee; //手续费	N	12,2	M	提现手续费
	private String routeCode; //路由代码	A	1	C	0-本行通道1-银联通2-人行通道空-自动选择
	private String cardBankCnaps; //绑定银行联行号	A	20	C	人民银行分配的12位联行号routeCode=2，必输
	private String cardBankCode; //绑定银行代码	A	20	C	绑定的银行卡对应的银行代码
	private String cardBankNameCn; //绑定银行中文名称	A	50	C	绑定的银行卡对应的银行中文名称
	private String cardBankNameEn; //绑定银行英文名称	A	20	C	绑定的银行卡对应的银行英文名称缩写
	private String cardBankProvince; //绑定银行卡开户省份	A	20	C	绑定的银行卡的开户省份
	private String cardBankCity; //绑定银行卡开户城市	A	50	C	绑定的银行卡的开户城市
	private String forgotPwdUrl; //忘记密码跳转	A	200	M	忘记密码的跳转URL
	private String retUrl; //前台跳转链接	A	200	C	交易后台跳转的前台URL
	private String notifyUrl; //后台通知链接	A	200	M	【响应参数】会返回到该URL，平台收到后请返回“success”

	
//	private String accountId; //电子账号	A	19	M	存管平台分配的账号
//	private String txAmount; //交易金额	N	12,2	M	提现金额
	
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "idType", "idNo", "name",
    		"mobile", "cardNo", "txAmount", "txFee", "routeCode", "cardBankCnaps", "cardBankCode", "cardBankNameCn",
    		"cardBankNameEn", "cardBankProvince", "cardBankCity", "forgotPwdUrl", "retUrl", "notifyUrl"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "txAmount"};

	public JxWithdrawModel() {
		super();
		super.setTxCode(JxConfig.WITHDRAW);
		setRetUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/cash/return.html");
		setForgotPwdUrl(ConfigUtils.getValue("notify_url")+ "/member/security/modifyTransactionPwd.html");
		setNotifyUrl(ConfigUtils.getValue("notify_url")+ "/jxbank/cash/notify.html");
		setSummitUrl(getBaseUrl() + "/p2p/page/withdraw");
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxFee() {
		return txFee;
	}

	public void setTxFee(String txFee) {
		this.txFee = txFee;
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public String getCardBankCnaps() {
		return cardBankCnaps;
	}

	public void setCardBankCnaps(String cardBankCnaps) {
		this.cardBankCnaps = cardBankCnaps;
	}

	public String getCardBankCode() {
		return cardBankCode;
	}

	public void setCardBankCode(String cardBankCode) {
		this.cardBankCode = cardBankCode;
	}

	public String getCardBankNameCn() {
		return cardBankNameCn;
	}

	public void setCardBankNameCn(String cardBankNameCn) {
		this.cardBankNameCn = cardBankNameCn;
	}

	public String getCardBankNameEn() {
		return cardBankNameEn;
	}

	public void setCardBankNameEn(String cardBankNameEn) {
		this.cardBankNameEn = cardBankNameEn;
	}

	public String getCardBankProvince() {
		return cardBankProvince;
	}

	public void setCardBankProvince(String cardBankProvince) {
		this.cardBankProvince = cardBankProvince;
	}

	public String getCardBankCity() {
		return cardBankCity;
	}

	public void setCardBankCity(String cardBankCity) {
		this.cardBankCity = cardBankCity;
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

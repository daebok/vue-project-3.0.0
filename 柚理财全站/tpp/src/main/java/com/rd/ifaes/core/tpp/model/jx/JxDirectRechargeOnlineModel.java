package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.3.1联机绑定卡到电子账户充值
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxDirectRechargeOnlineModel extends JxBaseModel {

	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String idType; //证件类型	A	2	M	01-身份证（18位）
	private String idNo; //证件号码	A	18	M	
	private String name; //姓名	A	60	M	
	private String mobile; //手机号	A	12	M	
	private String cardNo; //银行卡号	A	19	M	绑定的银行卡号
	private String txAmount; //交易金额	N	12,2	M	转账金额
	private String currency; //交易币种	A	3	M	156-人民币
	private String cardBankCode; //绑定银行代码	A	20	C	绑定的银行卡对应的银行代码，备用
	private String cardBankNameCn; //绑定银行中文名称	A	50	C	绑定的银行卡对应的银行中文名称，备用
	private String cardBankNameEn; //绑定银行英文名称	A	20	C	绑定的银行卡对应的银行英文名称缩写，备用
	private String cardBankProvince; //绑定银行卡开户省份	A	20	C	绑定的银行卡的开户省份，备用
	private String cardBankCity; //绑定银行卡开户城市	A	50	C	绑定的银行卡的开户城市，备用
	private String callBackAdrress; //回调地址	A	256	C	充值结果通知地址,p2p使用
	private String smsCode; //短信验证码	A	8	C	充值时短信验证,p2p使用
	private String smsSeq; //短信序列号	A	4	C	充值时短信验证,p2p使用
	private String userIP; //客户IP	A	32	C	

//	private String accountId; //电子账号	A	19	M	同请求
//	private String currency; //交易币种	A	3	M	156-人民币
//	private String txAmount; //交易金额	N	12,2	M	同请求
	
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "idType", "idNo", "name",
    		"mobile", "cardNo", "txAmount", "currency", "cardBankCode", "cardBankNameCn", 
    		"cardBankNameEn", "cardBankProvince", "cardBankCity", "callBackAdrress", "smsCode", "smsSeq", "userIP"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "currency", "txAmount"};

	public JxDirectRechargeOnlineModel() {
		super();
		super.setTxCode(JxConfig.DIRECT_RECHARGE_ONLINE);
	}
	
	@Override
	public void validResult(String retCode, String retMsg){
        if(!JxConfig.SUCCESS.equals(retCode)){
        	LOGGER.error("(P2P-->即信端)返回失败...{}", retMsg);
//        	throw new JxbankException(retMsg, BussinessException.TYPE_JSON);
        }
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getCallBackAdrress() {
		return callBackAdrress;
	}

	public void setCallBackAdrress(String callBackAdrress) {
		this.callBackAdrress = callBackAdrress;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getSmsSeq() {
		return smsSeq;
	}

	public void setSmsSeq(String smsSeq) {
		this.smsSeq = smsSeq;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
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

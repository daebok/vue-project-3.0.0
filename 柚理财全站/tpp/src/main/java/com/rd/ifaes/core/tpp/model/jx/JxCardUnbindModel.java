package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.2.6解绑银行卡
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxCardUnbindModel extends JxBaseModel {
	
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String idType; //证件类型	A	2	M	01-身份证（18位）
	private String idNo; //证件号码	A	18	M	
	private String name; //姓名	A	60	M	
	private String mobile; //手机号	A	12	M	
	private String cardNo; //银行卡号	A	19	M	绑定银行卡号
	
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "idType", "idNo", "name",
    		"mobile", "cardNo"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId"};

	public JxCardUnbindModel() {
		super();
		super.setTxCode(JxConfig.CARD_UNBIND);
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

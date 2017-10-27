package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxPasswordSetQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	C	存管平台分配的账号
	
	private String pinFlag; //是否设置过密码	A	1	C	0-无密码 1-有密码

	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "pinFlag"};

	public JxPasswordSetQueryModel() {
		super();
		super.setTxCode(JxConfig.PASSWORD_SET_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPinFlag() {
		return pinFlag;
	}

	public void setPinFlag(String pinFlag) {
		this.pinFlag = pinFlag;
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

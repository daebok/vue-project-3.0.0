package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxCreditInvestQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orgOrderId; //原订单号	A	30	M	原投标订单号
	
	private String name; //姓名	A	60	C	买入方姓名
	private String forAccountId; //对手电子账号	A	19	C	卖出方账号
	private String forName; //对手姓名	A	60	C	卖出方姓名
	private String tsfAmount; //转让金额	N	12,2	C	卖出的债权金额
	private String txAmount; //转让价格	N	12,2	C	成交价格，买入方实际付出金额
	private String availAmount; //剩余可转让金额	N	12,2	C	
	private String txFee; //转让手续费	N	12,2	C	向卖出方收取的手续费
	private String txIncome; //转让所得	N	12,2	C	转让所得=转让价格-转让手续费
	private String authCode; //授权码	A	20	C
	
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orgOrderId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "name", "forAccountId", "forName", 
    		"tsfAmount", "txAmount", "availAmount", "txFee", "txIncome", "authCode"};

	public JxCreditInvestQueryModel() {
		super();
		super.setTxCode(JxConfig.CREDIT_INVEST_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOrgOrderId() {
		return orgOrderId;
	}

	public void setOrgOrderId(String orgOrderId) {
		this.orgOrderId = orgOrderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getForAccountId() {
		return forAccountId;
	}

	public void setForAccountId(String forAccountId) {
		this.forAccountId = forAccountId;
	}

	public String getForName() {
		return forName;
	}

	public void setForName(String forName) {
		this.forName = forName;
	}

	public String getTsfAmount() {
		return tsfAmount;
	}

	public void setTsfAmount(String tsfAmount) {
		this.tsfAmount = tsfAmount;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getAvailAmount() {
		return availAmount;
	}

	public void setAvailAmount(String availAmount) {
		this.availAmount = availAmount;
	}

	public String getTxFee() {
		return txFee;
	}

	public void setTxFee(String txFee) {
		this.txFee = txFee;
	}

	public String getTxIncome() {
		return txIncome;
	}

	public void setTxIncome(String txIncome) {
		this.txIncome = txIncome;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
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

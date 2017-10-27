package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxCreditAuthQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	C	存管平台分配的账号
	
	private String type; //查询类型	A	1	M	1 自动投标签约 2 自动债转签约
	
	private String state; //签约状态	A	1	C	0：未签约1：已签约
	private String orderId; //签约订单号	A	30	C	
	private String txnDate; //签约日期	A	8	C	
	private String txnTime; //签约时间	A	6	C	
	private String txAmount; //交易金额	N	12,2	C	仅type为1时有效
	private String totAmount; //总交易金额	N	12,2	C	仅type为1时有效

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "type"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "type", "state", "orderId", 
    		"txnDate", "txnTime", "txAmount", "totAmount"};

	public JxCreditAuthQueryModel() {
		super();
		super.setTxCode(JxConfig.CREDIT_AUTH_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
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
	
	@Override
	public void validResult(String retCode, String retMsg){
        if(!JxConfig.SUCCESS.equals(retCode)){
        	LOGGER.error("(P2P-->即信端签约查询返回失败...retcode:{},retMsg:{}", retCode,retMsg);
        }
    }
}

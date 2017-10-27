package com.rd.ifaes.core.tpp.model.jx.json;

public class JxRepayBailSubPacksModel {
	private String accountId; //电子账号	A	19	M	融资人电子账号
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String txAmount; //交易金额	N	12,2	M	投标的金额
	private String intAmount; //交易利息	N	12,2	M	
	private String txFeeOut; //还款手续费	N	12,2	C	向融资人收取的手续费
	private String forAccountId; //对手电子账号	A	19	M	投资人账号
	private String orgOrderId; //原订单号	A	30	M	担保账户垫付时的原订单号
	private String authCode; //授权码	A	20	M	投资人投标成功的授权号
	
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
	public String getIntAmount() {
		return intAmount;
	}
	public void setIntAmount(String intAmount) {
		this.intAmount = intAmount;
	}
	public String getTxFeeOut() {
		return txFeeOut;
	}
	public void setTxFeeOut(String txFeeOut) {
		this.txFeeOut = txFeeOut;
	}
	public String getForAccountId() {
		return forAccountId;
	}
	public void setForAccountId(String forAccountId) {
		this.forAccountId = forAccountId;
	}
	public String getOrgOrderId() {
		return orgOrderId;
	}
	public void setOrgOrderId(String orgOrderId) {
		this.orgOrderId = orgOrderId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	
}

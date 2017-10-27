package com.rd.ifaes.core.tpp.model.jx.json;

public class JxBailRepaySubPacksModel {
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String txAmount; //交易金额	N	12,2	M	
	private String txCapAmout; //交易本金	N	12,2	M	垫付本金，本金不能为0
	private String txIntAmount; //交易利息	N	12,2	C	垫付利息
	private String txFee; //手续费	N	12,2	C	向债权卖出方收取手续费
	private String forAccountId; //对手电子账号	A	19	M	投资人电子账户
	private String orgOrderId; //原订单号	A	30	M	投资人投标的原订单号
	private String orgTxAmount; //原交易金额	N	12,2	M	投资人投标的原订单总金额
	
	private String authCode; //授权码	A	20	C	交易成功生成的买入方授权码

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

	public String getTxCapAmout() {
		return txCapAmout;
	}

	public void setTxCapAmout(String txCapAmout) {
		this.txCapAmout = txCapAmout;
	}

	public String getTxIntAmount() {
		return txIntAmount;
	}

	public void setTxIntAmount(String txIntAmount) {
		this.txIntAmount = txIntAmount;
	}

	public String getTxFee() {
		return txFee;
	}

	public void setTxFee(String txFee) {
		this.txFee = txFee;
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

	public String getOrgTxAmount() {
		return orgTxAmount;
	}

	public void setOrgTxAmount(String orgTxAmount) {
		this.orgTxAmount = orgTxAmount;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	
}

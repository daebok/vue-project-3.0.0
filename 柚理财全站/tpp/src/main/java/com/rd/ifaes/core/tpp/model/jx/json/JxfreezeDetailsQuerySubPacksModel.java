package com.rd.ifaes.core.tpp.model.jx.json;

public class JxfreezeDetailsQuerySubPacksModel {

	private String buyDate; //冻结日期	A	8	C	YYYYMMDD
	private String orderId; //订单号	A	30	C	冻结订单号
	private String txAmount; //交易金额	N	12,2	C	冻结金额
	
	private String state; //状态	A	1	C	1-冻结9-解冻

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}

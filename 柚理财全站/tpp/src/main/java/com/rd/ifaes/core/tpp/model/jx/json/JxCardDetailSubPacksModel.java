package com.rd.ifaes.core.tpp.model.jx.json;

public class JxCardDetailSubPacksModel {
	private String cardNo; //银行卡号	A	19	C	曾经绑定的银行卡
	private String txnDate; //绑定日期	A	8	C	YYYYMMDD
	private String txnTime; //绑定时间	A	6	C	hhmmss
	private String canclDate; //解绑日期	A	8	C	YYYYMMDD
	private String canclTime; //解绑时间	A	6	C	hhmmss
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	public String getCanclDate() {
		return canclDate;
	}
	public void setCanclDate(String canclDate) {
		this.canclDate = canclDate;
	}
	public String getCanclTime() {
		return canclTime;
	}
	public void setCanclTime(String canclTime) {
		this.canclTime = canclTime;
	}
	
}

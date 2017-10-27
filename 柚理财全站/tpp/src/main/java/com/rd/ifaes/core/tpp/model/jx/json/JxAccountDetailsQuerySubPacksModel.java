package com.rd.ifaes.core.tpp.model.jx.json;

public class JxAccountDetailsQuerySubPacksModel {
	private String accDate; //入账日期	A	8	C	YYYYMMDD
	private String inpDate; //交易日期	A	8	C	YYYYMMDD
	private String relDate; //自然日期	A	8	C	YYYYMMDD
	private String inpTime; //交易时间	A	8	C	HH24MISSTT
	private String traceNo; //流水号	N	6	C	该字段不足6位时，请平台自行前补0
	private String accountId; //电子账号	A	19	C	
	private String tranType; //交易类型	A	4	C	后台交易类型
	private String orFlag; //冲正撤销标志	A	1	C	O-原始交易 R-已经冲正或者撤销
	private String txAmount; //交易金额	N	12,2	C	
	private String txFlag; //交易金额符号	A	1	C	+/-
	private String describe; //交易描述	A	42	C	
	private String currency; //货币代码	A	3	C	156-人民币
	private String currBal; //交易后余额	N	12,2	C	该笔交易后账户余额
	
	private String forAccountId; //对手电子账号	A	19	C

	public String getAccDate() {
		return accDate;
	}

	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}

	public String getInpDate() {
		return inpDate;
	}

	public void setInpDate(String inpDate) {
		this.inpDate = inpDate;
	}

	public String getRelDate() {
		return relDate;
	}

	public void setRelDate(String relDate) {
		this.relDate = relDate;
	}

	public String getInpTime() {
		return inpTime;
	}

	public void setInpTime(String inpTime) {
		this.inpTime = inpTime;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getOrFlag() {
		return orFlag;
	}

	public void setOrFlag(String orFlag) {
		this.orFlag = orFlag;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxFlag() {
		return txFlag;
	}

	public void setTxFlag(String txFlag) {
		this.txFlag = txFlag;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrBal() {
		return currBal;
	}

	public void setCurrBal(String currBal) {
		this.currBal = currBal;
	}

	public String getForAccountId() {
		return forAccountId;
	}

	public void setForAccountId(String forAccountId) {
		this.forAccountId = forAccountId;
	}
	
	

}

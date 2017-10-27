package com.rd.ifaes.core.tpp.model.jx.json;

public class JxCreditDetailsQuerySubPacksModel {

	private String productId; //标的号	A	40	C	标的号不区分大小写；
	private String buyDate; //投标日期	A	8	C	YYYYMMDD
	private String orderId; //订单号	A	30	C	
	private String txAmount; //交易金额	N	12,2	C	
	private String yield; //预期年化收益率	N	8,5	C	
	private String forIncome; //预期收益	N	12,2	C	
	private String intTotal; //预期本息收益	N	12,2	C	
	private String income; //实际收益	N	12,2	C	
	private String incFlag; //实际收益符号	A	1	C	+/-
	private String endDate; //到期日	A	8	C	YYYYMMDD
	
	private String state; //状态	A	1	C	1-投标中2-计息中4-本息已返回8-审核中9-已撤销

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

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

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getForIncome() {
		return forIncome;
	}

	public void setForIncome(String forIncome) {
		this.forIncome = forIncome;
	}

	public String getIntTotal() {
		return intTotal;
	}

	public void setIntTotal(String intTotal) {
		this.intTotal = intTotal;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getIncFlag() {
		return incFlag;
	}

	public void setIncFlag(String incFlag) {
		this.incFlag = incFlag;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}

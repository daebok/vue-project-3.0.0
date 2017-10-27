package com.rd.ifaes.core.tpp.model.jx.json;

public class JxdebtDetailsQuerySubPacksModel {
	private String productId; //标的号	A	40	M	标的号不区分大小写；
	private String raiseDate; //募集日	A	8	M	YYYYMMDD
	private String raiseEndDate; //募集结束日期	A	8	M	YYYYMMDD募集期不得超过规定工作日，超过结束日期未能满标，标的失效，不能再投标或满标，但需要P2P主动发起撤销所有投标
	private String intType; //付息方式	A	1	M	0-到期与本金一起归还1-每月固定日期支付2-每月不确定日期支付平台仅记录
	private String intPayDay; //利息每月支付日	A	2	C	DD付息方式为1时必填；若设置日期大于月份最后一天时，则为该月最后一天支付平台仅记录
	private String duration; //借款期限	N	4	M	天数，从满标日期开始计算
	private String txAmount; //交易金额	N	12,2	M	借款金额
	private String rate; //年化利率	N	8,5	M	百分数
	private String fee; //平台手续费	N	12,2	C	平台收取的总手续费
	private String bailaccountId; //担保账户	A	19	C	担保电子账号
	private String intDate; //起息日	A	8	C	YYYYMMDD
	private String raiseAmount; //募集金额	N	12,2	C	募集总金额
	private String repaymentAmt; //已还金额	N	12,2	C	标的已还本金
	private String repaymentInt; //已还利息	N	12,2	C	标的已还利息
	private String state; //状态	A	1	C	1-投标中2-计息中3-到期待返还4-本息已返还8-审核中9-已撤销
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getRaiseDate() {
		return raiseDate;
	}
	public void setRaiseDate(String raiseDate) {
		this.raiseDate = raiseDate;
	}
	public String getRaiseEndDate() {
		return raiseEndDate;
	}
	public void setRaiseEndDate(String raiseEndDate) {
		this.raiseEndDate = raiseEndDate;
	}
	public String getIntType() {
		return intType;
	}
	public void setIntType(String intType) {
		this.intType = intType;
	}
	public String getIntPayDay() {
		return intPayDay;
	}
	public void setIntPayDay(String intPayDay) {
		this.intPayDay = intPayDay;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getBailaccountId() {
		return bailaccountId;
	}
	public void setBailaccountId(String bailaccountId) {
		this.bailaccountId = bailaccountId;
	}
	public String getIntDate() {
		return intDate;
	}
	public void setIntDate(String intDate) {
		this.intDate = intDate;
	}
	public String getRaiseAmount() {
		return raiseAmount;
	}
	public void setRaiseAmount(String raiseAmount) {
		this.raiseAmount = raiseAmount;
	}
	public String getRepaymentAmt() {
		return repaymentAmt;
	}
	public void setRepaymentAmt(String repaymentAmt) {
		this.repaymentAmt = repaymentAmt;
	}
	public String getRepaymentInt() {
		return repaymentInt;
	}
	public void setRepaymentInt(String repaymentInt) {
		this.repaymentInt = repaymentInt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	
}

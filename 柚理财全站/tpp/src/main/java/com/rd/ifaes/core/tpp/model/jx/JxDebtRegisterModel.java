package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.4.1借款人标的登记
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxDebtRegisterModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String productId; //标的号	A	40	M	由P2P生成，必须保证唯一
	private String productDesc; //标的描述	A	60	M	标的描述
	private String raiseDate; //募集日	A	8	M	YYYYMMDD
	private String raiseEndDate; //募集结束日期	A	8	M	YYYYMMDD募集期不得超过规定工作日，超过结束日期未能满标，标的失效，不能再投标或满标，但需要P2P主动发起撤销所有投标
	private String intType; //付息方式	A	1	M	0-到期与本金一起归还1-每月固定日期支付2-每月不确定日期支付
	private String intPayDay; //利息每月支付日	A	2	C	DD付息方式为1时必填；若设置日期大于月份最后一天时，则为该月最后一天支付平台仅记录
	private String duration; //借款期限	N	4	M	天数，从满标日期开始计算
	private String txAmount; //交易金额	N	12,2	M	借款金额
	private String rate; //年化利率	N	8,5	M	百分数
	private String txFee; //平台手续费	N	12,2	C	平台收取的总手续费
	private String bailAccountId; //担保账户	A	19	C	担保电子账号
	private String nominalAccountId; //名义借款人电子帐号	A	19	C	名义借款人电子账号 

	
	private String name; //姓名	A	60	C	
	private String nominalAcctountId; //名义借款人电子帐号	A	19	C	名义借款人电子账号
	private String receiptAccountId; //收款人电子帐户	A	19	C	暂时不使用

	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "productId", "productDesc", "raiseDate",
    		"raiseEndDate", "intType", "intPayDay", "duration", "txAmount", "rate", "txFee", "bailAccountId", "nominalAccountId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "productId", "productDesc", "txAmount", 
    		"name", "nominalAcctountId", "receiptAccountId"};

	public JxDebtRegisterModel() {
		super();
		super.setTxCode(JxConfig.DEBT_REGISTER);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
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

	public String getTxFee() {
		return txFee;
	}

	public void setTxFee(String txFee) {
		this.txFee = txFee;
	}

	public String getBailAccountId() {
		return bailAccountId;
	}

	public void setBailAccountId(String bailAccountId) {
		this.bailAccountId = bailAccountId;
	}

	public String getNominalAccountId() {
		return nominalAccountId;
	}

	public void setNominalAccountId(String nominalAccountId) {
		this.nominalAccountId = nominalAccountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNominalAcctountId() {
		return nominalAcctountId;
	}

	public void setNominalAcctountId(String nominalAcctountId) {
		this.nominalAcctountId = nominalAcctountId;
	}

	public String getReceiptAccountId() {
		return receiptAccountId;
	}

	public void setReceiptAccountId(String receiptAccountId) {
		this.receiptAccountId = receiptAccountId;
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxBidApplyQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orgOrderId; //原订单号	A	30	M	原投标订单号
	
	private String name; //姓名	A	60	C	买入方姓名
	private String productId; //标的号	A	40	C	标的号不区分大小写；
	private String txAmount; //投标金额	N	12,2	C	
	private String forIncome; //预期收益	N	12,2	C	
	private String buyDate; //投标日期	A	8	C	YYYYMMDDstate	状态	A	1	C	1：投标中； 2：计息中；4：本息已返还；9：已撤销；
	private String authCode; //授权码	A	20	C	
	private String bonusAmount; //抵扣红包金额	N	9,2	C	
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orgOrderId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "name", "productId", "txAmount", 
    		"forIncome", "buyDate", "authCode", "bonusAmount"};

	public JxBidApplyQueryModel() {
		super();
		super.setTxCode(JxConfig.BID_APPLY_QUERY);
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getForIncome() {
		return forIncome;
	}

	public void setForIncome(String forIncome) {
		this.forIncome = forIncome;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(String bonusAmount) {
		this.bonusAmount = bonusAmount;
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxBidAutoApplyModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String txAmount; //交易金额	N	12,2	M	投标金额
	private String productId; //标的号	A	40	M	标的号
	private String frzFlag; //是否冻结金额	A	1	M	0-不冻结1-冻结 为避免放款失败，应填1
	private String contOrderId; //签约订单号	A	30	M	自动投标签约订单号

	private String name; //姓名	A	60	C	
	private String authCode; //授权码	A	20	C	债权授权码
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "productId", "frzFlag", "contOrderId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "txAmount", "productId", "name", "authCode"};

	public JxBidAutoApplyModel() {
		super();
        this.frzFlag = "1";
        this.orderId = OrderNoUtils.getSerialNumber();
		super.setTxCode(JxConfig.BID_AUTO_APPLY);
	}

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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getFrzFlag() {
		return frzFlag;
	}

	public void setFrzFlag(String frzFlag) {
		this.frzFlag = frzFlag;
	}

	public String getContOrderId() {
		return contOrderId;
	}

	public void setContOrderId(String contOrderId) {
		this.contOrderId = contOrderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
}

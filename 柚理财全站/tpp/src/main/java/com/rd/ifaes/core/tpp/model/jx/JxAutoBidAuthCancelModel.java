package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxAutoBidAuthCancelModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String orgOrderId; //原订单号	A	30	M	原签约订单号

	private String name; //姓名	A	60	C	
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "orgOrderId",};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "name"};

	public JxAutoBidAuthCancelModel() {
		super();
		super.setTxCode(JxConfig.AUTO_BID_AUTH_CANCEL);
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

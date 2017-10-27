package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxAutoCreditInvestAuthCancelModel extends JxBaseModel {
	
    private String accountId; //电子账号	A	19	M	存管平台分配的账号
    private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
    private String orgOrderId; //原签约订单号

    /**
     * 请求参数
     */
    

    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "orgOrderId"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId"};

    public JxAutoCreditInvestAuthCancelModel() {
        super();
        super.setTxCode(JxConfig.AUTO_CREDIT_INVEST_AUTH_CANCEL);
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.6.2还款申请撤销资金解冻
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxBalanceUnfreezeModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orderId; //订单号	A	30	M	由P2P生成，必须保证唯一
	private String txAmount; //交易金额	N	12,2	M	冻结金额
	private String orgOrderId; //原订单号	A	30	M	原冻结订单号

	private String name; //姓名	A	60	C	
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orderId", "txAmount", "orgOrderId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "orderId", "txAmount", "name"};

	public JxBalanceUnfreezeModel() {
		super();
		super.setTxCode(JxConfig.BALANCE_UNFREEZE);
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

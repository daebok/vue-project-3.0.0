package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.4.2借款人标的撤销
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxDebtRegisterCancelModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String productId; //标的号	A	40	M	由P2P生成，必须保证唯一
	private String raiseDate; //募集日	A	8	M	YYYYMMDD

	private String txAmount; //交易金额	N	12,2	M	借款金额
	private String name; //姓名	A	60	C	
	private String state;//标的状态	A	1	C	1-投标中2-计息中3-到期待返还4-本息已返还9-已撤销12-标的登记失败

	public JxDebtRegisterCancelModel() {
		super();
		super.setTxCode(JxConfig.DEBT_REGISTER_CANCEL);
	} 

	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "productId", "raiseDate"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "productId", "txAmount", "name", "state"};

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
	public String getRaiseDate() {
		return raiseDate;
	}
	public void setRaiseDate(String raiseDate) {
		this.raiseDate = raiseDate;
	}
	public String getTxAmount() {
		return txAmount;
	}
	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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

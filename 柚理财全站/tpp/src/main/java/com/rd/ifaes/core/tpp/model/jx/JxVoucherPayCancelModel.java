package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.5.4红包发放撤销
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxVoucherPayCancelModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String txAmount; //交易金额	N	12,2	M	红包金额
	private String forAccountId; //对手电子账号	A	19	M	接收方账号
	private String orgTxDate; //原交易日期	A	8	M	
	private String orgTxTime; //原交易时间	A	6	M	
	private String orgSeqNo; //原交易流水号	N	6	M	

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "txAmount", "forAccountId", "orgTxDate", "orgTxTime", "orgSeqNo"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId","txAmount"};

	public JxVoucherPayCancelModel() {
		super();
		super.setTxCode(JxConfig.VOUCHER_PAY_CANCEL);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getForAccountId() {
		return forAccountId;
	}

	public void setForAccountId(String forAccountId) {
		this.forAccountId = forAccountId;
	}

	public String getOrgTxDate() {
		return orgTxDate;
	}

	public void setOrgTxDate(String orgTxDate) {
		this.orgTxDate = orgTxDate;
	}

	public String getOrgTxTime() {
		return orgTxTime;
	}

	public void setOrgTxTime(String orgTxTime) {
		this.orgTxTime = orgTxTime;
	}

	public String getOrgSeqNo() {
		return orgSeqNo;
	}

	public void setOrgSeqNo(String orgSeqNo) {
		this.orgSeqNo = orgSeqNo;
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

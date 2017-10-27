package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.5.5红包发放隔日撤销
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxVoucherPayDelayCancelModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String txAmount; //交易金额	N	12,2	M	红包金额
	private String forAccountId; //对手电子账号	A	19	M	接收方账号
	private String orgTxDate; //原交易日期	A	8	M	
	private String orgTxTime; //原交易时间	A	6	M	
	private String orgSeqNo; //原交易流水号	N	6	M	
	private String desLineFlag; //是否使用交易描述	A	1	M	0：不使用 1：使用，为空或不传默认不使用
	private String desLine; //交易描述	A	40	C	根据是否填写描述进行传递

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "txAmount", "forAccountId", "orgTxDate", 
    		"orgTxTime", "orgSeqNo", "desLineFlag", "desLine"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId","txAmount"};

	public JxVoucherPayDelayCancelModel() {
		super();
		super.setTxCode(JxConfig.VOUCHER_PAY_DELAY_CANCEL);
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

	public String getDesLineFlag() {
		return desLineFlag;
	}

	public void setDesLineFlag(String desLineFlag) {
		this.desLineFlag = desLineFlag;
	}

	public String getDesLine() {
		return desLine;
	}

	public void setDesLine(String desLine) {
		this.desLine = desLine;
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

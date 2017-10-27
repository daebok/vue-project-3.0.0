package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxFundTransQueryModel extends JxBaseModel {

	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String orgTxDate; //原交易日期	A	8	M	YYYYMMDD
	private String orgTxTime; //原交易时间	A	6	M	hhmmss
	private String orgSeqNo; //原交易流水号	A	6	M	定长6位

	private String retCode; //响应代码	A	8	M	
	private String retMsg; //响应描述	A	60	M	
	private String name; //姓名	A	60	C	
	private String txAmount; //交易金额	N	12,2	C	
	private String orFlag; //冲正撤销标志	A	1	C	0:正常   1：已冲正/撤销
	private String result; //交易处理结果	A	8	M	00：成功；其它：无该交易或者处理失败；

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "orgTxDate", "orgTxTime", "orgSeqNo"};
   
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"retCode", "retMsg", "accountId", "name", 
    		"txAmount", "orFlag", "result"};
	
    public void validResult(String retCode, String retMsg) {
        if (!JxConfig.SUCCESS.equals(retCode)) {
            LOGGER.error("(即信端------->P2P)返回失败...{}", retMsg);
//            throw new JxbankException(retMsg, BussinessException.TYPE_JSON);
        }
    }
    
    public JxFundTransQueryModel() {
		super();
		super.setTxCode(JxConfig.FUND_TRANS_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getOrFlag() {
		return orFlag;
	}

	public void setOrFlag(String orFlag) {
		this.orFlag = orFlag;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

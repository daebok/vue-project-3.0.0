package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxBatchQueryModel extends JxBaseModel {
	private String batchTxDate; //批次交易日期	A	8	M	YYYYMMDD
	private String batchNo; //批次号	N	6	M	

	private String batchTxCode; //批次交易代码	A	50	C	批次号对应的交易代码
	private String relAmount; //批次处理金额	N	12,2	C	请求金额数（批次结束债权为空）
	private String relCounts; //批次处理笔数	N	6	C	请求交易笔数
	private String batchState; //批次处理状态	A	1	M	待处理    --A处理中    --D处理结束  --S处理失败  --F已撤销    --C
	private String failMsg; //失败描述	A	60	C	当batchState=F时必填
	private String sucCounts; //成功笔数	N	6	C	当batchState=S时必填
	private String sucAmount; //成功金额	N	12,2	C 当batchState=S时必填
	private String failCounts; //失败笔数	N	6	C当batchState=S时必填
	private String failAmount; //失败金额	N	12,2	C 当batchState=S时必填

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"batchTxDate", "batchNo"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"batchTxDate", "batchNo", "batchTxCode", "relAmount", 
    		"relCounts", "batchState", "failMsg", "sucCounts", "sucAmount", "failCounts", "failAmount"};

    public JxBatchQueryModel(){
        super();
        super.setTxCode(JxConfig.BATCH_QUERY);
    }

	public String getBatchTxDate() {
		return batchTxDate;
	}

	public void setBatchTxDate(String batchTxDate) {
		this.batchTxDate = batchTxDate;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchTxCode() {
		return batchTxCode;
	}

	public void setBatchTxCode(String batchTxCode) {
		this.batchTxCode = batchTxCode;
	}

	public String getRelAmount() {
		return relAmount;
	}

	public void setRelAmount(String relAmount) {
		this.relAmount = relAmount;
	}

	public String getRelCounts() {
		return relCounts;
	}

	public void setRelCounts(String relCounts) {
		this.relCounts = relCounts;
	}

	public String getBatchState() {
		return batchState;
	}

	public void setBatchState(String batchState) {
		this.batchState = batchState;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	public String getSucCounts() {
		return sucCounts;
	}

	public void setSucCounts(String sucCounts) {
		this.sucCounts = sucCounts;
	}

	public String getSucAmount() {
		return sucAmount;
	}

	public void setSucAmount(String sucAmount) {
		this.sucAmount = sucAmount;
	}

	public String getFailCounts() {
		return failCounts;
	}

	public void setFailCounts(String failCounts) {
		this.failCounts = failCounts;
	}

	public String getFailAmount() {
		return failAmount;
	}

	public void setFailAmount(String failAmount) {
		this.failAmount = failAmount;
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

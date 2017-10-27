package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 *  2.5.10批次撤销
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxBatchCancelModel extends JxBaseModel {
	private String batchNo; //批次号	N	6	M	需要撤销的批次号
	private String txAmount; //交易金额	N	12,2	M	本批次所有交易金额汇总
	private String txCounts; //交易笔数	N	6	M	本批次所有交易笔数

	private String batchTxCode; //批次交易代码	A	50	C	批次号对应的交易代码
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"batchNo", "txAmount", "txCounts"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"batchNo", "batchTxCode"};

	public JxBatchCancelModel() {
		super();
		super.setTxCode(JxConfig.BATCH_CANCEL);
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(String txAmount) {
		this.txAmount = txAmount;
	}

	public String getTxCounts() {
		return txCounts;
	}

	public void setTxCounts(String txCounts) {
		this.txCounts = txCounts;
	}

	public String getBatchTxCode() {
		return batchTxCode;
	}

	public void setBatchTxCode(String batchTxCode) {
		this.batchTxCode = batchTxCode;
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

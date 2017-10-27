package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.5.8批次融资人还担保账户垫款
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxBatchRepayBailModel extends JxBaseModel {
	private String batchNo; //批次号	N	6	M	当日相同的批次号交易后台在一个批量中处理 批次号当日必须唯一
	private String txAmount; //交易金额	N	12,2	M	本批次所有交易金额汇总
	private String txCounts; //交易笔数	N	6	M	本批次所有交易笔数
	private String notifyURL; //合法性校验通知链接	A	200	M	【数据合法性检查的异步通知】返回到该URL，收到后请平台返回“success”
	private String retNotifyURL; //业务结果通知	A	200	M	【业务处理结果的异步通知】返回到该URL，收到后请平台返回“success”
	private String subPacks; //请求数组	A		C	JSON数组，内容解释见下文
	
	//同步返回
	private String received; //接收结果	A	10	M	success接收成功
	
	//异步返回
	private String sucAmount; //成功交易金额	N	12,2	C	成功金额汇总（请求的txAmount汇总）
	private String sucCounts; //成功交易笔数	N	6	C	成功笔数
	private String failAmount; //失败交易金额	N	12,2	C	失败金额汇总（请求的txAmount汇总）
	private String failCounts; //失败交易笔数	N	6	C	失败笔数

	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"batchNo", "txAmount", "txCounts", "notifyURL", "retNotifyURL", "subPacks"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"batchNo", "sucAmount", "sucCounts", "failAmount", "failCounts"};
    
    /**
     * 响应参数
     */
    private String[] directResponseValidParamNames = new String[]{"batchNo", "txAmount", "txCounts"};

	public JxBatchRepayBailModel() {
		super();
		super.setTxCode(JxConfig.batch_Repay_Bail);
		setNotifyURL(ConfigUtils.getValue("notify_url")+ "/jxbank/batchRepayBail/notify.html");
		setRetNotifyURL(ConfigUtils.getValue("notify_url")+ "/jxbank/batchRepayBail/retNotify.html");
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

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getRetNotifyURL() {
		return retNotifyURL;
	}

	public void setRetNotifyURL(String retNotifyURL) {
		this.retNotifyURL = retNotifyURL;
	}

	public String getSubPacks() {
		return subPacks;
	}

	public void setSubPacks(String subPacks) {
		this.subPacks = subPacks;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public String getSucAmount() {
		return sucAmount;
	}

	public void setSucAmount(String sucAmount) {
		this.sucAmount = sucAmount;
	}

	public String getSucCounts() {
		return sucCounts;
	}

	public void setSucCounts(String sucCounts) {
		this.sucCounts = sucCounts;
	}

	public String getFailAmount() {
		return failAmount;
	}

	public void setFailAmount(String failAmount) {
		this.failAmount = failAmount;
	}

	public String getFailCounts() {
		return failCounts;
	}

	public void setFailCounts(String failCounts) {
		this.failCounts = failCounts;
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

	public String[] getDirectResponseValidParamNames() {
		return directResponseValidParamNames;
	}

	public void setDirectResponseValidParamNames(String[] directResponseValidParamNames) {
		this.directResponseValidParamNames = directResponseValidParamNames;
	}
}

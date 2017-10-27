package com.rd.ifaes.core.tpp.model.cbhb.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbHelper;

/**
 * 
 *  3.8.1	还款（不含收取担保费）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class FileRepaymentModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileRepaymentModel.class);
	
	/**
	 * 交易金额	Number(16)
	 */
	private String transAmt;
	
	/**
	 * 手续费	Number(16)
	 */
	private String feeAmt;
	
	/**
	 * 标的ID	String(10)
	 */
	private String borrowId;
	
	/**
	 * 标的金额	Number(16)
	 */
	private String borrowerAmt;
	
	/**
	 * 账户存管平台借款人ID号	String(16)
	 */
	private String borrCustId;
	
	/**
	 * 总笔数	Number(5)
	 */
	private String totalNum;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id",
			"MerBillNo","TransAmt","FeeAmt","BorrowId","BorrowerAmt","BorrCustId","MerPriv","TotalNum"};

	/**
	 * 构造器
	 */
	public FileRepaymentModel(){
		super();
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbFileRepaymentModel valid...");
	}
	
	public String getFirstFileContent(String transAmt, String feeAmt,
			String borrowId, String borrowerAmt, String borrCustId,
			String totalNum,String merBillNo) {
		this.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(transAmt));
		this.setFeeAmt(CbhbHelper.getBigDecimalMoneyUpper(feeAmt));
		this.setBorrowId(borrowId);
		this.setBorrowerAmt(CbhbHelper.getBigDecimalMoneyUpper(borrowerAmt));
		this.setBorrCustId(borrCustId);
		this.setTotalNum(totalNum);
		this.setMerBillNo(merBillNo);
		return this.getFileContent();
	}
	
	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getBorrowerAmt() {
		return borrowerAmt;
	}

	public void setBorrowerAmt(String borrowerAmt) {
		this.borrowerAmt = borrowerAmt;
	}

	public String getBorrCustId() {
		return borrCustId;
	}

	public void setBorrCustId(String borrCustId) {
		this.borrCustId = borrCustId;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}


}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.ReleaseEnum;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbHelper;

/**
 * 放款文件实体类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月7日
 */
@SuppressWarnings("serial")
public class FileReleaseModel extends CbhbBaseModel{
	
	/**
	 * logger
	 */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FileReleaseModel.class);
	
	/**
	 * 交易金额  交易本金（实际到账金额为扣除手续费金额）
	 */
	private String transAmt;
	
	/**
	 * 手续费  商户收取借款人手续费
	 */
	private String feeAmt;
	
	/**
	 * 标的ID
	 */
	private String borrowId;
	
	/**
	 * 标的金额
	 */
	private String borrowerAmt;
	
	/**
	 * 账户存管平台借款人ID号    用户唯一标识或渤海对公账户
	 */
	private String borrCustId;
	
	/**
	 * 放款方式   0-普通放款
	 */
	private String releaseType;
	
	/**
	 * 投标明细总笔数
	 */
	private String totalNum;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set",
			"partner_id", "MerBillNo", "TransAmt", "FeeAmt", "BorrowId",
			"BorrowerAmt", "BorrCustId", "ReleaseType", "MerPriv","TotalNum"};

	/**
	 * 构造器
	 */
	public FileReleaseModel(){
		super();
		this.setReleaseType(ReleaseEnum.RELEASE_TYPE_NORMAL.getValue());
	}
	
	/**
	 * 获取txt第一行的内容
	 * @author QianPengZhan
	 * @date 2017年3月8日
	 * @param params
	 * @return
	 */
	public String getFirstFileContent(String transAmt,String feeAmt,String borrowId,String borrowerAmt,String borrCustId,String totalNum,String merBillNo){
		this.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(transAmt));//实际放款金额
		this.setFeeAmt(CbhbHelper.getBigDecimalMoneyUpper(feeAmt));//放款手续费
		this.setBorrowId(borrowId);//放款标的ID
		this.setBorrowerAmt(CbhbHelper.getBigDecimalMoneyUpper(borrowerAmt));//放款的标的金额
		this.setBorrCustId(borrCustId);//借款人在存管的账户号
		this.setTotalNum(totalNum);
		this.setMerBillNo(merBillNo);
		return this.getFileContent();
	}
	/**
	 * 获取属性transAmt的值
	 * @return transAmt属性值
	 */
	public String getTransAmt() {
		return transAmt;
	}

	/**
	 * 设置属性transAmt的值
	 * @param  transAmt属性值
	 */
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	/**
	 * 获取属性feeAmt的值
	 * @return feeAmt属性值
	 */
	public String getFeeAmt() {
		return feeAmt;
	}

	/**
	 * 设置属性feeAmt的值
	 * @param  feeAmt属性值
	 */
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	/**
	 * 获取属性borrowId的值
	 * @return borrowId属性值
	 */
	public String getBorrowId() {
		return borrowId;
	}

	/**
	 * 设置属性borrowId的值
	 * @param  borrowId属性值
	 */
	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	/**
	 * 获取属性borrowerAmt的值
	 * @return borrowerAmt属性值
	 */
	public String getBorrowerAmt() {
		return borrowerAmt;
	}

	/**
	 * 设置属性borrowerAmt的值
	 * @param  borrowerAmt属性值
	 */
	public void setBorrowerAmt(String borrowerAmt) {
		this.borrowerAmt = borrowerAmt;
	}

	/**
	 * 获取属性borrCustId的值
	 * @return borrCustId属性值
	 */
	public String getBorrCustId() {
		return borrCustId;
	}

	/**
	 * 设置属性borrCustId的值
	 * @param  borrCustId属性值
	 */
	public void setBorrCustId(String borrCustId) {
		this.borrCustId = borrCustId;
	}

	/**
	 * 获取属性releaseType的值
	 * @return releaseType属性值
	 */
	public String getReleaseType() {
		return releaseType;
	}

	/**
	 * 设置属性releaseType的值
	 * @param  releaseType属性值
	 */
	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}

	/**
	 * 获取属性totalNum的值
	 * @return totalNum属性值
	 */
	public String getTotalNum() {
		return totalNum;
	}

	/**
	 * 设置属性totalNum的值
	 * @param  totalNum属性值
	 */
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * 获取属性requestParamNames的值
	 * @return requestParamNames属性值
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置属性requestParamNames的值
	 * @param  requestParamNames属性值
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	
	
	
}

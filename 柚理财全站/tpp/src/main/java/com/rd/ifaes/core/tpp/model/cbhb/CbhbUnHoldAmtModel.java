package com.rd.ifaes.core.tpp.model.cbhb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;

/**
 * 
 * 3.10	资金转出（后台方式）  已经取消该接口
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@Deprecated
public class CbhbUnHoldAmtModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbUnHoldAmtModel.class);
	/**
	 * 转出金额	Number(16)
	 */
	private String unHoldAmt;
	
	/**
	 * 标的ID	String(10)
	 */
	private String borrowId;
	
	/**
	 * 商户费用	Number(16)
	 */
	private String mercAmt;
	
	/**
	 * 总笔数	Number(5)
	 */
	private int totalNum;
	
	/**
	 * 解冻明细	String(1024)
	 */
	private String bidDetailList;

	/**
	 * 账户存管平台客户号	String(16)
	 */
	private String customerId;
	
	/**
	 * 账户存管平台流水	String(32)
	 */
	private String transId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","UnHoldAmt","BorrowId","MercAmt","TotalNum","BidDetailList","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","MerBillNo","HoldAmt",
			"RespCode","RespDesc","CustomerId","UnHoldAmt","TransId","MerPriv","mac"};

	/**
	 * 构造器
	 */
	public CbhbUnHoldAmtModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_UN_HOLD_AMT);
		setMerPriv("");
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbSendUapMsgModel valid...");
	}
	
	public String getUnHoldAmt() {
		return unHoldAmt;
	}

	public void setUnHoldAmt(String unHoldAmt) {
		this.unHoldAmt = unHoldAmt;
	}

	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getMercAmt() {
		return mercAmt;
	}

	public void setMercAmt(String mercAmt) {
		this.mercAmt = mercAmt;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getBidDetailList() {
		return bidDetailList;
	}

	public void setBidDetailList(String bidDetailList) {
		this.bidDetailList = bidDetailList;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

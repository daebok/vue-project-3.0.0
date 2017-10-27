package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 
 *  3.4	用户投标（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@SuppressWarnings("serial")
public class CbhbBackInvestModel extends CbhbBaseModel{
	
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBackInvestModel.class);
	
	/**
	 * 原商户流水号	String(32)
	 */
	private String oldMerBillNo;
	
	/**
	 * 业务类型	String(1)
	 */
	private String busiType;
	
	/**
	 * 账户存管平台客户号	String(16)
	 */
	private String plaCustId;
	
	/**
	 * 短信验证码	String(6)
	 */
	private String smsCode;
	
	/**
	 * 交易金额	Number(16)
	 */
	private String transAmt;
	
	/**
	 * 营销金额	Number(16)
	 */
	private String marketAmt;
	
	/**
	 * 标的ID	String(10)
	 */
	private String borrowId;
	
	/**
	 * 账户存管平台流水	String(32)
	 */
	private String transId;
	
	/**
	 * 手续费	Number(16)
	 */
	private String feeAmt;
	
	/**
	 * 冻结编号	String(32)
	 */
	private String freezeId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","OldMerBillNo","BusiType","PlaCustId","SmsCode","TransAmt","MarketAmt","BorrowId","MerPriv",
			"mac","merchantCert"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo",
			"RespCode","RespDesc","TransAmt","TransId","FeeAmt","FreezeId","MerPriv","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbBackInvestModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_BACK_INVEST);
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("cbhbBackInvest valid...");
	}
	
	public void response(Map<String, String> map) {
		super.response(map);
		this.setTransId(StringUtils.isNull(map.get("TransId")));
		this.setFreezeId(StringUtils.isNull(map.get("FreezeId")));
		this.setFeeAmt(StringUtils.isNull(map.get("FeeAmt")));
	}

	public String getOldMerBillNo() {
		return oldMerBillNo;
	}

	public void setOldMerBillNo(String oldMerBillNo) {
		this.oldMerBillNo = oldMerBillNo;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getMarketAmt() {
		return marketAmt;
	}

	public void setMarketAmt(String marketAmt) {
		this.marketAmt = marketAmt;
	}

	public String getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

	public String getFreezeId() {
		return freezeId;
	}

	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
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

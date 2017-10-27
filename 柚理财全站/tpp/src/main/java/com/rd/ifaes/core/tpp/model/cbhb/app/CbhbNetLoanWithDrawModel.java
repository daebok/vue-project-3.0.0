package com.rd.ifaes.core.tpp.model.cbhb.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

@SuppressWarnings("serial")
public class CbhbNetLoanWithDrawModel extends CbhbAppBaseModel{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbNetLoanWithDrawModel.class);	
	/**
	 * 构造器
	 */
	public CbhbNetLoanWithDrawModel() {
		super();
		this.setTransid("CBHBNetLoanWithdraw");
		this.setBackUrl("http://localhost:8080"+"/cbhbApp/appWithDraw/notify.html");
		this.setFrontUrl("http://localhost:8080"+"/cbhbApp/appWithDraw/return.html");
	}
	
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		try {
			this.setTransAmt(URLDecoder.decode(StringUtils.isNull(map.get("TransAmt")),"GBK"));
			this.setTransId(URLDecoder.decode(StringUtils.isNull(map.get("TransId")),"GBK"));
			this.setMerFeeAmt(URLDecoder.decode(StringUtils.isNull(map.get("MerFeeAmt")),"GBK"));
			this.setFeeAmt(URLDecoder.decode(StringUtils.isNull(map.get("FeeAmt")),"GBK"));
			this.setCharSet(URLDecoder.decode(StringUtils.isNull(map.get("Char_Set")),"GBK"));
		}catch (UnsupportedEncodingException e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE));
			throw new BussinessException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
		LOGGER.info("CbhbNetLoanWithDrawModel APP 回调...{}",this.toString());
	}

	/**
	 * APP明文加密字段
	 */
	private String[] requestParamNames = new String[] {"PartnerId","MerBillNo","PlaCustId","FeeType","MerFeeAmt"
			,"BackUrl","FrontUrl","TransAmt","MerPriv"};

	/**
	 * 响应参数字段数组 包含所有的
	 */
	private String[] responseParamNames = new String[] {"PartnerId","MerBillNo","TransAmt","TransId","MerFeeAmt"
			,"FeeAmt","MerPriv","Version_No","Biz_Type","RpCode","RpDesc","Mac","Sign_Type","Char_Set"};
	/**
	 * 托管平台客户号
	 */
	private String PlaCustId;
	/**
	 * 手续费模式
	 */
	private String FeeType;
	/**
	 * 商户手续费收入
	 */
	private String MerFeeAmt;
	/**
	 * 提现金额
	 */
	private String TransAmt;
	/**
	 * 托管平台流水号
	 */
	private String transId;
	/**
	 * 手续费
	 */
	private String feeAmt;
	/**
	 * 字符集
	 */
	private String charSet;
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

	public String getPlaCustId() {
		return PlaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		PlaCustId = plaCustId;
	}

	public String getFeeType() {
		return FeeType;
	}

	public void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public String getMerFeeAmt() {
		return MerFeeAmt;
	}

	public void setMerFeeAmt(String merFeeAmt) {
		MerFeeAmt = merFeeAmt;
	}

	public String getTransAmt() {
		return TransAmt;
	}

	public void setTransAmt(String transAmt) {
		TransAmt = transAmt;
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

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	
}

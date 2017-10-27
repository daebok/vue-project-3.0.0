package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;

/**
 * 
 * 3.11	实时红包（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbExperBonusModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbExperBonusModel.class);
	/**
	 * 账户存管平台客户号	String(16)
	 */
	private String plaCustId;
	
	/**
	 * 交易金额	Number(16)
	 */
	private String transAmt;
	
	/**
	 * 手续费	Number(10)
	 */
	private String merFeeAmt;

	/**
	 * 账户存管平台流水号	String(32)
	 */
	private String transId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","PlaCustId","TransAmt","MerFeeAmt","MerPriv","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","MerBillNo",
			"TransId","RespCode","RespDesc","mac"};
	/**
	 * 构造器
	 */
	public CbhbExperBonusModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_EXPER_BONUS);
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbExperBonusModel valid...");
		if(StringUtils.isBlank(this.getPlaCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BATCHNO_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getMerFeeAmt())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MERFEEAMT_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getTransAmt())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_TRANSAMT_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	public void response(Map<String, String> map) {
		super.response(map);
		this.setTransId(StringUtils.isNull(map.get("TransId")));
	}
	
	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getMerFeeAmt() {
		return merFeeAmt;
	}

	public void setMerFeeAmt(String merFeeAmt) {
		this.merFeeAmt = merFeeAmt;
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

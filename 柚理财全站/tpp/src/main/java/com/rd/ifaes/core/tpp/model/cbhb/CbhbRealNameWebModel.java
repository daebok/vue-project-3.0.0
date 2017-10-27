package com.rd.ifaes.core.tpp.model.cbhb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;

/**
 * 
 * 2.2 新用户注册(页面方式)
 * 
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@SuppressWarnings("serial")
public class CbhbRealNameWebModel extends CbhbBaseModel {
	/**
	 * Log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbQueryTransStatModel.class);
	
	/**
	 * 开户类型 String(1)
	 */
	private String openType;

	/**
	 * 证件类型 String(2)
	 */
	private String identType;

	/**
	 * 证件号码 String(50)
	 */
	private String identNo;

	/**
	 * 姓名 String(50)
	 */
	private String usrName;

	/**
	 * 手机号 String(11)
	 */
	private String mobileNo;

	/**
	 * 开户银行代号 String(8)
	 */
	private String openBankId;

	/**
	 * 开户银行账号 String(40)
	 */
	private String openAcctId;

	/**
	 * 账户存管平台客户号 String(16)
	 */
	private String plaCustId;

	/**
	 * 手续费 Number(16)
	 */
	private String feeAmt;

	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set",
			"partner_id", "version_no", "biz_type", "sign_type", "MerBillNo",
			"OpenType", "IdentType", "IdentNo", "UsrName", "MobileNo",
			"OpenBankId", "OpenAcctId", "PageReturnUrl", "BgRetUrl", "MerPriv","mac"};
	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] { "partner_id",
			"version_no", "biz_type","sign_type","MerBillNo", "PlaCustId", "RespCode",
			"RespDesc", "OpenBankId", "OpenAcctId", "IdentType", "IdentNo",
			"UsrName", "MobileNo", "FEE_AMT", "MerPriv","mac"};

	
	/**
	 * 构造器
	 */
	public CbhbRealNameWebModel() {
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_REAL_NAME_WEB);
		this.setPageReturnUrl(ConfigUtils.getValue("notify_url")
				+ "/cbhb/register/return.html");
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")
				+ "/cbhb/register/notify.html");
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbQueryUserInfModel valid...");
		if(StringUtils.isBlank(this.getMobileNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MOBILENO_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getOpenType())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_OPENTYPE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getPageReturnUrl())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PAGERETURNURL_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBgRetUrl())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_NOTIFYURL_EMPTY),BussinessException.TYPE_JSON);
		}
		if(Constant.STRING_TWO.equals(this.getOpenType())){
			if(StringUtils.isBlank(this.getIdentType())){
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_IDENTITYPE_EMPTY),BussinessException.TYPE_JSON);
			}
			if(StringUtils.isBlank(this.getIdentNo())){
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_IDENTNO_EMPTY),BussinessException.TYPE_JSON);
			}
			if(StringUtils.isBlank(this.getUsrName())){
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_USRNAME_EMPTY),BussinessException.TYPE_JSON);
			}
			if(StringUtils.isBlank(this.getOpenBankId())){
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_OPENBANKID_EMPTY),BussinessException.TYPE_JSON);
			}
			if(StringUtils.isBlank(this.getOpenAcctId())){
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_OPENACCTID_EMPTY),BussinessException.TYPE_JSON);
			}
		}
	}
	
	/**
	 * 回调参数封装
	 */
	public void response(Map<String, String> map) {
		super.response(map);
		try {
			this.setPlaCustId(URLDecoder.decode(StringUtils.isNull(map.get("PlaCustId")),"GBK"));
			this.setOpenBankId(URLDecoder.decode(StringUtils.isNull(map.get("OpenBankId")),"GBK"));
			this.setOpenAcctId(URLDecoder.decode(StringUtils.isNull(map.get("OpenAcctId")),"GBK"));
			this.setIdentType(URLDecoder.decode(StringUtils.isNull(map.get("IdentType")),"GBK"));
			this.setIdentNo(URLDecoder.decode(StringUtils.isNull(map.get("IdentNo")),"GBK"));
			this.setUsrName(URLDecoder.decode(StringUtils.isNull(map.get("UsrName")),"GBK"));
			this.setMobileNo(URLDecoder.decode(StringUtils.isNull(map.get("MobileNo")),"GBK"));
			this.setFeeAmt(URLDecoder.decode(StringUtils.isNull(map.get("FEE_AMT")),"GBK"));
			this.setMerPriv(URLDecoder.decode(StringUtils.isNull(map.get("MerPriv")),"GBK"));
		} catch (UnsupportedEncodingException e) {
			throw new BussinessException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
	}
	
	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getIdentType() {
		return identType;
	}

	public void setIdentType(String identType) {
		this.identType = identType;
	}

	public String getIdentNo() {
		return identNo;
	}

	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
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

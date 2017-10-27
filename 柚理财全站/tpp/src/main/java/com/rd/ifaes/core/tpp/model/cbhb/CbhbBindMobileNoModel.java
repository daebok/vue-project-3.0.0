/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;

/**
 * 修改手机号(页面方式)
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月23日
 */
@SuppressWarnings("serial")
public class CbhbBindMobileNoModel extends CbhbBaseModel{
	
	/**
	 * 场景描述：对于注册的手机号需要修改时，则可以调用本接口进行修改，
	 * 网贷公司只需上送“用户唯一标识”到账户存管平台（如果上送了修改后手机号，则进行反显），
	 * 账户存管平台会从系统中查询出该客户是“注册未实名用户”还是“注册已实名”用户进行不同的处理。
	 * 如果是“注册未实名”用户，则进行“修改手机号”操作后，即可返回给网贷公司；如果是“注册已实名”用户，
	 * 操作“修改手机号”后，账户存管平台还会跳转到实名认证页面进行新手机号的实名认证。如果实名认证成功，
	 * 录入银行卡开卡相关信息，点击确认通过“修改手机号结果通知”接口返回给网贷公司；如果实名认证失败，
	 * 则修改回原手机号码并通过“修改手机号结果通知”接口返回给网贷公司告知修改手机号失败。
	 */
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbSendUapMsgModel.class);
	
	/**
	 * 开户银行代号
	 */
	private String openBankId;
	
	/**
	 * 开户银行账号
	 */
	private String openAcctId;
	
	/**
	 * 证件类型
	 */
	private String identType;
	
	/**
	 * 证件号码
	 */
	private String identNo;
	
	/**
	 * 姓名
	 */
	private String usrName;
	
	/**
	 * 对应响应参数mobileNo 
	 */
	private String mobileNo;
	
	/**
	 * 实名手续费
	 */
	private String feeAmt;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set",
			"partner_id", "version_no", "biz_type", "sign_type", "MerBillNo",
			"PlaCustId","MobileNo", "PageReturnUrl", "BgRetUrl", "MerPriv","mac","merchantCert"};
	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] { "partner_id",
			"version_no", "biz_type", "sign_type","MerBillNo", "PlaCustId", "RespCode",
			"RespDesc", "OpenBankId", "OpenAcctId", "IdentType", "IdentNo",
			"UsrName", "MobileNo", "FEE_AMT", "MerPriv","mac"};
	
	/**
	 * 构造器
	 */
	public  CbhbBindMobileNoModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_BIND_MOBILE_WEB);
		this.setPageReturnUrl(ConfigUtils.getValue("notify_url")+"/cbhb/bindMobileWeb/return.html");
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")+"/cbhb/bindMobileWeb/notify.html");
		LOGGER.info("初始化CbhbBindMobileNoModel");
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("cbhbBindMobileNo valid...");
		if(StringUtils.isBlank(this.getPlaCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PLACUSTID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getMobileNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MOBILENO_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 获取属性mobileNo的值
	 * @return mobileNo属性值
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * 设置属性mobileNo的值
	 * @param  mobileNo属性值
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * 获取属性openBankId的值
	 * @return openBankId属性值
	 */
	public String getOpenBankId() {
		return openBankId;
	}

	/**
	 * 设置属性openBankId的值
	 * @param  openBankId属性值
	 */
	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	/**
	 * 获取属性openAcctId的值
	 * @return openAcctId属性值
	 */
	public String getOpenAcctId() {
		return openAcctId;
	}

	/**
	 * 设置属性openAcctId的值
	 * @param  openAcctId属性值
	 */
	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	/**
	 * 获取属性identType的值
	 * @return identType属性值
	 */
	public String getIdentType() {
		return identType;
	}

	/**
	 * 设置属性identType的值
	 * @param  identType属性值
	 */
	public void setIdentType(String identType) {
		this.identType = identType;
	}

	/**
	 * 获取属性identNo的值
	 * @return identNo属性值
	 */
	public String getIdentNo() {
		return identNo;
	}

	/**
	 * 设置属性identNo的值
	 * @param  identNo属性值
	 */
	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}

	/**
	 * 获取属性usrName的值
	 * @return usrName属性值
	 */
	public String getUsrName() {
		return usrName;
	}

	/**
	 * 设置属性usrName的值
	 * @param  usrName属性值
	 */
	public void setUsrName(String usrName) {
		this.usrName = usrName;
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

	/**
	 * 获取属性responseParamNames的值
	 * @return responseParamNames属性值
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置属性responseParamNames的值
	 * @param  responseParamNames属性值
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}
	
	
	
}

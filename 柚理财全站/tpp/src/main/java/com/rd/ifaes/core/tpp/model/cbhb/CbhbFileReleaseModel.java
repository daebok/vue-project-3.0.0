/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

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
 * 放款接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月6日
 */
@SuppressWarnings("serial")
public class CbhbFileReleaseModel extends CbhbBaseModel{
	
	/**
	 * 场景描述：放款通过后台方式，接口分为两种，区分是否收取担保手续费。商户同时提交放款文件和放款请求，
	 * 账户存管平台接收商户发来的请求，实时处理文件明细并返回结果。<b>目前只启用不收取担保费接口。</b>
	 */
	
	/**
	 * LOGGER 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbFileReleaseModel.class);
	
	/**
	 * 标ID
	 */
	private String borrowId;
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set",
			"partner_id", "version_no", "biz_type", "sign_type", "MerBillNo",
			"BorrowId", "FileName", "BgRetUrl", "MerPriv","mac","merchantCert"};
	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] { "partner_id",
			"version_no", "biz_type", "sign_type","MerBillNo","RespCode",
			"RespDesc","mac"};

	
	/**
	 * 构造器
	 */
	public  CbhbFileReleaseModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_FILE_RELEASE);
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")+"/cbhb/fileRelease/notify.html");
		this.setFileName(this.getFileNameOnByRule());
	}
	
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbFileReleaseModel valid...");
		LOGGER.info("borrowId3：{}",this.getBorrowId());
		if(StringUtils.isBlank(this.getBorrowId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_BORROWID_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 回调响应
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		LOGGER.info("CbhbFileReleaseModel response...");
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
	 * 获取属性fileName的值
	 * @return fileName属性值
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置属性fileName的值
	 * @param  fileName属性值
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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

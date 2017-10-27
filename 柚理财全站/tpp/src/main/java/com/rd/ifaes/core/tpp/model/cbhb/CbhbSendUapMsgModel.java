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
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;

/**
 * 动态口令申请（后台方式） 同步响应    测试OK
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月21日
 */
@SuppressWarnings("serial")
public class CbhbSendUapMsgModel extends CbhbBaseModel {
	/**
	 * 场景描述：投标时若商户需要存管平台验证动态口令，则通过该接口，网贷公司商户可以调用账户存管平台获取动态口令，
	 * 并获取识别码（展示给用户做识别验证用，非实际手机验证码）。
	 */

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CbhbSendUapMsgModel.class);

	/**
	 * 构造器
	 */
	public CbhbSendUapMsgModel() {
		super();
		this.setBizType(CbhbConstant.BIZ_TYLE_SEND_UAP_MSG);
	}

	/**
	 * ===================================request 请求参数
	 * ==============================
	 */
	/**
	 * 手机号码 String(11)
	 */
	private String MobileNo;

	/**
	 * 请求参数 -- 用于实时回调提交参数Map  其中包含了mac和merchantCert
	 */
	private String[] requestParamNames = new String[] { "char_set",
			"partner_id", "version_no", "biz_type", "sign_type", "MerBillNo",
			"MobileNo","mac","merchantCert"};
	
	/**
	 * 响应参数 -- 只是用于回调验签的拼接组装  无需mac和merchantCert
	 */
	private String[] responseParamNames = new String[] { "partner_id",
			"version_no", "biz_type", "sign_type","MerBillNo","RespCode","RtnCod","mac"};
	
	/**
	 * 回调信息处理
	 * 
	 * @author QianPengZhan
	 * @date 2017年2月24日
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		LOGGER.info("sendUapMsg,....");
		this.setRtnCod(StringUtils.isNull(map.get("RtnCod")));
		LOGGER.info("sendUapMsg:{},{},{}", this.getRespCode(),this.getRtnCod(), this.getRespDesc());
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbSendUapMsgModel valid...");
		if(StringUtils.isBlank(this.getMobileNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MOBILENO_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * ===================================response 响应参数
	 * ==============================
	 */
	/**
	 * 短信识别码(与验证码一一对应，建议商户申请短信发送接口后，将识别码展示在页面上，供用户鉴别交易。交易成功返回时，不可空。)
	 */
	private String RtnCod;

	/**
	 * 获取属性mobileNo的值
	 * 
	 * @return mobileNo属性值
	 */
	public String getMobileNo() {
		return MobileNo;
	}

	/**
	 * 设置属性mobileNo的值
	 * 
	 * @param mobileNo属性值
	 */
	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	/**
	 * 获取属性rtnCod的值
	 * 
	 * @return rtnCod属性值
	 */
	public String getRtnCod() {
		return RtnCod;
	}

	/**
	 * 设置属性rtnCod的值
	 * 
	 * @param rtnCod属性值
	 */
	public void setRtnCod(String rtnCod) {
		RtnCod = rtnCod;
	}

	/**
	 * 获取属性requestParamNames的值
	 * 
	 * @return requestParamNames属性值
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置属性requestParamNames的值
	 * 
	 * @param requestParamNames属性值
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取属性responseParamNames的值
	 * 
	 * @return responseParamNames属性值
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置属性responseParamNames的值
	 * 
	 * @param responseParamNames属性值
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

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
 * 投标撤销接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月4日
 */
@SuppressWarnings("serial")
public class CbhbInvestCancleModel extends CbhbBaseModel{
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbInvestCancleModel.class);
	
	/**
	 * 原账户存管平台流水
	 */
	private String oldTransId;
	
	/**
	 * 短信验证码
	 */
	private String SmsCode;
	
	/**
	 * 原投标金额
	 */
	private String TransAmt;
	
	/**
	 * 冻结编号
	 */
	private String freezeId;
	
	
	/**
	 * 回调参数：账户存管平台流水号
	 */
	private String transId;
	
	
	/**
	 * 构造器
	 */
	public CbhbInvestCancleModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_INVEST_CANCLE);
	}
	
	/**
	 * 响应回调
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		LOGGER.info("CbhbInvestCancleModel===response");
		this.setOldTransId(StringUtils.isNull(map.get("OldTransId")));
		this.setTransAmt(StringUtils.isNull(map.get("TransAmt")));
		this.setTransId(StringUtils.isNull(map.get("TransId")));
	}

	
	/**
	 * 每个实体类的提交参数的字段的校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbInvestCancleModel===requestColumnValid");
		if(StringUtils.isBlank(this.getOldTransId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_OLDTRANSID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getSmsCode())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_SMSCODE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getTransAmt())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_OLDTENDERMONEY_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getFreezeId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_FREEZEID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getPlaCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PLACUSTID_EMPTY),BussinessException.TYPE_JSON);
		}
	}




	/**
	 * 获取属性oldTransId的值
	 * @return oldTransId属性值
	 */
	public String getOldTransId() {
		return oldTransId;
	}


	/**
	 * 设置属性oldTransId的值
	 * @param  oldTransId属性值
	 */
	public void setOldTransId(String oldTransId) {
		this.oldTransId = oldTransId;
	}


	/**
	 * 获取属性smsCode的值
	 * @return smsCode属性值
	 */
	public String getSmsCode() {
		return SmsCode;
	}


	/**
	 * 设置属性smsCode的值
	 * @param  smsCode属性值
	 */
	public void setSmsCode(String smsCode) {
		SmsCode = smsCode;
	}


	/**
	 * 获取属性transAmt的值
	 * @return transAmt属性值
	 */
	public String getTransAmt() {
		return TransAmt;
	}


	/**
	 * 设置属性transAmt的值
	 * @param  transAmt属性值
	 */
	public void setTransAmt(String transAmt) {
		TransAmt = transAmt;
	}


	/**
	 * 获取属性freezeId的值
	 * @return freezeId属性值
	 */
	public String getFreezeId() {
		return freezeId;
	}


	/**
	 * 设置属性freezeId的值
	 * @param  freezeId属性值
	 */
	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
	}


	/**
	 * 获取属性transId的值
	 * @return transId属性值
	 */
	public String getTransId() {
		return transId;
	}


	/**
	 * 设置属性transId的值
	 * @param  transId属性值
	 */
	public void setTransId(String transId) {
		this.transId = transId;
	}
	
}


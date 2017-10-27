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
 * 修改/找回支付密码（页面方式）
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月24日
 */
@SuppressWarnings("serial")
public class CbhbBindPassModel extends CbhbBaseModel{
	
	/**
	 * 场景描述：对于支付密码丢失的客户，可以通过本接口找回支付密码。
	 * 网贷公司发起“修改/找回支付密码”接口，账户存管平台接收后跳转到“修改/找回支付密码”界面，
	 * 如果操作用户选择的是修改支付密码选项，则输入“原支付密码”和“新支付密码”即可完成修改；
	 * 如果操作用户选择的是找回支付密码，则输入“身份证”、“姓名”、“银行卡”、“新支付密码”即可完成支付密码修改，
	 * 点击确认后，通过“修改/找回支付密码”接口通知网贷公司修改结果。
	 */
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBindPassModel.class);
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","PlaCustId","PageReturnUrl","BgRetUrl","mac","merchantCert"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type","MerBillNo",
			"RespCode","RespDesc","PlaCustId","mac"};
	
	/**
	 * 构造器
	 */
	public  CbhbBindPassModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_BIND_PASS);
		this.setPageReturnUrl(ConfigUtils.getValue("notify_url")+"/cbhb/bindPass/return.html");
		this.setBgRetUrl(ConfigUtils.getValue("notify_url")+"/cbhb/bindPass/notify.html");
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("cbhbBindPass valid...");
		if(StringUtils.isBlank(this.getPlaCustId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PLACUSTID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getPageReturnUrl())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PAGERETURNURL_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getBgRetUrl())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_NOTIFYURL_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
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

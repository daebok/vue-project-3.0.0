package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *  4.3	商户账户查询（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class CbhbQueryMerchantAcctsModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = 1407295941390405207L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbQueryMerchantAcctsModel.class);
	/**
	 * 返回数据	String(256)
	 */
	private String respData;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type","mac"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","RespCode","RespDesc","respData"};
	
	/**
	 * 构造器
	 */
	public CbhbQueryMerchantAcctsModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_QUERY_MERCHANT_ACCTS);
	}

	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbQueryMerchantAcctsModel valid...");
	}
	
	public void response(Map<String, String> map) {
		super.response(map);
		this.setRespData(StringUtils.isNull(map.get("RespData")));
	}
	
	public String getRespData() {
		return respData;
	}

	public void setRespData(String respData) {
		this.respData = respData;
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

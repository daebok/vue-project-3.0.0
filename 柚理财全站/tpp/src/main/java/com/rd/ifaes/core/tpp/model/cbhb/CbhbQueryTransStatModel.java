package com.rd.ifaes.core.tpp.model.cbhb;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 
 *  4.4	交易状态查询（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
@SuppressWarnings("serial")
public class CbhbQueryTransStatModel extends CbhbBaseModel{
	
	
	/**
	 * Log
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbQueryTransStatModel.class);
	/**
	 * 查询交易类型	String(32)
	 */
	private String queryTransType;
	
	/**
	 * 交易状态	String(2)
	 */
	private String transStat;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id","version_no","biz_type","sign_type",
			"MerBillNo","QueryTransType","mac","merchantCert"};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {"partner_id","version_no","biz_type","sign_type","RespCode","RespDesc","MerBillNo","TransStat","mac"};
	
	/**
	 * 构造器
	 */
	public CbhbQueryTransStatModel(){
		super();
		this.setBizType(CbhbConstant.BIZ_TYPE_QUERY_TRANS_STAT);
	}
	
	/**
	 * 响应回调
	 */
	@Override
	public void response(Map<String, String> map) {
		super.response(map);
		LOGGER.info(CbhbConstant.BIZ_TYPE_QUERY_TRANS_STAT);
		this.setTransStat(StringUtils.isNull(map.get("TransStat")));
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbQueryTransStatModel valid...");
	}
	


	public String getQueryTransType() {
		return queryTransType;
	}

	public void setQueryTransType(String queryTransType) {
		this.queryTransType = queryTransType;
	}

	public String getTransStat() {
		return transStat;
	}

	public void setTransStat(String transStat) {
		this.transStat = transStat;
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

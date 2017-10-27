package com.rd.ifaes.core.tpp.model.ufx;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xhf
 * @version 3.0
 * @date 2015年12月21日 下午1:56:25 
 */
@SuppressWarnings("serial")
public class UfxFreezeModel extends UfxBaseModel {
	
	/**
	 * 资金冻结，不依赖其它业务
	 */
	public static final String FREEZE_NORMAL = "00";
	
	/**
	 * 取现冻结
	 */
	public static final String FREEZE_CASH = "01";
	
	/**
	 * 还款冻结
	 */
	public static final String FREEZE_REPAY = "02";
	/** 提现ufx变更处理 2016-05-27 end **/
	
	/**
	 * 服务接口
	 */
	private String service = "freeze";
	
	/**
	 * 冻结流水号
	 */
	private String freezeNo;
	
	/** 提现ufx变更处理 2016-05-27 start **/
	/**
	 * 冻结类型
	 */
	private String freezeType;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "amount", "userId", "freezeType", "projectId", "reqExt", "merPriv", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "amount","freezeType", "projectId", "reqExt", "merPriv", "notifyUrl", "freezeNo", "signInfo" };

	/**
	 * 构造函数
	 */
	public UfxFreezeModel() {
		super();
	}

	@Override
	public void response(String resp) {
		super.response(resp);
		final JSONObject json = JSONObject.parseObject(resp);
		this.setFreezeNo(json.getString("freezeNo"));
		this.setAmount(json.getString("amount"));
	}
	
	/**
	 * 获取服务接口
	 * @return service
	 */
	public String getService() {
		return service;
	}

	/**
	 * 设置服务接口
	 * @param service
	 */
	public void setService(final String service) {
		this.service = service;
	}

	/**  
	 * 获取冻结流水号  
	 * @return freezeNo  
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**  
	 * 设置冻结流水号  
	 * @param freezeNo  
	 */
	public void setFreezeNo(final String freezeNo) {
		this.freezeNo = freezeNo;
	}

	/**  
	 * 获取请求参数数组  
	 * @return requestParamNames  
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**  
	 * 设置请求参数数组  
	 * @param requestParamNames  
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**  
	 * 获取响应参数数组  
	 * @return responseParamNames  
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**  
	 * 设置响应参数数组  
	 * @param responseParamNames  
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获得冻结类型
	 * @return
	 */
	public String getFreezeType() {
		return freezeType;
	}

	/**
	 * 设置冻结类型
	 * @param freezeType
	 */
	public void setFreezeType(final String freezeType) {
		this.freezeType = freezeType;
	}

}

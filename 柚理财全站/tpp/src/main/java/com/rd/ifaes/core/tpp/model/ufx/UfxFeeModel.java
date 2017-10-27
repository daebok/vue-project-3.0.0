package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 
 * @author zb
 * @version 1.0
 * @date 2016年5月23日 13:22:25
 * 汇付转双乾 接口拆分 添加手续费收取接口
 */
@SuppressWarnings("serial")
public class UfxFeeModel extends UfxBaseModel {
	
	/**
	 * 放款类型
	 */
	public static final String TYPE_FULL = "01";
	
	/**
	 * 还款类型
	 */
	public static final String TYPE_REPAY = "02";
	
	/**
	 * 服务接口
	 */
	private String service = "fee";
	
	/**
	 * 商户用户号
	 */
	private String userId;
	
	/**
	 * 类型01：放款02还款
	 */
	private String type;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "userId", "userCustId",
			"amount", "type", "reqExt", "merPriv", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc", "projectId",
			"userId", "userCustId", "amount", "type", "reqExt", "merPriv",
			"signInfo"  };

	public UfxFeeModel() {
		super();
/*		this.setService(UfxConstant.FEE);
		this.setNotifyUrl(Global.getValue("web_url")+"/ufx/fee/notify.html");*/
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
	public void setService(String service) {
		this.service = service;
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
	public void setRequestParamNames(String[] requestParamNames) {
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
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

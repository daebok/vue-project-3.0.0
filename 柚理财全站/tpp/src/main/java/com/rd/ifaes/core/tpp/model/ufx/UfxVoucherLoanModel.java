package com.rd.ifaes.core.tpp.model.ufx;


/**
 * 
 * @author zb
 * @version 1.0
 * @date 2016年5月23日 13:22:25
 * 汇付转双乾 接口拆分 添加代金券红包放款接口
 */
@SuppressWarnings("serial")
public class UfxVoucherLoanModel extends UfxBaseModel {
	
	/**
	 * 付款账户类型
	 */
	public static final String TYPE_ACCOUNT = "01";
	
	/**
	 * 服务接口
	 */
	private String service = "voucherLoan";
	
	/**
	 * 商户用户号
	 */
	private String userId;
	
	/**
	 * 转账账户类型
	 */
	private String accountType;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 第三方交易流水号
	 */
	private String loanNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "userId", "userCustId",
			"realName", "amount", "accountType", "reqExt", "merPriv", "notifyUrl", 
			"signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc", "projectId", 
			"userId", "userCustId", "realName", "amount", "loanNo", "reqExt", 
			"merPriv", "signInfo"  };

	public UfxVoucherLoanModel() {
		super();
/*		this.setService(UfxConstant.VOUCHER_LOAN);
		this.setAccountType(TYPE_ACCOUNT);
		this.setNotifyUrl(Global.getValue("web_url")+"/ufx/voucherLoan/notify.html");*/
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

}

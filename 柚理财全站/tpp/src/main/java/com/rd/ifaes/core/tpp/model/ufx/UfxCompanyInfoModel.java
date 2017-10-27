package com.rd.ifaes.core.tpp.model.ufx;


@SuppressWarnings("serial")
public class UfxCompanyInfoModel extends UfxBaseModel {

	/**
	 * 企业账户
	 */
	private String accountId;

	/**
	 * 户名
	 */
	private String realName;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 企业证件类型
	 */
	private String certType;

	/**
	 * 企业证件号码
	 */
	private String certNo;
	/**
	 * 对公账户
	 */
	private String caccount;

	/**
	 * 营业执照号
	 */
	private String bussinessCode;

	/**
	 * 税务登记号
	 */
	private String taxId;

	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"accountId", "merPriv", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"respCode", "respDesc", "accountId", "realName", "phone", "certType", "certNo", "caccount", "bussinessCode",
			"taxId", "merPriv", "signInfo" };
	
	
	public UfxCompanyInfoModel() {
		super();
		this.setService("queryComAccInfo");
		this.setReturnUrl("/ufx/autoInvest/return.html");
		this.setNotifyUrl("/ufx/autoInvest/notify.html");
	}
	

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}

	public String getBussinessCode() {
		return bussinessCode;
	}

	public void setBussinessCode(String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
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

	@Override
	public String toString() {
		return "UfxCompanyInfoModel [accountId=" + accountId + ", realName=" + realName + ", phone=" + phone
				+ ", certType=" + certType + ", certNo=" + certNo + ", caccount=" + caccount + ", bussinessCode="
				+ bussinessCode + ", taxId=" + taxId + "]";
	}

}

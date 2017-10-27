package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.7.15企业账户查询
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxCorprationQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	C	存管平台分配的账号
	
	private String idType; //证件类型	A	2	C	20：其他证件（组织机构代码）25：社会信用号
	private String idNo; //证件号码	A	18	C	组织机构代码/社会信用号
	private String name; //姓名	A	60	C	企业名称
	private String mobile; //手机号	A	12	C	
	private String caccount; //对公账号	A	60	C	
	private String busId; //营业执照编号	A	1	C	
	private String taxId; //税务登记号	A	5	C	
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "idNo", "name", "mobile", "caccount", "busId", "taxId"};

	public JxCorprationQueryModel() {
		super();
		super.setTxCode(JxConfig.CORPRATION_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String[] getDirectRequestParamNames() {
		return directRequestParamNames;
	}

	public void setDirectRequestParamNames(String[] directRequestParamNames) {
		this.directRequestParamNames = directRequestParamNames;
	}

	public String[] getDirectResponseParamNames() {
		return directResponseParamNames;
	}

	public void setDirectResponseParamNames(String[] directResponseParamNames) {
		this.directResponseParamNames = directResponseParamNames;
	}
}

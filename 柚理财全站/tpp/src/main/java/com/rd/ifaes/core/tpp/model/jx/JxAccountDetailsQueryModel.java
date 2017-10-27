package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxAccountDetailsQueryModel extends JxBaseModel {

	private String accountId; //电子账号	A	19	C	
	private String startDate; //	起始日期	A	8	M	YYYYMMDD
	private String endDate; //结束日期	A	8	M	YYYYMMDD
	private String type; //交易种类	A	1	M	0-所有交易1-转入交易2-转出交易9-指定交易类型
	private String tranType; //交易类型	A	4	C	type=9必填，后台交易类型
	private String pageNum; //页数	N	3	M	查询页数
	private String pageSize; //页长	N	2	M	每页笔数


	private String retCode; //响应代码	A	8	M	
	private String retMsg; //响应描述	A	60	M	
	private String name; //姓名	A	60	C	
	private String totalItems; //总记录数	N	5	C	不做
	private String subPacks; //结果数组	A		C	JSON数组，内容解释见下文

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "startDate", "endDate", "type", "tranType", "pageNum", "pageSize"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "startDate", "endDate", "type", 
    		"name", "pageNum", "pageSize", "totalItems", "subPacks"};
	
    public JxAccountDetailsQueryModel() {
		super();
		super.setTxCode(JxConfig.ACCOUNT_DETAILS_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}

	public String getSubPacks() {
		return subPacks;
	}

	public void setSubPacks(String subPacks) {
		this.subPacks = subPacks;
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxDebtDetailsQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的借款人电子账号
	private String productId; //标的号	A	40	C	为空表示查询所有标的productId不能与（startDate和endDate）同时为空 ；标的号不区分大小写；
	private String startDate; //起始日期	A	8	C	YYYYMMDD，对应募集日
	private String endDate; //结束日期	A	8	C	YYYYMMDD，对应募集日
	private String pageNum; //页数	N	3	M	查询页数
	private String pageSize; //页长	N	2	M	每页笔数

	private String name; //姓名	A	60	C	
	private String totalItems; //总记录数	N	5	C	
	private String subPacks; //结果数组	A		C	JSON数组，内容解释见下文
	
	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "productId", "startDate", 
    		"endDate", "pageNum", "pageSize"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "startDate", 
    		"endDate", "name", "pageNum", "pageSize", "totalItems", "subPacks"};
    
    public JxDebtDetailsQueryModel() {
		super();
		super.setTxCode(JxConfig.DEBT_DETAILS_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

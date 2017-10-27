package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

public class JxCreditDetailsQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String productId; //查询标的号	A	40	C	为空查询所有名下所有债权；标的号不区分大小写；
	private String state; //查询记录状 态	A	1	M	0-所有债权1-有效债权（投标成功，且本息尚未返还完成）
	private String startDate; //起始日期	A	8	M	YYYYMMDD
	private String endDate; //结束日期	A	8	M	YYYYMMDD
	private String pageNum; //页数	N	3	M	查询页数
	private String pageSize; //页长	N	2	M	每页笔数

	private String name; //姓名	A	60	C	
	private String totalItems; //总记录数	N	5	C	
	private String subPacks; //结果数组	A		C	JSON数组，内容解释见下文

	/**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId", "productId", "state", "startDate", 
    		"endDate", "pageNum", "pageSize"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "productId", "state", "startDate", 
    		"endDate", "name", "pageNum", "pageSize", "totalItems", "subPacks"};

	public JxCreditDetailsQueryModel() {
		super();
		super.setTxCode(JxConfig.CREDIT_DETAILS_QUERY);
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

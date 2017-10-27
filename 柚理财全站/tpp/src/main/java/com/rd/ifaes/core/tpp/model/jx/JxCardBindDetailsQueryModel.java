package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.7.4绑卡关系查询
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxCardBindDetailsQueryModel extends JxBaseModel {
	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	private String state; //电查询状态	A	1	C	0-所有（默认）1-当前有效的绑定卡

	private String name; //姓名	A	60	C	转让方姓名
	private String totalItems; //总记录数	N	5	C	最大10条记录
	private String subPacks; //结果数组	A		C	JSON数组，内容解释见下文
//	private String cardNo; //银行卡号	A	19	C	曾经绑定的银行卡
//	private String txnDate; //绑定日期	A	8	C	YYYYMMDD
//	private String txnTime; //绑定时间	A	6	C	hhmmss
//	private String canclDate; //解绑日期	A	8	C	YYYYMMDD
//	private String canclTime; //解绑时间	A	6	C	hhmmss
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"accountId", "state"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"name", "totalItems", "subPacks"};

	public JxCardBindDetailsQueryModel() {
		super();
		super.setTxCode(JxConfig.CARD_BIND_DETAILS_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

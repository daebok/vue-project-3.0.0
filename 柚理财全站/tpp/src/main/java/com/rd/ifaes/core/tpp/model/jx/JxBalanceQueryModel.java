package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.7.1电子账户余额查询
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxBalanceQueryModel extends JxBaseModel {

	private String accountId; //电子账号	A	19	M	存管平台分配的账号
	
	private String name; //姓名	A	60	M	
	private String accType; //账户类型	A	1	C	0-基金账户1-靠档计息账2-活期账户
	private String acctUse; //账户用途	A	5	M	00000-普通账户10000-红包账户01000-手续费账户00100-担保账户
	private String availBal; //可用余额	N	12,2	C	
	private String currBal; //账面余额	N	12,2	C	账面余额-可用余额=冻结金额
	private String withdrawFlag; //提现开关	A	1	C	0-关闭 1-打开

	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"accountId"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "name", "accType", "acctUse", 
    		"availBal", "currBal", "withdrawFlag"};
	
    public JxBalanceQueryModel() {
		super();
		super.setTxCode(JxConfig.BALANCE_QUERY);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getAcctUse() {
		return acctUse;
	}

	public void setAcctUse(String acctUse) {
		this.acctUse = acctUse;
	}

	public String getAvailBal() {
		return availBal;
	}

	public void setAvailBal(String availBal) {
		this.availBal = availBal;
	}

	public String getCurrBal() {
		return currBal;
	}

	public void setCurrBal(String currBal) {
		this.currBal = currBal;
	}

	public String getWithdrawFlag() {
		return withdrawFlag;
	}

	public void setWithdrawFlag(String withdrawFlag) {
		this.withdrawFlag = withdrawFlag;
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

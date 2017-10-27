package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.7.5按证件号查询电子账号
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxAccountIdQueryModel extends JxBaseModel {

	private String idType; //证件类型	A	2	M	01-身份证（18位）20-组织机构代码25-企业社会信用代码
	private String idNo; //	证件号码	A	18	M	

	
	private String accountId; //电子账号	A	19	C	
	private String openDate; //开户日期	A	8	C	YYYYMMDD
	private String acctState; //账户状态	A	1	C	空-正常A-待激活Z-注销
	private String frzState; //冻结状态	A	1	C	空-未冻结J-司法冻结
	private String pinLosCd; //密码挂失状态	A	1	C	空-未挂失Q-已挂失

	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"idType", "idNo"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId", "openDate", "acctState", "frzState", "pinLosCd"};
	
    public JxAccountIdQueryModel() {
		super();
		super.setTxCode(JxConfig.ACCOUNT_ID_QUERY);
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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getAcctState() {
		return acctState;
	}

	public void setAcctState(String acctState) {
		this.acctState = acctState;
	}

	public String getFrzState() {
		return frzState;
	}

	public void setFrzState(String frzState) {
		this.frzState = frzState;
	}

	public String getPinLosCd() {
		return pinLosCd;
	}

	public void setPinLosCd(String pinLosCd) {
		this.pinLosCd = pinLosCd;
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

package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.tpp.exception.JxbankException;
import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.2.2个人开户增强
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxAcountOpenPlusModel extends JxBaseModel {

	private String idType; //证件类型	A	2	M	01-身份证（18位）
	private String idNo; //证件号码	A	18	M	
	private String name; //姓名	A	60	M	
	private String mobile; //手机号	A	12	M	
	private String cardNo; //手银行卡号	A	19	M	绑定银行卡号
	private String email; //邮箱	A	40	C	
	private String acctUse; //账户用途账户用途	A	5	M	00000-普通账户10000-红包账户（只能有一个）01000-手续费账户（只能有一个）00100-担保账户
	private String lastSrvAuthCode; //前导业务授权码	A	60	M	通过请求发送短信验证码接口获取
	private String smsCode; //短信验证码	A	6	M	手机接收到短信验证码
	private String userIP; //客户IP	A	32	C	
	
	private static String CA110150 = "CA110150";//该手机已申领过电子账号
	
	
	private String accountId; //电子账号	A	19	C	存管平台分配的账号

	 /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"idType", "idNo", "name", "mobile", "cardNo", "email",
    		"acctUse", "lastSrvAuthCode", "smsCode", "userIP"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"accountId"};
   
    
    public JxAcountOpenPlusModel() {
		super();
		super.setTxCode(JxConfig.ACCOUNT_OPEN_PLUS);
	}
    
    
    public void validResult(String retCode, String retMsg) {
        if (!JxConfig.SUCCESS.equals(retCode)) {
            LOGGER.error("(即信端------->P2P)返回失败...{}", retMsg);
            if (JxAcountOpenPlusModel.CA110150.equals(retCode)) {
            	throw new JxbankException("该手机已申领过电子账号", BussinessException.TYPE_JSON);
            }
            if (StringUtils.isBlank(retMsg)) {
                throw new JxbankException("存管接口返回错误，请联系管理员", BussinessException.TYPE_JSON);
            }
            throw new JxbankException(retMsg, BussinessException.TYPE_JSON);
        }
    }

    @Override
	public String[] getDirectRequestParamNames() {
        return directRequestParamNames;
    }
    
    public void setDirectRequestParamNames(String[] directRequestParamNames) {
        this.directRequestParamNames = directRequestParamNames;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAcctUse() {
		return acctUse;
	}

	public void setAcctUse(String acctUse) {
		this.acctUse = acctUse;
	}

	public String getLastSrvAuthCode() {
		return lastSrvAuthCode;
	}

	public void setLastSrvAuthCode(String lastSrvAuthCode) {
		this.lastSrvAuthCode = lastSrvAuthCode;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String[] getDirectResponseParamNames() {
		return directResponseParamNames;
	}

	public void setDirectResponseParamNames(String[] directResponseParamNames) {
		this.directResponseParamNames = directResponseParamNames;
	}
}

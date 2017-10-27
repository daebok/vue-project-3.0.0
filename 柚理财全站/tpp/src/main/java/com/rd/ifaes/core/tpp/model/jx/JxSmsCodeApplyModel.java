package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.util.JxConfig;

/**
 * 
 * 2.2.1请求发送短信验证码
 * @version 3.0
 * @author jxx
 * @date 2017年8月18日
 */
public class JxSmsCodeApplyModel extends JxBaseModel {

	private String mobile; //手机号	A	12	M	
	private String reqType; //请求类型	A	1	C	不填默认为11-适用于 增强类的接口 2-适用于 金运通通道充值
	
	/**
	 * 业务交易代码	A	50	M	当reqType=1accountOpenPlus cardBindPlus mobileModifyPlus passwordResetPlus directRechargePlus indirectRechargePlus autoBidAuthPlus autoCreditInvestAuthPlus 当reqType=2 directRechargeOnline
	 */
	private String srvTxCode; 
	private String cardNo; //银行卡号	A	19	C	绑定的银行卡号 当reqType=2时必填
	private String acqRes; //请求方保留	A	200	C	

	
	private String srvAuthCode; //业务授权码	A	60	C	交易成功时有效，供后续交易使用，只能使用一次。
	private String sendTime; //短信发送时间	A	8	C	
	private String smsSeq; //短信序号	A	4	C	
	private String validTime; //验证码有效时长	A	4	C

	
	/**
	* 请求参数
	*/
    private String[] directRequestParamNames = new String[]{"mobile", "reqType", "srvTxCode", "cardNo"};
    
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"mobile", "srvTxCode", "srvAuthCode", "sendTime", "smsSeq", "validTime"};

    
	public JxSmsCodeApplyModel() {
		super();
		super.setTxCode(JxConfig.SMS_CODE_APPLY);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getSrvTxCode() {
		return srvTxCode;
	}

	public void setSrvTxCode(String srvTxCode) {
		this.srvTxCode = srvTxCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAcqRes() {
		return acqRes;
	}

	public void setAcqRes(String acqRes) {
		this.acqRes = acqRes;
	}

	public String getSrvAuthCode() {
		return srvAuthCode;
	}

	public void setSrvAuthCode(String srvAuthCode) {
		this.srvAuthCode = srvAuthCode;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSmsSeq() {
		return smsSeq;
	}

	public void setSmsSeq(String smsSeq) {
		this.smsSeq = smsSeq;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
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

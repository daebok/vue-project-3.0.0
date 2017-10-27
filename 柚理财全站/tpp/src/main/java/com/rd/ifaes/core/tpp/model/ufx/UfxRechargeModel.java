package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 充值model
 * @author xhf
 * @version 3.0
 * @date 2016年6月12日 下午7:59:33
 */
@SuppressWarnings("serial")
public class UfxRechargeModel extends UfxBaseModel {
	
	/**
	 * 充值用户类型 - 个人
	 */
	public static final String USER_PERSONAL = "01";

	/**
	 * 充值用户类型 - 企业
	 */
	public static final String USER_COMPANY = "02";
	
	/**
	 * 网银充值
	 */
	public static final String RECHARGE_ONLINE = "0";
	
	/**
	 * 代扣充值
	 */
	public static final String RECHARGE_WITHHOLD = "1";
	
	/**
	 * 快捷支付
	 */
	public static final String RECHARGE_SHORTCUT = "2";
	
	/**
	 * 汇款充值
	 */
	public static final String RECHARGE_REMITTANCE = "3";

	/**
	 * 企业网银充值
	 */
	public static final String RECHARGE_COMPANY = "4";
	
	
	/**
	 * 充值用户身份证号
	 */
	private String idNo;
	
	/**
	 * 充值用户姓名
	 */
	private String realName;
	
	/**
	 * 充值手续费
	 */
	private String rechargeFee;
	
	/**
	 * 发卡银行编号
	 */
	private String gateId;
	
	/**
	 * 支付方式(联动) 取值范围：
	 *	B2CDEBITBANK（借记卡网银）
	 *	B2BBANK（企业网银）
	 *	DEBITCARD（借记卡快捷）
	 */
	private String payType;
	
	/**
	 * 充值用户类型，默认为01：个人
	 * 02：企业 
	 * 
	 */
	private String userType;
	
	/** 充值ufx变更处理 2016-05-27 start **/
	/**
	 * 手续费 
	 * 
	 */
	private String fee;
	
	/**
	 * 手续费类型
	 * 
	 */
	private String feeType;
	
	/**
	 * 支付方式 0.网银充值1.代扣充值2.快捷支付3.汇款充值4.企业网银充值
	 * 
	 */
	private String payWay;
	
	/**
	 * ufx处理流水号
	 */
	private String tradeNo;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "amount", "idNo", "userType", "userId", "realName","returnUrl", "notifyUrl", "signInfo" };
	
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "amount", "idNo", "reqExt", "merPriv", "rechargeFee", "userType", "tradeNo", "signInfo" };

	/**
	 * 用户充值
	 */
	public UfxRechargeModel() {
		super();
		this.setService(UfxConstant.RECHARGE);
		this.setUserType(UfxRechargeModel.USER_PERSONAL);
		this.setReturnUrl("/ufx/recharge/return.html");
		this.setNotifyUrl("/ufx/recharge/notify.html");
	}
	
	/**
	 * 商户充值
	 * @param ufxCustomerId
	 */
	public UfxRechargeModel(final String ufxCustomerId) {
		super();
		this.setService(UfxConstant.RECHARGE);
		//商户充值,把商户的客户号放进来
		this.setUserCustId(ufxCustomerId);
		this.setUserType(UfxRechargeModel.USER_COMPANY);
		this.setManageReturnUrl("/ufx/merchantRecharge/return.html");
		this.setManageNotifyUrl("/ufx/merchantRecharge/notify.html");
	}

	/**
	 * 获取充值用户身份证号
	 * @return
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置充值用户身份证号
	 * @param idNo
	 */
	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取充值手续费
	 * @return
	 */
	public String getRechargeFee() {
		return rechargeFee;
	}

	/**
	 * 设置充值手续费
	 * @param rechargeFee
	 */
	public void setRechargeFee(final String rechargeFee) {
		this.rechargeFee = rechargeFee;
	}

	/**
	 * 获取请求参数数组
	 * @return
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}
	
	/**
	 * 获取响应参数数组
	 * @return
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取发卡银行编号
	 * @return gateId
	 */
	public String getGateId() {
		return gateId;
	}

	/**
	 * 设置发卡银行编号
	 * @param gateId
	 */
	public void setGateId(final String gateId) {
		this.gateId = gateId;
	}

	/**  
	 * 获取支付方式(联动)取值范围：B2CDEBITBANK（借记卡网银）B2BBANK（企业网银）DEBITCARD（借记卡快捷）  
	 * @return payType  
	 */
	
	public String getPayType() {
		return payType;
	}

	/**  
	 * 设置支付方式(联动)取值范围：B2CDEBITBANK（借记卡网银）B2BBANK（企业网银）DEBITCARD（借记卡快捷）  
	 * @param payType  
	 */
	
	public void setPayType(final String payType) {
		this.payType = payType;
	}
	
	/**
	 * 获取充值用户类型：01个人，02：企业
	 * @param userType
	 */
	public String getUserType() {
		return userType;
	}
	
	/**
	 * 设置充值用户类型：01个人，02：企业
	 * @param userType
	 */
	public final void setUserType(final String userType) {
		this.userType = userType;
	}

	/**
	 * 获取手续费
	 * @return
	 */
	public String getFee() {
		return fee;
	}

	/**
	 * 设置手续费
	 * @param fee
	 */
	public void setFee(final String fee) {
		this.fee = fee;
	}

	/**
	 * 获取手续费类型
	 * @return
	 */
	public String getFeeType() {
		return feeType;
	}

	/**
	 * 设置手续费类型
	 * @param feeType
	 */
	public void setFeeType(final String feeType) {
		this.feeType = feeType;
	}

	/**
	 * 获取支付方式
	 * @return
	 */
	public String getPayWay() {
		return payWay;
	}

	/**
	 * 设置支付方式
	 * @param payWay
	 */
	public void setPayWay(final String payWay) {
		this.payWay = payWay;
	}

	/**
	 * 获取姓名
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置姓名
	 * @param realName
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/**
	 * 获取ufx订单流水号
	 * @return the tradeNo
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置ufx订单流水号
	 * @param tradeNo the tradeNo to set
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
}

package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;


/**
 * 用户取现审核接口
 * @author xhf
 * @version 3.0
 * @date 2016年6月8日 上午10:38:34
 */
@SuppressWarnings("serial")
public class UfxCashAuditModel extends UfxBaseModel {
	
	/**
	 * 审核标识 Y--通过
	 */
	public static final String AUDIT_FLAG_Y = "Y";
	
	/**
	 * 审核标识 N--拒绝
	 */
	public static final String AUDIT_FLAG_N = "N";
	
	/**
	 * 异步返回消息类型
	 */
	private String respType;
	
	/**
	 * 审核标识
	 * Y--通过
	 * N--拒绝
	 */
	private String auditFlag;
	
	/**
	 * 取现原始订单号
	 */
	private String cashNo;
	
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 取现平台服务费
	 */
	private String servFee;
	
	/**
	 * 是否需要审核
	 * Y--需要
	 * N--不需要
	 */
	private String needAudit;
	
	/**
	 * 手续费是否垫付
	 * Y--垫付
	 * N--不垫付
	 */
	private String isAdvance;
	
	/**
	 * 取现手续费
	 */
	private String cashFee;
	
	/**
	 * 实际到账金额
	 */
	private String realAmount;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"userCustId", "amount", "auditFlag", "cashNo", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"respType", "userCustId", "amount", "auditFlag", "cashNo", "cardId", "servFee", "needAudit", "isAdvance", "cashFee", "realAmount", "signInfo" };
	
    /**
     * 构造函数	
     */
	public UfxCashAuditModel() {
		super();
		this.setService(UfxConstant.CASH_AUDIT);
		this.setNotifyUrl("/ufx/cashAudit/notify.html");
	}

	/**  
	 * 获取审核标识Y--通过N--拒绝  
	 * @return auditFlag  
	 */
	public String getAuditFlag() {
		return auditFlag;
	}

	/**  
	 * 设置审核标识Y--通过N--拒绝  
	 * @param auditFlag  
	 */
	public void setAuditFlag(final String auditFlag) {
		this.auditFlag = auditFlag;
	}

	/**  
	 * 获取取现原始订单号  
	 * @return cashNo  
	 */
	public String getCashNo() {
		return cashNo;
	}

	/**  
	 * 设置取现原始订单号  
	 * @param cashNo  
	 */
	public void setCashNo(final String cashNo) {
		this.cashNo = cashNo;
	}
	
	/**
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**
	 * @return the servFee
	 */
	public String getServFee() {
		return servFee;
	}

	/**
	 * @param servFee the servFee to set
	 */
	public void setServFee(final String servFee) {
		this.servFee = servFee;
	}

	/**
	 * @return the needAudit
	 */
	public String getNeedAudit() {
		return needAudit;
	}

	/**
	 * @param needAudit the needAudit to set
	 */
	public void setNeedAudit(final String needAudit) {
		this.needAudit = needAudit;
	}

	/**
	 * @return the isAdvance
	 */
	public String getIsAdvance() {
		return isAdvance;
	}

	/**
	 * @param isAdvance the isAdvance to set
	 */
	public void setIsAdvance(final String isAdvance) {
		this.isAdvance = isAdvance;
	}

	/**
	 * @return the cashFee
	 */
	public String getCashFee() {
		return cashFee;
	}

	/**
	 * @param cashFee the cashFee to set
	 */
	public void setCashFee(final String cashFee) {
		this.cashFee = cashFee;
	}

	/**
	 * @return the realAmount
	 */
	public String getRealAmount() {
		return realAmount;
	}

	/**
	 * @param realAmount the realAmount to set
	 */
	public void setRealAmount(final String realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * @return the respType
	 */
	public String getRespType() {
		return respType;
	}

	/**
	 * @param respType the respType to set
	 */
	public void setRespType(final String respType) {
		this.respType = respType;
	}

	/**  
	 * 获取请求参数数组  
	 * @return requestParamNames  
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**  
	 * 设置请求参数数组  
	 * @param requestParamNames  
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**  
	 * 获取响应参数数组  
	 * @return responseParamNames  
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**  
	 * 设置响应参数数组  
	 * @param responseParamNames  
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}
	
}

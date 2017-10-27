package com.rd.ifaes.core.tpp.model.ufx;

import java.util.Arrays;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 取现model
 * @author xhf
 * @version 3.0
 * @date 2016年6月11日 下午4:44:33
 */
@SuppressWarnings("serial")
public class UfxCashModel extends UfxBaseModel{
	
	/**
	 * 取现用户类型 - 个人
	 */
	public static final String TYPE_PERSONAL = "01";

	/**
	 * 取现用户类型 - 企业
	 */
	public static final String TYPE_COMPANY = "02";
	
	/**
	 * 手续费是否垫付 Y 垫付
	 */
	public static final String IS_ADVANCE_Y = "Y";
	
	/**
	 * 手续费是否垫付 N 不垫付
	 */
	public static final String IS_ADVANCE_N = "N";
	
	/**
	 * 异步返回消息类型
	 */
	private String respType;

	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 取现平台服务费
	 */
	private String servFee;
	
	/**
	 * 取现手续费
	 */
	private String cashFee;
	
	/**
	 * 实际到账金额
	 */
	private String realAmount;
	
	/**
	 * 取现第三方流水号
	 */
	private String cashNo;
	
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
	 * 取现用户类型 01 个人  02 企业
	 */
	private String userType = "01";
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "amount", "cardId","servFee", "needAudit", "isAdvance", "userType", "reqExt","merPriv", "returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay","respCode", "respDesc",
			"respType","userCustId", "amount", "cardId", "servFee", "needAudit","isAdvance", "userType", "reqExt", "merPriv", "cashFee","realAmount", "cashNo", "signInfo" };

	/**
	 * 构造函数
	 */
	public UfxCashModel() {
		super();
		this.setService(UfxConstant.CASH);
		this.setUserType(UfxCashModel.TYPE_PERSONAL);
		this.setReturnUrl("/ufx/cash/return.html");
		this.setNotifyUrl("/ufx/cash/notify.html");
	}
	
	/**
	 * 商户提现
	 * @param ufxCustomerId
	 */
	public UfxCashModel(final String ufxCustomerId) {
		super();
		this.setService(UfxConstant.CASH);
		//商户取现,把商户的客户号放进来
		this.setUserCustId(ufxCustomerId);
		this.setUserType(UfxCashModel.TYPE_COMPANY);
		this.setManageReturnUrl("/ufx/merchantCash/return.html");
		this.setManageNotifyUrl("/ufx/merchantCash/notify.html");
	}

	/**  
	 * 获取异步返回消息类型  
	 * @return respType  
	 */
	public String getRespType() {
		return respType;
	}

	/**  
	 * 设置异步返回消息类型  
	 * @param respType  
	 */
	public void setRespType(final String respType) {
		this.respType = respType;
	}

	/**  
	 * 获取银行卡号  
	 * @return cardId  
	 */
	public String getCardId() {
		return cardId;
	}

	/**  
	 * 设置银行卡号  
	 * @param cardId  
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**  
	 * 获取取现平台服务费  
	 * @return servFee  
	 */
	public String getServFee() {
		return servFee;
	}

	/**  
	 * 设置取现平台服务费  
	 * @param servFee  
	 */
	public void setServFee(final String servFee) {
		this.servFee = servFee;
	}

	/**
	 * 获取取现手续费
	 * @return
	 */
	public String getCashFee() {
		return cashFee;
	}

	/**
	 * 设置取现手续费
	 * @param cashFee
	 */
	public void setCashFee(final String cashFee) {
		this.cashFee = cashFee;
	}

	/**
	 * 获取实际到账金额
	 * @return realAmount
	 */
	public String getRealAmount() {
		return realAmount;
	}

	/**
	 * 设置实际到账金额
	 * @param realAmount
	 */
	public void setRealAmount(final String realAmount) {
		this.realAmount = realAmount;
	}

	/**  
	 * 获取取现第三方流水号  
	 * @return cashNo  
	 */
	public String getCashNo() {
		return cashNo;
	}

	/**  
	 * 设置取现第三方流水号  
	 * @param cashNo  
	 */
	public void setCashNo(final String cashNo) {
		this.cashNo = cashNo;
	}

	/**  
	 * 获取是否需要审核
	 * Y--需要
	 * N--不需要  
	 * @return needAudit  
	 */
	public String getNeedAudit() {
		return needAudit;
	}

	/**  
	 * 设置是否需要审核
	 * Y--需要
	 * N--不需要  
	 * @param needAudit  
	 */
	public void setNeedAudit(final String needAudit) {
		this.needAudit = needAudit;
	}

	/**  
	 * 获取手续费是否垫付
	 * Y--垫付
	 * N--不垫付  
	 * @return isAdvance  
	 */
	public String getIsAdvance() {
		return isAdvance;
	}

	/**  
	 * 设置手续费是否垫付
	 * Y--垫付
	 * N--不垫付  
	 * @param isAdvance  
	 */
	public void setIsAdvance(final String isAdvance) {
		this.isAdvance = isAdvance;
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
	 * 获取取现用户类型01个人02企业
	 * @return userType
	 */
	public String getUserType() {
		return userType;
	}

	/** 
	 * 设置取现用户类型01个人02企业
	 * @param userType
	 */
	public final void setUserType(final String userType) {
		this.userType = userType;
	}


    /**
     * 覆盖toString()方法
     */
	@Override
	public String toString() {
		return "UfxCashModel [respType=" + respType + ", cardId=" + cardId
				+ ", servFee=" + servFee + ", cashFee=" + cashFee
				+ ", realAmount=" + realAmount + ", cashNo=" + cashNo
				+ ", needAudit=" + needAudit + ", isAdvance=" + isAdvance
				+ ", userType=" + userType + ", requestParamNames="
				+ Arrays.toString(requestParamNames) + ", responseParamNames="
				+ Arrays.toString(responseParamNames) + "]";
	}
	
}

package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 商户出账接口
 * @author jxx
 * @version 3.0
 * @date 2016年08月16日 上午10:45:08 
 */
@SuppressWarnings("serial")
public class UfxMerTransferModel extends UfxBaseModel {
	/**
	 * 账户类型，对公
	 */
	public static final String ACCOUNT_PUBLIC = "02";
	/**
	 * 账户类型，对私
	 */
	public static final String ACCOUNT_PRIVATE = "01";
	/**
	 * 转账到用户
	 */
	public static final String TRANS_ACTION_USER = "02";
	/**
	 * 转账到平台
	 */
	public static final String TRANS_ACTION_UFX = "01";
	
	/**
	 * 入账人姓名
	 */
	private String realName;
	
	/**
	 * 第三方交易流水号
	 */
	private String loanNo;
	
	/**
	 * 账户性质
	 * 01对私（个人用户）
	 *02对公（企业用户）
	 */
	private String particAccType;
	
	/**
	 * 转账方向:
	 * 01:用户（仅支持对公）到平台
	 * 02:平台到用户
	 */
	private String transAction;
	
	/**
	 * 订单时间
	 */
	private String investDate;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "userCustId", "amount","userId","realName",
			"investDate", "particAccType", "transAction", "reqExt", "merPriv",
			"returnUrl", "notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "amount", "loanNo", "investDate", "reqExt",
			"merPriv", "notifyUrl", "signInfo" };

	/**
	 * 构造函数
	 */
	public UfxMerTransferModel() {
		super();
		this.setService(UfxConstant.MERTRANSFER);
		this.setManageNotifyUrl("/ufx/merTransfer/notify.html");
	}

	/**
	 * 获取第三方交易流水号
	 * @return loanNo
	 */
	public String getLoanNo() {
		return loanNo;
	}

	/**
	 * 设置第三方交易流水号
	 * @param loanNo
	 */
	public void setLoanNo(final String loanNo) {
		this.loanNo = loanNo;
	}

	/**
	 * 获取账户性质01对私（个人用户）02对公（企业用户）
	 * @return particAccType
	 */
	public String getParticAccType() {
		return particAccType;
	}

	/**
	 * 设置账户性质01对私（个人用户）02对公（企业用户）
	 * @param particAccType
	 */
	public void setParticAccType(final String particAccType) {
		this.particAccType = particAccType;
	}

	/**
	 * 获取转账方向:01:用户（仅支持对公）到平台02:平台到用户
	 * @return transAction
	 */
	public String getTransAction() {
		return transAction;
	}

	/**
	 * 设置转账方向:01:用户（仅支持对公）到平台02:平台到用户
	 * @param transAction
	 */
	public void setTransAction(final String transAction) {
		this.transAction = transAction;
	}

	/**
	 * 获取订单时间
	 * @return investDate
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**
	 * 设置订单时间
	 * @param investDate
	 */
	public void setInvestDate(final String investDate) {
		this.investDate = investDate;
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

	/**
	 * 获得入账人姓名
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置入账人姓名
	 * @param realName
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

}

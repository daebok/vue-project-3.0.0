package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 
 * 交易查询
 * @version 3.0
 * @author xhf
 * @date 2016年9月27日
 */
public class UfxQueryTransferModel extends UfxBaseModel {
	 
	private static final long serialVersionUID = -5121179270575577770L;
	/**
	 * 放款
	 */
	public static final String TRANS_TYPE_LOAN = "1";
	/**
	 * 还款
	 */
	public static final String TRANS_TYPE_REPAY = "2";
	/**
	 * 投资
	 */
	public static final String TRANS_TYPE_INVEST = "3";
	/**
	 * 取现
	 */
	public static final String TRANS_TYPE_CASH = "4";
	/**
	 * 冻结
	 */
	public static final String TRANS_TYPE_FREEZE = "5";
	/**
	 * 解冻
	 */
	public static final String TRANS_TYPE_UNFREEZE = "6";
	/**
	 * 标交易查询
	 */
	public static final String TRANS_TYPE_BORROW = "7";
	/**
	 * 转账
	 */
	public static final String TRANS_TYPE_TRANSFER = "8";
	/**
	 * 充值
	 */
	public static final String TRANS_TYPE_RECHARGE = "9";
	
	/**
	 * 查询订单号
	 */
	private String loanNo;
	
	/**
	 * 交易时间
	 */
	private String loanDate;
	
	/**
	 * 交易类型
	 * 1：放款，2：还款，3：投资，4：取现，5：冻结
	 * 6：解冻,7:标交易查询，8：转账，9：充值
	 */
	private String transType;
	
	/**
	 * 交易金额
	 */
	private String amount;
	
	/**
	 * 商户平台用户唯一标识
	 */
	private String userId;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 项目编号
	 */
	private String projectId;
	
	/**
	 * 交易状态（中文）
	 */
	private String transferMsg;
	
	/**
	 * 交易返回流水
	 */
	private String trxId;
	
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"loanNo", "loanDate", "transType","reqExt", "merPriv", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc", 
			"loanNo","loanDate", "transType", "amount","userId","realName","cardId","projectId", "reqExt", "merPriv","transferMsg", "trxId", "signInfo" };
	
	
	
	public UfxQueryTransferModel() {
		super();
		this.setService(UfxConstant.QUERY_TRANSFER);
	}

	/**
	 * 获取查询订单号
	 * @return loanNo
	 */
	public String getLoanNo() {
		return loanNo;
	}

	/**
	 * 设置查询订单号
	 * @param  loanNo
	 */
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	/**
	 * 获取交易时间
	 * @return loanDate
	 */
	public String getLoanDate() {
		return loanDate;
	}

	/**
	 * 设置交易时间
	 * @param  loanDate
	 */
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	/**
	 * 获取交易类型 1：放款，2：还款，3：投资，4：取现，5：冻结，6：解冻，7:标交易查询，8：转账，9：充值
	 * @return transType
	 */
	public String getTransType() {
		return transType;
	}

	/**
	 * 设置交易类型 1：放款，2：还款，3：投资，4：取现，5：冻结，6：解冻，7:标交易查询，8：转账，9：充值
	 * @param  transType
	 */
	public void setTransType(String transType) {
		this.transType = transType;
	}

	/**
	 * 获取交易金额
	 * @return amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * 设置交易金额
	 * @param  amount
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * 获取商户平台用户唯一标识
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置商户平台用户唯一标识
	 * @param  userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取真实姓名
	 * @return realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名
	 * @param  realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
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
	 * @param  cardId
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 获取项目编号
	 * @return projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置项目编号
	 * @param  projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取交易状态（中文）
	 * @return transferMsg
	 */
	public String getTransferMsg() {
		return transferMsg;
	}

	/**
	 * 设置交易状态（中文）
	 * @param  transferMsg
	 */
	public void setTransferMsg(String transferMsg) {
		this.transferMsg = transferMsg;
	}

	/**
	 * 获取交易返回流水
	 * @return trxId
	 */
	public String getTrxId() {
		return trxId;
	}

	/**
	 * 设置交易返回流水
	 * @param  trxId
	 */
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}

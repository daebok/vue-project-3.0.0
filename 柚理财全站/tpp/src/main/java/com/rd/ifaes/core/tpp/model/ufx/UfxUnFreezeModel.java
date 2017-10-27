package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 账户解冻model
 * @author xhf
 * @version 1.0
 * @date 2015年12月21日 下午2:05:51 
 */
@SuppressWarnings("serial")
public class UfxUnFreezeModel extends UfxBaseModel {
	
	/**
	 * 资金解冻，不依赖其它业务
	 */
	public static final String FREEZE_NORMAL = "00";
	
	/**
	 * 取现解冻
	 */
	public static final String FREEZE_CASH = "01";
	
	/**
	 * 还款解冻
	 */
	public static final String FREEZE_REPAY = "02";
	/** 提现ufx变更处理 2016-05-27 end **/
	
	/**
	 * 解冻流水号
	 */
	private String freezeNo;
	
	/**
	 * 解冻流水号
	 */
	private String unFreezeType;
	
	/**
	 * 账户余额
	 */
	private String balance;
	
	/**
	 * 冻结余额
	 */
	private String frzBal;
	
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"freezeNo", "userCustId", "amount", "userId", "unFreezeType", "projectId","reqExt", "merPriv", "returnUrl", 
			"notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay", "respCode", "respDesc",
			"freezeNo", "userCustId", "amount", "balance", "frzBal", "unFreezeType", "projectId", "reqExt", "merPriv", "notifyUrl", "signInfo" };

	/**
	 * 构造函数
	 */
	public UfxUnFreezeModel() {
		super();
		this.setService(UfxConstant.UNFREEZE);
	}

	/**
	 * 获取解冻流水号
	 * @return freezeNo
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**
	 * 设置解冻流水号
	 * @param freezeNo
	 */
	public void setFreezeNo(final String freezeNo) {
		this.freezeNo = freezeNo;
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
	 * 获取解冻类型
	 * @return
	 */
	public String getUnFreezeType() {
		return unFreezeType;
	}

	/**
	 * 设置解冻类型
	 * @param unFreezeType
	 */
	public void setUnFreezeType(final String unFreezeType) {
		this.unFreezeType = unFreezeType;
	}

	/**
	 * 获得剩余金额
	 * @return
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * 设置剩余金额
	 * @param balance
	 */
	public void setBalance(final String balance) {
		this.balance = balance;
	}

	/**
	 * 获取冻结金额
	 * @return
	 */
	public String getFrzBal() {
		return frzBal;
	}

	/**
	 * 设置冻结金额
	 * @param frzBal
	 */
	public void setFrzBal(final String frzBal) {
		this.frzBal = frzBal;
	}

}

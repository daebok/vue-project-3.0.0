package com.rd.ifaes.core.tpp.model.ufx;

import java.math.BigDecimal;

/**
 * 充值回调业务处理model
 * @author xhf
 * @version 3.0
 * @date 2016年7月3日 下午4:48:47
 */
public class TppRechargeModel {
	
	/**订单号**/
	private String ordNo;
	
	/**用户承担的手续费**/
	private BigDecimal rechargeFee;
	
	/**充值金额**/
	private BigDecimal rechargeMoney;
	
	/**到账金额**/
	private BigDecimal realAmount;
	
	/**用户号**/
	private String userCustId;
	
	/***充值手续费是否垫付**/
	private boolean advanceFee;
	
	/***ufx处理流水号**/
	private String tradeNo;
	
	/***支付方式**/
	private String payway;
	
	/***失败回调信息**/
	private String respDesc;
	
	/***充值垫付手续费金额**/
	private BigDecimal advanceFeeAmount;
	
	/**
	 * 构造函数
	 */
	public TppRechargeModel() {
		super();
	}

	/**
	 * 全参构造函数
	 */
	public TppRechargeModel(final String ordNo, final BigDecimal rechargeFee,
			final BigDecimal rechargeMoney, final BigDecimal realAmount, final String userCustId,
			final boolean advanceFee, final String tradeNo, final String payway, final String respDesc) {
		super();
		this.ordNo = ordNo;
		this.rechargeFee = rechargeFee;
		this.rechargeMoney = rechargeMoney;
		this.realAmount = realAmount;
		this.userCustId = userCustId;
		this.advanceFee = advanceFee;
		this.tradeNo = tradeNo;
		this.payway = payway;
		this.respDesc = respDesc;
	}

	/**
	 * 获得订单号
	 * @return
	 */
	public String getOrdNo() {
		return ordNo;
	}

	/**
	 * 设置订单号
	 * @return
	 */
	public void setOrdNo(final String ordNo) {
		this.ordNo = ordNo;
	}

	/**
	 * 获得充值金额
	 * @return
	 */
	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}

	/**
	 * 设置充值金额
	 * @param rechargeMoney
	 */
	public void setRechargeMoney(final BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	/**
	 * 获得实际到账金额
	 * @return
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}

	/**
	 * 设置实际到账金额
	 * @return
	 */
	public void setRealAmount(final BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * 获取用户userCustId
	 * @return
	 */
	public String getUserCustId() {
		return userCustId;
	}

	/**
	 * 设置用户userCustId
	 * @return
	 */
	public void setUserCustId(final String userCustId) {
		this.userCustId = userCustId;
	}
	
	/**
	 * 判断充值手续费是否垫付
	 * @return
	 */
	public boolean isAdvanceFee() {
		return advanceFee;
	}

	/**
	 * 设置充值手续费是否垫付
	 * @return
	 */
	public void setAdvanceFee(final boolean advanceFee) {
		this.advanceFee = advanceFee;
	}

	/**
	 * ufx处理流水号
	 * @return
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置ufx处理流水号
	 * @param tradeNo
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取支付方式
	 * @return
	 */
	public String getPayway() {
		return payway;
	}

	/**
	 * 设置支付方式
	 * @param payway
	 */
	public void setPayway(final String payway) {
		this.payway = payway;
	}

	/**
	 * 获取用户承担的手续费
	 * @return
	 */
	public BigDecimal getRechargeFee() {
		return rechargeFee;
	}

	/**
	 * 设置用户承担的手续费
	 * @return
	 */	
	public void setRechargeFee(final BigDecimal rechargeFee) {
		this.rechargeFee = rechargeFee;
	}

	/**
	 * 
	 * 获取失败回调信息
	 * @return
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * 设置失败回调信息
	 * @return
	 */	
	public void setRespDesc(final String respDesc) {
		this.respDesc = respDesc;
	}

	public BigDecimal getAdvanceFeeAmount() {
		return advanceFeeAmount;
	}

	public void setAdvanceFeeAmount(BigDecimal advanceFeeAmount) {
		this.advanceFeeAmount = advanceFeeAmount;
	}
	
}

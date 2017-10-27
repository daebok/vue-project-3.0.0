package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现记录模型
 * @author yoseflin
 */
public class CashLogItemModel {

	/**
	 * 添加IP
	 */ 
	private String	addIp;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 取现金额 
	 */ 
	private BigDecimal	amount;	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 备注 
	 */ 
	private String	remark;
	/**
	 * 所属银行
	 */
	private String	bankCode;
	 /**
	  * 取现银行卡号
	  */
	private String	cardId;
	/**
	 * 取现手续费
	 */ 
	private BigDecimal	cashFee;
	/**
	 * 取现订单号（UFX流水号）
	 */ 
	private String	cashNo;	
	/**
	 * 手续费承担方
	 */
	private String feeBearTip;
	/**
	 * 核查原因 
	 */ 
	private String handleReason;
	/**
	 * 是否垫付手续费（1：需要 0：不需要）
	 */ 
	private String	isAdvance;
	/**
	 * 实际操作金额 
	 */
	private BigDecimal	money;
	/**
	 * 是否需要审核（1：需要 0：不需要）
	 */ 
	private String	needAudit;
	/**
	 * 订单号（平台本地生成订单号）
	 */ 
	private String	orderNo;
	/** 
	 * 0：提现申请，1：提现处理中，2：提现待审核， 3：提现成功， 4：提现失败 
	 */ 
	private String	status;	
	/**
	 * 状态说明
	 */
	private String statusStr;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 用户id
	 */
	private String userId;
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public BigDecimal getCashFee() {
		return cashFee;
	}
	public void setCashFee(BigDecimal cashFee) {
		this.cashFee = cashFee;
	}
	public String getCashNo() {
		return cashNo;
	}
	public void setCashNo(String cashNo) {
		this.cashNo = cashNo;
	}
	public String getFeeBearTip() {
		return feeBearTip;
	}
	public void setFeeBearTip(String feeBearTip) {
		this.feeBearTip = feeBearTip;
	}
	public String getHandleReason() {
		return handleReason;
	}
	public void setHandleReason(String handleReason) {
		this.handleReason = handleReason;
	}
	public String getIsAdvance() {
		return isAdvance;
	}
	public void setIsAdvance(String isAdvance) {
		this.isAdvance = isAdvance;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getNeedAudit() {
		return needAudit;
	}
	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}

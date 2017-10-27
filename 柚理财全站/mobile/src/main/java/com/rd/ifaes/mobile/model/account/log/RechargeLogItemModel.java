package com.rd.ifaes.mobile.model.account.log;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值记录条目模型
 * @author yoseflin
 */
public class RechargeLogItemModel {

	/**
	 * 记录时间
	 */
	private Date createTime;
	
	/**
	 * UFX处理流水号
	 */
	private String tradeNo;
	
	/**
	 * 交易状态说明
	 */
    private String statusStr;
    /**
     * 交易 金额 
     */ 
	private BigDecimal	amount;	
	/**
	 * 备注 
	 */ 
	private String	remark;
	/**
	 * 支付方式(0：网银充值  2：快捷支付 3：线下充值 ) 
	 */ 
	private String	payWay;	
	/**
	 * 订单号
	 */ 
	private String	orderNo;
	//--------------------------华丽分割线----------------------------------
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
}

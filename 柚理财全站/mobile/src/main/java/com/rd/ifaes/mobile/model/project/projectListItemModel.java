/**
 * 标投资记录列表条目
 */
package com.rd.ifaes.mobile.model.project;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lgx
 * 申购记录列表条目
 */
public class projectListItemModel {

	/**
	 * 用户名（马赛克）
	 */
	private String userName;
	
	/**
	 * 申购金额
	 */
	private BigDecimal money;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getInvestChannel() {
		return investChannel;
	}
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getRedenvelopeAmount() {
		return redenvelopeAmount;
	}
	public void setRedenvelopeAmount(BigDecimal redenvelopeAmount) {
		this.redenvelopeAmount = redenvelopeAmount;
	}
	public BigDecimal getRateCouponVal() {
		return rateCouponVal;
	}
	public void setRateCouponVal(BigDecimal rateCouponVal) {
		this.rateCouponVal = rateCouponVal;
	}
	private String investChannel; /* 投资渠道 1 PC 2 APP 3 微信 */
	private Date createTime; /* 添加时间 */
	/**
	 * 使用红包总金额（有效投资金额-用户实付资金）
	 */
	private  BigDecimal redenvelopeAmount;
	/**
	 * 加息劵数值
	 */
	private  BigDecimal rateCouponVal ;
	
}

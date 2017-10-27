/**
 * 用户基本信息
 */
package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;

/**
 * @author yoseflin 用户基本信息
 */
public class AccountBasicModel {

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 马赛克后的手机号码
	 */
	private String hideMobilePhone;

	/**
	 * 可用金额
	 */
	private BigDecimal useMoney;

	/**
	 * 账户金额
	 */
	private BigDecimal totalMoney;

	/**
	 * 冻结金额
	 */
	private BigDecimal noUseMoney;

	/**
	 * 累计收益
	 */
	private BigDecimal earnAmount;

	/**
	 * 未读消息总数
	 */
	private int unreadMessageCount;

	// --------------------------华丽分割线----------------------------------

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHideMobilePhone() {
		return hideMobilePhone;
	}

	public void setHideMobilePhone(String hideMobilePhone) {
		this.hideMobilePhone = hideMobilePhone;
	}

	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getNoUseMoney() {
		return noUseMoney;
	}

	public void setNoUseMoney(BigDecimal noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	public BigDecimal getEarnAmount() {
		return earnAmount;
	}

	public void setEarnAmount(BigDecimal earnAmount) {
		this.earnAmount = earnAmount;
	}

	public int getUnreadMessageCount() {
		return unreadMessageCount;
	}

	public void setUnreadMessageCount(int unreadMessageCount) {
		this.unreadMessageCount = unreadMessageCount;
	}

}

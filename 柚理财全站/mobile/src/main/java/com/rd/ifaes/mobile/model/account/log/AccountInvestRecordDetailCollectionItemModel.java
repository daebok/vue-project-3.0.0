package com.rd.ifaes.mobile.model.account.log;

import java.util.Date;

/**
 * 投资信息中的回款详情
 * @author yoseflin
 */
public class AccountInvestRecordDetailCollectionItemModel {

	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 金额
	 */
	private double amount;
	
	/**
	 * 状态: 还款状态 0未收款 1已收款
	 */
	private int status;
	
	/**
	 * 期数
	 */
	private int period;

	//--------------------------华丽分割线----------------------------------
	
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusStr() {
		String [] strs = { "未收款" , "已收款" };
		return strs[status];
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	
}

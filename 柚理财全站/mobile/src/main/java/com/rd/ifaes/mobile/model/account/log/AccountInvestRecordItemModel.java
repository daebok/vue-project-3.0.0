package com.rd.ifaes.mobile.model.account.log;

import java.util.Date;
/**
 * @author shenliang
 * 用户投资记录（普通产品）
 */

public class AccountInvestRecordItemModel {
	
	/**
	 * 标的ID
	 */
	private String id;
	
	/**
	 * 标的名称
	 */
	private String name;
	
	/**
	 * 标的金额
	 */
	private double money;
	
	/**
	 * 投资时间
	 */
	private Date starTime;
	
	/**
	 * 投资状态
	 */
	private int status;
	
	/**
	 * 投资状态（文本）
	 */
	private String statusStr;
	
	//--------------------------华丽分割线----------------------------------
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	public Date getStarTime() {
		return starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
}

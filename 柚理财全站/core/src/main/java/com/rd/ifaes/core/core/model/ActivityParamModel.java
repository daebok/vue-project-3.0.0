package com.rd.ifaes.core.core.model;

import java.util.Date;

/**
 * 活动方案 发放时需要传递的参数封装
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-06-20 11：01 
 */
public class ActivityParamModel {
	/**
	 * 奖励接收人
	 */
	private String userId;
	/**
	 * 金额（当红包等规则需要金额区间时使用），多指投资金额
	 */
	private double money; 
	/**
	 * 发放时间
	 */
	private Date date;
	/**
	 * 类型 ： 活动方案类型
	 */
	private String type;
	/**
	 * 邀请奖励 ：被邀请人
	 */
	private long beInvitedUserId;
	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getBeInvitedUserId() {
		return beInvitedUserId;
	}
	public void setBeInvitedUserId(long beInvitedUserId) {
		this.beInvitedUserId = beInvitedUserId;
	}
}

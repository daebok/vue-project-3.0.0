package com.rd.ifaes.core.tpp.model.ufx;

/**
 * 查询类接口-项目查询
 * @author xx
 * @version 1.0
 * @date 2015年12月22日 上午10:48:42
 * Copyright 杭州融都科技股份有限公司 UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
@SuppressWarnings("serial")
public class UfxQueryProjectModel extends UfxBaseModel {
	
	/**
	 * 项目发起人第三方帐号状态，01正常、02冻结
	 */
	private String userState;
	
	/**
	 * 项目在第三方的状态，
		取消-1、
		初始90、
		建标中91、
		建标成功92、
		建标失败93、
		标的锁定94、
		开标0、
		投资中1、
		还款中2、
		已还款3、
		结束4
	 */
	private String projectState;
	
	/**
	 * 项目创建时间
	 */
	private String createTime;
	
	/**
	 * 项目余额（项目剩余可投金额）
	 */
	private String balance;

	/**
	 * 获取项目发起人第三方帐号状态，01正常、02冻结
	 * @return userState
	 */
	public String getUserState() {
		return userState;
	}

	/**
	 * 设置项目发起人第三方帐号状态，01正常、02冻结
	 * @param userState
	 */
	public void setUserState(String userState) {
		this.userState = userState;
	}

	/**
	 * 获取项目在第三方的状态，取消-1、初始90、建标中91、建标成功92、建标失败93、标的锁定94、开标0、投资中1、还款中2、已还款3、结束4
	 * @return projectState
	 */
	public String getProjectState() {
		return projectState;
	}

	/**
	 * 设置项目在第三方的状态，取消-1、初始90、建标中91、建标成功92、建标失败93、标的锁定94、开标0、投资中1、还款中2、已还款3、结束4
	 * @param projectState
	 */
	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}

	/**
	 * 获取项目创建时间
	 * @return createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 设置项目创建时间
	 * @param createTime
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取项目余额（项目剩余可投金额）
	 * @return balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * 设置项目余额（项目剩余可投金额）
	 * @param balance
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
}

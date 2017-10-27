package com.rd.ifaes.common.rabbit.model;

import java.math.BigDecimal;

import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;

/**
 * 自动投标model - mq
 * @version 3.0
 * @author lh
 * @date 2016年8月4日
 */
public class MqAutoInvestModel {

	/**
	 * 投标金额
	 */
	private BigDecimal amount;
	/**
	 * 自动投标最大金额
	 */
	private BigDecimal mostAmount;
	/**
	 * 自动投标最小金额
	 */
	private BigDecimal lowestAccount;
	
	/**
	 * 自动投资结束标识
	 */
	private String flagAuto;
	
	/**
	 * 项目
	 */
	private Project project;
	
	/**
	 * 用户属性
	 */
	private UserCache userCache;
	
	/**
	 * 投资人
	 */
	private User user;
	
	private BigDecimal realAmount;
	
	private BigDecimal redEnvelope;
	
	private BigDecimal upApr;
	
	private String redUuid;
	
	private String rateUuid;
	
	/**
	 * 无参构造方法
	 */
	public MqAutoInvestModel() {
		super();
	}
	
	/**
	 * 
	 * @param amount
	 * @param mostAmount
	 * @param lowestAccount
	 * @param project
	 * @param user
	 * @param userCache
	 */
	public MqAutoInvestModel(BigDecimal amount, BigDecimal mostAmount, BigDecimal lowestAccount, Project project,
			User user,UserCache userCache) {
		super();
		this.amount = amount;
		this.realAmount = amount;
		this.mostAmount = mostAmount;
		this.lowestAccount = lowestAccount;
		this.project = project;
		this.user = user;
		this.userCache = userCache;
	}


	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getMostAmount() {
		return mostAmount;
	}

	public void setMostAmount(BigDecimal mostAmount) {
		this.mostAmount = mostAmount;
	}

	public BigDecimal getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(BigDecimal lowestAccount) {
		this.lowestAccount = lowestAccount;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "MqAutoInvestModel [amount=" + amount + ", mostAmount=" + mostAmount + ", lowestAccount=" + lowestAccount
				+ ", project=" + project + ", user=" + user + "]";
	}

	public String getFlagAuto() {
		return flagAuto;
	}

	public void setFlagAuto(String flagAuto) {
		this.flagAuto = flagAuto;
	}

	public UserCache getUserCache() {
		return userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public BigDecimal getUpApr() {
		return upApr;
	}

	public void setUpApr(BigDecimal upApr) {
		this.upApr = upApr;
	}

	public String getRedUuid() {
		return redUuid;
	}

	public void setRedUuid(String redUuid) {
		this.redUuid = redUuid;
	}

	public String getRateUuid() {
		return rateUuid;
	}

	public void setRateUuid(String rateUuid) {
		this.rateUuid = rateUuid;
	}

	public BigDecimal getRedEnvelope() {
		return redEnvelope;
	}

	public void setRedEnvelope(BigDecimal redEnvelope) {
		this.redEnvelope = redEnvelope;
	}
	
}

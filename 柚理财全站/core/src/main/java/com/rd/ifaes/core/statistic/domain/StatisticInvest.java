package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 投资统计
 * @version 3.0
 * @author xhf
 * @date 2017年2月22日
 */
public class StatisticInvest extends BaseEntity<StatisticInvest> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 投资日期
	 * 精确到天
	 */
	private Date investDate;
	
	/**
	 * 投资订单ID
	 */
	private String investId;
	
	/**
	 * 投资金额
	 */
	private BigDecimal investAmount;
	
	/**
	 * 投资地区
	 */
	private String investArea;
	
	/**
	 * 投资渠道
	 * 1-PC 2-APP 3-微信
	 */
	private String investChannel;
	
	/**
	 * 产品类型
	 * 1-变现理财，2-债权转让、其它理财类型以project_type表(type_level=1)的uuid为准
	 */
	private String projectType;
	
	/**
	 * 年利率
	 */
	private BigDecimal projectApr;
	
	/**
	 * 借款期限
	 * 以天为单位，1个月30天，1年365天
	 */
	private int projectTimeLimit;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 用户类型
	 * 1-个人用户、2-企业用户
	 */
	private String userNature;
	
	/**
	 *  实付金额=投资金额-红包金额
	 */ 
	private BigDecimal realAmount;	
	/**
	 *  是否使用了红包，0-未使用，1-已使用
	 */
	private String	isUseRed;		
	/**
	 *  是否使用了加息券，0-未使用，1-已使用
	 */
	private String	isUseRate;		
	/**
	 * 产品是否加息，0-未加息，1-已加息
	 */
	private String	isAddApr;		
	
	public StatisticInvest() {
		super();
	}

	public StatisticInvest(Date investDate) {
		super();
		this.investDate = investDate;
	}



	/**
	 * @return the 投资日期
	 */
	public Date getInvestDate() {
		return investDate;
	}

	/**
	 * @param 投资日期 the investDate to set
	 */
	public void setInvestDate(Date investDate) {
		this.investDate = investDate;
	}

	/**
	 * @return the 投资订单ID
	 */
	public String getInvestId() {
		return investId;
	}

	/**
	 * @param 投资订单ID the investId to set
	 */
	public void setInvestId(String investId) {
		this.investId = investId;
	}

	/**
	 * @return the 投资金额
	 */
	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	/**
	 * @param 投资金额 the investAmount to set
	 */
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	/**
	 * @return the 投资地区
	 */
	public String getInvestArea() {
		return investArea;
	}

	/**
	 * @param 投资地区 the investArea to set
	 */
	public void setInvestArea(String investArea) {
		this.investArea = investArea;
	}

	/**
	 * @return the 投资渠道
	 */
	public String getInvestChannel() {
		return investChannel;
	}

	/**
	 * @param 投资渠道 the investChannel to set
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}

	/**
	 * @return the 产品类型
	 */
	public String getProjectType() {
		return projectType;
	}

	/**
	 * @param 产品类型 the projectType to set
	 */
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	/**
	 * @return the 年利率
	 */
	public BigDecimal getProjectApr() {
		return projectApr;
	}

	/**
	 * @param 年利率 the projectApr to set
	 */
	public void setProjectApr(BigDecimal projectApr) {
		this.projectApr = projectApr;
	}

	/**
	 * @return the 借款期限
	 */
	public int getProjectTimeLimit() {
		return projectTimeLimit;
	}

	/**
	 * @param 借款期限 the projectTimeLimit to set
	 */
	public void setProjectTimeLimit(int projectTimeLimit) {
		this.projectTimeLimit = projectTimeLimit;
	}

	/**
	 * @return the 用户ID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param 用户ID the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the 用户类型
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * @param 用户类型 the userNature to set
	 */
	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}
	
	/**
	 * @return the 实付金额=投资金额-红包金额
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}

	/**
	 * @param 实付金额=投资金额-红包金额 the realAmount to set
	 */
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	/**
	 * @return the 是否使用了红包，0-未使用，1-已使用
	 */
	public String getIsUseRed() {
		return isUseRed;
	}

	/**
	 * @param 是否使用了红包，0-未使用，1-已使用 the isUseRed to set
	 */
	public void setIsUseRed(String isUseRed) {
		this.isUseRed = isUseRed;
	}

	/**
	 * @return the 是否使用了加息券，0-未使用，1-已使用
	 */
	public String getIsUseRate() {
		return isUseRate;
	}

	/**
	 * @param 是否使用了加息券，0-未使用，1-已使用 the isUseRate to set
	 */
	public void setIsUseRate(String isUseRate) {
		this.isUseRate = isUseRate;
	}

	/**
	 * @return the 产品是否加息，0-未加息，1-已加息
	 */
	public String getIsAddApr() {
		return isAddApr;
	}

	/**
	 * @param 产品是否加息，0-未加息，1-已加息 the isAddApr to set
	 */
	public void setIsAddApr(String isAddApr) {
		this.isAddApr = isAddApr;
	}

	@Override
	public String toString() {
		return "StatisticInvest [" + "uuid=" + uuid + ", investDate=" + investDate + ", investId=" + investId + ", investAmount=" + investAmount + ", investArea=" + investArea + ", investChannel=" + investChannel + ", projectType=" + projectType + ", projectApr=" + projectApr + ", projectTimeLimit=" + projectTimeLimit + ", userId=" + userId + ", userNature=" + userNature +  "]";
	}
	
}

package com.rd.ifaes.core.statistic.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 用户统计
 * @version 3.0
 * @author fxl
 * @date 2017年3月6日
 */
public class StatisticUser extends BaseEntity<StatisticUser> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 注册日期
	 */
	private Date registDate;
	
	/**
	 * 认证日期
	 */
	private Date realNameDate;
	
	/**
	 * 用户类型
	 */
	private String userNature;
	
	/**
	 * 注册渠道
	 */
	private String registChannel;
	
	/**
	 * 性别
	 */
	private String sex;
	
	/**
	 * 生日年份
	 */
	private int year;
	
	/**
	 * 实名认证
	 */
	private String realNameStatus;
	
	/**
	 * 地区
	 */
	private String area;

	/**
	 * 是否为投资人 0 普通用户 1 投资人 
	 */
	private String isInvestor;
	
	/**
	 * 是否为借款人 0 普通用户 1 借款人
	 */
	private String isBorrower;
	
	/** 
	 * 学历 1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他
	 */
	private String	education;
	
	/** 
	 * 婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶
	 */ 
	private String	maritalStatus;
	
	/** 
	 * 收入范围 
	 */ 
	private String	monthIncomeRange;
	
	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性registDate的值
	 * @return registDate属性值
	 */
	public Date getRegistDate() {
		return registDate;
	}

	/**
	 * 设置属性registDate的值
	 * @param  registDate属性值
	 */
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	/**
	 * 获取属性userNature的值
	 * @return userNature属性值
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * 设置属性userNature的值
	 * @param  userNature属性值
	 */
	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 获取属性registChannel的值
	 * @return registChannel属性值
	 */
	public String getRegistChannel() {
		return registChannel;
	}

	/**
	 * 设置属性registChannel的值
	 * @param  registChannel属性值
	 */
	public void setRegistChannel(String registChannel) {
		this.registChannel = registChannel;
	}

	/**
	 * 获取属性sex的值
	 * @return sex属性值
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置属性sex的值
	 * @param  sex属性值
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 获取属性year的值
	 * @return year属性值
	 */
	public int getYear() {
		return year;
	}

	/**
	 * 设置属性year的值
	 * @param  year属性值
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 获取属性realNameStatus的值
	 * @return realNameStatus属性值
	 */
	public String getRealNameStatus() {
		return realNameStatus;
	}

	/**
	 * 设置属性realNameStatus的值
	 * @param  realNameStatus属性值
	 */
	public void setRealNameStatus(String realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	/**
	 * 获取属性area的值
	 * @return area属性值
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置属性area的值
	 * @param  area属性值
	 */
	public void setArea(String area) {
		this.area = area;
	}
	
	/**
	 * 获取属性isInvestor的值
	 * @return isInvestor属性值
	 */
	public String getIsInvestor() {
		return isInvestor;
	}

	/**
	 * 设置属性isInvestor的值
	 * @param  isInvestor属性值
	 */
	public void setIsInvestor(String isInvestor) {
		this.isInvestor = isInvestor;
	}

	/**
	 * 获取属性isBorrower的值
	 * @return isBorrower属性值
	 */
	public String getIsBorrower() {
		return isBorrower;
	}

	/**
	 * 设置属性isBorrower的值
	 * @param  isBorrower属性值
	 */
	public void setIsBorrower(String isBorrower) {
		this.isBorrower = isBorrower;
	}

	/**
	 * 获取属性education的值
	 * @return education属性值
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * 设置属性education的值
	 * @param  education属性值
	 */
	public void setEducation(String education) {
		this.education = education;
	}

	/**
	 * 获取属性maritalStatus的值
	 * @return maritalStatus属性值
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * 设置属性maritalStatus的值
	 * @param  maritalStatus属性值
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * 获取属性monthIncomeRange的值
	 * @return monthIncomeRange属性值
	 */
	public String getMonthIncomeRange() {
		return monthIncomeRange;
	}

	/**
	 * 设置属性monthIncomeRange的值
	 * @param  monthIncomeRange属性值
	 */
	public void setMonthIncomeRange(String monthIncomeRange) {
		this.monthIncomeRange = monthIncomeRange;
	}

	/**
	 * 获取属性realNameDate的值
	 * @return realNameDate属性值
	 */
	public Date getRealNameDate() {
		return realNameDate;
	}

	/**
	 * 设置属性realNameDate的值
	 * @param  realNameDate属性值
	 */
	public void setRealNameDate(Date realNameDate) {
		this.realNameDate = realNameDate;
	}

	@Override
	public String toString() {
		return "StatisticUser [" + "userId=" + userId + ", registDate=" + registDate + ", userNature=" + userNature + ", registChannel=" + registChannel + ", sex=" + sex + ", year=" + year + ", realNameStatus=" + realNameStatus + ", area=" + area +  "]";
	}
	
	
}

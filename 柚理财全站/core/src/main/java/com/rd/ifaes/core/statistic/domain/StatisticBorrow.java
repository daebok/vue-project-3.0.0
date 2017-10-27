package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 借款统计
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public class StatisticBorrow extends BaseEntity<StatisticBorrow> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 借款ID
	 */
	private String projectId;
	
	/**
	 * 借款日期(成立审核日期)
	 */
	private Date borrowDate;
	
	/**
	 * 借款用户ID
	 */
	private String userId;

	/**
	 * 是否担保
	 */
	private String isVouch;
	
	/**
	 * 是否抵押
	 */
	private String isMortgage;
	
	/**
	 * 借款金额
	 */
	private BigDecimal account;
	
	/**
	 * 借款期限
	 */
	protected Integer timeLimit;
	
	/**
	 * 借款性质
	 */
	private String borrowNature;
	
	/**
	 * 借款人地区
	 */
	private String area;

	// Constructor
	public StatisticBorrow() {
	}

	public StatisticBorrow(Date borrowDate,BigDecimal account) {
		this.borrowDate = borrowDate;
		this.account = account;
	}
	/**
	 * full Constructor
	 */
	public StatisticBorrow(String projectId, Date borrowDate, String userId, String isVouch, String isMortgage, BigDecimal account, String borrowNature) {
		this.projectId = projectId;
		this.borrowDate = borrowDate;
		this.userId = userId;
		this.isVouch = isVouch;
		this.isMortgage = isMortgage;
		this.account = account;
		this.borrowNature = borrowNature;
	}
	
	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取属性borrowDate的值
	 * @return borrowDate属性值
	 */
	public Date getBorrowDate() {
		return borrowDate;
	}

	/**
	 * 设置属性borrowDate的值
	 * @param  borrowDate属性值
	 */
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

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
	 * 获取属性isVouch的值
	 * @return isVouch属性值
	 */
	public String getIsVouch() {
		return isVouch;
	}

	/**
	 * 设置属性isVouch的值
	 * @param  isVouch属性值
	 */
	public void setIsVouch(String isVouch) {
		this.isVouch = isVouch;
	}

	/**
	 * 获取属性isMortgage的值
	 * @return isMortgage属性值
	 */
	public String getIsMortgage() {
		return isMortgage;
	}

	/**
	 * 设置属性isMortgage的值
	 * @param  isMortgage属性值
	 */
	public void setIsMortgage(String isMortgage) {
		this.isMortgage = isMortgage;
	}

	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAccount() {
		return account;
	}

	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	/**
	 * 获取属性borrowNature的值
	 * @return borrowNature属性值
	 */
	public String getBorrowNature() {
		return borrowNature;
	}

	/**
	 * 设置属性borrowNature的值
	 * @param  borrowNature属性值
	 */
	public void setBorrowNature(String borrowNature) {
		this.borrowNature = borrowNature;
	}

	/**
	 * 获取属性timeLimit的值
	 * @return timeLimit属性值
	 */
	public Integer getTimeLimit() {
		return timeLimit;
	}

	/**
	 * 设置属性timeLimit的值
	 * @param  timeLimit属性值
	 */
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
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
	
	@Override
	public String toString() {
		return "StatisticBorrow [" + "uuid=" + uuid + ", projectId=" + projectId + ", borrowDate=" + borrowDate + ", userId=" + userId + ", isVouch=" + isVouch + ", isMortgage=" + isMortgage + ", account=" + account + ", borrowNature=" + borrowNature +  "]";
	}
}

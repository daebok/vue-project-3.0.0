package com.rd.ifaes.core.user.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserEarnLog
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-7-22
 */
public class UserEarnLog extends BaseEntity<UserEarnLog> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String userId;		
	/** 收益日期 */ 
	private Date earnDate;	
	/** 收益金额 */ 
	private BigDecimal earn;	
	/** 累计收益 */
	private BigDecimal total;		  

	//其他自定义属性
	
	/**
	 * 构造函数
	 */
	public UserEarnLog() {
		super();
	}

	/**
	 * 构造函数
	 */
	public UserEarnLog(final String userId, final Date earnDate) {
		super();
		this.userId = userId;
		this.earnDate = earnDate;
	}

	/**
	 * 构造函数
	 */
	public UserEarnLog(final String uuid, final String userId, final Date earnDate, 
			final BigDecimal earn, final BigDecimal total) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.earnDate = earnDate;
		this.earn = earn;
		this.total = total;
	}
	
	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取收益日期
	 * @return earnDate
	 */
	public Date getEarnDate() {
		return earnDate;
	}

	/**
	 * 设置收益日期
	 * @param  earnDate
	 */
	public void setEarnDate(final Date earnDate) {
		this.earnDate = earnDate;
	}

	/**
	 * 获取收益金额
	 * @return earn
	 */
	public BigDecimal getEarn() {
		return earn;
	}

	/**
	 * 设置收益金额
	 * @param  earn
	 */
	public void setEarn(final BigDecimal earn) {
		this.earn = earn;
	}

	/**
	 * 获取累计收益
	 * @return total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * 设置累计收益
	 * @param  total
	 */
	public void setTotal(final BigDecimal total) {
		this.total = total;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserEarnLog [" + "uuid=" + uuid + ", userId=" + userId + ", earnDate=" + earnDate + ", earn=" + earn + ", total=" + total +  "]";
	}
}

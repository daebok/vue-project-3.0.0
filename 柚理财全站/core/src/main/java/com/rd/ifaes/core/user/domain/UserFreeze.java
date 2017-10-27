package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserFreeze
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserFreeze extends BaseEntity<UserFreeze> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String	userId;	
	/** 用户可用功能标识 */ 
	private String	operation;	
	/** 添加时间 */ 
	private Date	createTime;		 

	//其他自定义属性
	

	/**
	 * 构造函数
	 */
	public UserFreeze() {
		super();
	}
	
	/**
	 * 构造函数
	 */
	public UserFreeze(final String userId, final String operation) {
		super();
		this.userId = userId;
		this.operation = operation;
	}

	/**
	 * 构造函数
	 */
	public UserFreeze(final String uuid, final String userId, final String operation, final Date createTime) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.operation = operation;
		this.createTime = createTime;
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
	 * 获取用户可用功能标识
	 * @return operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 设置用户可用功能标识
	 * @param  operation
	 */
	public void setOperation(final String operation) {
		this.operation = operation;
	}

	/**
	 * 获取添加时间
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置添加时间
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserFreeze [" + "uuid=" + uuid + ", userId=" + userId + ", operation=" + operation + ", createTime=" + createTime +  "]";
	}
}

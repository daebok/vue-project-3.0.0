package com.rd.ifaes.core.credit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 用户额度
 * author:wzy
 * date:2017-09-08
 * @author Administrator
 *
 */
public class UserCreditLine extends BaseEntity<UserCreditLine>{
	/** 用户id */
	private String userId ;
	/** 总额度 */
	private Integer totalCredit ;
	/** 可用额度 */
	private Integer useCredit ;
	/** 添加时间 */
	private Date createTime ;
	
	//自定义属性
	private String userName ;
	private String realName ;
	private String mobile ;
	private String email ;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(Integer totalCredit) {
		this.totalCredit = totalCredit;
	}
	public Integer getUseCredit() {
		return useCredit;
	}
	public void setUseCredit(Integer useCredit) {
		this.useCredit = useCredit;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserCreditLine() {
	}
	
	public UserCreditLine(String userId, Integer totalCredit, Integer useCredit, Date createTime) {
		this.userId = userId;
		this.totalCredit = totalCredit;
		this.useCredit = useCredit;
		this.createTime = createTime;
	}
	public String toString() {
		return "UserCreditLine [userId=" + userId + ", totalCredit=" + totalCredit + ", useCredit=" + useCredit
				+ ", createTime=" + createTime + "]";
	}
	
}

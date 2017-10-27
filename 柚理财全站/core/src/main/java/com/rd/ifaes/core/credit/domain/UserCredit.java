package com.rd.ifaes.core.credit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 用户额度表
 * author:wzy
 * date:2017-09-08
 * @author Administrator
 *
 */
public class UserCredit extends BaseEntity<UserCredit>{
	/** 用户id */
	private String userId ;
	/** 申请额度 */
	private Integer account ;
	/** 申请内容 */
	private String content ;
	/** 申请时间 */
	private Date createTime ;
	/** 状态 */
	private String status ;
	/** 审核内容 */
	private String remark ;
	/** 扣除额度 */
	private Integer deduct ;
	
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
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public Integer getDeduct() {
		return deduct;
	}
	public void setDeduct(Integer deduct) {
		this.deduct = deduct;
	}
	public UserCredit() {
	}
	
	public UserCredit(String userId, Integer account, String content, Date createTime, String status, String remark,
			Integer deduct, String userName, String realName, String mobile, String email) {
		this.userId = userId;
		this.account = account;
		this.content = content;
		this.createTime = createTime;
		this.status = status;
		this.remark = remark;
		this.deduct = deduct;
		this.userName = userName;
		this.realName = realName;
		this.mobile = mobile;
		this.email = email;
	}
	@Override
	public String toString() {
		return "UserCredit [userId=" + userId + ", account=" + account + ", content=" + content + ", createTime="
				+ createTime + ", status=" + status + ", remark=" + remark + ", deduct=" + deduct + ", userName="
				+ userName + ", realName=" + realName + ", mobile=" + mobile + ", email=" + email + "]";
	}
	
	
}

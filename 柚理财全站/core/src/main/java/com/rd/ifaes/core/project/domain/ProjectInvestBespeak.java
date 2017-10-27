package com.rd.ifaes.core.project.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ProjectInvestBespeak
 * 
 * @author wyw
 * @version 3.0
 * @date 2016-8-20
 */
public class ProjectInvestBespeak extends BaseEntity<ProjectInvestBespeak> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 投资预约人id */ 
	private String	projectId;		 /* 项目id */ 
	private Date	saleTime;		 /* 项目开售时间 */ 
	private String	remindStatus;		 /* 提醒状态 0未提醒 1已经提醒 */ 
	private Date	remindTime;		 /* 提醒时间 */ 
	private Date	createTime;		 /* 预约时间 */ 
	
	/**开始日期**/
	private String startSaleTime;
	/**结束日期**/
	private String endSaleTime;
	/**当前时间，数据库查询使用**/
	private Date nowTime;
	
	//其他自定义属性
	public static final String REMIND_STATUS_YES="1";     //已经提醒
	public static final String REMIND_STATUS_NO = "0";     //未提醒
	
	private String mobile; /* 用户手机号 */ 
	private String email;  /* 邮箱 */ 
	private String projectName;/* 项目名称 */ 

	// Constructor
	public ProjectInvestBespeak() {
	}

	/**
	 * full Constructor
	 */
	public ProjectInvestBespeak(String uuid, String userId, String projectId, Date saleTime, String remindStatus, Date remindTime, Date createTime) {
		setUuid(uuid);
		this.userId = userId;
		this.projectId = projectId;
		this.saleTime = saleTime;
		this.remindStatus = remindStatus;
		this.remindTime = remindTime;
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Date getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}

	public String getRemindStatus() {
		return remindStatus;
	}

	public void setRemindStatus(String remindStatus) {
		this.remindStatus = remindStatus;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ProjectInvestBespeak [" + "uuid=" + uuid + ", userId=" + userId + ", projectId=" + projectId + ", saleTime=" + saleTime + ", remindStatus=" + remindStatus + ", remindTime=" + remindTime + ", createTime=" + createTime +  "]";
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStartSaleTime() {
		return startSaleTime;
	}

	public void setStartSaleTime(String startSaleTime) {
		this.startSaleTime = startSaleTime;
	}

	public String getEndSaleTime() {
		return endSaleTime;
	}

	public void setEndSaleTime(String endSaleTime) {
		this.endSaleTime = endSaleTime;
	}

	public Date getNowTime() {
		return nowTime;
	}

	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}

	
}

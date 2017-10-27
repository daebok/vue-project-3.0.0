package com.rd.ifaes.core.project.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Buyback
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public class Buyback extends BaseEntity<Buyback> {
	
	private static final long serialVersionUID = 1L;
	
	private String	projectId;		 /* 项目ID */ 
	private String	investId;		 /* 投资记录ID */ 
	private String	userId;		 /* 申请人ID */ 
	private double	money;		 /* 赎回金额 */ 
	private Date	applyTime;		 /* 申请时间 */ 
	private String	status;		 /* 状态：0 新建  1 申请已提交   2 审批通过 3 审批不通过  4 成功 5 失败 */ 

	//其他自定义属性
	

	// Constructor
	public Buyback() {
	}

	/**
	 * full Constructor
	 */
	public Buyback(String uuid, String projectId, String investId, String userId, double money, Date applyTime, String status) {
		setUuid(uuid);
		this.projectId = projectId;
		this.investId = investId;
		this.userId = userId;
		this.money = money;
		this.applyTime = applyTime;
		this.status = status;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getInvestId() {
		return investId;
	}

	public void setInvestId(String investId) {
		this.investId = investId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Buyback [" + "uuid=" + uuid + ", projectId=" + projectId + ", investId=" + investId + ", userId=" + userId + ", money=" + money + ", applyTime=" + applyTime + ", status=" + status +  "]";
	}
}

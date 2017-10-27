package com.rd.ifaes.core.user.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserAutoInvest
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public class UserAutoInvest extends BaseEntity<UserAutoInvest> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户id */ 
	private String	ruleId;		 /* 设置规则ID */ 
	private Long	sortTime;		 /* 排序时间 */ 

	//其他自定义属性
	private String status; // 状态
	private String remark; // 错误信息

	// Constructor
	public UserAutoInvest() {
	}

	/**
	 * full Constructor
	 */
	public UserAutoInvest(String uuid, String userId, String ruleId, Long sortTime) {
		setUuid(uuid);
		this.userId = userId;
		this.ruleId = ruleId;
		this.sortTime = sortTime;
	}

	public UserAutoInvest(String status, Long sortTime) {
		this.status = status;
		this.sortTime = sortTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Long getSortTime() {
		return sortTime;
	}

	public void setSortTime(Long sortTime) {
		this.sortTime = sortTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "UserAutoInvest [" + "uuid=" + uuid + ", userId=" + userId + ", ruleId=" + ruleId + ", sortTime=" + sortTime +  "]";
	}
}

package com.rd.ifaes.core.user.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:UserInvestAutoLog
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public class UserInvestAutoLog extends BaseEntity<UserInvestAutoLog> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户id */ 
	private String	projectId;		 /* 项目id */ 
	private BigDecimal	investMoney;		 /* 投资金额 */ 
	private String	status;		 /* 状态 0处理中. 1 成功 2 失败  */ 
	private Date	investTime;		 /* 投资时间 */ 
	private String	remark;		 /* 结果备注 */ 

	//其他自定义属性

	// Constructor
	public UserInvestAutoLog() {
	}

	/**
	 * full Constructor
	 */
	public UserInvestAutoLog(String uuid, String userId, String projectId, BigDecimal investMoney, String status, Date investTime, String remark) {
		setUuid(uuid);
		this.userId = userId;
		this.projectId = projectId;
		this.investMoney = investMoney;
		this.status = status;
		this.investTime = investTime;
		this.remark = remark;
	}

	public UserInvestAutoLog(String projectId, String userId) {
		this.projectId = projectId;
		this.userId = userId;
		this.investTime = DateUtils.getNow();
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

	public BigDecimal getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(BigDecimal investMoney) {
		this.investMoney = investMoney;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "UserInvestAutoLog [" + "uuid=" + uuid + ", userId=" + userId + ", projectId=" + projectId + ", investMoney=" + investMoney + ", status=" + status + ", investTime=" + investTime + ", remark=" + remark +  "]";
	}
}

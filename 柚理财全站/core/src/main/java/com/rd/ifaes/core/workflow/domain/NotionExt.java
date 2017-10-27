package com.rd.ifaes.core.workflow.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.ifaes.common.entity.BaseEntity;
/**
 * 流程办理意见扩展表
 * @author xxb
 *
 */
public class NotionExt extends BaseEntity<NotionExt> {
	
	private static final long serialVersionUID = 2861326511322176285L;
	
	private String processInstanceId; // 流程实例id
	private String taskId; // 工作任务id
	private String activityName; // 环节名称
	private String operator; // 办理人
	private Date operateDate; // 办理时间
	private String actionName; // 操作名称
	private String remark; // 办理意见

	@JsonProperty("id")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

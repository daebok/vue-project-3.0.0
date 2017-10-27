package com.rd.ifaes.core.workflow.domain;

import java.io.Serializable;
import java.util.Date;

import org.activiti.engine.history.HistoricTaskInstance;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 流程已办任务信息
 * @author xxb
 *
 */
public class TaskExt implements Serializable {
	
	private static final long serialVersionUID = 7532945169879173785L;
	
	private String uuid;
	private String procDefId; // '流程定义id'
	private String taskDefKey; // '节点定义id'
	private String procInstId; // '实例id'
	private String executionId;// '执行id'
	private String activityName; // '环节名'
	private String parentTaskId; // '父任务id'
	private String description; // '备注'
	private String ownerId; // '签收人'
	private String assignee; // '办理人'
	private String title;  //业务标题
	private Date createTime; // '开始时间'
	private Date claimTime; // '提醒时间'
	private Date endTime; // '结束时间'
	private Long duration; // '停留时间'
	private String deleteReason; // '任务结束缘由'
	private int priority; // '优先级'
	private Date dueDate; // '过期时间'
	private String formKey; // '业务表单信息'
	private String bussnissId;  // 业务主键

	public TaskExt(){
		
	}
	
	public TaskExt(HistoricTaskInstance taskInstance){
		this.uuid = taskInstance.getId();
		this.activityName = taskInstance.getName();
		this.assignee = taskInstance.getAssignee();
		this.claimTime = taskInstance.getClaimTime();
		this.createTime = taskInstance.getCreateTime();
		this.deleteReason = taskInstance.getDeleteReason();
		this.description = taskInstance.getDescription();
		this.dueDate = taskInstance.getDueDate();
		this.duration = taskInstance.getDurationInMillis();
		this.executionId = taskInstance.getExecutionId();
		this.endTime = taskInstance.getEndTime();
		this.formKey = taskInstance.getFormKey();
		this.ownerId = taskInstance.getOwner();
		this.parentTaskId = taskInstance.getParentTaskId();
		this.priority = taskInstance.getPriority();
		this.procInstId = taskInstance.getProcessInstanceId();
		this.procDefId = taskInstance.getProcessDefinitionId();
		this.taskDefKey = taskInstance.getTaskDefinitionKey();
		//RuntimeService runtimeService = (RuntimeService)SpringContextHolder.getBean("runtimeService");
		//Object title = runtimeService.getVariable(taskInstance.getProcessInstanceId(), Constant.PROCESS_BUSINESS_TITLE);
		//this.setTitle(ObjectUtils.toString(title));
	}
	
	@JsonProperty("id")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBussnissId() {
		return bussnissId;
	}

	public void setBussnissId(String bussnissId) {
		this.bussnissId = bussnissId;
	}

}

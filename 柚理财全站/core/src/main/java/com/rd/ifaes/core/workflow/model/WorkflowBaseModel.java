package com.rd.ifaes.core.workflow.model;

import java.io.Serializable;

public class WorkflowBaseModel implements Serializable {
	private static final long serialVersionUID = 241122131237889316L;
	private String processkey;
	private String processInstanceId;
	private String taskId;
	private String currentActivityName;
	private String operator;
	private String actionName;
	private String notionContent;
	private String title;

	public String getProcesskey() {
		return processkey;
	}

	public void setProcesskey(String processkey) {
		this.processkey = processkey;
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

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}


	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}


	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	
	public String getNotionContent() {
		return notionContent;
	}

	public void setNotionContent(String notionContent) {
		this.notionContent = notionContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public static enum ActionName {
		COMMIT, ROLLBACK;
	}
}

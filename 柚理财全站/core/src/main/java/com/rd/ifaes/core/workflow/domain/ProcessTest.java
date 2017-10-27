package com.rd.ifaes.core.workflow.domain;

import java.io.Serializable;

import com.rd.ifaes.common.entity.BaseEntity;
/**
 * 流程测试
 * @author xxb
 *
 */
public class ProcessTest extends BaseEntity<ProcessTest> implements Serializable {
	
	private static final long serialVersionUID = 2861326511322176285L;
	
	private String processId; // 流程实例id
	private String taskId; // 工作任务id
	private String title; // 环节名称
	private String createId; // 办理人
	private String status; // 办理意见

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}

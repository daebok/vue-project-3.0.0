package com.rd.ifaes.core.project.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.DictUtils;

/**
 * entity:VerifyLog
 * 
 * @author lh
 * @version 3.0
 * @date 2016-9-26
 */
public class ProjectVerifyLog extends BaseEntity<ProjectVerifyLog> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String	uuid;		 /* 主键 */ 
	private String	projectId;		 /* 项目ID */ 
	private String	verifyName;		 /* 处理人用户名 */ 
	private String	processNode;		 /* 处理环节 */ 
	private String	processResult;		 /* 处理结果 */ 
	private Date	createTime;		 /* 处理时间 */ 
	private String	remark;		 /* 备注 */ 
	
	//其他自定义属性
	private String operatorName;//处理人姓名
	@SuppressWarnings("unused")
	private String processNodeName;/* 处理环节名称 */

	// Constructor
	public ProjectVerifyLog() {
	}

	/**
	 * full Constructor
	 */
	public ProjectVerifyLog(String uuid, String projectId, String verifyName, String processNode, String processResult, Date createTime, String remark) {
		this.uuid = uuid;
		this.projectId = projectId;
		this.verifyName = verifyName;
		this.processNode = processNode;
		this.processResult = processResult;
		this.createTime = createTime;
		this.remark = remark;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getVerifyName() {
		return verifyName;
	}

	public void setVerifyName(String verifyName) {
		this.verifyName = verifyName;
	}

	public String getProcessNode() {
		return processNode;
	}

	public void setProcessNode(String processNode) {
		this.processNode = processNode;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	public String getProcessNodeName() {
		if(StringUtils.isNotBlank(processNode)){
			return DictUtils.getItemName(ProjectConstant.DICT_TYPE_PROCESS_NODE, processNode);
		}
		return processNode;
	}

	public void setProcessNodeName(String processNodeName) {
		this.processNodeName = processNodeName;
	}

	@Override
	public String toString() {
		return "VerifyLog [" + "uuid=" + uuid + ", projectId=" + projectId + ", verifyName=" + verifyName + ", processNode=" + processNode + ", processResult=" + processResult + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}

package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Operation
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public class Operation extends BaseEntity<Operation> {
	
	private static final long serialVersionUID = 1L;
	
	private String	code;		 /* 菜单英文标识,如:systemmange:user:page */ 
	private String	operationName;		
	private String	operationType;		 /* 操作类型：1操作  2 页面元素 （按钮、图表等）3文件  */ 
	private Integer	sort;		
	private Date	createTime;		 /* 添加时间 */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private String	remark;		 /* 备注 */ 

	//其他自定义属性
	private String permissionId;//对应权限Id

	// Constructor
	public Operation() {
	}

	/**
	 * full Constructor
	 */
	public Operation(String uuid, String code, String operationName, String operationType, Integer sort, Date createTime, String deleteFlag, String remark) {
		setUuid(uuid);
		this.code = code;
		this.operationName = operationName;
		this.operationType = operationType;
		this.sort = sort;
		this.createTime = createTime;
		this.deleteFlag = deleteFlag;
		this.remark = remark;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "Operation [" + "uuid=" + uuid + ", code=" + code + ", operationName=" + operationName + ", operationType=" + operationType + ", sort=" + sort + ", createTime=" + createTime + ", deleteFlag=" + deleteFlag + ", remark=" + remark +  "]";
	}
}

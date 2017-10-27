package com.rd.ifaes.core.sys.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:OperatorRole
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-8
 */
public class OperatorRole extends BaseEntity<OperatorRole> {
	
	private static final long serialVersionUID = 1L;
	
	private String	operatorId;		 /* 用户主键 */ 
	private String	roleId;		 /* 角色主键 */ 

	//其他自定义属性
	

	// Constructor
	public OperatorRole() {
	}

	/**
	 * full Constructor
	 */
	public OperatorRole(String uuid, String operatorId, String roleId) {
		setUuid(uuid);
		this.operatorId = operatorId;
		this.roleId = roleId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "OperatorRole [" + "uuid=" + uuid + ", operatorId=" + operatorId + ", roleId=" + roleId +  "]";
	}
}

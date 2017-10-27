package com.rd.ifaes.core.sys.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:RolePermission
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public class RolePermission extends BaseEntity<RolePermission> {
	
	private static final long serialVersionUID = 1L;
	
	private String	roleId;		
	private String	permissionId;		

	// Constructor
	public RolePermission() {
	}

	/**
	 * full Constructor
	 */
	public RolePermission(String id, String roleId, String permissionId) {
		setUuid(id);
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	@Override
	public String toString() {
		return "RolePermission [" + "uuid=" + getUuid() + ", roleId=" + roleId + ", permissionId=" + permissionId +  "]";
	}
}

package com.rd.ifaes.core.sys.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Permission
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public class Permission extends BaseEntity<Permission> {
	
	private static final long serialVersionUID = 1L;
	
	private String	code;		 /* 权限标识,如: systemmange:user:page */ 
	private String	permissionName;		 /* 权限名称 */ 
	private String	menuId;		 /* 菜单ID */ 
	private String	operationId;		 /* 功能操作ID */
	private String  operationName;   /* 权限（简称）名称 */
	//其他自定义属性
	

	// Constructor
	public Permission() {
	}
	
	/**
	 * full Constructor
	 */
	public Permission(String uuid, String code, String permissionName, String menuId, String operationId) {
		setUuid(uuid);
		this.code = code;
		this.permissionName = permissionName;
		this.menuId = menuId;
		this.operationId = operationId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	@Override
	public String toString() {
		return "Permission [" + "uuid=" + uuid + ", code=" + code + ", permissionName=" + permissionName + ", menuId=" + menuId + ", operationId=" + operationId +  "]";
	}

}

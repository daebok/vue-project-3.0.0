package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Permission;

/**
 * Dao Interface:PermissionMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface PermissionMapper extends BaseMapper<Permission> {
	
	/**
	 * 根据角色查询对应的权限
	 * @param id
	 * @return
	 */
	List<String> findIdListByRoleId(String roleId);
	
	/**
	 * 根据codes 查找 permissionId集合
	 * @param codes
	 * @return
	 */
	List<String> findIdListByCodes(String [] codes);
	
	/**
	 * 根据菜单ID删除菜单操作权限
	 * @param menuIds
	 */
	void deleteBatchByMenuIds(String [] menuIds);
	
	/**
	 * 根据菜单查询对于的权限
	 * @param menuId
	 * @return
	 */
	List<Permission> findListByMenuId(String menuId);
	
	
	/**
     * 查询所有权限（按钮）
	 * @param  
	 * @return List<Permission>
	 * @author xxb
	 * @date 2016年10月31日
	 */
	List<Permission> findAllOperation();
	
	/**
	 *  查询角色对应的权限
	 * @param roleId
	 * @return List<Permission>
	 * @author xxb
	 * @date 2016年10月31日
	 */
	List<Permission> findListByRole(String roleId);

}
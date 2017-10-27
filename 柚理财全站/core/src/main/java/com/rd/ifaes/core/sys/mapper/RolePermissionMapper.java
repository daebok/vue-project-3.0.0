package com.rd.ifaes.core.sys.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.RolePermission;

/**
 * Dao Interface:RolePermissionMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
	
	/**
	 * 根据角色id删除
	 * @param roleId
	 * @return
	 */
	int deleteByRoleId(String roleId);
	

	int deleteBatchByMenuIds(String[] menusId);

}
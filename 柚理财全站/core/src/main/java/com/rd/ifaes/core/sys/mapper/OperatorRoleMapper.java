package com.rd.ifaes.core.sys.mapper;

import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.OperatorRole;

/**
 * Dao Interface:operatorRoleMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public interface OperatorRoleMapper extends BaseMapper<OperatorRole> {

	/**
	 * 根据roleId删除对象
	 * @param roleId
	 * @return
	 */
	int deleteRoleOperators(String roleId);
	
	/**
	 * 删除选中的记录
	 * @param list
	 * @return
	 */
	int deleteSelected(Map<String, Object> params);
	
	/**
	 * 删除用户角色
	 * @param userId
	 * @return
	 */
	int deleteOperatorRoles(String userId);
}
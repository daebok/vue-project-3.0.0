package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Role;

/**
 * Dao Interface:RoleMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findByRoalName(String roleName);
	
	void deleteChildren(String[] parentIds);
	
	int checkDefaultRole(String[] ids);

	/**
	 * 根据用户id获取权限名字
	 * @author ZhangBiao
	 * @date 2016年10月26日
	 * @param uuid
	 * @return
	 */
	List<Role> findListByOperatorId(String operatorId);

}
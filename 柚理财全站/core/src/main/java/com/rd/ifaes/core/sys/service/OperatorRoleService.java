package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.OperatorRole;

/**
 * Service Interface:operatorRoleService
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public interface OperatorRoleService extends BaseService<OperatorRole>{
	
	/**
	 * 保存角色下的用户
	 * @param roleId
	 * @param operatorIds
	 * @return
	 */
	public int saveRoleOperators(String roleId, String[] operatorIds);
	
	/**
	 * 删除选中的记录
	 * @param roleId
	 * @param operatorIds
	 * @return
	 */
	public int deleteSelected(String roleId, String[] operatorIds);

}
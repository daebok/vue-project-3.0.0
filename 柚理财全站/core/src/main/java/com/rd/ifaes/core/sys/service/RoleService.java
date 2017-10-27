package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.common.util.ZTreeNode;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Role;
import com.rd.ifaes.core.sys.model.RolePermModel;

/**
 * Service Interface:RoleService
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface RoleService extends BaseService<Role>{
	
	/**
	 * 取得角色权限树
	 * @param id
	 * @return
	 */
	List<RolePermModel> findRolePermTree(String id);
	
	/**
	 * 保存角色授权
	 * @param roleId
	 * @param nids
	 */
	void saveRolePermissions(String roleId, String [] ids);
	
	/**
	 * 根据标识获取角色
	 * @author ywt
	 * @date 2016-10-13
	 * @param code
	 */
	Role findRoleByCode(String code);
	
	
	void deleteChildren(String[] ids);

	/**
	 * 根据用户id获取权限名字
	 * @author ZhangBiao
	 * @date 2016年10月26日
	 * @param uuid
	 * @return
	 */
	List<Role> findListByOperatorId(String uuid);
	
	/**
	 *  取得角色权限Z树数据项
	 * @param  
	 * @return List<ZTreeNode>
	 * @author xxb
	 * @date 2016年10月29日
	 */
	List<ZTreeNode> findAuthTree(String roleId);

}
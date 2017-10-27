package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Operator;

/**
 * Dao Interface:OperatorMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-30
 */
public interface OperatorMapper extends BaseMapper<Operator> {
	
	/**
	 * 查询登录用户
	 * @param loginName	用户登录名
	 * @return
	 */
	Operator getByLoginName(String loginName);
	
	
	/**
	 * 用户角色标示集
	 * @param id
	 * @return
	 */
	List<String> findRoles(String id);
	
	/**
	 * 用户权限标示集
	 * @param id
	 * @return
	 */
	List<String> findPermissions(String id);
	
	
	/**
	 * 查询改角色下的用户列表
	 * @param user
	 * @return
	 */
	List<Operator> findRoleOperators(Operator user);
	
	/**
	 * 查询在线客服
	 * @return
	 */
	List<Operator> findOnlineServer();

	/**
	 * 修改后台用户密码
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 * @param operator
	 */
	void updatePwd(Operator operator);

	/**
	 * 查询组织机构中后台用户数
	 * @author ZhangBiao
	 * @date 2016年11月3日
	 * @param ids
	 * @return
	 */
	int getNumByOrgIds(@Param("ids") String[] ids);
	/**
	 * 查询是否是首次登录
	 * @param operator
	 * @return
	 */
	int findIsFirstLogin(Operator operator);
	/**
	 * 根据状态查询后台用户
	 */
	List<Operator> findByStatus(String status);
}
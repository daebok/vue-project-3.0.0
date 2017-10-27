package com.rd.ifaes.core.user.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserFreeze;

/**
 * Dao Interface:UserFreezeMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserFreezeMapper extends BaseMapper<UserFreeze> {

	/**
	 * 
	 * 根据用户id查询冻结信息
	 * @author zb
	 * @date 2016年7月27日
	 * @param userId
	 * @return
	 */
	UserFreeze getByUserId(String userId);

	/**
	 * 
	 * 根据userId查询操作列表
	 * @author xhf
	 * @date 2016年7月30日
	 * @param userId
	 * @return
	 */
	List<String> findOperationByUserId(String userId);
	
	/**
	 * 
	 * 根据用户ID删除记录
	 * @author xhf
	 * @date 2016年7月30日
	 * @param userId
	 */
	void deleteByUserId(String userId);

	/**
	 * 根据用户列表获取冻结信息
	 * @param list
	 * @return
	 */
	String[] getUserFreezeByUserList(List<User> list);
}
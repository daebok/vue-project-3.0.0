package com.rd.ifaes.core.user.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserFreeze;
import com.rd.ifaes.core.user.model.UserModel;

/**
 * Service Interface:UserFreezeService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserFreezeService extends BaseService<UserFreeze>{

	/**
	 * 校验用户某个操作是否被冻结
	 * @param userId
	 * @param operation
	 * @return
	 */
	boolean isFreezed(String userId, String operation);
	
	/**
	 * 根据用户userId查询冻结功能列表
	 * @author xhf
	 * @date 2016年7月30日
	 * @param userId
	 * @return
	 */
	List<String> findOperationByUserId(String userId);
	
	/**
	 * 冻结用户
	 * @author xhf
	 * @date 2016年7月30日
	 * @param model
	 * @return
	 */
	void freeze(UserModel model);

	/**
	 * 根据用户列表获取冻结信息
	 * @param list
	 * @return
	 */
	String[] getUserFreezeByUserList(List<User> list);
}
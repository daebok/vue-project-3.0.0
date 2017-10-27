package com.rd.ifaes.core.user.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserInvite;

/**
 * Service Interface:UserInviteService
 * @author wyw
 * @version 3.0
 * @date 2016-8-17
 */
public interface UserInviteService extends BaseService<UserInvite>{
	/**
	 * 
	 * 获取用户直接邀请好友列表
	 * @author wyw
	 * @date 2016-8-17
	 * @param user_id
	 * @return
	 */
	Page<UserInvite> findFirstInvite(UserInvite userInvite);
	
	/**
	 * 
	 *  获取用户二级邀请好友列表
	 * @author wyw
	 * @date 2016-8-17
	 * @param user_id
	 * @return
	 */
	Page<UserInvite> findSecondInvite(UserInvite userInvite);
	
	/**
	 * 
	 * 获取用户的邀请者
	 * @author wyw
	 * @date 2016-8-17
	 * @return
	 */
	User getInviteUser(String userId);
	
	/**
	 * 
	 * 邀请统计
	 * @author wyw
	 * @date 2016-8-22
	 * @param userInvite
	 * @return
	 */
	Page<UserInvite> findInvitestatistics(UserInvite userInvite);
	
}
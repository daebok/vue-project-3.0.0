package com.rd.ifaes.core.user.mapper;

import java.util.List;

import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserInviteMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-8-17
 */
public interface UserInviteMapper extends BaseMapper<UserInvite> {

	
	
	/**
	 * 
	 * 获取用户的二级人脉
	 * @author wyw
	 * @date 2016-8-19
	 * @param model
	 * @return
	 */
	List<UserInvite> findSecondInvite(UserInvite userInvite);
	
	/**
	 * 
	 * 邀请统计
	 * @author wyw
	 * @date 2016-8-22
	 * @param userInvite
	 * @return
	 */
	List<UserInvite> findInvitestatistics(UserInvite userInvite);
	
	
	
}
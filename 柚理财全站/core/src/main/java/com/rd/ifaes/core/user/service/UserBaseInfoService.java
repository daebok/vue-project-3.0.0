package com.rd.ifaes.core.user.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserBaseInfo;

/**
 * Service Interface:UserBaseInfoService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserBaseInfoService extends BaseService<UserBaseInfo>{

	/**
	 * 
	 * 根据用户ID查询用户基本信息
	 * @author xhf
	 * @date 2016年8月26日
	 * @param userId
	 * @return
	 */
	UserBaseInfo findByUserId(String userId);
}
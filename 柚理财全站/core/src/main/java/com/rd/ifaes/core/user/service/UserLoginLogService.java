package com.rd.ifaes.core.user.service;

import java.util.Date;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserLoginLog;

/**
 * Service Interface:UserLoginLogService
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserLoginLogService extends BaseService<UserLoginLog>{

	/**
	 * 获取最近一次登录时间
	 */
	Date getLastLoginTime(String userId);
}
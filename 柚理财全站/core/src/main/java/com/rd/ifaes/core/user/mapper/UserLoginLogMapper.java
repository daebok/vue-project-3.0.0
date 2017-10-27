package com.rd.ifaes.core.user.mapper;

import java.util.Date;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserLoginLog;

/**
 * Dao Interface:UserLoginLogMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

	/**
	 * 获取最近登录时间
	 */
	Date getLastLoginTime(String userId); 
}
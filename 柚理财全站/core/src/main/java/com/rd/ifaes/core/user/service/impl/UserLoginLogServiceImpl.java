package com.rd.ifaes.core.user.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.user.domain.UserLoginLog;
import com.rd.ifaes.core.user.mapper.UserLoginLogMapper;
import com.rd.ifaes.core.user.service.UserLoginLogService;

/**
 * 用户登录日志处理类
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userLoginLogService") 
public class UserLoginLogServiceImpl  extends BaseServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService{
	
	@Override
	public Date getLastLoginTime(final String userId){
		return dao.getLastLoginTime(userId);
	}
	

}
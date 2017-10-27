package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.mapper.UserActivityAwardLogMapper;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;

/**
 * ServiceImpl:UserActivityAwardLogServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-8-15
 */
@Service("userActivityAwardLogService") 
public class UserActivityAwardLogServiceImpl  extends BaseServiceImpl<UserActivityAwardLogMapper, UserActivityAwardLog> implements UserActivityAwardLogService{

	@Override
	public BigDecimal getAwardSum(UserActivityAwardLog userActivityAwardLog) {
		return dao.getAwardSum(userActivityAwardLog);
	}
	
	@Override
	public BigDecimal countRate(UserActivityAwardLog userActivityAwardLog) {
		return dao.countRate(userActivityAwardLog);
	}
}
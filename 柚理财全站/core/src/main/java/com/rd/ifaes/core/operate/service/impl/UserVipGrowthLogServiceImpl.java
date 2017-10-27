package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.operate.domain.UserVipGrowthLog;
import com.rd.ifaes.core.operate.mapper.UserVipGrowthLogMapper;
import com.rd.ifaes.core.operate.service.UserVipGrowthLogService;

/**
 * ServiceImpl:UserVipGrowthLogServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-8-2
 */
@Service("userVipGrowthLogService") 
public class UserVipGrowthLogServiceImpl  extends BaseServiceImpl<UserVipGrowthLogMapper, UserVipGrowthLog> implements UserVipGrowthLogService{

	/**
	 * 获取用户在成为vip之前的最大成长值
	 */
	@Override
	public BigDecimal getMaxGrowth(final String userId) {
		BigDecimal maxGrowth = dao.getMaxGrowth(userId);
		return null==maxGrowth?BigDecimal.ZERO:maxGrowth;
	}

}
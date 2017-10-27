package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.operate.domain.UserVipGrowthLog;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserVipGrowthLogService
 * @author 
 * @version 3.0
 * @date 2016-8-2
 */
public interface UserVipGrowthLogService extends BaseService<UserVipGrowthLog>{
	/**
	 * 
	 * 获取用户在成为vip之前的最大成长值
	 * @author wyw
	 * @date 2016-8-3
	 * @param userId
	 * @return
	 */
	BigDecimal getMaxGrowth(final String userId);
}
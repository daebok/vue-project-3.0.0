package com.rd.ifaes.core.operate.mapper;

import java.math.BigDecimal;

import com.rd.ifaes.core.operate.domain.UserVipGrowthLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserVipGrowthLogMapper
 * @author 
 * @version 3.0
 * @date 2016-8-2
 */
public interface UserVipGrowthLogMapper extends BaseMapper<UserVipGrowthLog> {
	
	/**
	 * 获取用户的最大成长值
	 * @author wyw
	 * @date 2016-8-30
	 * @param userId
	 * @return
	 */
	BigDecimal getMaxGrowth(String userId);
}
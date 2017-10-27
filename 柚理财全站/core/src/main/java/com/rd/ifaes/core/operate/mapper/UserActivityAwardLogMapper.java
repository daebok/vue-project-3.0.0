package com.rd.ifaes.core.operate.mapper;

import java.math.BigDecimal;

import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserActivityAwardLogMapper
 * @author 
 * @version 3.0
 * @date 2016-8-15
 */
public interface UserActivityAwardLogMapper extends BaseMapper<UserActivityAwardLog> {
	
	/**
	 * 
	 * 计算奖励总额
	 * @author wyw
	 * @date 2016-9-10
	 * @param userActivityAwardLog
	 * @return
	 */
	BigDecimal getAwardSum(UserActivityAwardLog userActivityAwardLog);
	
	/**
	 * 统计奖励加息卷数量
	 * @author fxl
	 * @date 2016年10月10日
	 * @param userActivityAwardLog
	 * @return
	 */
	BigDecimal countRate(UserActivityAwardLog userActivityAwardLog);
}
package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;

/**
 * Service Interface:UserActivityAwardLogService
 * @author 
 * @version 3.0
 * @date 2016-8-15
 */
public interface UserActivityAwardLogService extends BaseService<UserActivityAwardLog>{
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
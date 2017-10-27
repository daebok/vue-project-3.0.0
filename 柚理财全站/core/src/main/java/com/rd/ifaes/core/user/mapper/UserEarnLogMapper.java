package com.rd.ifaes.core.user.mapper;

import java.math.BigDecimal;

import com.rd.ifaes.core.user.domain.UserEarnLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:UserEarnLogMapper
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserEarnLogMapper extends BaseMapper<UserEarnLog> {

	/**
	 * 统计所有的收益金额
	 */
	BigDecimal getAllEarnAmount(UserEarnLog userEarnLog);
	
	/**
	 * 按日期统计所有的收益金额
	 */
	BigDecimal getAllEarnAmountByDate(String earnDate);
	
	/**
	 * 
	 * 查询用户指定日期收益记录
	 * @author xhf
	 * @date 2016年8月20日
	 * @param userEarnLog
	 * @return
	 */
	BigDecimal getUserEarnAmountByDate(UserEarnLog userEarnLog);
	
	/**
	 * 
	 * 查询最近一次收益记录
	 * @author xhf
	 * @date 2016年8月20日
	 * @param userId
	 * @return
	 */
	UserEarnLog getLastLog(String userId);
}
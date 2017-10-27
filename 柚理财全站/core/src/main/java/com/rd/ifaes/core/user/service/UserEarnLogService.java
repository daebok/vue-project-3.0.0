package com.rd.ifaes.core.user.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.UserEarnLog;

/**
 * Service Interface:UserEarnLogService
 * @author xhf
 * @version 3.0
 * @date 2016-7-21
 */
public interface UserEarnLogService extends BaseService<UserEarnLog>{

	/**
	 * 获取昨日收益
	 * @param userId
	 * @return
	 */
	BigDecimal yesterdayEarnAmount(String userId);

	/**
	 * 获取全部收益
	 * @param userId
	 * @return
	 */
	BigDecimal allEarnAmount(String userId);
	
	/**
	 * 
	 * 统计昨日收益
	 * @author xhf
	 * @date 2016年8月19日
	 */
	void calcuYesterdayEarn();
	
	/**
	 * 获取指定日期所有收益
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 * @param dateStart
	 * @return
	 */
	BigDecimal getAllEarnAmountByDate(String dateStart);

}
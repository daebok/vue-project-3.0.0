package com.rd.ifaes.core.statistic.service;

import com.rd.ifaes.core.statistic.domain.StatisticRepayInfo;
import com.rd.ifaes.core.statistic.model.StatisticModel;

import java.util.Date;

import com.rd.ifaes.core.base.service.BaseService;

/**
 * 还款信息Service
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
public interface StatisticRepayInfoService extends BaseService<StatisticRepayInfo>{

	/**
	 * 统计还款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param lastDate
	 */
	void recordRepayInfoByDate(Date lastDate) ;
	
	/**
	 * 获取还款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param model
	 * @return
	 */
	StatisticRepayInfo getRepayInfo();
}
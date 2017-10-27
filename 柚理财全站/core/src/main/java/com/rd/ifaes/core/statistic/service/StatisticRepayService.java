package com.rd.ifaes.core.statistic.service;

import com.rd.ifaes.core.statistic.domain.StatisticRepay;
import com.rd.ifaes.core.statistic.model.StatisticModel;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;

/**
 * 还款统计Service
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
public interface StatisticRepayService extends BaseService<StatisticRepay>{

	/**
	 * 记录所传日期的借款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param lastDate
	 */
	void recordRepayByDate(Date lastDate);
	
	/**
	 * 获取还款数据
	 * @author fxl
	 * @date 2017年3月1日
	 * @param model
	 * @return
	 */
	Map<String, Object> getBorrowRepayment(StatisticModel model);
	
}
package com.rd.ifaes.core.statistic.service;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticEarn;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * Service Interface:StatisticEarnService
 * @author xhf
 * @version 3.0
 * @date 2017-3-14
 */
public interface StatisticEarnService extends BaseService<StatisticEarn>{

	/**
	 * 根据日期统计数据
	 * @param statDate
	 */
	void recordByDate(Date statDate);

	/**
	 * 根据日期统计投资收益-二维图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);

	/**
	 * 统计投资收益-饼状图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);

}
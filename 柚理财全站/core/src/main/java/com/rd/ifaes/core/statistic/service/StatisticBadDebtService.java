package com.rd.ifaes.core.statistic.service;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticBadDebt;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * Service Interface:StatisticBadDebtService
 * @author xhf
 * @version 3.0
 * @date 2017-3-9
 */
public interface StatisticBadDebtService extends BaseService<StatisticBadDebt>{

	/**
	 * 根据日期统计坏账
	 * @param statDate
	 */
	void recordByDate(Date statDate);

	/**
	 * 统计坏账金额/笔数-二维图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);

	/**
	 * 统计坏账金额/笔数-饼状图
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);

}
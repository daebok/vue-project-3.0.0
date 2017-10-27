package com.rd.ifaes.core.statistic.service;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticBond;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * Service Interface:StatisticBondService
 * @author xhf
 * @version 3.0
 * @date 2017-3-2
 */
public interface StatisticBondService extends BaseService<StatisticBond>{

	/**
	 * 根据日期统计债权投资记录
	 * @param recordDate
	 */
	void recordByDate(Date recordDate);
	
	/**
	 * 统计债权投资人数/金额/笔数
	 * @param model
	 * @return
	 */
	Map<String, Object> findInvestByStatType(StatisticModel model);

	/**
	 * 获得债权期限统计情况
	 * @param model
	 * @return
	 */
	Map<String, Object> getBondTimeLimit(StatisticModel model);
}
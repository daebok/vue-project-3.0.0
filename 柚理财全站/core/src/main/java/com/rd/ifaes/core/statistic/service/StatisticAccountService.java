package com.rd.ifaes.core.statistic.service;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticAccount;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * Service Interface:StatisticAccountService
 * @author xhf
 * @version 3.0
 * @date 2017-3-6
 */
public interface StatisticAccountService extends BaseService<StatisticAccount>{

	/**
	 * 新增可用余额统计分析记录
	 * @param statDate
	 */
	void recordByDate(Date statDate);

	/**
	 * 统计可用余额
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);

}
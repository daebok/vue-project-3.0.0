package com.rd.ifaes.core.statistic.service;

import java.util.Date;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticOverdue;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * 逾期统计Service
 * @author fxl
 * @version 3.0
 * @date 2017-3-10
 */
public interface StatisticOverdueService extends BaseService<StatisticOverdue>{

	/**
	 * 记录所传日期的逾期信息
	 * @author fxl
	 * @date 2017年3月10日
	 * @param lastDate
	 */
	void recordOverdueByDate(Date lastDate);
	
	/**
	 * 获取逾期数据
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	Map<String,Object> getOverdueCount(StatisticModel model);
	
	/**
	 * 获取逾期数据
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	Map<String, Object> getOverdueRate(StatisticModel model);
	
	/**
	 * 逾期借款人地区
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	Map<String, Object> getBorrowOverdueArea(StatisticModel model);
	
}
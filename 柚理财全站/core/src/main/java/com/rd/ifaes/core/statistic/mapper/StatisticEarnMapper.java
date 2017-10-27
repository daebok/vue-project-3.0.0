package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticEarn;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:StatisticEarnMapper
 * @author xhf
 * @version 3.0
 * @date 2017-3-14
 */
public interface StatisticEarnMapper extends BaseMapper<StatisticEarn> {

	/**
	 * 根据日期统计收益信息
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatDate(StatisticModel model);
	
	/**
	 * 获得前一天的收益金额
	 * @param model
	 * @return
	 */
	BigDecimal getCountBeforeStartDate(StatisticModel model);
	
	/**
	 * 根据账户类型获取金额分布情况
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getAmntByAccountType(StatisticModel model);
}
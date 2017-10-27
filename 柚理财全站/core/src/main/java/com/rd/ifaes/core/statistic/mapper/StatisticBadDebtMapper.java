package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticBadDebt;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:StatisticBadDebtMapper
 * @author xhf
 * @version 3.0
 * @date 2017-3-9
 */
public interface StatisticBadDebtMapper extends BaseMapper<StatisticBadDebt> {

	/**
	 * 根据查询类型统计数据
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatDate(StatisticModel model);
	
	/**
	 * 统计不同借款类型坏账笔数或金额 
	 * @param model
	 * @return
	 */
	Map<String, BigDecimal> countByBorrowType(StatisticModel model);
}
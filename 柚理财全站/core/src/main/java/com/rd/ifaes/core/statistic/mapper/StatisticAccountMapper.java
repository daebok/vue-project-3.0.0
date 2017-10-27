package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticAccount;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * Dao Interface:StatisticAccountMapper
 * @author xhf
 * @version 3.0
 * @date 2017-3-6
 */
public interface StatisticAccountMapper extends BaseMapper<StatisticAccount> {

	/**
	 * 根据统计日期获取可用余额
	 * @param statDate
	 * @return
	 */
	BigDecimal getUseMoneyByStatDate(Date statDate);
	
	/**
	 * 根据统计时间查询记录
	 * @param model
	 * @return
	 */
	List<StatisticAccount> findByStatDate(StatisticModel model);
}
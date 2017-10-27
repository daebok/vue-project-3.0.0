package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticBond;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:StatisticBondMapper
 * @author xhf
 * @version 3.0
 * @date 2017-3-2
 */
public interface StatisticBondMapper extends BaseMapper<StatisticBond> {

	/**
	 * 根据查询类型统计数据
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findInvestByStatType(StatisticModel model);
	
	/**
	 * 获取起始日期前一天数值
	 * @author xhf
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	BigDecimal getCountBeforeStartDate(StatisticModel model);
	
	/**
	 * 根据投资金额获取债权期限分布情况
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getTimeLimitByAmnt(StatisticModel model);
	/**
	 * 根据投资笔数获取债权期限分布情况
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getTimeLimitByNum(StatisticModel model);
	/**
	 * 根据投资人数获取债权期限分布情况
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getTimeLimitByUsers(StatisticModel model);
}
package com.rd.ifaes.core.statistic.mapper;

import com.rd.ifaes.core.statistic.domain.StatisticOverdue;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * 逾期统计Mapper
 * @author fxl
 * @version 3.0
 * @date 2017-3-10
 */
public interface StatisticOverdueMapper extends BaseMapper<StatisticOverdue> {

	/**
	 * 获取逾期项目金额
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	StatisticOverdue sumOverdueAccount();
	/**
	 * 获取逾期项目笔数说
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	StatisticOverdue countOverdueNum();
	/**
	 * 获取逾期人数量
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	StatisticOverdue countOverdueBorrower();
	
	/**
	 * 统计借款笔数
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getOverdueCount(StatisticModel model);
	
	/**
	 * 获取起始日期前一天数值
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	BigDecimal getLastOverdue(StatisticModel model);
	
	/**
	 * 获取逾期率
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	StatisticOverdue getOverdueRate(StatisticModel model);
	
	/**
	 * 逾期用户地区
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	List<Map<String,Object>> getBorrowOverdueArea(StatisticModel model);
}
package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticBorrow;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 借款统计Mapper
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public interface StatisticBorrowMapper extends BaseMapper<StatisticBorrow> {
	
	/**
	 * 借款统计
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getBorrowStatistic(StatisticModel model);
	
	/**
	 * 获取起始日期前一天数值
	 * @author fxl
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	BigDecimal getLastTotal(StatisticModel model);
	
	/**
	 * 统计借款地区
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getBorrowArea(StatisticModel model);
	
	/**
	 * 借款总数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	StatisticResultModel getBorrowTotal(StatisticModel model);
	
	/**
	 * 统计不同借款金额笔数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countByBorrowAccount(StatisticModel model);
	
	/**
	 * 统计不同借款期限笔数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countByBorrowTimeLimit(StatisticModel model);
	
	/**
	 * 统计不同借款类型笔数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countByBorrowType(StatisticModel model);
	
	/**
	 * 统计不同借款期限金额
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> sumByBorrowTimeLimit(StatisticModel model);
	
	/**
	 * 统计不同借款类型金额
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> sumByBorrowType(StatisticModel model);
}
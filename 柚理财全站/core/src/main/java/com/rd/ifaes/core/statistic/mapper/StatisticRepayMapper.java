package com.rd.ifaes.core.statistic.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticRepay;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 还款统计Mapper
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
public interface StatisticRepayMapper extends BaseMapper<StatisticRepay> {

	/**
	 * 获取还款数据
	 * @author fxl
	 * @date 2017年3月2日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getStatisticRepay(StatisticModel model);
	
	
	/**
	 * 统计还款数据
	 * @author fxl
	 * @date 2017年3月2日
	 * @param model
	 * @return
	 */
	StatisticRepay recordBorrowRepayment(StatisticModel model);
	
	/**
	 * 获取前一次数据
	 * @author fxl
	 * @date 2017年3月2日
	 * @param model
	 * @return
	 */
	StatisticResultModel getLastRepay(StatisticModel model);
	
}
package com.rd.ifaes.core.statistic.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticBorrow;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * 借款统计Service
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public interface StatisticBorrowService extends BaseService<StatisticBorrow>{
	
	/**
	 * 记录所传日期的借款信息
	 * @author fxl
	 * @date 2017年2月22日
	 * @param lastDate
	 */
	void recordBorrowByDate(Date lastDate);
	
	/**
	 * 获取借款统计
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	Map<String,Object> getBorrowStatistic(StatisticModel model);
	
	/**
	 * 对比时段
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	Map<String,Object> compareDataByDate(StatisticModel model);
	
	/**
	 * 借款列表
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	Map<String, Object> getBorrowList(StatisticModel model);
	
	/**
	 * 统计借款地区
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	Map<String, Object> getBorrowArea(StatisticModel model);
	
	/**
	 * 借款地区列表
	 * @author fxl
	 * @date 2017年3月15日
	 * @param model
	 * @return
	 */
	Map<String, Object> getBorrowAreaList(StatisticModel model);
	
	/**
	 * 统计借款情况
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<Map<String,BigDecimal>> getBorrowInfo(StatisticModel model);
}
package com.rd.ifaes.core.statistic.service;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.core.statistic.model.StatisticModel;


/**
 * 统计Service基类
 * @version 3.0
 * @author fxl
 * @date 2017年3月23日
 */
public interface BaseStatisticService<T>{

	
	/**
	 * 格式化显示日期格式
	 * @param date
	 * @param model
	 * @return
	 */
	String fomateDateStr(Date date,StatisticModel model);
	
	/**
	 * 获取环比值
	 * @author fxl
	 * @date 2017年2月27日
	 * @return
	 */
	String getRatioValue(BigDecimal lastToal,BigDecimal total);
	
	/**
	 * 日期处理
	 * @author fxl
	 * @date 2017年3月3日
	 * @param model
	 * @param calDate
	 */
	Date nextCalDate(StatisticModel model,Date calDate);
	
	/**
	 * 日期字符串处理
	 * @author fxl
	 * @date 2017年3月3日
	 * @param model
	 * @param calDate
	 */
	String nextDateStr(StatisticModel model,Date calDate);
	
}

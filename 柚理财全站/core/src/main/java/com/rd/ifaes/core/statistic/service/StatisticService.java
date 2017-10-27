package com.rd.ifaes.core.statistic.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 统计Service
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public interface StatisticService{

	/**
	 * 执行统计方法
	 * @author fxl
	 * @date 2017年2月22日
	 */
	void doStatistic();
	
	/**
	 * 获取统计图表时间（横坐标）
	 * @author xhf
	 * @param model
	 * @return
	 */
	List<String> getStatChartDate(StatisticModel model);
	
	/**
	 * 格式化显示日期格式
	 * @param date
	 * @param model
	 * @return
	 */
	String fomateDateStr(Date date,StatisticModel model);
	
	/**
	 * 统计环比值
	 * @author xhf
	 * @param dateList
	 * @param beforeStartDateVal
	 * @return
	 */
	List<String> statisticRatio(List<String> dateList, BigDecimal beforeStartDateVal);
	
	/**
	 * 根据查询日期、类型等条件统计信息
	 * @author xhf
	 * @param investList
	 * @param model
	 * @return
	 */
	List<String> statByDate(List<StatisticResultModel> investList, StatisticModel model);

	/**
	 * 根据查询日期、类型等条件统计信息
	 * @param typeMap
	 * @param model
	 * @return
	 */
	List<String> statByDate(Map<String, Object> typeMap, StatisticModel model);

	/**
	 * 获得对比日期
	 * @param dateSelect
	 * @param dateContrast
	 * @return
	 */
	List<String> getCompareDateList(List<String> dateSelect, List<String> dateContrast);
	
	/**
	 * 导出报表
	 */
	void exportExcel(final StatisticModel model,final List<Object> dataList,
				final HttpServletRequest request,final HttpServletResponse response) throws IOException;
	
}
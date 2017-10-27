package com.rd.ifaes.core.statistic.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.statistic.domain.StatisticInvest;
import com.rd.ifaes.core.statistic.model.StatisticModel;

/**
 * 投资统计Service
 * @version 3.0
 * @author xhf
 * @date 2017年2月21日
 */
public interface StatisticInvestService extends BaseService<StatisticInvest>{
	
	/**
	 * 新增投资分析记录
	 */
	void recordByDate(Date investDate);
	
	/**
	 * 获得统计人数集合
	 * @param model
	 * @return
	 */
	Map<String, Object> findInvestByStatType(StatisticModel model);
	
	/**
	 * 统计投资情况
	 * @param model
	 * @return
	 */
	List<Map<String, BigDecimal>> getInvestCondition(StatisticModel model);
	
	/**
	 * 统计投资地区
	 * @param model
	 * @return
	 */
	Map<String, Object> getInvestArea(StatisticModel model);

	/**
	 * 获得投资列表
	 * @param model
	 * @return
	 */
	Map<String, Object> getInvestList(StatisticModel model);

	/**
	 * 统计活动金额
	 * @param model
	 * @return
	 */
	Map<String, Object> findRealAmntByDate(StatisticModel model);
	
	/**
	 * 获得实付金额map
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getRealAmntMap(StatisticModel model);

	/**
	 * 根据日期对比数据
	 * @param model
	 * @return
	 */
	Map<String, Object> compareDataByDate(StatisticModel model);

	/**
	 * 获得产品类型
	 * @return
	 */
	Map<String, String> getProjectTypeMap();

	/**
	 * 投资地区列表统计
	 * @param model
	 * @return
	 */
	Map<String, Object> getInvestAreaList(StatisticModel model);
}
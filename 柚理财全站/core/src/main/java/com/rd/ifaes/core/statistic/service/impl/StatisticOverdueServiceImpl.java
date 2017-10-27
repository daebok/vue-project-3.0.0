package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticOverdue;
import com.rd.ifaes.core.statistic.mapper.StatisticOverdueMapper;
import com.rd.ifaes.core.statistic.model.DataModel;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticOverdueService;

/**
 * 逾期统计实现类
 * @author fxl
 * @version 3.0
 * @date 2017-3-10
 */
@Service("statisticOverdueService") 
public class StatisticOverdueServiceImpl  extends BaseStatisticServiceImpl<StatisticOverdueMapper, StatisticOverdue> implements StatisticOverdueService{

	
	/**
	 * 记录所传日期的逾期信息
	 * @author fxl
	 * @date 2017年3月10日
	 * @param lastDate
	 */
	@Override
	public void recordOverdueByDate(Date lastDate) {
		List<StatisticOverdue> allList = new ArrayList<StatisticOverdue>();
		// 人数
		fillStatisticOverdue(allList,dao.countOverdueBorrower(),StatisticConstant.STAT_TYPE_CUSTOMERS,lastDate);
		// 金额
		fillStatisticOverdue(allList,dao.sumOverdueAccount(),StatisticConstant.STAT_TYPE_AMOUNT,lastDate);
		// 笔数
		fillStatisticOverdue(allList,dao.countOverdueNum(),StatisticConstant.STAT_TYPE_NUMBERS,lastDate);
		// 批量插入
		if(CollectionUtils.isNotEmpty(allList)){
			dao.insertBatch(allList);
		}
	}

	/**
	 * 封装逾期数据
	 * @author fxl
	 * @date 2017年2月23日
	 * @param list
	 * @param overdue
	 * @param statType
	 */
	private void fillStatisticOverdue(List<StatisticOverdue> list,StatisticOverdue overdue,String statType,Date lastDate){
		overdue.setStatType(statType);
		overdue.setRecordDate(lastDate);
		overdue.preInsert();
		list.add(overdue);
	}
	/**
	 * 获取逾期数据
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getOverdueCount(StatisticModel model) {
		// 获取统计结果集
		Map<String, StatisticResultModel> countMap = new HashMap<String, StatisticResultModel>();
		List<StatisticResultModel> list =  dao.getOverdueCount(model);
		BigDecimal lastTotal = dao.getLastOverdue(model);
		for (StatisticResultModel resultModel : list) {
			countMap.put(resultModel.getStatDate(), resultModel);
		}
		return fillStatisticList(model, lastTotal, countMap);
	}
	
	
	/**
	 * 封装统计逾期列表
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @param countMap
	 * @return
	 */
	private Map<String, Object> fillStatisticList(StatisticModel model,BigDecimal lastTotal,Map<String, StatisticResultModel> countMap){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<String> ratioList = new ArrayList<String>();// 环比列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			BigDecimal total = resultModel!=null?resultModel.getTotalCount():BigDecimal.ZERO;
			totalList.add(total);
			ratioList.add(getRatioValue(lastTotal, total));
			// 记录本次的总计
			lastTotal = total;
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		} 
		map.put(StatisticConstant.DATE, dateList);
		map.put(StatisticConstant.TOTAL, totalList);
		map.put(StatisticConstant.RATIO, ratioList);
		return map;
	}

	@Override
	public Map<String, Object> getOverdueRate(StatisticModel model) {
		Map<String,Object> map = new HashMap<>();
		StatisticOverdue statisticOverdue= dao.getOverdueRate(model);
		if(statisticOverdue != null){
			map.put(StatisticConstant.UNDER_NINETY, statisticOverdue.getFirstSeries());
			map.put(StatisticConstant.UNDER_HUNDRED_EIGHTY, statisticOverdue.getSecondSeries());
			map.put(StatisticConstant.ABOVE_HUNDRED_EIGHTY, statisticOverdue.getThirdSeries());
		}else{
			map.put(StatisticConstant.UNDER_NINETY, BigDecimal.ZERO);
			map.put(StatisticConstant.UNDER_HUNDRED_EIGHTY, BigDecimal.ZERO);
			map.put(StatisticConstant.ABOVE_HUNDRED_EIGHTY, BigDecimal.ZERO);
		}
		return map;
	}

	
	/**
	 * 逾期借款人地区
	 * @author fxl
	 * @date 2017年3月10日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowOverdueArea(StatisticModel model) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = dao.getBorrowOverdueArea(model);
		List<DataModel> totalList = new ArrayList<DataModel>();
		for (Map<String, Object> result : list) {
			String area = (String) result.get("area");
			totalList.add(new DataModel(area,result.get("totalCount")));
		}
		map.put(StatisticConstant.TOTAL, totalList);
		return map;
	}
}
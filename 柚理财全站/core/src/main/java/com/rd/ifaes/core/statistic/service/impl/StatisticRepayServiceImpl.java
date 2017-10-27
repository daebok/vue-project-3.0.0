package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticRepay;
import com.rd.ifaes.core.statistic.mapper.StatisticRepayMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticRepayService;

/**
 * 还款统计实现类
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
@Service("statisticRepayService") 
public class StatisticRepayServiceImpl  extends BaseStatisticServiceImpl<StatisticRepayMapper, StatisticRepay> implements StatisticRepayService{
	
	/**
	 * 记录所传日期的还款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param lastDate
	 */
	@Override
	@Transactional(readOnly = false)
	public void recordRepayByDate(Date lastDate) {
		List<StatisticRepay> allList = new ArrayList<StatisticRepay>();
		String[] borrowTypeArr = new String[]{"0","1","2","3"};
		for (String borrowType : borrowTypeArr) {
			if(borrowTypeArr!=null){
				allList.add(getBorrowRepayment(lastDate, borrowType, "0","0"));//未还个人
				allList.add(getBorrowRepayment(lastDate, borrowType, "0","1"));//未还个人
				allList.add(getBorrowRepayment(lastDate, borrowType, "1","0"));//已还企业
				allList.add(getBorrowRepayment(lastDate, borrowType, "1","1"));//已还企业
			}
		}
		// 批量插入
		if(CollectionUtils.isNotEmpty(allList)){
			dao.insertBatch(allList);
		}
	}

	/**
	 * 获取还款数据
	 * @author fxl
	 * @date 2017年3月3日
	 * @param lastDate
	 * @param borrowType
	 * @param borrowNature
	 * @param repayStatus
	 * @return
	 */
	private StatisticRepay getBorrowRepayment(Date lastDate,String borrowType,String borrowNature,String repayStatus){
		StatisticModel model = new StatisticModel(borrowType,borrowNature,repayStatus);
		StatisticRepay statisticRepay = dao.recordBorrowRepayment(model);
		statisticRepay.setRepayStatus(repayStatus);
		statisticRepay.setRecordDate(lastDate);
		statisticRepay.setBorrowNature(borrowNature);
		statisticRepay.setBorrowType(borrowType);
		statisticRepay.preInsert();
		return statisticRepay;
	}
	
	/**
	 * 获取还款数据
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	@Override
	public Map<String, Object> getBorrowRepayment(StatisticModel model){
		// 获取统计结果集
		Map<String, StatisticResultModel> countMap = new HashMap<String, StatisticResultModel>();
		List<StatisticResultModel> list = dao.getStatisticRepay(model);
		StatisticResultModel lastResult = dao.getLastRepay(model);
		for (StatisticResultModel resultModel : list) {
			countMap.put(resultModel.getStatDate(), resultModel);
		}
		if(StatisticConstant.REPORT_TYPE_COUNT.equals(model.getReportType())){
			return fillStatisticCountList(model, lastResult.getRepayCount(), countMap);
		}else{// 默认金额
			return fillStatisticAccountList(model, lastResult.getPayment(), countMap);
		}
	}
	
	/**
	 * 封装统计金额列表
	 * @author fxl
	 * @date 2017年3月03日
	 * @param model
	 * @param countMap
	 * @return
	 */
	private Map<String, Object> fillStatisticAccountList(StatisticModel model,BigDecimal lastTotal,Map<String, StatisticResultModel> countMap){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<BigDecimal> capitalList = new ArrayList<BigDecimal>();// 本金列表
		List<BigDecimal> interestList = new ArrayList<BigDecimal>();// 利息列表
		List<String> ratioList = new ArrayList<String>();// 环比列表
		Date calDate = model.getStartDateTime();
		String dateStr = model.getStartDate();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			BigDecimal total = resultModel!=null?resultModel.getPayment():BigDecimal.ZERO;
			totalList.add(total);
			capitalList.add(resultModel!=null?resultModel.getCapital():BigDecimal.ZERO);
			interestList.add(resultModel!=null?resultModel.getInterest():BigDecimal.ZERO);
			ratioList.add(getRatioValue(lastTotal, total));
			// 记录本次的总计
			lastTotal = total;
			// 日期处理
			calDate = nextCalDate(model, calDate);
			dateStr = nextDateStr(model, calDate);
		}
		map.put(StatisticConstant.DATE, dateList);
		map.put(StatisticConstant.TOTAL, totalList);
		map.put(StatisticConstant.CAPITAL, capitalList);
		map.put(StatisticConstant.INTEREST, interestList);
		map.put(StatisticConstant.RATIO, ratioList);
		return map;
	}
	
	
	/**
	 * 封装统计笔数列表
	 * @author fxl
	 * @date 2017年3月3日
	 * @param model
	 * @param lastTotal
	 * @param countMap
	 * @return
	 */
	private Map<String, Object> fillStatisticCountList(StatisticModel model,BigDecimal lastTotal,Map<String, StatisticResultModel> countMap){
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> dateList = new ArrayList<String>();// 时间列表
		List<BigDecimal> totalList = new ArrayList<BigDecimal>();// 总计列表
		List<String> ratioList = new ArrayList<String>();// 环比列表
		String dateStr = model.getStartDate();
		Date calDate = model.getStartDateTime();
		while(!NumberUtils.isBigger(dateStr, model.getEndDate())){
			// 封装日期
			dateList.add(fomateDateStr(calDate,model));
			// 获取数据
			StatisticResultModel resultModel = countMap.get(dateStr);
			// 总计值
			BigDecimal total = resultModel!=null?resultModel.getRepayCount():BigDecimal.ZERO;
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
	
}
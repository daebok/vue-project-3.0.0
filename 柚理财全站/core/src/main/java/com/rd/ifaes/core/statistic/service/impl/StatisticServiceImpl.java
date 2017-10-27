package com.rd.ifaes.core.statistic.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.excel.ExportModel;
import com.rd.ifaes.common.util.excel.ExportUtil;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticAccountService;
import com.rd.ifaes.core.statistic.service.StatisticBadDebtService;
import com.rd.ifaes.core.statistic.service.StatisticBondService;
import com.rd.ifaes.core.statistic.service.StatisticBorrowService;
import com.rd.ifaes.core.statistic.service.StatisticEarnService;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticOverdueService;
import com.rd.ifaes.core.statistic.service.StatisticRepayInfoService;
import com.rd.ifaes.core.statistic.service.StatisticRepayService;
import com.rd.ifaes.core.statistic.service.StatisticService;

/**
 * 统计实现类
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
@Service("statisticService") 
public class StatisticServiceImpl implements StatisticService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StatisticServiceImpl.class);
	
	@Resource
	private StatisticBorrowService statisticBorrowService;
	@Resource
	private StatisticRepayService statisticRepayService;
	@Resource
	private StatisticInvestService statisticInvestService;
	@Resource
	private StatisticBondService statisticBondService;
	@Resource
	private StatisticAccountService statisticAccountService;
	@Resource
	private StatisticBadDebtService statisticBadDebtService;
	@Resource
	private StatisticEarnService statisticEarnService;
	@Resource
	private StatisticRepayInfoService statisticRepayInfoService;
	@Resource
	private StatisticOverdueService statisticOverdueService;
	
	/**
	 * 统计方法
	 */
	@Override
	public void doStatistic() {
		Date lastDate = DateUtils.rollDay(DateUtils.getToday(), -1);
		// 用户统计为实时添加
		// 借款统计
		LOGGER.info("借款统计开始---------------------------------");
		try {
			statisticBorrowService.recordBorrowByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("借款统计出现异常",e);
		}
		LOGGER.info("借款统计结束---------------------------------");
		// 还款统计
		LOGGER.info("还款统计开始---------------------------------");
		try {
			statisticRepayService.recordRepayByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("还款统计出现异常",e);
		}
		LOGGER.info("还款统计结束---------------------------------");
		// 还款信息统计
		LOGGER.info("还款信息统计开始---------------------------------");
		try {
			statisticRepayInfoService.recordRepayInfoByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("还款信息统计出现异常",e);
		}
		LOGGER.info("还款统计结束---------------------------------");
		
		LOGGER.info("逾期统计开始---------------------------------");
		try {
			statisticOverdueService.recordOverdueByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("逾期统计出现异常",e);
		}
		LOGGER.info("逾期统计结束---------------------------------");
		//投资统计
		LOGGER.info("投资统计开始---------------------------------");
		try {
			statisticInvestService.recordByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("投资统计出现异常",e);
		}
		LOGGER.info("投资统计结束---------------------------------");
		//债权统计
		LOGGER.info("债权统计开始---------------------------------");
		try {
			statisticBondService.recordByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("债权统计出现异常",e);
		}
		LOGGER.info("债权统计结束---------------------------------");
		// 可用余额统计
		LOGGER.info("可用余额统计开始---------------------------------");
		try {
			statisticAccountService.recordByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("可用余额统计出现异常",e);
		}
		LOGGER.info("可用余额统计结束---------------------------------");	
		// 坏账统计
		LOGGER.info("坏账统计开始---------------------------------");
		try {
			statisticBadDebtService.recordByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("坏账统计出现异常",e);
		}
		LOGGER.info("坏账统计结束---------------------------------");
		// 投资收益统计
		LOGGER.info("投资收益统计开始---------------------------------");
		try {
			statisticEarnService.recordByDate(lastDate);
		} catch (Exception e) {
			LOGGER.error("投资收益统计出现异常",e);
		}
		LOGGER.info("投资收益统计结束---------------------------------");
	}
	
	/**
	 * 获取统计图表时间（横坐标）
	 * @param dateList
	 * @param model
	 * @return
	 */
	@Override
	public List<String> getStatChartDate(StatisticModel model){
		List<String> dateList = new ArrayList<String>();
		String startDateStr = model.getStartDate();
		Date startDateTime = model.getStartDateTime();
		while(Integer.valueOf(startDateStr)<=Integer.valueOf(model.getEndDate())){ //开始日期小于结束日期
			//日、周、月
			if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
				dateList.add(fomateDateStr(startDateTime, model));
				startDateTime = DateUtils.rollMon(startDateTime, 1);
				startDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_013);
			}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){
				dateList.add(fomateDateStr(startDateTime, model));				
				startDateTime = DateUtils.rollDay(startDateTime, 7);
				startDateStr = DateUtils.getYearWeek(startDateTime);
			}else{ 
				dateList.add(fomateDateStr(startDateTime, model));
				startDateTime = DateUtils.rollDay(startDateTime, 1);
				startDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_012);
			}
		}
		return dateList;
	}
	
	/**
	 * 格式化显示日期格式
	 * @param date
	 * @param model
	 * @return
	 */
	@Override
	public String fomateDateStr(Date date,StatisticModel model){
		if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
			return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_018);
		}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){
			return DateUtils.getFirstAndLastOfWeek(date, DateUtils.DATEFORMAT_STR_017);			
		}else{ 
			return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_017);
		}
	}
	
	/**
	 * 计算环比值
	 * @author xhf
	 * @param before
	 * @param after
	 * @return
	 */
	private String calRatio(BigDecimal before,BigDecimal after){
		String result = "0.00";
		if(before!=null && BigDecimal.ZERO.compareTo(before)<0){
			after = (after==null)?BigDecimal.ZERO:after;
			BigDecimal subNum = BigDecimalUtils.mul(BigDecimalUtils.sub(after, before), new BigDecimal(100));
			result = subNum.divide(before, 2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return result;
	}
	
	@Override
	public List<String> getCompareDateList(List<String> dateSelect, List<String> dateContrast){
		List<String> compareDate = null;
		if(CollectionUtils.isNotEmpty(dateSelect) && CollectionUtils.isNotEmpty(dateContrast)){
			if(dateSelect.size()==dateContrast.size()){
				compareDate = new ArrayList<String>();
                for (int i = 0; i < dateSelect.size(); i++) {
                	compareDate.add(dateSelect.get(i)+" | "+dateContrast.get(i));
				}
			}
		}
		return compareDate;
	}
	
	/**
	 * 根据查询日期、类型等条件统计信息
	 * @author xhf
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@Override
	public List<String> statByDate(List<StatisticResultModel> investList, StatisticModel model){
		//将数据库记录按照(时间,计数)放入到map中
		Map<String, Object> typeMap = Maps.newHashMap();
		for (StatisticResultModel resultModel : investList) {
			if(StatisticConstant.STAT_TYPE_AMOUNT.equals(model.getStatType())){
				typeMap.put(resultModel.getStatDate(), BigDecimalUtils.round(resultModel.getTotalCount()));
			}else{
				typeMap.put(resultModel.getStatDate(), BigDecimalUtils.round(resultModel.getTotalCount(),0));
			}
		}
		return statByDate(typeMap,model);
	}
	
	/**
	 * 根据查询日期、类型等条件统计信息
	 * @author xhf
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@Override
	public List<String> statByDate(Map<String, Object> typeMap, StatisticModel model){
		//轮询查询时间，无记录则为空
		List<String> dateCount = new ArrayList<String>();
		String startDateStr = model.getStartDate();
		Date startDateTime = model.getStartDateTime();
		while(!startDateStr.equals(model.getEndDate())){ //开始日期小于结束日期
			dateCount.add(getBlankDateCount(typeMap, model, startDateStr));
			//日、周、月
			if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
				startDateTime = DateUtils.rollMon(startDateTime, 1);
				startDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_013);
			}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){ 
				startDateTime = DateUtils.rollDay(startDateTime, 7);
				startDateStr = DateUtils.getYearWeek(startDateTime);
			}else{ 
				startDateTime = DateUtils.rollDay(startDateTime, 1);
				startDateStr = DateUtils.formatDate(startDateTime, DateUtils.DATEFORMAT_STR_012);
			}
		}
		dateCount.add(getBlankDateCount(typeMap, model, startDateStr));
		return dateCount;
	}
	
	private String getBlankDateCount(Map<String, Object> typeMap, StatisticModel model, String dateStr){
		String result = "";
		if(typeMap.get(dateStr)==null){
			if(StatisticConstant.STAT_TYPE_AMOUNT.equals(model.getStatType())){
				result = "0.00";
			}else{
				result = "0";
			}
		}else{
			if(StatisticConstant.STAT_TYPE_AMOUNT.equals(model.getStatType())){
				result = BigDecimalUtils.round(typeMap.get(dateStr).toString()).toString();
			}else{
				result = BigDecimalUtils.round(typeMap.get(dateStr).toString(),0).toString();
			}
		}
		return result;
	}
	
	/**
	 * 统计环比值
	 * @author xhf
	 * @param dateList
	 * @param beforeStartDateVal
	 * @return
	 */
	@Override
	public List<String> statisticRatio(List<String> dateList, BigDecimal beforeStartDateVal){
		List<String> ratioList = new ArrayList<String>();
		for (int i = 0; i < dateList.size(); i++) {
			if(i==0){
				String firstDayRatio = "";
				if(!StringUtils.isNoneBlank(dateList.get(i))){
					//计算查询日期前一天的记录数
					if(beforeStartDateVal!=null && beforeStartDateVal.compareTo(BigDecimal.ZERO)>0){
						firstDayRatio = calRatio(beforeStartDateVal,new BigDecimal(dateList.get(i)));
					}
				}
				ratioList.add(firstDayRatio);
			}else{
				if(StringUtils.isNoneBlank(dateList.get(i-1)) && StringUtils.isNoneBlank(dateList.get(i))){
					ratioList.add(calRatio(new BigDecimal(dateList.get(i-1)),new BigDecimal(dateList.get(i))));
				}else{
					ratioList.add("0.00");
				}
			}
		}
		return ratioList;
	}
	
	
	/**
	 * 导出报表
	 */
	@Override
	public void exportExcel(final StatisticModel model, final List<Object> dataList,final HttpServletRequest request,final HttpServletResponse response)
			throws IOException {
		ExportUtil.exportExcel(new ExportModel<StatisticModel>() {
			@Override
			public String getCacheKey() {
				return PrincipalUtils.getCurrUserId();
			}
			@Override
			public StatisticModel getModel() {
				return model;
			}
			@Override
			public int getTotal(StatisticModel entity) {
				return dataList.size();
			}
			@Override
			public List<Object> getPageRecords(StatisticModel entity) {
				return dataList;
			}
		}, request, response);
	}
}
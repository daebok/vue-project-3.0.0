package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticAccount;
import com.rd.ifaes.core.statistic.mapper.StatisticAccountMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticAccountService;
import com.rd.ifaes.core.statistic.service.StatisticService;

/**
 * ServiceImpl:StatisticAccountServiceImpl
 * @author xhf
 * @version 3.0
 * @date 2017-3-6
 */
@Service("statisticAccountService") 
public class StatisticAccountServiceImpl  extends BaseStatisticServiceImpl<StatisticAccountMapper, StatisticAccount> implements StatisticAccountService{
	
    @Resource
    private AccountService accountService;
    @Resource
    private StatisticService statisticService;

	/**
	 * 新增可用余额统计分析记录
	 * @param statDate
	 */
	@Override
	public void recordByDate(Date statDate){
		//判断是否已经统计过
		int count = dao.getCount(new StatisticAccount(statDate));
		if(count>0){
			return;
		}
		
		BigDecimal yesterdayUseMoney = dao.getUseMoneyByStatDate(DateUtils.rollDay(statDate, -1));
		BigDecimal statDateUseMoney = accountService.getTotalUseMoney();
		StatisticAccount stat = new StatisticAccount();
		stat.setStatDate(statDate);
		stat.setUseMoney(statDateUseMoney);
		stat.setRelativeRatio(calRatio(yesterdayUseMoney,statDateUseMoney));
		this.save(stat);
	}
	
	private String calRatio(BigDecimal before,BigDecimal after){
		String result = "";
		if(before!=null && after!=null && BigDecimal.ZERO.compareTo(before)<0){
			BigDecimal subNum = BigDecimalUtils.mul(BigDecimalUtils.sub(after, before), new BigDecimal(100));
			result = subNum.divide(before, 2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return result;
	}
	
	/**
	 * 统计可用余额
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatType(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		final Map<String, Object> useMoneyMap = Maps.newHashMap();
		final Map<String, Object> ratioMap = Maps.newHashMap();
		final List<String> useMoneyList = new ArrayList<String>();
		final List<String> ratioList = new ArrayList<String>();
		//校验时间
		model.checkQueryDate();
		//查询
		List<StatisticAccount> list = dao.findByStatDate(model);
		for (StatisticAccount stat : list) {
			String statDateStr = DateUtils.formatDate(stat.getStatDate(), DateUtils.DATEFORMAT_STR_017);
			useMoneyMap.put(statDateStr, BigDecimalUtils.round(stat.getUseMoney()));
			ratioMap.put(statDateStr, stat.getRelativeRatio());
		}
		//日期横坐标
		List<String> dateList = statisticService.getStatChartDate(model);
		for (String date : dateList) {
			useMoneyList.add(null==useMoneyMap.get(date)?"0.00":useMoneyMap.get(date).toString());
			ratioList.add(StringUtils.isBlank(ratioMap.get(date))?"0.00":ratioMap.get(date).toString());
		}
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, dateList);
		//环比值
		map.put(StatisticConstant.STAT_CHART_RATIO, ratioList);
		//总计
		map.put(StatisticConstant.STAT_CHART_TOTAL, useMoneyList);
		//结果
		map.put("result", true);
		return map;
	}
	
	
	
}
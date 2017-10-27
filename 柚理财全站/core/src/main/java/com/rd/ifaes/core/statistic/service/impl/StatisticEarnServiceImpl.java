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
import com.rd.account.domain.AccountLog;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticEarn;
import com.rd.ifaes.core.statistic.mapper.StatisticEarnMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticEarnService;
import com.rd.ifaes.core.statistic.service.StatisticService;

/**
 * ServiceImpl:StatisticEarnServiceImpl
 * @author xhf
 * @version 3.0
 * @date 2017-3-14
 */
@Service("statisticEarnService") 
public class StatisticEarnServiceImpl  extends BaseStatisticServiceImpl<StatisticEarnMapper, StatisticEarn> implements StatisticEarnService{
	
    @Resource
    private AccountLogService accountLogService;
	@Resource 
	private StatisticService statisticService;
    
	/**
	 * 新增可用余额统计分析记录
	 * @param statDate
	 */
	@Override
	public void recordByDate(Date statDate){
		//判断是否已经统计过
		int count = dao.getCount(new StatisticEarn(statDate));
		if(count>0){
			return;
		}
		
		//查询记录
		AccountLog model = new AccountLog();
		model.setStartTime(DateUtils.getDateStart(statDate));
		model.setEndTime(DateUtils.getDateEnd(statDate));
		//收益:投资利息、投资红包、加息利息（加息券、产品）、债权转让收回利息、债权受让收益、债权转让溢价金额、逾期罚息
		List<String> typeList = new ArrayList<String>();
		typeList.add(Constant.COLLECT_INTEREST); //投资利息
		typeList.add(Constant.BOND_SELL_CAPITAL);//红包-债权
		typeList.add(Constant.COLLECT_CAPITAL);  //红包-还款
		typeList.add(Constant.COLLECT_ADD_INTEREST); //加息利息
		typeList.add(Constant.BOND_SELL_INTEREST);   //债权转让收回利息，后期待改
		typeList.add(Constant.BOND_SELL_EARN);       //转让溢价金额，后期待改
		typeList.add(Constant.COLLECT_LATE_INTEREST);//逾期利息收回
		model.setAccountTypeSet((String[])typeList.toArray(new String[typeList.size()]));
		List<AccountLog> logList = accountLogService.findListByDate(model);
		//保存数据
		List<StatisticEarn> earnList = new ArrayList<StatisticEarn>();
		for (AccountLog log : logList) {
			if(log.getEarn()!=null && log.getEarn().compareTo(BigDecimal.ZERO)>0){
				StatisticEarn earn = new StatisticEarn();
				earn.setStatDate(statDate);
				earn.setUserId(log.getUserId());
				earn.setAmount(log.getEarn());
				earn.setAccountType(log.getAccountType());
				earnList.add(earn);
			}
		}
		this.insertBatch(earnList);
	}
	
	/**
	 * 统计投资收益-二维图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatDate(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//收益为金额类型，保持两位小数
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		//校验时间
		model.checkQueryDate();
		//总计
		List<String> totalNumList = statisticService.statByDate(dao.findByStatDate(model), model); 
		map.put(StatisticConstant.STAT_CHART_TOTAL, totalNumList);
		//环比值
		BigDecimal beforeCount = dao.getCountBeforeStartDate(model);
		map.put(StatisticConstant.STAT_CHART_RATIO, statisticService.statisticRatio(totalNumList, beforeCount));
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		return map;
	}
	
	/**
	 * 统计投资收益-饼状图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatType(StatisticModel model){
		//校验时间
		model.checkQueryDate();
		
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		map.put(StatisticConstant.STAT_PIE_AMNT, dao.getAmntByAccountType(model));
		return map;
	}

}
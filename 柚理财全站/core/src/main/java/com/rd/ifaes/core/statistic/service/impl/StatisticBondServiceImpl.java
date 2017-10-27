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
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticBond;
import com.rd.ifaes.core.statistic.mapper.StatisticBondMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticBondService;
import com.rd.ifaes.core.statistic.service.StatisticService;

/**
 * ServiceImpl:StatisticBondServiceImpl
 * @author xhf
 * @version 3.0
 * @date 2017-3-2
 */
@Service("statisticBondService") 
public class StatisticBondServiceImpl  extends BaseStatisticServiceImpl<StatisticBondMapper, StatisticBond> implements StatisticBondService{

	@Resource
	private BondInvestService bondInvestService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private ProjectCollectionService projectCollectionService;
	@Resource 
	private StatisticService statisticService;
	@Resource 
	private ProjectService projectService;

	@Override
	public void recordByDate(Date recordDate){
		//判断是否已经统计过
		int count = dao.getCount(new StatisticBond(recordDate));
		if(count>0){
			return;
		}
		//根据时间查询记录
		List<BondInvest> investList = bondInvestService.findByInvestDate(DateUtils.formatDate(recordDate, DateUtils.DATEFORMAT_STR_012));
		//轮询记录
		final List<StatisticBond> statisticInvestList = new ArrayList<StatisticBond>();
		for (BondInvest invest : investList) {
			Date investDate = DateUtils.valueOf(invest.getInvestDate(), DateUtils.DATEFORMAT_STR_012);
			StatisticBond statBond = new StatisticBond();
			statBond.setUserId(invest.getUserId());
			statBond.setBondInvestId(invest.getUuid());
			statBond.setBondDate(investDate);
			statBond.setBondAmount(invest.getAmount());
            //是否担保
			Project project = projectService.get(invest.getProjectId());
			statBond.setIsVouch(CommonConstants.YES.equals(project.getIsVouch())?CommonConstants.NO:CommonConstants.YES);
			//是否抵押
			Borrow borrow = borrowService.getByProjectId(invest.getProjectId());
			statBond.setIsMortgage(borrow==null?CommonConstants.NO:borrow.getHasPawn());
			//债权期限
			Date lastRepayDate = projectCollectionService.getLastRepayTimeByBondInvestId(invest.getUuid());
			statBond.setBondTimeLimit(DateUtils.daysBetween(investDate, lastRepayDate));
            //添加到列表
			statisticInvestList.add(statBond);
		}
		//save
		this.insertBatch(statisticInvestList);
	}
	
	/**
	 * 统计投资人数/金额/笔数
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findInvestByStatType(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		//转让金额
		model.fillDictListTwo(DictTypeEnum.ACCOUNT_RANGE.getValue());
		//总计
		List<String> totalNumList = statisticService.statByDate(dao.findInvestByStatType(model), model); 
		map.put(StatisticConstant.STAT_CHART_TOTAL, totalNumList);
		//环比值
		BigDecimal beforeCount = dao.getCountBeforeStartDate(model);
		map.put(StatisticConstant.STAT_CHART_RATIO, statisticService.statisticRatio(totalNumList, beforeCount));
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		return map;
	}
	
	@Override
	public Map<String,Object> getBondTimeLimit(StatisticModel model){
		final Map<String,Object> dataMap = Maps.newHashMap();
		dataMap.put("result", true);
		//校验时间
		model.checkQueryDate();
		//转让金额和债权期限
		model.fillDictList(DictTypeEnum.BOND_TIME_LIMIT_RANGE.getValue());
		model.fillDictListTwo(DictTypeEnum.ACCOUNT_RANGE.getValue());
		//统计金额
		dataMap.put(StatisticConstant.STAT_PIE_AMNT, dao.getTimeLimitByAmnt(model));
		//统计笔数
		dataMap.put(StatisticConstant.STAT_PIE_NUM, dao.getTimeLimitByNum(model));
		//统计人数
		dataMap.put(StatisticConstant.STAT_PIE_USER, dao.getTimeLimitByUsers(model));
		return dataMap;
	}
	
}
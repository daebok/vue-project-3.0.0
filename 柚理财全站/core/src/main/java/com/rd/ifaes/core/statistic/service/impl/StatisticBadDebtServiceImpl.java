package com.rd.ifaes.core.statistic.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.domain.StatisticBadDebt;
import com.rd.ifaes.core.statistic.mapper.StatisticBadDebtMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.StatisticBadDebtService;
import com.rd.ifaes.core.statistic.service.StatisticService;

/**
 * ServiceImpl:StatisticBadDebtServiceImpl
 * @author xhf
 * @version 3.0
 * @date 2017-3-9
 */
@Service("statisticBadDebtService") 
public class StatisticBadDebtServiceImpl  extends BaseStatisticServiceImpl<StatisticBadDebtMapper, StatisticBadDebt> implements StatisticBadDebtService{
	
    @Resource
    private ProjectCollectionService projectCollectionService;
	@Resource
	private BorrowService borrowService;
	@Resource 
	private ProjectService projectService;
	@Resource 
	private StatisticService statisticService;
    
	@Override
    public void recordByDate(Date statDate){
		//判断是否已经统计过
		int count = dao.getCount(new StatisticBadDebt(statDate));
		if(count>0){
			return;
		}
		
		List<StatisticBadDebt> debtList = new ArrayList<StatisticBadDebt>();
		//坏账
		List<ProjectCollection> collectionList = projectCollectionService.findBadDebtCollection();
		for (ProjectCollection collection : collectionList) {
			StatisticBadDebt debt = new StatisticBadDebt();
			debt.setStatDate(statDate);
			debt.setProjectId(collection.getProjectId());
			debt.setAmount(collection.getCapital());
            //是否担保和变现
			Project project = projectService.get(collection.getProjectId());
			debt.setIsVouch(CommonConstants.YES.equals(project.getIsVouch())?CommonConstants.NO:CommonConstants.YES);
			debt.setIsRealize(project.getRealizeFlag());
			//是否抵押
			Borrow borrow = borrowService.getByProjectId(collection.getProjectId());
			debt.setIsMortgage(borrow==null?CommonConstants.NO:borrow.getHasPawn());
			//添加到列表
			debtList.add(debt);
		}
		//save
		this.insertBatch(debtList);
    }
	
	/**
	 * 统计坏账金额/笔数-二维图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatDate(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//校验时间
		model.checkQueryDate();
		//总计
		List<String> totalNumList = statisticService.statByDate(dao.findByStatDate(model), model); 
		map.put(StatisticConstant.STAT_CHART_TOTAL, totalNumList);
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		//饼状图
		map.put(StatisticConstant.STAT_PIE_CHART, dao.countByBorrowType(model));
		return map;
	}
	
	/**
	 * 统计坏账金额/笔数-饼状图
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public Map<String, Object> findByStatType(StatisticModel model){
		//校验时间
		model.checkQueryDate();
		
		final Map<String, Object> map = Maps.newHashMap();
		map.put("result", true);
		//饼状图
		map.put(StatisticConstant.STAT_PIE_CHART, dao.countByBorrowType(model));
		return map;
	}

}
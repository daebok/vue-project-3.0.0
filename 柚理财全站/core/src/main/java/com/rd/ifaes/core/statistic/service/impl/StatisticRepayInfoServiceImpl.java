package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.statistic.domain.StatisticRepayInfo;
import com.rd.ifaes.core.statistic.mapper.StatisticRepayInfoMapper;
import com.rd.ifaes.core.statistic.service.StatisticRepayInfoService;

/**
 * 还款信息实现类
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
@Service("statisticRepayInfoService") 
public class StatisticRepayInfoServiceImpl  extends BaseStatisticServiceImpl<StatisticRepayInfoMapper, StatisticRepayInfo> implements StatisticRepayInfoService{
	
	/**
	 * 统计还款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param lastDate
	 */
	@Override
	public void recordRepayInfoByDate(Date lastDate) {
		// 待还总和
		BigDecimal waitTotal= dao.getRepayTotal();
		List<BigDecimal> repayList = dao.getRepayList();
		// 最大户
		BigDecimal biggestOne = BigDecimal.ZERO;
		// 最大十户
		BigDecimal biggestTen = BigDecimal.ZERO;
		//是否最大户
		boolean isOne = true;
		if(CollectionUtils.isNotEmpty(repayList)){
			for (BigDecimal waitMoney : repayList) {
				if(isOne){
					biggestOne = waitMoney;
				}
				isOne = false;
				biggestTen = biggestTen.add(waitMoney);
			}
		}
		// 获取逾期数据
		StatisticRepayInfo repayInfo  = dao.getOverdueInfo();
		repayInfo.setRecordDate(lastDate);
		repayInfo.setBiggestOne(biggestOne);
		repayInfo.setWaitTotal(waitTotal);
		repayInfo.setBiggestTen(biggestTen);
		repayInfo.setBiggestOneRatio(getDivValue(biggestOne,waitTotal));
		repayInfo.setBiggestTenRatio(getDivValue(biggestTen,waitTotal));
		// 逾期数据
		repayInfo.setOverdueRatio(getDivValue(repayInfo.getOverdueNum(), repayInfo.getWaitNum()));
		repayInfo.setOverdueAccountRatio(getDivValue(repayInfo.getOverdueAccount(), repayInfo.getWaitAccount()));
		repayInfo.setOldOverdueRatio(getDivValue(repayInfo.getOldOverdueAccount(), repayInfo.getTotalAccount()));
		repayInfo.preInsert();
		dao.insert(repayInfo);
	}

	/**
	 * 获取还款信息
	 * @author fxl
	 * @date 2017年3月1日
	 * @param model
	 * @return
	 */
	@Override
	public StatisticRepayInfo getRepayInfo() {
		Date lastDate = DateUtils.rollDay(DateUtils.getToday(),-1);
		StatisticRepayInfo info = dao.getRepayInfoByDate(lastDate);
		if(info!=null){
			return info;
		}
		return new StatisticRepayInfo(lastDate);
	}

}
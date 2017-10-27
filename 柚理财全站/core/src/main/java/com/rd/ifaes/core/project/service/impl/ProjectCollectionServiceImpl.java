package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.mapper.ProjectCollectionMapper;
import com.rd.ifaes.core.project.model.MonthCollection;
import com.rd.ifaes.core.project.model.PeriodCollectionModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;

/**
 * ServiceImpl:ProjectCollectionServiceImpl
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("projectCollectionService")
public class ProjectCollectionServiceImpl extends BaseServiceImpl<ProjectCollectionMapper, ProjectCollection>
		implements ProjectCollectionService {

	@Override
	@Cacheable(key= CacheConstant.KEY_PREFIX_COLLECTION_YEARCOLLECTDATA_USERID+"{userId}:{status}", expire = ExpireTime.TEN_SEC)
	public Object[] getYearCollectDataByUserId(String userId,String status) {

		String[] section = DateUtils.getYearDynamicSection();
		ProjectCollection model = new ProjectCollection();
		model.setUserId(userId);
		model.setStartTime(section[0]);
		model.setEndTime(section[1]);
		model.setStatus(status);
		List<MonthCollection> repays = dao.findMonthRepayByTime(model);
		
		Object[] repayments = new Object[12];
		//开始月份
		int currentMonth = Integer.valueOf(section[0].substring(5, 7));
		for (int i = 0; i < 12; i++) {
			//项目待收
			Iterator<MonthCollection> pt = repays.iterator();
			MonthCollection mc = new MonthCollection(String.valueOf(currentMonth),BigDecimal.ZERO);
			while (pt.hasNext()) {
				MonthCollection collection = pt.next();
				if(collection.getRepayMonth().equals(mc.getRepayMonth())){
					mc.setRepayment(mc.getRepayment().add(collection.getRepayment()));
					pt.remove();
				}				
			}
			currentMonth++;
			if (currentMonth > 12) {
				currentMonth -= 12;
			}
			repayments[i] = mc.getRepayment();
		}
		return repayments;
	}

	@Override
	@Cacheable(key= CacheConstant.KEY_COLLECTION_YEARCOLLECTMONTHS, expire = ExpireTime.ONE_DAY)
	public String[] getYearCollectMonths() {
		int monthsSize = 12;
		String[] months = new String[monthsSize];
		String[] section = DateUtils.getYearDynamicSection();
		int currentMonth = Integer.valueOf(section[0].substring(5, 7));
		for (int i = 0; i < monthsSize; i++) {
			months[i] = currentMonth+"月";
			currentMonth++;
			if (currentMonth > 12) {
				currentMonth -= 12;
			}
		}
		return months;
	}
	
	@Override
	public List<PeriodCollectionModel> findPeriodCollectionList(String projectId) {
		return dao.findPeriodCollectionList(projectId);
	}

	@Override
	public List<ProjectCollection> findByProjectIdAndPeriod(String projectId, Integer period,String investId){
		return dao.findByProjectIdAndPeriod(projectId, period, investId,ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
	}

	@Override
	public List<ProjectCollection> findByInvestId(String investId, String status) {
		return dao.findByInvestId(investId, status);
	}

	@Override
	public Page<ProjectCollection> findForPage(ProjectCollection model) {
		if (model.getPage() == null) {
			model.setPage(new Page<ProjectCollection>());
		}
		List<ProjectCollection> list = dao.findForPage(model);
		for (ProjectCollection projectCollection : list) {
			// 实际回款金额 扣除利息管理费
			projectCollection.setRepayedAmount(BigDecimalUtils.add(projectCollection.getRepayedAmount(),projectCollection.getManageFee().negate()));
			String repayTypeStr = "";
			if(BigDecimalUtils.validAmount(projectCollection.getCapital())){
				repayTypeStr = repayTypeStr+"本金+利息";
			}else{
				repayTypeStr = repayTypeStr+"利息";
			}
			if(BigDecimalUtils.validAmount(projectCollection.getRaiseInterest())){
				repayTypeStr = repayTypeStr + "+加息";
			}
			if(BigDecimalUtils.validAmount(projectCollection.getLateInterest())){
				repayTypeStr = repayTypeStr+"+罚息";
			}
			if(BigDecimalUtils.validAmount(projectCollection.getManageFee()) && ProjectCollectionEnum.STATUS_PAID.eq(projectCollection.getStatus())){
				repayTypeStr = repayTypeStr+"-管理费";
			}
			projectCollection.setRepayTypeStr(repayTypeStr);
		}
		model.getPage().setRows(list);
		return model.getPage();
	}

	@Override
	public int getReypayDays(String investId) {
		ProjectCollection lastCollection = dao.getLastCollectionByInvestId(investId);
		int remainDay = 0;
		if (lastCollection != null) {
			remainDay = (int) DateUtils.getDistanceOfTwoDate(DateUtils.getToday(), lastCollection.getRepayTime());
		} else {
			throw new BussinessException("还款计划获取异常");
		}
		return remainDay;
	}

	@Override
	public Date getlastRepayTime(String investId) {
		ProjectCollection lastCollection = dao.getLastCollectionByInvestId(investId);
		if (lastCollection != null) {
			return lastCollection.getRepayTime();
		} else {
			throw new BussinessException("还款计划获取异常");
		}
	}

	@Override
	public List<ProjectCollection> findPcByPIdAndUserId(ProjectCollection pcModel) {
		return dao.findPcByPIdAndUserId(pcModel);
	}

	@Override
	public ProjectCollection getProjectCollectionByInvestIdAndPeriod(String investId, int period) {
		return dao.getProjectCollectionByInvestIdAndPeriod(investId, period);
	}

	@Override
	public int getProjectPeriodByInvestId(String investId, String status) {
		return dao.getProjectPeriodByInvestId(investId, status);
	}

	@Override
	public BigDecimal getRepayedAccountTotal(String projectId, String status, String investId,
			String[] collectionTypeSet) {
		return dao.getRepayedAccountTotal(projectId, status, investId, collectionTypeSet);
	}

	@Override
	public int updateOverdueInfo(String projectId, Integer period, Integer lateDays, BigDecimal lateRate) {
		String[] collectionTypeSet = { ProjectCollectionEnum.COLLECTION_TYPE_BOND.getValue(),
				ProjectCollectionEnum.COLLECTION_TYPE_COMMON.getValue(),
				ProjectCollectionEnum.COLLECTION_TYPE_INVEST.getValue() };
		return dao.updateOverdueInfo(projectId, period, lateDays, lateRate, collectionTypeSet);
	}

	@Override
	public void updateBatchLateInterest(List<ProjectCollection> list) {
		dao.updateBatchLateInterest(list);
	}

	@Override
	public BigDecimal getRepayedAmount(String userId, String projectId) {
		return dao.getRepayedAmount(userId, projectId);
	}

	@Override
	public BigDecimal getNoRepayRaiseInterest(String investId) {
		return dao.getNoRepayRaiseInterest(investId);
	}
	
	@Override
	public List<ProjectCollection> findByProjectIdPeriodStatus(
			String projectId, Integer period, String value) {
		return dao.findByProjectIdAndPeriod(projectId, period, null,value);
	}
	
	/**
	 * 查询债权转让最后还款日期
	 * @param bondInvestId
	 * @return
	 */
	@Override
	public Date getLastRepayTimeByBondInvestId(String bondInvestId){
		return dao.getLastRepayTimeByBondInvestId(bondInvestId);
	}
	
	@Override
	public List<ProjectCollection> findBadDebtCollection(){
		return dao.findBadDebtCollection(LoanEnum.STATUS_BAD_DEBT.getValue(), CommonConstants.NO);
	}
}
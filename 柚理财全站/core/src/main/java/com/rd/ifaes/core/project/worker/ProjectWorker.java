package com.rd.ifaes.core.project.worker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.Global;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.AverageCapitalRepaymentCalculator;
import com.rd.ifaes.core.interest.EachPlan;
import com.rd.ifaes.core.interest.InstallmentRepaymentCalculator;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.interest.MonthlyInterestCalculator;
import com.rd.ifaes.core.interest.OnetimeRepaymentCalculator;
import com.rd.ifaes.core.interest.QuarterInterestCalculator;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.model.PeriodCollectionModel;

/**
 * project相关业务处理
 * 
 * @author lh
 * @version 3.0
 * @since 2016-7-26
 *
 */
public class ProjectWorker {

	private Project project;
	
	/**
	 * 一天的最后一分钟时间点
	 */
	private static final String END_MINUTE_OF_DAY="23:59";

	public ProjectWorker(Project project) {
		super();
		this.project = project;
	}

	/**
	 * 计算收益
	 * 
	 * @param repayStyle
	 *            还款方式
	 * @param money
	 *            投标金额
	 * @param apr
	 *            利率
	 * @param timeType
	 *            是否天标
	 * @param timeLimit
	 *            借款期限
	 * @param interestManageRate
	 *            利息管理费
	 * @param interestTime
	 *            计息日
	 * @param periodTime
	 *            项目期限起始日
	 * @param fixedRepayDay
	 *            固定还款日
	 * 
	 * @return 根据还款方式，返回不同的利息计算器
	 */
	public static InterestCalculator doInterestCalculator(String repayStyle, BigDecimal money, BigDecimal apr,BigDecimal addApr,
			String timeType, int timeLimit, BigDecimal interestManageRate, Date interestTime, Date periodTime, Integer fixedRepayDay) {
		if(interestTime == null){
			interestTime = DateUtils.getToday();
		}else{
			interestTime=DateUtils.trunc(interestTime);
		}
		if(periodTime == null){
			periodTime = DateUtils.getToday();
		}else{
			periodTime=DateUtils.trunc(periodTime);
		}
		InterestCalculator ic = null;
		if (LoanEnum.TIME_TYPE_DAY.getValue().equals(timeType)
				&& LoanEnum.STYLE_ONETIME_REPAYMENT.getValue().equals(repayStyle)) { // 天标,一次性还款
			ic = new OnetimeRepaymentCalculator(money, apr, addApr, interestTime, periodTime, 1, interestManageRate);
		} else if (LoanEnum.STYLE_ONETIME_REPAYMENT.getValue().equals(repayStyle)) { // 一次性还款
			ic = new OnetimeRepaymentCalculator(money, apr, addApr, interestTime, periodTime, timeLimit, interestManageRate);
		} else if (LoanEnum.STYLE_MONTHLY_INTEREST.getValue().equals(repayStyle)) { // 每月还息到期还本
			ic = new MonthlyInterestCalculator(money, apr, addApr, interestTime, periodTime, timeLimit, true, interestManageRate, fixedRepayDay);
		} else if (LoanEnum.STYLE_QUARTER_INTEREST.getValue().equals(repayStyle)) { // 每季还息到期还本
			ic = new QuarterInterestCalculator(money, apr, addApr, interestTime, periodTime, timeLimit/Constant.INT_THREE, true, interestManageRate, fixedRepayDay);
		} else if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.getValue().equals(repayStyle)) { // 月等额本息
			ic = new InstallmentRepaymentCalculator(money, apr, addApr, interestTime, periodTime, timeLimit, interestManageRate);
		} else if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.getValue().equals(repayStyle)) { // 月等额本金
			ic = new AverageCapitalRepaymentCalculator(money, apr, addApr, interestTime, periodTime, timeLimit, interestManageRate);
		} 
		if (ic == null) {
			throw new BussinessException("repayStyle:{"+repayStyle+"} not supported!");
		}
		if (LoanEnum.TIME_TYPE_DAY.getValue().equals(timeType)) {
			ic.calculator(timeLimit);
		} else {
			ic.calculator();
		}
		return ic;
	}

	/**
	 * 计算起息日
	 * 
	 * @param investTime	投资时间
	 * 	 以下情况下投资时间不能为空：<br>
	 * 	1、计息方式为T+N计息；<br>
	 * @return
	 */
	public Date getInterestTime(Date investTime) {
		return getInterestTime(project.getInterestStyle(),project.getInterestTime(), project.getInterestStartDays()==null?0:project.getInterestStartDays(),
				project.getReviewTime(), investTime);
	}
	
	/**
	 * 计算起息日
	 * @param interestStyle	计息方式: 1、成立计息 2、 T+N计息
	 * @param interestTime	计息临界时间点
	 * @param interestStartDays	起始计息天数
	 * @param reviewTime	成立审核时间
	 * @param investTime	投资时间
	 * 	 以下情况下投资时间不能为空：<br>
	 * 	1、计息方式为T+N计息；<br>
	 * 
	 * @return
	 */
	private Date getInterestTime(String interestStyle, String interestTime, int interestStartDays,
			Date reviewTime, Date investTime) {
		Date date = DateUtils.getNow();
		if (LoanEnum.INTEREST_STYLE_EV.eq(interestStyle) ) {//成立审核并且成立审核时间不为空
			if(NumberUtils.isDefault(project.getRaiseTimeLimit())) { 
				//无募集期  (投资成功算成立，成立计息 == T+0 计息)
				date = investTime;
			}else if(reviewTime != null){
				date = reviewTime;
			}
		} else if (LoanEnum.INTEREST_STYLE_TN.eq(interestStyle)) {
			if(investTime == null){
				throw new BussinessException(ResourceUtils.get("project.investTime.emptyMsg"));
			}
			if(StringUtils.isBlank(interestTime) || Integer.valueOf(interestTime.split(":")[0]) == 0){
				interestTime = END_MINUTE_OF_DAY;
			}
			int diffDay = DateUtils.before(investTime, interestTime) ? interestStartDays : interestStartDays + 1;
			
			date = DateUtils.rollDay(investTime, diffDay);
		}
		return date;
	}
	
	
	/**
	 * 
	 * 取得项目期限起始日
	 * @author lh
	 * @date 2016年8月2日
	 * @param investTime	投资时间
	 *  以下情况下投资时间不能为空：<br>
	 * 	1、没有募集期的理财产品；<br>
	 * 
	 * @return
	 */
	private Date getPeriodTime(Date investTime) {
		Date periodTime = DateUtils.getNow();
		if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag()) || LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {// 借贷
			if (project.getReviewTime() != null) {
				periodTime = project.getReviewTime();
			}
		} else if (LoanEnum.BORROW_FLAG_PRODUCT.eq(project.getBorrowFlag())) {// 产品
			if (!NumberUtils.isDefault(project.getRaiseTimeLimit())) {// 有募集期
				if (project.getReviewTime() != null) {
					periodTime = project.getReviewTime();
				}
			} else if (NumberUtils.isDefault(project.getRaiseTimeLimit())) {// 无募集期，无需成立审核

				if(investTime == null){
					throw new BussinessException(ResourceUtils.get("project.investTime.emptyMsg"));
				}
				periodTime = investTime;
				
				//定期T+N计息--有固定产品期限
				if (!NumberUtils.isDefault(project.getTimeLimit()) &&
						LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle()) ) {				
					String interestTime = project.getInterestTime();
					if(StringUtils.isBlank(interestTime) || Integer.valueOf(interestTime.split(":")[0]) == 0){
						interestTime = END_MINUTE_OF_DAY;
					}
					int diffDay = DateUtils.before(investTime, interestTime)? project.getInterestStartDays() : project.getInterestStartDays() + 1;
					periodTime = DateUtils.rollDay(investTime, diffDay);
				}
			}
		}
		return periodTime;
	}
	
	
	/**
	 * 计算待还利息总额
	 * @author lh
	 * @date 2016年8月3日
	 * @param investTime 投资时间<br>
	 * 	以下情况下投资时间不能为空：<br>
	 * 	1、计息方式为T+N计息；<br>
	 * 	2、没有募集期的理财产品；<br>
	 * 
	 * 
	 * @return
	 */
	public BigDecimal getRepaymentInterest(Date investTime) {
		InterestCalculator ic = doInterestCalculator(project.getRepayStyle(), project.getAccount(),
				project.getApr().divide(BigDecimal.valueOf(100)), 
				project.getAddApr()==null?BigDecimal.ZERO:project.getAddApr().divide(BigDecimal.valueOf(100)), 
				project.getTimeType(), project.getTimeLimit(),
				project.getInterestManageRate().divide(BigDecimal.valueOf(100)), 
				getInterestTime(investTime),
				getPeriodTime(investTime),
				project.getFixedRepayDay());
		return ic.repayInterest();
	}

	/**
	 * 计算收益
	 * 
	 * @param money
	 *            投资金额(非空)
	 * @param apr
	 *            年利率（可为空，默认project的apr）
	 * @param investTime
	 *            投资时间<br>
	 * 	以下情况下投资时间不能为空：<br>
	 * 	1、计息方式为T+N计息；<br>
	 * 	2、没有募集期的理财产品；<br>
	 * 
	 * @return
	 */
	public InterestCalculator interestCalculator(BigDecimal money, BigDecimal apr, BigDecimal addApr, Date investTime) {
		if(NumberUtils.isDefault(money) || money.compareTo(BigDecimal.ONE) < 0){
			throw new BussinessException("投资金额不能小于1");
		}
		/*if(investDate == null){
			throw new BussinessException("投资时间不能为空");
		}*/
		if(NumberUtils.isDefault(apr)){
			apr = project.getApr();
		}
		return doInterestCalculator(project.getRepayStyle(), money, 
				apr.divide(BigDecimal.valueOf(100)),
				addApr==null?BigDecimal.ZERO:addApr.divide(BigDecimal.valueOf(100)),
				project.getTimeType(), project.getTimeLimit(),
				project.getInterestManageRate().divide(BigDecimal.valueOf(100)), 
				getInterestTime(investTime),
				getPeriodTime(investTime),
				project.getFixedRepayDay());
	}

	/**
	 * 生成待收
	 * 
	 * @param invest
	 * @param ic
	 * @param addTime
	 * @return
	 */
	public List<ProjectCollection> createCollectionList(ProjectInvest invest, InterestCalculator ic) {
		List<ProjectCollection> list = new ArrayList<>();
		List<EachPlan> eplist = ic.getEachPlan();
		int period = CommonConstants.PROJECT_PERIODS_START;//期数起始值
		final int totalPeriod=CollectionUtils.size(eplist);
		for (EachPlan eachPlan : eplist) {
			ProjectCollection c = fillCollection(eachPlan, invest, ic);
			c.setPeriod(period++);
			c.setPeriods(totalPeriod);
			list.add(c);
		}
		return list;
	}

	/**
	 * 待收初始化
	 * 
	 * @param e
	 * @param invest
	 * @param ic
	 * @param addTime
	 * @return
	 */
	private ProjectCollection fillCollection(EachPlan e, ProjectInvest invest, InterestCalculator ic) {
		ProjectCollection c = new ProjectCollection();
		c.setProjectId(invest.getProjectId());
		c.setInvestId(invest.getUuid());
		c.setInterest(e.getInterest());
		c.setRaiseInterest(e.getAddInterest()==null? BigDecimal.ZERO : BigDecimalUtils.round(e.getAddInterest()));
		c.setCapital(e.getCapital());
		c.setPayment(e.getTotal().add(c.getRaiseInterest()));//加上加息利息
		c.setCreateTime(DateUtils.getNow());
		c.setAddIp(Global.getIP());
		c.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
		c.setRepayedAmount(BigDecimal.ZERO);
		c.setLateDays(Constant.INT_ZERO);
		c.setLateInterest(BigDecimal.ZERO);
		c.setFreezeCapital(BigDecimal.ZERO);
		c.setFreezeInterest(BigDecimal.ZERO);
		c.setMerchantLateInterest(BigDecimal.ZERO);
		c.setUserId(invest.getUserId());
		c.setRepayTime(DateUtils.trunc(e.getRepayTime())); //截除时分秒，便于生成待收按还款时间分组查询
		c.setManageFee(BigDecimalUtils.sub(e.getInterest(), e.getNetInterest()));
		c.setCollectionType(ProjectCollectionEnum.COLLECTION_TYPE_COMMON.getValue());
		return c;
	}

	/**
	 * 生成待还（产生还款计划）
	 * 
	 * @return 待还集合
	 */
	public List<ProjectRepayment> createRepaymentList(final List<ProjectCollection> pclist) {
		List<ProjectRepayment> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(pclist)){
			return list;
		}
		int period = CommonConstants.PROJECT_PERIODS_START;//期数起始值
		List<PeriodCollectionModel> pcmlist = allCollToPeriodColl(pclist);
		final int totalPeriod=CollectionUtils.size(pcmlist);
		//即息理财，待还和待收 1:1 ,通过investId关联
		final String investId=project.isInterestFinancing() ? pclist.get(0).getInvestId() : null;
		for (PeriodCollectionModel pc : pcmlist) {
			ProjectRepayment repayment = new ProjectRepayment();
			if(pc == null || pc.getRepayTime() == null){
				continue;
			}
		    repayment.setInvestId(investId);
			repayment.setRepayTime(pc.getRepayTime());
			repayment.setCapital(pc.getCapital());// 本金
			repayment.setInterest(pc.getInterest());// 利息
			repayment.setPayment(BigDecimalUtils.add(pc.getCapital(),pc.getInterest()));// 预还金额
			repayment.setUserId(project.getUserId());
			repayment.setProjectId(project.getUuid());
			repayment.setPayedAmount(BigDecimal.ZERO);
			repayment.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
			repayment.setPeriod(period++);
			repayment.setPeriods(totalPeriod);
			repayment.setLateDays(Constant.INT_ZERO);
			repayment.setLateInterest(BigDecimal.ZERO);
			repayment.setMerchantLateInterest(BigDecimal.ZERO);
			repayment.setCreateTime(DateUtils.getNow());
			repayment.setBorrowManageFee(pc.getBorrowManageFee());
			list.add(repayment);
		}
		return list;
	}
	
	/**
	 * 将投资人的待收按还款日期进行汇总
	 * @return
	 */
	private List<PeriodCollectionModel> allCollToPeriodColl(List<ProjectCollection> pclist){
		//按还款日期进行统计
		Map<String, PeriodCollectionModel> pcmMap = new HashMap<>();
		for (ProjectCollection projectCollection : pclist) {
			String pcDate = DateUtils.formatDate(projectCollection.getRepayTime(), "yyyy-MM-dd");
			PeriodCollectionModel model;
			if(pcmMap.containsKey(pcDate)){
				model = pcmMap.get(pcDate);
				model.setCapital(BigDecimalUtils.add(model.getCapital(), projectCollection.getCapital()));
				model.setInterest(BigDecimalUtils.add(model.getInterest(), projectCollection.getInterest()));
				model.setManageFee(BigDecimalUtils.add(model.getManageFee(), projectCollection.getManageFee()));
				model.setPayment(BigDecimalUtils.add(model.getPayment(), projectCollection.getPayment()));
				model.setBorrowManageFee(BigDecimalUtils.add(model.getBorrowManageFee(), projectCollection.getBorrowManageFee()));
			}else{
				model = new PeriodCollectionModel();
				model.setRepayTime(projectCollection.getRepayTime());
				model.setCapital(projectCollection.getCapital());
				model.setInterest(projectCollection.getInterest());
				model.setManageFee(projectCollection.getManageFee());
				model.setPayment(projectCollection.getPayment());
				model.setBorrowManageFee(projectCollection.getBorrowManageFee());
			}
			pcmMap.put(pcDate, model);
			
		}
		List<PeriodCollectionModel> pcmlist = new ArrayList<>();
		Iterator<String> it = pcmMap.keySet().iterator();
		while (it.hasNext()) {
			pcmlist.add(pcmMap.get(it.next()));			
		}		
		//按还款日期升序排列
		Collections.sort(pcmlist, new Comparator<PeriodCollectionModel>() {
			@Override
			public int compare(PeriodCollectionModel o1, PeriodCollectionModel o2) {
				return o1.getRepayTime().compareTo(o2.getRepayTime());
			}
		});
		
		return pcmlist;		
	}

}

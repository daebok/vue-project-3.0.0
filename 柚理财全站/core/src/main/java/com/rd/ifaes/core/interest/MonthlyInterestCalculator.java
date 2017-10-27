package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * <p>每月还息到期还本利息计算函数</p>
 * 设贷款额为a，月利率为i,还款月数为n,每月还款额为b，还款本息总和为Y,还款利息总和为Z
	a)	还款日为起息日的计算公式：
	第一期还款金额为：b1=a*i
	第二期还款金额为：b2=a*i
	第三期还款金额为：b3=a*i
	第...期还款金额为：bx=a*i
	第n期还款金额为：bn=a+a*i
	还款本息总额为：Y=a+a*i*n
	还款利息总和为：Z=a*i*n
	
	b)	还款日不为起息日的计算公式
	前面几期的还款日都为固定还款日，最后一期的还款日为到期日，则还款期数会比还款日刚好为起息日的多一期，设起息日为D1，还款日为D2
		若D2>D1
	第一期还款利息：b1=a*（D2-D1）*i/30
	第二期还款金额为：b2=a*i
	第三期还款金额为：b3=a*i
	第...期还款金额为：bx=a*i
	第n期还款金额为：bn=a*i
	第n+1期还款金额为：bn+1=a+（Z- b1- b2-…-bn）
	还款本息总额为：Y=a+a*i*n
	还款利息总和为：Z=a*i*n
		若D2<D1
	第一期还款利息：b1=a*（30-D1+D2）*i/30
	第二期还款金额为：b2=a*i
	第三期还款金额为：b3=a*i
	第...期还款金额为：bx=a*i
	第n期还款金额为：bn=a*i
	第n+1期还款金额为bn+1=a+（Z- b1- b2-…-bn）
	还款本息总额为：Y=a+a*i*n
	还款利息总和为：Z=a*i*n
 * 
 * @author：xx
 * @version 3.0
 * @since 2016年7月6日
 */
public class MonthlyInterestCalculator extends AbstractInterestCalculator {
	
	
	/** 标记是否为满标复审 */
	private boolean flag;
	
	/**
	 * 固定还款日
	 */
	private Integer fixedRepayDay;
	/**
	 * 计息日
	 */
	private int interestDay;
	
	/**
	 * 初始化每月还息到期还本构造函数
	 * 
	 * @param account 借款金额
	 * @param yearApr 年化利率
	 * @param addApr  加息利率
	 * @param interestTime 开始计息日
	 * @param periods 期数
	 * @param flag ？？？
	 * @param manageFee 利率管理费
	 */
	public MonthlyInterestCalculator(BigDecimal account, BigDecimal yearApr, BigDecimal addApr, Date interestTime, Date startPeriod, int periods, boolean flag, BigDecimal manageFee, Integer fixedRepayDay) {
		this.account = account;
		this.yearApr = yearApr;
		this.addApr = addApr==null?BigDecimal.ZERO:addApr;
		this.manageFee = manageFee;
		this.interestTime = interestTime;
		this.periodTime = startPeriod;
		this.periods = periods;
		this.flag = flag;
		this.fixedRepayDay = fixedRepayDay;
		eachPlan = new ArrayList<EachPlan>();
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<EachPlan> calculator() {
		
		Date eInterestTime = interestTime;//DateUtils.rollDay(interestTime, 1);
		interestDay = eInterestTime.getDate();		
		if(!NumberUtils.isDefault(fixedRepayDay) && periodTime.getDate() != fixedRepayDay.intValue() ){//	固定日还款
			return fixDayCalculator();
		}
		/*
		 * 如果是发标或复审或投标或网贷计算器则total = account; 否则total = BigDecimalUtils.mul(account, periods);
		 */
		BigDecimal total = account;
		final BigDecimal bdPeriods = BigDecimal.valueOf(periods);		
		if (flag) {
			total = account;
		} else {
			total = BigDecimalUtils.mul(account, bdPeriods);
		}
		BigDecimal needRepay = total;
		BigDecimal eInterest = BigDecimal.ZERO;
		BigDecimal netInterest = BigDecimal.ZERO;
		BigDecimal eCapital = BigDecimal.ZERO;
		BigDecimal eTotal = BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;//总利息
		Date eRepayTime = null;
		EachPlan e = null;
		
		/***** 利差处理  start *****/
		//利差
		BigDecimal daysInterest = getDaysInterest();
		//总利息
		BigDecimal totalInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, yearApr, bdPeriods), MONTHS_OF_FULL_YEAR, 2);
		totalInterest = totalInterest.add(daysInterest);
		/***** 利差处理  end *****/
		
		//加息利息
		BigDecimal remainAddInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, addApr, bdPeriods), MONTHS_OF_FULL_YEAR, 2);
		//加息利差
		BigDecimal daysAddInterest = getDaysInterest(addApr);
		remainAddInterest = BigDecimalUtils.add(remainAddInterest, daysAddInterest);
		
		this.collectInterest=remainAddInterest;
		BigDecimal addInterest = BigDecimal.ZERO;
		BigDecimal remainInterest = totalInterest;
		for (int i = 0; i < periods; i++) {
			e = new EachPlan();
			// 计算每月需要支付的利息
			eInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, yearApr), MONTHS_OF_FULL_YEAR, 2) ;
			addInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, addApr), MONTHS_OF_FULL_YEAR, 2) ;
			// 计算本金
			eCapital = BigDecimal.ZERO;
			// 每期还款即是本期的还款总额
			if (i == periods - 1) { // 判断是否是最后一期
				eCapital = account;
				//前几期的总利息
				//BigDecimal lastInterest = BigDecimalUtils.mul(eInterest, BigDecimalUtils.sub(bdPeriods, BigDecimal.ONE));
				//最后一期利息
				eInterest=remainInterest;//BigDecimalUtils.round(BigDecimalUtils.sub(totalInterest,lastInterest), 2);
				addInterest = remainAddInterest;
				
			}else if(i == 0){//第一期加上利差
				eInterest = eInterest.add(daysInterest);
				addInterest = addInterest.add(daysAddInterest);
			}
			remainAddInterest = BigDecimalUtils.sub(remainAddInterest, addInterest);
			remainInterest = BigDecimalUtils.sub(remainInterest, eInterest);
			
			netInterest = BigDecimalUtils.mul(eInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
			eTotal = BigDecimalUtils.round(BigDecimalUtils.add(eCapital, eInterest), 2);
			// 本期开始计息日
			//eInterestTime = DateUtils.rollDay(interestTime, 1);
			// 本期还款日
			eRepayTime = this.calculatorRepaytime(periodTime, i+1);//还款日计算依据期限起始日：2016-8-2
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			e.setAddInterest(addInterest);
			e.setNetInterest(BigDecimalUtils.round(netInterest,2));
			e.setTotal(eTotal);
			BigDecimal netTotal = BigDecimalUtils.round(BigDecimalUtils.add(eCapital, netInterest), 2);
			e.setNetTotal(netTotal);
			e.setInterestTime(eInterestTime);
			e.setPeriodTime(periodTime);
			e.setRepayTime(eRepayTime);
			e.setNeedRepay(BigDecimalUtils.sub(BigDecimalUtils.add(total, totalInterest), BigDecimalUtils.mul(eInterest, BigDecimalUtils.add(BigDecimal.valueOf(i),BigDecimal.ONE))));
			repayTime = eRepayTime;
			eachPlan.add(e);
			sum = BigDecimalUtils.round(BigDecimalUtils.add(eInterest, sum), 2);
		}
		this.repayInterest = sum ;
		this.collectInterest=BigDecimalUtils.add(this.repayInterest,collectInterest);
		this.repayAccount = BigDecimalUtils.decimal(BigDecimalUtils.add(account, sum),2);
		e = eachPlan.get(periods - 1);
		e.setNeedRepay(BigDecimal.ZERO);
		//修正金额，防止出现负数情况 added by lh 20161009
		correctEachPlan();
		return eachPlan;
	}
	
	/**
	 * 固定还款日计息
	 * @return
	 */
	private List<EachPlan> fixDayCalculator(){
		//月均天数
		BigDecimal daysOfMonth = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_MONTH);
		daysOfMonth = (daysOfMonth.compareTo(BigDecimal.ZERO) == 0)?DEFAULT_DAYS_OF_MONTH:daysOfMonth;
		if(daysOfMonth.intValue() < fixedRepayDay.intValue()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_FIXED_REPAY_DAY_GT_DAYSOFMONTH, daysOfMonth.intValue()));
		}
		
		/*
		 * 如果是发标或复审或投标或网贷计算器则total = account; 否则total = BigDecimalUtils.mul(account, periods);
		 */
		BigDecimal total = account;
		final BigDecimal bdPeriods = BigDecimal.valueOf(periods);		
		if (flag) {
			total = account;
		} else {
			total = BigDecimalUtils.mul(account, bdPeriods);
		}
		BigDecimal needRepay = total;
		BigDecimal eInterest = BigDecimal.ZERO;
		BigDecimal netInterest = BigDecimal.ZERO;
		BigDecimal eCapital = BigDecimal.ZERO;
		BigDecimal eTotal = BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;//总利息
		BigDecimal periodDiffInterest = BigDecimal.ZERO;//利息差值
		BigDecimal periodDiffAddInterest = BigDecimal.ZERO;//加息差值
		
		Date eInterestTime = interestTime;//DateUtils.rollDay(interestTime, 1);
		Date eFixRepayTime = DateUtils.fixDay(eInterestTime, fixedRepayDay.intValue());
		int diffDay = fixedRepayDay.intValue() - interestDay;	//固定还款日与计息日相差的天数	
		if(diffDay > 0){	//固定还款日在计息日之后，还款日期往前推一个月
			eFixRepayTime = DateUtils.rollMon(eFixRepayTime, -1);
		}
		Date eRepayTime = null;
		EachPlan e = null;
		
		/***** 利差处理 start *****/
		//利差
		BigDecimal daysInterest = getDaysInterest();
		//总利息
		BigDecimal totalInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, yearApr, bdPeriods), MONTHS_OF_FULL_YEAR, 2);
		totalInterest = totalInterest.add(daysInterest);//固定还款日的利差只需累加到总利息上
		//剩余本息
		BigDecimal remainAccount = totalInterest.add(total);
		/***** 利差处理 end *****/
		
		//加息利息 2016-8-20
		BigDecimal remainAddInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, addApr, bdPeriods), MONTHS_OF_FULL_YEAR, 2);
		//加息利差
		BigDecimal daysAddInterest = getDaysInterest(addApr);
		remainAddInterest = BigDecimalUtils.add(remainAddInterest, daysAddInterest);
		
		BigDecimal addInterest = BigDecimal.ZERO;
		this.collectInterest=remainAddInterest;
		
		//第一期
		EachPlan firstPlan = new EachPlan();
		firstPlan.setCapital(BigDecimal.ZERO);		
		BigDecimal firstInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, yearApr), MONTHS_OF_FULL_YEAR, 2) ;
		
		Date firstRepayTime = calculatorRepaytime(eFixRepayTime, 1);//eFixRepayTime;
		if(diffDay < 0){
			diffDay += daysOfMonth.intValue();
		}
		firstPlan.setInterestTime(eInterestTime);
		firstPlan.setPeriodTime(periodTime);
		firstPlan.setRepayTime(firstRepayTime);
		firstInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, firstInterest, BigDecimal.valueOf(diffDay)), daysOfMonth);
		if(firstInterest.compareTo(BigDecimal.ZERO) < 0){//首期利息为负值时，利息置空，并将负值加到二期利息
			periodDiffInterest = firstInterest;
			firstInterest = BigDecimal.ZERO;
		}
		firstPlan.setInterest(firstInterest);
		
		BigDecimal firstAddInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, addApr), MONTHS_OF_FULL_YEAR, 2);
		firstAddInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, firstAddInterest, BigDecimal.valueOf(diffDay)), daysOfMonth);
		if(firstAddInterest.compareTo(BigDecimal.ZERO) < 0){//首期加息利息为负值时，加息利息置空，并将负值加到二期加息利息
			periodDiffAddInterest = firstAddInterest;
			firstAddInterest = BigDecimal.ZERO;
		}
		remainAddInterest = BigDecimalUtils.sub(remainAddInterest, firstAddInterest);
		firstPlan.setAddInterest(firstAddInterest);
		firstPlan.setNetInterest(BigDecimalUtils.mul(firstInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee)));		
		firstPlan.setTotal(firstInterest);
		firstPlan.setNetTotal(BigDecimalUtils.mul(firstInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee)));
		remainAccount = BigDecimalUtils.sub(remainAccount,firstInterest);
		firstPlan.setNeedRepay(remainAccount);
		eachPlan.add(firstPlan);
		sum = BigDecimalUtils.add(sum, firstInterest);
		//第一期 end
		
		for (int i = 0; i < periods; i++) {
			e = new EachPlan();
			
			// 每期还款即是本期的还款总额
			if (i == periods - 1) { // 判断是否是最后一期
				eCapital = account;
				//最后一期利息
				eInterest=BigDecimalUtils.round(BigDecimalUtils.sub(totalInterest, sum), 2);
				eRepayTime = this.calculatorRepaytime(periodTime, i+1);
				addInterest = remainAddInterest;
				
			}else{				
				eRepayTime = this.calculatorRepaytime(firstRepayTime, (i+1), fixedRepayDay);
				if (i == periods - 2 && calculatorRepaytime(periodTime, i+2).before(eRepayTime)) {//倒数第二期的还款时间晚于最后一期的还款时间
					continue;
				}
				// 计算本金
				eCapital = BigDecimal.ZERO;
				// 计算每月需要支付的利息
				eInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, yearApr), MONTHS_OF_FULL_YEAR, 2) ;
				addInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, needRepay, addApr), MONTHS_OF_FULL_YEAR, 2) ;
				if (i == 0) {//第二期
					eInterest = BigDecimalUtils.add(eInterest, periodDiffInterest);
					addInterest = BigDecimalUtils.add(addInterest, periodDiffAddInterest);
					periodDiffInterest = null;
					periodDiffAddInterest = null;					
				}
			}
			remainAddInterest = BigDecimalUtils.sub(remainAddInterest, addInterest);
			netInterest = BigDecimalUtils.mul(eInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
			eTotal = BigDecimalUtils.round(BigDecimalUtils.add(eCapital, eInterest), 2);
			// 本期开始计息日
			//eInterestTime = DateUtils.rollDay(interestTime, 1);
			// 本期还款日
			//eRepayTime = this.calculatorRepaytime(eInterestTime,i+1);
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			e.setAddInterest(addInterest);//加息利息
			e.setNetInterest(BigDecimalUtils.round(netInterest,2));
			e.setTotal(eTotal);
			BigDecimal netTotal = BigDecimalUtils.round(BigDecimalUtils.add(eCapital, netInterest), 2);
			e.setNetTotal(netTotal);
			e.setInterestTime(eInterestTime);
			e.setPeriodTime(periodTime);
			e.setRepayTime(eRepayTime);
			remainAccount = BigDecimalUtils.sub(remainAccount,eTotal);
			e.setNeedRepay(remainAccount);
			repayTime = eRepayTime;
			eachPlan.add(e);
			sum = BigDecimalUtils.round(BigDecimalUtils.add(eInterest, sum), 2);
		}
		this.repayInterest = totalInterest ;
		this.collectInterest = BigDecimalUtils.add(this.repayInterest,this.collectInterest);
		this.repayAccount = BigDecimalUtils.decimal(BigDecimalUtils.add(account, sum),2);
		//修正金额，防止出现负数情况 added by lh 20161009
		correctEachPlan();
		return eachPlan;
	}
	
	
	
	@Override
	public List<EachPlan> calculator(int days) {
		throw new BussinessException(ResourceUtils.get(ResourceConstant.CALCULATOR_DAYREPAY_NOTSUPPORT), 
				BussinessException.TYPE_JSON);
	}

	@Override
	public Date calculatorRepaytime(Date date,int i) {
		return DateUtils.rollMon(date,i);
	}
	
	private Date calculatorRepaytime(Date date, int i, Integer fixDay) {
		Date repayTime = DateUtils.rollMon(date,i);
		if (repayTime.getMonth() != 1 && repayTime.getDate() != fixDay.intValue()) {//2月 不处理
			repayTime.setDate(fixDay.intValue());
		}
		return repayTime;
	}
	
	@Override
	public Date calculatorRepaytime(Date date) {
		return null;
	}

	public Integer getFixedRepayDay() {
		return fixedRepayDay;
	}

	public void setFixedRepayDay(Integer fixedRepayDay) {
		this.fixedRepayDay = fixedRepayDay;
	}

	
}

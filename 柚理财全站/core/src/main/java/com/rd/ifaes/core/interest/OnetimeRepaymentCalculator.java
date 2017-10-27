package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;


/**
 * <p>一次性还款利息计算函数</p>
 * 计算公式：<br>
 * 设贷款额为a,月利率为i，借款月数为n,还款本息总和为Y,还款利息总和为Z<br>
 * 还款本息总和：Y=a+a*i*n<br>
 * 还款利息总和：Z=a*i*n<br>
 *  
 * @author：xx
 * @version 3.0
 * @since 2016年7月6日
 */
public class OnetimeRepaymentCalculator extends AbstractInterestCalculator {

	/** 天利率 */
	private BigDecimal dayApr;

	/**
	 * 初始化一次性还款构造函数
	 * @param account 借款总额
	 * @param yearApr 年化利率
	 * @param addApr  加息利率
	 * @param interestTime 开始计息日
	 * @param startPeriod 期限开始日
	 * @param periods 期数
	 * @param manageFee 利息管理费
	 */
	public OnetimeRepaymentCalculator(BigDecimal account, BigDecimal yearApr, BigDecimal addApr, Date interestTime, Date startPeriod, int periods, BigDecimal manageFee) {
		super();
		this.account = account;
		this.yearApr = yearApr;
		this.addApr = addApr;
		this.manageFee = manageFee;
		this.interestTime = interestTime;
		this.periodTime = startPeriod;
		this.periods = periods;
		this.eachPlan = new ArrayList<EachPlan>();
	}
	
	/**
	 * 按月计息
	 * 
	 * <p>利差处理</p> 
	 * 
	 */
	@Override
	public List<EachPlan> calculator() {
		EachPlan e = new EachPlan();
		//开始计息日
		Date eInterestTime = interestTime;//DateUtils.rollDay(interestTime, 1);
		//还款日
		Date eRepayTime = this.calculatorRepaytime(periodTime);//还款日计算依据期限起始日：2016-8-2
		
		/***** 利差处理 start *****/
		//利差
		BigDecimal daysInterest = getDaysInterest();
		//利息
		BigDecimal interest = BigDecimalUtils.div(BigDecimalUtils.mul(true, account, yearApr, BigDecimal.valueOf(periods)), MONTHS_OF_FULL_YEAR, 2);
		interest = interest.add(daysInterest);//加上利差
		/***** 利差处理  end *****/
		
		e.setCapital(account);
		e.setInterest(interest);
		//加息利息计算	2016-8-20
		BigDecimal addInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, account, addApr, BigDecimal.valueOf(periods)), MONTHS_OF_FULL_YEAR, 2);
		addInterest = addInterest.add(getDaysInterest(addApr));
		e.setAddInterest(addInterest);
		//加息利息计算 end
		BigDecimal netInterest = BigDecimalUtils.mul(interest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
		e.setNetInterest(netInterest);
		BigDecimal netTotal = BigDecimalUtils.add(account, netInterest);
		e.setNetTotal(netTotal);
		e.setTotal(BigDecimalUtils.add(interest, account));
		e.setInterestTime(eInterestTime);
		e.setPeriodTime(periodTime);
		e.setRepayTime(eRepayTime);
		eachPlan.add(e);
		// 汇总信息
		this.repayAccount = e.getTotal();
		this.repayInterest = e.getInterest();
		this.collectInterest=BigDecimalUtils.add(repayInterest,addInterest);
		repayTime = e.getRepayTime();
		return eachPlan;
	}

	/**
	 * 按日计息
	 */
	@Override
	public List<EachPlan> calculator(int days) {
		
		BigDecimal daysOfYear = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_YEAR);
		daysOfYear = (daysOfYear.compareTo(BigDecimal.ZERO) == 0)?DEFAULT_DAYS_OF_FULL_YEAR:daysOfYear;
		
		eachPlan = new ArrayList<EachPlan>();
		EachPlan e = new EachPlan();
		//利息= 年化利率 * 金额  * 投资天数 / 一年的天数
		BigDecimal interest = BigDecimalUtils.div(BigDecimalUtils.mul(true, this.yearApr, account, BigDecimal.valueOf(days)), daysOfYear);
		interest = interest.add(getDaysInterest());//利差
		if(interest.compareTo(BigDecimal.ZERO) < 0){
			interest = BigDecimal.ZERO;
		}
		//还款日
		Date eRepayTime = this.calculatorRepaytime(periodTime,days);
		e.setCapital(account);
		e.setInterest(interest);
		// 加息利息 2016-8-20
		BigDecimal addInterest = BigDecimalUtils.mul(BigDecimalUtils.div(addApr, daysOfYear, DEFAULT_MONEY_SCALE), account, BigDecimal.valueOf(days));
		addInterest = addInterest.add(getDaysInterest(addApr));// 利差
		e.setAddInterest(addInterest);
		// 加息利息 end
		
		BigDecimal netInterest = BigDecimalUtils.mul(interest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
		e.setNetInterest(netInterest);
		BigDecimal total = BigDecimalUtils.round(BigDecimalUtils.add(account, interest), 2);
		e.setTotal(total);
		BigDecimal netTotal = BigDecimalUtils.add(account, netInterest);
		e.setNetTotal(netTotal);
		e.setInterestTime(interestTime);
		e.setPeriodTime(periodTime);
		e.setRepayTime(eRepayTime);
		eachPlan.add(e);
		// 汇总信息
		this.repayAccount = e.getTotal();
		this.repayInterest = e.getInterest();
		this.collectInterest=BigDecimalUtils.add(this.repayInterest,addInterest);
		repayTime = e.getRepayTime();
		return eachPlan;
	}


	@Override
	public Date calculatorRepaytime(Date date) {
		return DateUtils.rollMon(date, periods);
	}

	@Override
	public Date calculatorRepaytime(Date date, int i) {		
		return DateUtils.rollDay(date, i);
	}
	
}

package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;


/**
 * <p>等额本金利息计算函数</p>
 * 设贷款额为a，月利率为i，还款月数为n，每月还款额为b，每月还款本金为c，还款本息总和为Y，还款利息总和为Z
	计算公式：
	每月还款本金为：c=a÷n
	第一期还款金额为：b1=c+a*i
	第二期还款金额为：b2=c+(a-c) *i
	第三期还款金额为：b3=c+(a-2c) *i
	第...期还款金额为：bx=c+[a-(x-1)*c]*i
	第n期还款金额为：bn= c+c*i
	还款本息总额为：Y=a+(n-1)*a*i/2
	还款利息总和为：Z=(n+1)*a*i/2

 * 
 * @author lh
 * @version 3.0
 * @since 2016年7月6日
 */
public class AverageCapitalRepaymentCalculator extends AbstractInterestCalculator {

	/**
	 * 初始化月等额本金构造函数
	 * 
	 * @param account 借款总额
	 * @param yearApr 年化利率
	 * @param addApr  加息利率
	 * @param interestTime 开始计息日
	 * @param periodTime 项目期限起始日
	 * @param periods 期数
	 * @param manageFee 利息管理费
	 */
	public AverageCapitalRepaymentCalculator(BigDecimal account, BigDecimal yearApr, BigDecimal addApr, Date interestTime, Date periodTime, int periods,
			BigDecimal manageFee) {
		this.account = account;
		this.yearApr = yearApr;
		this.addApr = addApr;
		this.periods = periods;
		this.interestTime = interestTime;
		this.periodTime = periodTime;
		this.manageFee = manageFee;
		this.eachPlan = new ArrayList<EachPlan>();
	}

	@Override
	public List<EachPlan> calculator() {
		
		final BigDecimal bdPeriods = BigDecimal.valueOf(periods);
		//月利率
		BigDecimal monthApr =  BigDecimalUtils.div(yearApr, MONTHS_OF_FULL_YEAR, DEFAULT_MONEY_SCALE);
		//总利息
		BigDecimal totalInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, bdPeriods.add(BigDecimal.ONE), account, monthApr), BigDecimal.valueOf(2), 2);	
		// 利差
		BigDecimal daysInterest = getDaysInterest();
		totalInterest = totalInterest.add(daysInterest);// 总待还利息加上利差
		//加息利差
		BigDecimal daysAddInterest = BigDecimal.ZERO;
		if (addApr.compareTo(BigDecimal.ZERO) > 0) {
			daysAddInterest = getDaysInterest(addApr);			
		}		
		
		//剩余待还
		BigDecimal needRepay = BigDecimalUtils.add(account, totalInterest);
		BigDecimal eInterest = BigDecimal.ZERO;
		BigDecimal netInterest = BigDecimal.ZERO;
		BigDecimal eTotal = BigDecimal.ZERO;
		//每月还款本金
		BigDecimal eCapital = BigDecimalUtils.div(account, bdPeriods);
		BigDecimal sumCapital = BigDecimal.ZERO;
		BigDecimal sumInterest = BigDecimal.ZERO;
		Date eInterestTime = interestTime;
		Date eRepayTime = null;
		repayInterest= BigDecimal.ZERO;
		/**
		 * 加息利息计算
		 * @date 2016-8-20
		 * @author lh
		 */
		// 月加息利率
		BigDecimal monthAddApr = BigDecimalUtils.div(addApr, MONTHS_OF_FULL_YEAR, DEFAULT_MONEY_SCALE);
		// 剩余加息利息
		BigDecimal remainAddInterest = BigDecimal.ZERO;
		if (addApr.compareTo(BigDecimal.ZERO) > 0) {
			remainAddInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, bdPeriods.add(BigDecimal.ONE), account, monthAddApr), BigDecimal.valueOf(2), 2);
			remainAddInterest = BigDecimalUtils.add(remainAddInterest, daysAddInterest);//加息利息加上加息利差		
		} 
		this.collectInterest=remainAddInterest;
		// 每月加息利息
		BigDecimal addInterest = BigDecimal.ZERO;
		
		for (int i = 0; i < periods; i++) {
			EachPlan e = new EachPlan();
			if (i == periods - 1) { // 最后一期
				eCapital = BigDecimalUtils.sub(account, sumCapital);
				eInterest = BigDecimalUtils.sub(totalInterest, sumInterest);
				addInterest = remainAddInterest;
			}else{
				sumCapital = BigDecimalUtils.add(sumCapital, eCapital);
				//( 借款金额-(期数-1)*月还本金 )*月利率
				eInterest = BigDecimalUtils.mul(BigDecimalUtils.sub(account, BigDecimalUtils.mul(true, BigDecimal.valueOf(i), eCapital)) , monthApr);
				addInterest = BigDecimalUtils.mul(BigDecimalUtils.sub(account, BigDecimalUtils.mul(true, BigDecimal.valueOf(i), eCapital)) , monthAddApr);
				
				if (i == 0) {// 第一期			
					eInterest = eInterest.add(daysInterest);// 第一期加上利差
					addInterest = addInterest.add(daysAddInterest);// 第一期加上利差
				}
				
				sumInterest = BigDecimalUtils.add(sumInterest, eInterest);				
			}
			remainAddInterest = BigDecimalUtils.sub(remainAddInterest, addInterest);
			netInterest = BigDecimalUtils.mul(eInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
			// 本期的还款总额
			eTotal = BigDecimalUtils.add(eInterest, eCapital);
			needRepay = BigDecimalUtils.sub(needRepay, eTotal);
			// 本期开始计息日
			//eInterestTime = DateUtils.rollDay(interestTime, 1);
			// 本期还款日
			eRepayTime = this.calculatorRepaytime(periodTime, i+1);//还款日计算依据期限起始日：2016-8-2
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			e.setAddInterest(addInterest);
			e.setNetInterest(netInterest);
			e.setTotal(eTotal);
			BigDecimal netTotal = BigDecimalUtils.add(eCapital, netInterest);
			e.setNetTotal(netTotal);
			e.setNeedRepay(needRepay);
			e.setInterestTime(eInterestTime);
			e.setPeriodTime(periodTime);
			e.setRepayTime(eRepayTime);
			repayTime = eRepayTime;
			eachPlan.add(e);
		}
		// 汇总信息
		this.repayAccount = BigDecimalUtils.add(account, totalInterest);
		this.repayInterest = totalInterest;
		this.collectInterest=BigDecimalUtils.add(this.collectInterest,repayInterest);
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
	public Date calculatorRepaytime(Date date) {
		return DateUtils.rollMon(date, 1);
	}
	
	@Override
	public Date calculatorRepaytime(Date date, int i) {
		return DateUtils.rollMon(date, i);
	}

}

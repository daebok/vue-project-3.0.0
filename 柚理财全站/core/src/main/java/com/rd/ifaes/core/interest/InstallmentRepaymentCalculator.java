package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;


/**
 * <p>等额本息利息计算函数</p>
 * 设贷款额为a，月利率为i，还款月数为n，每月还款额为b，每月还款本金为ax，每月还款利息为zx，还款本息总和为Y，还款利息总和为Z
	计算公式：
	月均还款本息：b＝a*i*（1＋i）^n÷〔（1＋i）^n-1〕
	第一期   还款利息为：z1=a*i                       还款本金为：a1=b-z1
	第二期   还款利息为：z2=（a-a1）*i                 还款本金为：a2=b-z2
	第X期    还款利息为：zx=（a-a1-a2-…-ax-1）*i       还款本金为：ax=b-zx
	第n期    还款利息为：zn=(a-a1-a2-…-a(n-1))*i         还款本金为：an=b-zn
	还款本息总和为:Y=bn
	还款利息总和：Z＝Y-a
 * 
 * @author：xx
 * @version 3.0
 * @since 2016年7月6日
 */
public class InstallmentRepaymentCalculator extends AbstractInterestCalculator {

	/**
	 * 初始化月等额本息构造函数
	 * 
	 * @param account 借款总额
	 * @param yearApr 年化利率
	 * @param addApr  加息利率
	 * @param interestTime 开始计息日
	 * @param periodTime 项目期限起息日
	 * @param periods 期数
	 * @param manageFee 利息管理费
	 */
	public InstallmentRepaymentCalculator(BigDecimal account, BigDecimal yearApr, BigDecimal addApr, Date interestTime, Date periodTime, int periods,
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
		BigDecimal eachRepay = BigDecimalUtils.round(mrpi(account, yearApr, periods), 2);
		BigDecimal remain = account;
		BigDecimal needRepay = BigDecimal.ZERO;
		BigDecimal eInterest = BigDecimal.ZERO;
		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal netInterest = BigDecimal.ZERO;
		BigDecimal eCapital = BigDecimal.ZERO;
		BigDecimal eTotal = BigDecimal.ZERO;
		Date eInterestTime = interestTime;
		Date eRepayTime = null;
		this.collectInterest=BigDecimal.ZERO;
		//利差
		BigDecimal daysInterest = getDaysInterest();
		for (int i = 0; i < periods; i++) {
			EachPlan e = new EachPlan();
			
//			等额本息取消最后一笔用减法的计算方式，直接通过公式计算// 判断是否为最后一期，最后一期做减法
//			if (i == periods -1) {
//				// 计算本金
//				eCapital = BigDecimalUtils.round(remain, 2);
//				// 利息 = 每月还款金额 - 本金
//				eInterest = BigDecimalUtils.round(BigDecimalUtils.sub(eachRepay, eCapital),2);
//			} else {
				// 计算每月需要支付的利息：剩余本金*月息
				eInterest = BigDecimalUtils.round(BigDecimalUtils.div(BigDecimalUtils.mul(true, remain, yearApr), MONTHS_OF_FULL_YEAR));
				// 计算本金
				eCapital = BigDecimalUtils.sub(eachRepay, eInterest);				
				
				if (i == 0) {// 第一期：2016-8-2
					/**
					 * 注：月等额本息的总利息是每期的利息累加的，所以不需要在总利息上加上利差
					 */
					eInterest = eInterest.add(daysInterest);// 第一期加上利差
				}else if(i == periods -1){//最后一笔本金用减法
					eCapital = remain;
				}
				
//			}
				
			
			netInterest = BigDecimalUtils.mul(eInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
			// 每期还款即是本期的还款总额
			eTotal = BigDecimalUtils.add(eInterest, eCapital);
			//needRepay = BigDecimalUtils.sub(needRepay, eachRepay);
			// 用于计算利息的剩余金额
			remain = BigDecimalUtils.sub(remain, eCapital);
			// 本期开始计息日
			//eInterestTime = DateUtils.rollDay(interestTime, 1);
			// 本期还款日
			eRepayTime = this.calculatorRepaytime(periodTime, i+1);
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			totalInterest = BigDecimalUtils.add(totalInterest, eInterest);
			e.setNetInterest(netInterest);
			e.setTotal(eTotal);
			BigDecimal netTotal = BigDecimalUtils.add(eCapital, netInterest);
			e.setNetTotal(netTotal);
			//e.setNeedRepay(needRepay);
			e.setInterestTime(eInterestTime);
			e.setPeriodTime(periodTime);
			e.setRepayTime(eRepayTime);
			repayTime = eRepayTime;
			eachPlan.add(e);
		}
		
		for (int i = periods-1; i >= 0; i--) {
			EachPlan plan = eachPlan.get(i);
			plan.setNeedRepay(needRepay);			
			needRepay = BigDecimalUtils.add(needRepay, plan.getTotal());
		}
		
		
		// 加息利息
		if(addApr.compareTo(BigDecimal.ZERO) > 0){
			List<BigDecimal> addAprList = addAprCalculator(addApr);
			for (int j = 0; j < addAprList.size(); j++) {
				EachPlan ep = eachPlan.get(j);
				ep.setAddInterest(addAprList.get(j));// 加息利息
				this.collectInterest=BigDecimalUtils.add(this.collectInterest,ep.getAddInterest());
			}
		}
		// 汇总信息
		this.repayAccount = needRepay.add(daysInterest);//总额加上利差
		this.repayInterest = totalInterest;
		this.collectInterest=BigDecimalUtils.add(this.repayInterest,this.collectInterest);
		//修正金额，防止出现负数情况 added by lh 20161009
		correctEachPlan();
		//修正中间期数本息过小的问题 added by lh 20161116
		correctInstallEachPlan();
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

	/**
	 * 计算每一期等额的还款金额
	 * 
	 * @param p 借款总额
	 * @param r 年化利率
	 * @param mn 期数
	 * @return 每期还款金额
	 */
	public BigDecimal mrpi(BigDecimal p, BigDecimal r, int mn) {
		BigDecimal mr = BigDecimalUtils.div(r, MONTHS_OF_FULL_YEAR, DEFAULT_MONEY_SCALE);
		BigDecimal aprPow = mr.add(BigDecimal.ONE).pow(mn);
		BigDecimal period = BigDecimal.ONE;
		if (aprPow.compareTo(BigDecimal.ONE) > 0) {
			period = aprPow.subtract(BigDecimal.ONE);
		}
		return BigDecimalUtils.div(BigDecimalUtils.mul(true, p, mr, aprPow), period, DEFAULT_MONEY_SCALE);
	}
	
	/**
	 * 加息利息计算
	 * @param addApr	加息利率
	 * @return
	 */
	public List<BigDecimal> addAprCalculator(BigDecimal addApr){
		
		List<BigDecimal> addInterestList = new ArrayList<BigDecimal>();
		BigDecimal eachRepay = BigDecimalUtils.round(mrpi(account, addApr, periods), 2);
		BigDecimal remain = account;
		BigDecimal eCapital = BigDecimal.ZERO;
		BigDecimal eInterest = BigDecimal.ZERO;
		//利差
		BigDecimal daysInterest = getDaysInterest(addApr);
		
		for (int i = 0; i < periods; i++) {
			
//			// 判断是否为最后一期，最后一期做减法
//			if (i == periods -1) {
//				// 计算本金
//				eCapital = BigDecimalUtils.round(remain, 2);
//				// 利息 = 每月还款金额 - 本金
//				eInterest = BigDecimalUtils.round(BigDecimalUtils.sub(eachRepay, eCapital),2);
//			} else {
				// 计算每月需要支付的利息：剩余本金*月息
				eInterest = BigDecimalUtils.round(BigDecimalUtils.div(BigDecimalUtils.mul(true, remain, addApr), MONTHS_OF_FULL_YEAR));
				// 计算本金
				eCapital = BigDecimalUtils.sub(eachRepay, eInterest);				
				
				if (i == 0) {// 第一期
					eInterest = eInterest.add(daysInterest);// 第一期加上利差
				}else if(i == periods -1){//最后一笔本金用减法
					eCapital = remain;
				}				
//			}
			// 用于计算利息的剩余金额
			remain = BigDecimalUtils.sub(remain, eCapital);
			addInterestList.add(eInterest);
		}
		return addInterestList;
	}
	
	/**
	 * 修正等额本息中间出现较小本息的情况，当出现时位置互换
	 */
	private void correctInstallEachPlan(){
		List<EachPlan> leftPlans = new ArrayList<>();
		int idx = 0;
		for (EachPlan ep : eachPlan) {
			if(ep.getInterest().compareTo(BigDecimal.ZERO) > 0 ){
				idx++;
				continue;
			}
			if(ep.getTotal().compareTo(BigDecimal.ZERO) == 0){
				continue;
			}
			leftPlans.add(ep);
		}
		if(!leftPlans.isEmpty() && leftPlans.size() >= 2){
			int leftSize = leftPlans.size()-1;
			int middle = leftSize/2;
			for (int i = 0; i < middle ; i++) {
				EachPlan left = leftPlans.get(i);
				int j = leftSize-i;
				EachPlan right = leftPlans.get(j);
				
				if(left.getTotal().compareTo(right.getTotal()) < 0){
					Date leftRepayTime = left.getRepayTime();
					Date rightRepayTime = right.getRepayTime();
					left.setRepayTime(rightRepayTime);
					right.setRepayTime(leftRepayTime);
					Collections.swap(eachPlan, idx+i, idx+j);
				}					
			}
		}
	}
	
}

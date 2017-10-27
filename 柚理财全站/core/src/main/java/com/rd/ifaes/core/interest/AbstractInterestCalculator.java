package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 利息计算抽象类
 * @author lh
 * @version 3.0
 * @since 2016-7-21
 *
 */
public abstract class AbstractInterestCalculator implements InterestCalculator {
	
	/** 借款总额 */
	protected BigDecimal account;
	/** 年化利率 */
	protected BigDecimal yearApr;
	/** 加息利率 */
	protected BigDecimal addApr;
	/** 期数 */
	protected int periods;
	/** 还款金额 */
	protected BigDecimal repayAccount;
	/** 还款总利息 （不含加息）*/
	protected BigDecimal repayInterest;
	/** 待收总利息（含加息） */
	protected BigDecimal collectInterest;
	/** 利息管理费 */
	protected BigDecimal manageFee;
	/** 开始计息日 */
	protected Date interestTime;
	/** 产品期限开始日 */
	protected Date periodTime;
	/** 还款日 */
	protected Date repayTime;
	/** 还款计划 */
	protected List<EachPlan> eachPlan;
	
	public AbstractInterestCalculator() {
		super();
	}
	
	@Override
	public List<EachPlan> getEachPlan() {
		return eachPlan;
	}

	@Override
	public BigDecimal repayTotal() {
		return repayAccount;
	}
	
	@Override
	public BigDecimal repayInterest() {
		return repayInterest;
	}

	@Override
	public Date repayTime() {
		return repayTime;
	}

	@Override
	public int repayPeriods() {
		return periods;
	}

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public BigDecimal getYearApr() {
		return yearApr;
	}

	public void setYearApr(BigDecimal yearApr) {
		this.yearApr = yearApr;
	}

	public BigDecimal getAddApr() {
		return addApr;
	}

	public void setAddApr(BigDecimal addApr) {
		this.addApr = addApr;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public BigDecimal getRepayAccount() {
		return repayAccount;
	}

	public void setRepayAccount(BigDecimal repayAccount) {
		this.repayAccount = repayAccount;
	}

	public BigDecimal getRepayInterest() {
		return repayInterest;
	}

	/**
	 * 获取属性collectInterest的值
	 * @return collectInterest属性值
	 */
	public BigDecimal getCollectInterest() {
		return collectInterest;
	}
 
	public BigDecimal collectInterest() {
		return collectInterest;
	}
	
	@Override
	public BigDecimal collectTotal() {
		return  BigDecimalUtils.sub(this.repayAccount.add(this.collectInterest), this.repayInterest);
	}
	
	
	@Override
	public BigDecimal raiseInterest() {
		return  BigDecimalUtils.sub(this.collectInterest, this.repayInterest);
	}
	
	/**
	 * 设置属性collectInterest的值
	 * @param  collectInterest属性值
	 */
	public void setCollectInterest(BigDecimal collectInterest) {
		this.collectInterest = collectInterest;
	}

	public void setRepayInterest(BigDecimal repayInterest) {
		this.repayInterest = repayInterest;
	}

	public BigDecimal getManageFee() {
		return manageFee;
	}

	public void setManageFee(BigDecimal manageFee) {
		this.manageFee = manageFee;
	}

	public Date getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}

	public Date getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(Date periodTime) {
		this.periodTime = periodTime;
	}

	public Date getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}

	public void setEachPlan(List<EachPlan> eachPlan) {
		this.eachPlan = eachPlan;
	}
	
	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(final Date start, final Date end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(sdf.format(start));
			endDate = sdf.parse(sdf.format(end));
		} catch (ParseException e) {
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		final long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(betweenDays));
	}
	
	/**
	 * 取得当月最后一天
	 * @author lh
	 * @date 2016年8月1日
	 * @param date 日期
	 * @return
	 */
	public static int monthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * <p>利差：项目期限起始日和起息日不是同一天，导致利息会多算或者少算，多算或者少算的利息差值即为利差</p>
	 * 固定还款日和无固定还款日的利差计算相同<br>
	 * 不同之处：<br>
	 * 	1、固定还款日的利差只需加在总利息上；<br>
	 * 	2、无固定还款日的利差需加在总利息和第一期利息上；<br>
	 *  
	 * @createTime 	2016-8-2
	 * @author 		lihua
	 */
	public BigDecimal getDaysInterest() {
		int days = daysBetween(interestTime, periodTime);
		//相差天数的利息
		BigDecimal daysInterest = BigDecimal.ZERO;
		if(days != 0){
			//一年的天数
			BigDecimal daysOfYear = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_YEAR);
			daysOfYear = (daysOfYear.compareTo(BigDecimal.ZERO) <= 0)?DEFAULT_DAYS_OF_FULL_YEAR:daysOfYear;
			//日利息
			BigDecimal dayInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, account, yearApr), daysOfYear, DEFAULT_MONEY_SCALE);
			//相差天数的利息
			daysInterest = BigDecimalUtils.mul(dayInterest, BigDecimal.valueOf(days));
		}
		return daysInterest;
	}
	
	/**
	 * 利差计算
	 * @param apr
	 * @return
	 */
	public BigDecimal getDaysInterest(BigDecimal apr) {
		if(apr == null || apr.compareTo(BigDecimal.ZERO) == 0){
			return BigDecimal.ZERO;
		}
		int days = daysBetween(interestTime, periodTime);
		//相差天数的利息
		BigDecimal daysInterest = BigDecimal.ZERO;
		if(days != 0){
			//一年的天数
			BigDecimal daysOfYear = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_YEAR);
			daysOfYear = (daysOfYear.compareTo(BigDecimal.ZERO) <= 0)?DEFAULT_DAYS_OF_FULL_YEAR:daysOfYear;
			//日利息
			BigDecimal dayInterest = BigDecimalUtils.div(BigDecimalUtils.mul(true, account, apr), daysOfYear, DEFAULT_MONEY_SCALE);
			//相差天数的利息
			daysInterest = BigDecimalUtils.mul(dayInterest, BigDecimal.valueOf(days));
		}
		return daysInterest;
	}
	
	/**
	 * 每期的金额（本金、利息、加息利息等）修正
	 * @author lh
	 * @since 2016-10-9
	 */
	public void correctEachPlan(){
		int size = eachPlan.size();
		EachPlan lastPlan = eachPlan.get(size - 1);
	
		//修正本金
		correctCapital(size, lastPlan);
		
		//修正利息
		correctInterest(size, lastPlan);
		
		//修正加息利息
		correctAddInterest(size, lastPlan);
	}

	/**
	 * 修正加息利息
	 * @param size
	 * @param lastPlan
	 */
	private void correctAddInterest(int size, EachPlan lastPlan) {
		BigDecimal lastAddInterest = lastPlan.getAddInterest();
		if( lastAddInterest!=null &&negative(lastAddInterest) ){			
			lastPlan.setAddInterest(BigDecimal.ZERO);
			lastPlan.setNeedRepay(BigDecimal.ZERO);
			if(size >= 2){
				for (int i = size - 2; i >= 0; i--) {
					EachPlan plan = eachPlan.get(i);
					BigDecimal addInterest = plan.getAddInterest();
					BigDecimal diff = BigDecimal.ZERO;//修正差值，>=0
					if(!negative(lastAddInterest)){//剩余修正非负，跳出循环
						break;
					}
					diff = addInterest.compareTo(lastAddInterest.negate())>0?lastAddInterest.negate():addInterest;
					plan.setAddInterest(addInterest.subtract(diff));
					lastAddInterest = lastAddInterest.add(diff);//剩余修正
				}					
			}
		}
	}

	/**
	 * 修正利息
	 * @param size
	 * @param lastPlan
	 */
	private void correctInterest(int size, EachPlan lastPlan) {
		BigDecimal lastInterest = lastPlan.getInterest();
		if(negative(lastInterest) ){			
			BigDecimal lastNetInterest = BigDecimalUtils.mul(lastInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
			lastPlan.setInterest(BigDecimal.ZERO);
			lastPlan.setNetInterest(BigDecimal.ZERO);				
			lastPlan.setTotal(lastPlan.getTotal().subtract(lastInterest));//本息
			lastPlan.setNetTotal(lastPlan.getNetTotal().subtract(lastNetInterest));//净收
			lastPlan.setNeedRepay(BigDecimal.ZERO);
			if(size >= 2){
				for (int i = size - 2; i >= 0; i--) {
					EachPlan plan = eachPlan.get(i);
					BigDecimal interest = plan.getInterest();
					BigDecimal diff = BigDecimal.ZERO;//修正差值，>=0
					if(!negative(lastInterest)){//剩余修正非负，跳出循环
						break;
					}
					diff = interest.compareTo(lastInterest.negate())>0?lastInterest.negate():interest;	
					plan.setInterest(interest.subtract(diff));
					plan.setTotal(plan.getTotal().subtract(diff));//本息
					//净利息
					BigDecimal netInterest = BigDecimalUtils.mul(plan.getInterest(), BigDecimalUtils.sub(BigDecimal.ONE, manageFee));
					BigDecimal diffNet = plan.getNetInterest().subtract(netInterest);
					plan.setNetInterest(netInterest);					
					plan.setNetTotal(plan.getNetTotal().subtract(diffNet));//净收
					plan.setNeedRepay(plan.getNeedRepay().subtract(BigDecimalUtils.mul(lastInterest, BigDecimalUtils.sub(BigDecimal.ONE, manageFee))));//剩余待还
					lastInterest = lastInterest.add(diff);//剩余修正
				}					
			}
		}
	}

	/**
	 * 修正本金
	 * @param size
	 * @param lastPlan
	 */
	private void correctCapital(int size, EachPlan lastPlan) {		
		BigDecimal lastCapital = lastPlan.getCapital();
		if(negative(lastCapital) ){			
			lastPlan.setCapital(BigDecimal.ZERO);
			lastPlan.setTotal(lastPlan.getTotal().subtract(lastCapital));//本息
			lastPlan.setNetTotal(lastPlan.getNetTotal().subtract(lastCapital));//净收
			lastPlan.setNeedRepay(BigDecimal.ZERO);
			if(size >= 2){
				for (int i = size - 2; i >= 0; i--) {
					EachPlan plan = eachPlan.get(i);
					BigDecimal capital = plan.getCapital();
					BigDecimal diff = BigDecimal.ZERO;//修正差值，>=0
					if(!negative(lastCapital)){//剩余修正非负，跳出循环
						break;
					}
					diff = capital.compareTo(lastCapital.negate())>0?lastCapital.negate():capital;					
					plan.setCapital(capital.subtract(diff));
					plan.setTotal(plan.getTotal().subtract(diff));//本息
					plan.setNetTotal(plan.getNetTotal().subtract(diff));//净收
					plan.setNeedRepay(plan.getNeedRepay().subtract(lastCapital));//剩余待还
					lastCapital = lastCapital.add(diff);//剩余修正
				}			
			}
		}
	}
	
	
	/**
	 * 是否负数
	 * @param decimal
	 * @return
	 */
	private static boolean negative(BigDecimal decimal){
		return decimal.compareTo(BigDecimal.ZERO) < 0;
	}
	
}

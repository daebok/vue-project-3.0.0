package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 利息计算接口：策略模式
 * http://www.cnblogs.com/java-my-life/archive/2012/05/10/2491891.html
 * 
 * @author：xx
 * @version 3.0
 * @since 2016年7月6日
 */
public interface InterestCalculator {
	
	/**
	 * 全年的季度数
	 */
	BigDecimal QUARTER_OF_FULL_YEAR = BigDecimal.valueOf(4);
	/**
	 * 全年的月数
	 */
	BigDecimal MONTHS_OF_FULL_YEAR = BigDecimal.valueOf(12);	
	
	/**
	 * 每月的平均天数
	 */
	BigDecimal DEFAULT_DAYS_OF_MONTH = BigDecimal.valueOf(30);
	
	/**
	 * 全年的天数
	 */
	BigDecimal DEFAULT_DAYS_OF_FULL_YEAR = BigDecimal.valueOf(360);
	
	/**
	 * 默认精度
	 */
	int DEFAULT_MONEY_SCALE = 20;
	
	/**
	 * 计算每期还款信息
	 * 
	 * @return 还款计划
	 */
	List<EachPlan> calculator();

	/**
	 * 计算按天计算还款信息
	 * 
	 * @param days 天数
	 * @return 还款计划
	 */
	List<EachPlan> calculator(int days);

	/**
	 * 取得每期还款计划
	 * 
	 * @return 每期还款计划
	 */
	List<EachPlan> getEachPlan();

	/**
	 * 还款总额
	 * 
	 * @return 还款总额
	 */
	BigDecimal repayTotal();
	
	/**
	 * 待还总利息（不含加息利息）
	 * @return
	 */
	BigDecimal repayInterest();
	
	/**
	 * 加息利息
	 * @return
	 */
	BigDecimal raiseInterest();
	
	/**
	 * 待收总额（本金+利息+加息利息）
	 * @return
	 */
	BigDecimal collectTotal();
	
	/**
	 * 待收总利息（利息+加息利息）
	 * @return
	 */
	BigDecimal collectInterest();
	

	/**
	 * 最后一期还款时间
	 * 
	 * @return 最后一期还款时间
	 */
	Date repayTime();

	/**
	 * 还款总期数
	 * 
	 * @return 还款总期数
	 */
	int repayPeriods();
	
	/**
	 * 还款时间计算公式
	 * 
	 * @param date 投资时间
	 * @return 还款时间
	 */
	Date calculatorRepaytime(Date date);

	/**
	 * 还款时间计算公式
	 * 
	 * @param date 投资时间
	 * @param i 期数
	 * @return 还款时间
	 */
	Date calculatorRepaytime(Date date,int i);
}

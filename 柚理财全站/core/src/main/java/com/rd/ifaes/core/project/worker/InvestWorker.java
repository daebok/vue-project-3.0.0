package com.rd.ifaes.core.project.worker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.project.domain.Product;
import com.rd.ifaes.core.project.domain.ProjectInvest;

/**
 * 投资 相关业务处理
 * @author lh
 * @version 3.0
 * @since 2016-8-1
 *
 */
public class InvestWorker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestWorker.class);
	private ProjectInvest invest;	
	public InvestWorker(ProjectInvest invest) {
		this.invest = invest;
	}
	
	
	/**
	 * 当日能否赎回
	 * @param date		实际赎回日（默认为系统当前日期）
	 * @param product	理财产品
	 * @return
	 */
	public boolean canRansom(final Date date, final Product product){
		if(product == null){
			throw new BussinessException("no product selected!");
		}
		Date localDate = date;
		if(localDate == null){
			localDate = DateUtils.getNow();
		}
		localDate = formatToDate(localDate);
		
		//赎回开始日
		Date ransomDate = getRansomStartDate(product.getInterestStyle(), product.getReviewTime(), invest.getCreateTime());
		//到期日
		Date lastRepayDate = getLastRepayDate(ransomDate, product.getTimeType(), product.getTimeLimit().intValue());		
		
		if(NumberUtils.isDefault(product.getLockTimeLimit())&& 
				NumberUtils.isDefault(product.getSaleTimeLimit()) ){//无开放期和锁定期
			
			if(NumberUtils.isDefault(product.getTimeLimit())){//活期，可随时赎回
				return true;				
			}else{//定期，到期可赎回
				if(localDate.compareTo(lastRepayDate) < 0){//未到期
					return false;
				}
			}
			
		}else if(NumberUtils.isDefault(product.getSaleTimeLimit()) ){//仅一次锁定期
			ransomDate = DateUtils.rollDay(ransomDate, product.getLockTimeLimit());
			if(localDate.before(ransomDate)){//没过锁定期，不可赎回
				return false;
			}
		}else{
			//有产品期限，判断是否到期
			if(!NumberUtils.isDefault(product.getTimeLimit())){
				if(localDate.compareTo(lastRepayDate) >= 0){
					return true;
				}				
			}
			
			//是否在赎回起始日之前
			if(localDate.compareTo(ransomDate) >= 0){//
				return false;
			}
			
			//实际赎回日距离赎回开始日的天数
			int days = (int)DateUtils.getDistanceOfTwoDate(ransomDate, localDate);						
			//周期步长
			int step = product.getLockTimeLimit().intValue() + product.getSaleTimeLimit().intValue();
			//周期数
			int periods = (days%step==0)?(days/step):(days/step+1);
			//本期开始赎回日
			Date periodSaleStart = DateUtils.rollDay(ransomDate, step * periods - product.getSaleTimeLimit().intValue());
			if(localDate.before(periodSaleStart)){
				return false;
			}			
		}
		
		return true;
	}
	
	/**
	 * 取得到期日
	 * @param ransomDate
	 * @param timeType
	 * @param timeLimit
	 * @return
	 */
	public static Date getLastRepayDate(final Date ransomDate, String timeType, int timeLimit){
		Date localDate = ransomDate;
		if(timeLimit == 0){//无固定期限
			return null;
		}
		localDate = DateUtils.rollDay(localDate, 1);
		Date lastRepayDate = null;
		if(LoanEnum.TIME_TYPE_DAY.eq(timeType)){//天标
			lastRepayDate = DateUtils.rollDay(ransomDate, timeLimit);
		}else{
			lastRepayDate = DateUtils.rollMon(ransomDate, timeLimit);
		}
		lastRepayDate = formatToDate(lastRepayDate);
		return lastRepayDate;
	}
	
	/**
	 * 赎回计算起始日
	 * @param interestStyle	计息方式: 1、成立计息 2、 T+N计息
	 * @param interestTime	计息临界时间点
	 * @param interestStartDays	起始计息天数
	 * @param reviewTime	成立审核时间
	 * @param investDate	投资时间
	 * @return
	 */
	public static Date getRansomStartDate(final String interestStyle,	final Date reviewTime, final Date investDate) {
		Date date = DateUtils.getNow();
		if (LoanEnum.INTEREST_STYLE_EV.eq(interestStyle) && reviewTime != null) {//成立审核并且成立审核时间不为空
			date = reviewTime;//DateUtils.rollDay(reviewTime, 1);
		} else if (LoanEnum.INTEREST_STYLE_TN.eq(interestStyle)) {
			date = investDate;//DateUtils.rollDay(investDate, 1);
		}
		return formatToDate(date);
	}
	
	/**
	 * 日期转换，舍去 时分秒
	 * @param date
	 * @return
	 */
	public static Date formatToDate(final Date date){
		Date localDate = date;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			localDate = format.parse(format.format(localDate));
		} catch (ParseException e) {
			LOGGER.warn(e.getMessage(), e);
		}		
		return date;
	}
	
	
	
	public ProjectInvest getInvest() {
		return invest;
	}
	public void setInvest(ProjectInvest invest) {
		this.invest = invest;
	}
	
	
	

}

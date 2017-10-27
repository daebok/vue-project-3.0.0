package com.rd.ifaes.core.statistic.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.service.BaseStatisticService;

/**
 * 
 * 统计基类
 * @version 3.0
 * @author fxl
 * @date 2017年3月23日
 */
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class BaseStatisticServiceImpl<D extends BaseMapper<T>, T extends BaseEntity<T>> extends BaseServiceImpl<D, T> implements BaseStatisticService<T> {

	/**
	 * 格式化显示日期格式
	 * @param date
	 * @param model
	 * @return
	 */
	@Override
	public String fomateDateStr(Date date,StatisticModel model){
		if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
			return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_018);
		}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){
			return DateUtils.getFirstAndLastOfWeek(date, DateUtils.DATEFORMAT_STR_017);			
		}else{ 
			return DateUtils.formatDate(date, DateUtils.DATEFORMAT_STR_017);
		}
	}
	
	/**
	 * 获取环比值
	 * @author fxl
	 * @date 2017年2月27日
	 * @return
	 */
	@Override
	public String getRatioValue(BigDecimal lastToal,BigDecimal total){
		if(lastToal==null || lastToal.compareTo(BigDecimal.ZERO) == 0){
			return StringUtils.EMPTY;
		}
		BigDecimal diff = BigDecimalUtils.mul(BigDecimalUtils.sub(total, lastToal),BigDecimalUtils.valueOf("100"));
		return diff.divide(lastToal, 2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public BigDecimal getDivValue(BigDecimal dividend,BigDecimal divisor){
		if(divisor==null || divisor.compareTo(BigDecimal.ZERO) == 0){
			return BigDecimal.ZERO;
		}
		BigDecimal diff = BigDecimalUtils.mul(dividend,BigDecimalUtils.valueOf("100"));
		return diff.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 日期处理
	 * @author fxl
	 * @date 2017年3月3日
	 * @param model
	 * @param calDate
	 */
	public Date nextCalDate(StatisticModel model,Date calDate){
		if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
			return DateUtils.rollMon(calDate, 1);
		}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){ 
			return DateUtils.rollDay(calDate, 7);
		}else{ 
			return DateUtils.rollDay(calDate, 1);
		}
	}
	
	/**
	 * 日期字符串处理
	 * @author fxl
	 * @date 2017年3月3日
	 * @param model
	 * @param calDate
	 */
	public String nextDateStr(StatisticModel model,Date calDate){
		if(StatisticConstant.DATE_TYPE_MONTH.equals(model.getDateType())){ 
			return DateUtils.formatDate(calDate, DateUtils.DATEFORMAT_STR_013);
		}else if(StatisticConstant.DATE_TYPE_WEEK.equals(model.getDateType())){ 
			return DateUtils.getYearWeek(calDate);
		}else{ 
			return DateUtils.formatDate(calDate, DateUtils.DATEFORMAT_STR_012);
		}
	}
}

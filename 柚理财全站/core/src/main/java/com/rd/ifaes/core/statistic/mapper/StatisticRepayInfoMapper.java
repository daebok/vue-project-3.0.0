package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticRepayInfo;

/**
 * 还款信息Mapper
 * @author fxl
 * @version 3.0
 * @date 2017-3-1
 */
public interface StatisticRepayInfoMapper extends BaseMapper<StatisticRepayInfo> {

	/**
	 * 获取待还总额
	 * @author fxl
	 * @date 2017年3月1日
	 * @return
	 */
	BigDecimal getRepayTotal();
	
	/**
	 * 获取待还列表
	 * @author fxl
	 * @date 2017年3月1日
	 * @return
	 */
	List<BigDecimal> getRepayList();
	
	/**
	 * 获取逾期信息
	 * @author fxl
	 * @date 2017年3月14日
	 * @return
	 */
	StatisticRepayInfo getOverdueInfo();
	
	/**
	 * 根据日期取得还款统计
	 * @author fxl
	 * @date 2017年3月2日
	 * @param recordDate
	 * @return
	 */
	StatisticRepayInfo getRepayInfoByDate(@Param("recordDate")Date recordDate);
}
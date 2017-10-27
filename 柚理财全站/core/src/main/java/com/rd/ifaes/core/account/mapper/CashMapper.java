package com.rd.ifaes.core.account.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.account.domain.Cash;
import com.rd.ifaes.core.account.model.CashModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:CashMapper
 * @author xhf
 * @version 3.0
 * @date 2016-7-2
 */
public interface CashMapper extends BaseMapper<Cash> {
	
	/**
	 * 查询江西银行提现记录 申请中 和 处理中的记录
	 * @author QianPengZhan
	 * @date 2017年3月2日
	 * @param orderNo
	 * @param status
	 * @return
	 */
	List<Cash> findCashList(@Param("statusSet") String[] statusSet);
	
	/**
	 * 根据订单号查询记录
	 * @param orderNo
	 * @return
	 */
	Cash findByOrderNo(String orderNo);
	
	/**
	 * 根据取现订单号查询记录
	 * @param orderNo
	 * @return
	 */
	Cash findByCashNo(String cashNo);
	
	/**
	 * 
	 * @Title: findManageList 
	 * @Description:后台提现记录
	 * @param @param model
	 * @param @return 
	 * @return List<Cash>
	 * @throws
	 */
	List<Cash> findManageList(CashModel model);
	
	/**
	 * 
	 * 提现信息
	 * @author jxx
	 * @date 2016年7月29日
	 * @param id
	 * @return
	 */
	CashModel getCashModel(String uuid);
	
	/**
	 * 根据日期获取提现记录
	 * @param model
	 * @return
	 */
	List<Cash> findByDate(CashModel model);
	
	/**
	 * 更新状态
	 */
	int updateByTppResult(Cash cash);
	
	/**
	 * 更新审核信息
	 */
	int updateAuditMsg(Cash cash);
	
	/**
	 * 获得单日已提现次数(不含提现失败)
	 * @param model
	 * @return
	 */
	int getSingleDaytime(CashModel model);
	
    /**
	 * 获取当日已提现金额(不含提现失败)
	 * @param userId
	 * @return
	 */
	BigDecimal getSingleDayAmount(CashModel model);
	
	/**
	 * 根据订单状态获取提现记录
	 * @param model
	 * @return
	 */
	List<Cash> findByStatus(String status);
	/**
	 * 后台提现记录数
	 * @param model
	 * @return
	 */
	int getManageCount(CashModel model);
	
	/**
	 * 根据查询类型统计数据
	 * @author xhf
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatType(StatisticModel model);
	
	/**
	 * 获取起始日期前一天数值
	 * @author xhf
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	BigDecimal getCountBeforeStartDate(StatisticModel model);
	
	/**
	 * 根据人数统计提现来源
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getCountByUsers(StatisticModel model);
	
	/**
	 * 根据金额统计提现来源
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getCountByAmnt(StatisticModel model);
}
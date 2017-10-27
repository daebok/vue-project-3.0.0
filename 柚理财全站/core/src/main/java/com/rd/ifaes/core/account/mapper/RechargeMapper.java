package com.rd.ifaes.core.account.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.model.RechargeModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:RechargeMapper
 * @author xhf
 * @version 3.0
 * @date 2016-6-30
 */
public interface RechargeMapper extends BaseMapper<Recharge> {

	/**
	 * 
	 * @Title: findManageList 
	 * @Description:后台充值记录
	 * @param @param model
	 * @param @return 
	 * @return List<Recharge>
	 * @throws
	 */
	List<Recharge> findManageList(RechargeModel model);
	
	/**
	 * 根据日期获取充值记录
	 * @param model
	 * @return
	 */
	List<Recharge> findByDate(RechargeModel model);
	
	/**
	 * 更新状态
	 */
	int updateByTppResult(Recharge model);
	
	/**
	 * 根据状态获取充值记录
	 * @param model
	 * @return
	 */
	List<Recharge> findByStatus(String status);
	
	/**
	 * 
	 * 更改超时订单状态
	 * @author xhf
	 * @date 2016年8月20日
	 * @param model
	 */
	void updateOverTimeOrderStatus(Recharge model);
	
	/**
	 * 后台充值记录数
	 * @param model
	 * @return
	 */
	int getManageCount(RechargeModel model);
	
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
	 * 根据人数统计充值订单来源
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getCountByUsers(StatisticModel model);
	
	/**
	 * 根据金额统计充值订单来源
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getCountByAmnt(StatisticModel model);
}
package com.rd.ifaes.core.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 
 * @ClassName: TppMerchantLogMapper 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 下午4:51:42 
 *
 */
public interface TppMerchantLogMapper extends BaseMapper<TppMerchantLog> {
	
	TppMerchantLog findByOrderNo(String orderNo);
	
	/**
	 * 
	 * @Title: findManageList 
	 * @Description:平台资金记录
	 * @param @param model
	 * @param @return 
	 * @return List<TppMerchantLog>
	 * @throws
	 */
	List<TppMerchantLog> findManageList(TppMerchantLogModel model);
	
	/**
	 * 取得商户交易记录数
	 * @param model
	 * @return
	 */
	int getManageCount(TppMerchantLogModel model);
	
	/**
	 * 根据统计日期查询统计信息
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatDate(StatisticModel model);
	
	/**
	 * 获取起始日期前一天数值
	 * @param model
	 * @return
	 */
	BigDecimal getCountBeforeStartDate(StatisticModel model);
	
	/**
	 * 根据状态查询统计信息
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getMoneyByType(StatisticModel model);
}
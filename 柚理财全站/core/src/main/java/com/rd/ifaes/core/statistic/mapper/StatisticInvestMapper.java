package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticInvest;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 投资统计Mapper
 * @version 3.0
 * @author xhf
 * @date 2017年2月22日
 */
public interface StatisticInvestMapper extends BaseMapper<StatisticInvest> {
 
	/**
	 * 统计投资人数
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findInvestByStatType(StatisticModel model);
	
	/**
	 * 查找前一天的投资人数
	 * @param model
	 * @return
	 */
	BigDecimal findInvestDayBefore(StatisticModel model);
	
	/**
	 * 根据产品类型统计投资笔数
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> countByProjectType(StatisticModel model);
	
	/**根据产品类型统计投资金额
	 * 
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> sumByProjectType(StatisticModel model);
	
	/**
	 * 根据年利率统计投资笔数
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> countByProjectApr(StatisticModel model);
	
	/**
	 * 根据年利率统计投资金额
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> sumByProjectApr(StatisticModel model);
	
	/**
	 * 根据借款期限统计投资笔数
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> countByProjectTimeLimit(StatisticModel model);
	
	/**
	 * 根据借款期限统计投资金额
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> sumByProjectTimeLimit(StatisticModel model);
	
	/**
	 * 根据投资金额统计投资笔数
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> countByProjectAmnt(StatisticModel model);
	
	/**
	 * 根据地区统计投资信息
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getInvestArea(StatisticModel model);
	
	/**
	 * 统计时间段内的投资总数 
	 * @param model
	 * @return
	 */
	BigDecimal getTotalCountByDate(StatisticModel model);
	
	/**
	 * 统计活跃用户列表 投资
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	public List<StatisticResultModel> getUserActiveByInvest(StatisticModel model);

	/**
	 * 用户活跃度地区 投资
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	public List<Map<String,Object>> getUserActiveAreaByInvest(StatisticModel model);
	
	/**
	 * 用户活跃度总计列表投资
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	public Map<String, Object> getUserActiveCountByInvest(@Param("channelArr")String[] channelArr,@Param("lastSevenDay") Date lastSevenDay,@Param("lastThirtyDay") Date lastThirtyDay
			, @Param("lastNinetyDay") Date lastNinetyDay,@Param("lastHalfYear") Date lastHalfYear);
	
	/**
	 * 根据日期查询实付金额
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findRealAmntByDate(StatisticModel model);
	
	/**
	 * 计算所有可用余额
	 * @param model
	 * @return
	 */
	BigDecimal getTotalRealAmnt(StatisticModel model);
	
	/**
	 * 获得实付金额map
	 * @param model
	 * @return
	 */
	Map<String,BigDecimal> getRealAmntMap(StatisticModel model);
}
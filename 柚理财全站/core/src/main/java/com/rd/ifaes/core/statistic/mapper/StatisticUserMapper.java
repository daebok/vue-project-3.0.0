package com.rd.ifaes.core.statistic.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * Dao Interface:StatisticUserMapper
 * @author fxl
 * @version 3.0
 * @date 2017-3-6
 */
public interface StatisticUserMapper extends BaseMapper<StatisticUser> {

	
	/**
	 * 获取起始日期前一天数值
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	BigDecimal getLastTotal(StatisticModel model);
	
	/**
	 * 获取起始日期前数值
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	BigDecimal sumLastUserNum(StatisticModel model);
	
	/**
	 * 统计用户地区
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	List<Map<String,Object>> getUserArea(StatisticModel model);
	
	/**
	 * 统计用户数量
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getStatisticUser(StatisticModel model);
	
	/**
	 * 统计活跃用户列表
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> getUserActiveByLogin(StatisticModel model);

	/**
	 * 用户活跃度地区 登录
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	List<Map<String,Object>> getUserActiveAreaByLogin(StatisticModel model);
	
	/**
	 * 用户活跃度总计列表 登录
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	Map<String, Object> getUserActiveCountByLogin(@Param("channelArr")String[] channelArr,@Param("lastSevenDay") Date lastSevenDay,@Param("lastThirtyDay") Date lastThirtyDay
			, @Param("lastNinetyDay") Date lastNinetyDay,@Param("lastHalfYear") Date lastHalfYear);
	
	/**
	 * 用户总计列表
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	Map<String, Object> getUserCountList(@Param("realNameStatus") String realNameStatus,@Param("lastMonth") Date lastMonth
			, @Param("lastThreeMonth") Date lastThreeMonth,@Param("lastSixMonth") Date lastSixMonth);
	
	/**
	 * 更新用户类型
	 * @author fxl
	 * @date 2017年3月7日
	 * @param userId
	 * @param userType
	 */
	void updateUserType(@Param("userId")String userId, @Param("userType")String userType);
	
	/**
	 * 不同性别用户数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUserSex(StatisticModel model);
	
	/**
	 * 统计不同学历的人数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUsereducation(StatisticModel model);
	/**
	 * 统计婚姻状态的人数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUserMarital(StatisticModel model);
	/**
	 * 统计不同年收入人数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUserIncome(StatisticModel model);
	/**
	 * 不同年龄用户数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUserAge(StatisticModel model);

	/**
	 * 不同用户类型数
	 * @author fxl
	 * @date 2017年2月23日
	 * @param model
	 * @return
	 */
	LinkedHashMap<String,BigDecimal> countUserNatrue(StatisticModel model);
}
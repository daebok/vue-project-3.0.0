package com.rd.ifaes.core.statistic.service;

import com.rd.ifaes.core.statistic.domain.StatisticUser;
import com.rd.ifaes.core.statistic.model.StatisticModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:StatisticUserService
 * @author fxl
 * @version 3.0
 * @date 2017-3-6
 */
public interface StatisticUserService extends BaseService<StatisticUser>{

	
	/**
	 * 用户统计数据接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	Map<String,Object> getUserStatistic(StatisticModel model);
	
	/**
	 * 用户地区
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	Map<String, Object> getUserArea(StatisticModel model);
	
	/**
	 * 用户总数列表
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	Map<String,Object> getUserCountList(StatisticModel model);
	
	/**
	 * 用户活跃度列表接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	Map<String,Object> getUserActiveList(StatisticModel model);
	
	/**
	 * 用户活跃度地区接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	Map<String,Object> getUserActiveArea(StatisticModel model);

	/**
	 * 用户活跃度总计接口
	 * @author fxl
	 * @date 2017年3月6日
	 * @param model
	 * @return
	 */
	Map<String,Object> getUserActiveCountList(StatisticModel model);


	/**
	 * 获取用户画像
	 * @author fxl
	 * @date 2017年3月7日
	 * @param model
	 * @return
	 */
	List<Map<String, BigDecimal>> getUserPortrait(StatisticModel model);
	
	/**
	 * 更新用户类型
	 * @author fxl
	 * @date 2017年3月7日
	 * @param userId
	 * @param userType
	 */
	void updateUserType(String userId,String userType);
	
}
package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:UserRatecouponService
 * @author 
 * @version 3.0
 * @date 2016-7-27
 */
public interface UserRateCouponService extends BaseService<UserRateCoupon>{
 
	/**
	 *  修改加息券状态
	 * @author  FangJun
	 * @param uuid 加息券ID
	 * @return  成功 1 ，失败 0
	 */
	boolean  updateStatus(String uuid,String investId, String status,String preStatus);
	
	 /**
	  *  查询指定投资使用的加息劵
	  * @author  FangJun
	  * @date 2016年8月20日
	  * @param investId  投资记录ID
	  * @return  加息劵信息
	  */
	 UserRateCoupon   findByInvestId(String investId);
	 
	 /**
	 * 修改加息卷使用tenderId
	 * @author fxl
	 * @date 2016年8月24日
	 * @param oldInvestId
	 * @param newInvestId
	 */
	void updateRateCouponTenderId(String oldInvestId,String newInvestId);
	/**
	 * 加息券过期处理
	 * @author wyw
	 * @date 2016-8-25
	 */
	int expiredHandle();
	
	/**
	 * 获取用户可用的加息券 
	 * @author ywt
	 * @date 2016-10-17
	 * @param user
	 * @param projectInvest
	 * @return
	 */
	List<UserRateCoupon> findAvailableRateCoupon(UserRateCoupon userRateCoupon,Project project, BigDecimal tenderMoney);
	
	/**
	 * 获取自定义活动的红包（每个用户只能获取一次）
	 * @param user
	 * @param RateCouponRuleId
	 * @return
	 */
	void gainActivityRateCoupon(User user,String RateCouponRuleId);
	
	/**
	 * 前台获取加息券列表
	 * @author fxl
	 * @date 2016年11月8日
	 * @param entity
	 * @return
	 */
	Page<UserRateCoupon> findWebPage(UserRateCoupon entity);
	
	/**
	 * 作废加息券
	 * @author ywt
	 * @date 2016年11月16日
	 * @param RateCouponId
	 * @return
	 */
	void cancellationRateConpon(String RateCouponId);

	/**
	 * 根据统计时间查询记录
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatDate(StatisticModel model);

	/**
	 * 根据统计类型查询记录
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatType(StatisticModel model);
	
	/**
	 * 根据统计状态查询记录
	 * @param model
	 * @return
	 */
	Map<String, Object> findByStatStatus(StatisticModel model);

	/**
	 * 根据tenderId计数
	 * @param tenderId
	 * @return
	 */
	int getCountByTenderId(String tenderId);
}
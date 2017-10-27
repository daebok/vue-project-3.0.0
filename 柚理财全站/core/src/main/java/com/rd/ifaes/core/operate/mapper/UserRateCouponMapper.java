package com.rd.ifaes.core.operate.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;

/**
 * 
 *  UserRateCoupon Mapper
 * @version 3.0
 * @author wyw
 * @date 2016-7-29
 */
public interface UserRateCouponMapper extends BaseMapper<UserRateCoupon> {
	
	/**
	 * 批量更新状态
	 * @author QianPengZhan
	 * @date 2017年1月10日
	 * @param list
	 */
	void updateBatchStatus(List<UserRateCoupon> list);
	
	/**
	 * 查询过期加息券
	 * @author QianPengZhan
	 * @date 2017年1月10日
	 * @param preStatus
	 * @return
	 */
	List<String> findExpireList( @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("preStatus") String preStatus);
	
	
	/**
	 *  修改加息券状态
	 * @author  FangJun
	 * @param uuid 加息券ID
	 * @param investId 投资记录D
	 * @param status  加息券当前状态
	 * @param preStatus 加息券前一状态
	 * @return   修改记录条数
	 */
	int  updateStatus(@Param("uuid")String uuid,@Param("investId")String investId,
			@Param("useTime") Date useTime,@Param("status")String status,@Param("preStatus")String preStatus);
	
	
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
	void updateRateCouponTenderId(@Param("oldInvestId")String oldInvestId,@Param("newInvestId")String newInvestId);
	
	/**
	 * 
	 * 获取可使用的红包
	 * @author ywt
	 * @date 2016-10-12
	 * @param status
	 * @param preStatus
	 * @return
	 */
	List<UserRateCoupon> viableUserRateCoupon (UserRateCoupon userRateCoupon);
	
	/**
	 * 根据统计日期获取记录
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatDate(StatisticModel model);
	/**
	 * 根据统计类型获取记录
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatType(StatisticModel model);
	/**
	 * 根据统计状态获取记录
	 * @param model
	 * @return
	 */
	List<StatisticResultModel> findByStatStatus(StatisticModel model);
	
	/**
	 * 根据tenderId计数
	 * @param tenderId
	 * @return
	 */
	int getCountByTenderId(String tenderId);
}
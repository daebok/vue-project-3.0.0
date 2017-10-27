package com.rd.ifaes.core.operate.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.UserGiftGrant;

/**
 * 用户礼包发放
 * @author fxl
 * @version 3.0
 * @date 2017-5-10
 */
public interface UserGiftGrantMapper extends BaseMapper<UserGiftGrant> {

	/**
	 * 获取可领取礼包
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	UserGiftGrant getAvailableUserGift(@Param("userId") String userId,@Param("giftType") String giftType);
	
	/**
	 * 更新礼包状态
	 * @author fxl
	 * @date 2017年5月10日
	 * @param uuid
	 * @param status
	 * @param preStatus
	 * @return
	 */
	int updateGiftStatus(@Param("uuid") String uuid,@Param("status") String status,@Param("preStatus") String preStatus);
	
	/**
	 * 专享礼包过期处理
	 * @author fxl
	 * @date 2017年5月10日
	 * @param lastMonth
	 * @param month
	 */
	void overdueExclusiveGift(@Param("lastMonth")Date lastMonth,@Param("month")Date month);
	
	/**
	 * 生日礼包过期处理
	 * @author fxl
	 * @date 2017年5月10日
	 */
	void overdueBirthdayGift();
	
}
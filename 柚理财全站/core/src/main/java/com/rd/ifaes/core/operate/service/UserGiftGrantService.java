package com.rd.ifaes.core.operate.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.operate.domain.UserGiftGrant;
import com.rd.ifaes.core.user.domain.User;

/**
 * 用户礼包发放
 * @author fxl
 * @version 3.0
 * @date 2017-5-10
 */
public interface UserGiftGrantService extends BaseService<UserGiftGrant>{

	/**
	 * 发放礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	void grantGift(UserGift userGift,String userId,String giftLevel);
	
	/**
	 * 更新未发放生日礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	void reFreshBirthGift(UserGiftGrant userGiftGrant,UserGift userGift,User user,String vipLevel);
	
	/**
	 * 获取可领取礼包
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	UserGiftGrant getAvailableUserGift(User user,String giftType);
	
	/**
	 * 更新礼包状态
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	boolean updateGiftStatus(String uuid,String status,String preStatus);
	
	/**
	 * 处理过期专享礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	void doOverDueExclusiveGift();
	
	/**
	 * 每月初处理生日礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	void doOverDueBirthGift();
	
}
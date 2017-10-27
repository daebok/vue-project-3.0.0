package com.rd.ifaes.core.operate.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.operate.domain.UserGiftGrant;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.mapper.UserGiftGrantMapper;
import com.rd.ifaes.core.operate.service.UserGiftGrantService;
import com.rd.ifaes.core.operate.service.UserGiftService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 用户礼包发放
 * @author fxl
 * @version 3.0
 * @date 2017-5-10
 */
@Service("userGiftGrantService") 
public class UserGiftGrantServiceImpl  extends BaseServiceImpl<UserGiftGrantMapper, UserGiftGrant> implements UserGiftGrantService{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGiftGrantServiceImpl.class);
	
	@Resource
	private UserGiftService userGiftService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private UserVipLevelService userVipLevelService;
	
	/**
	 * 发放礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	@Override
	public void grantGift(UserGift userGift,String userId,String giftLevel) {
		if(userGift==null){
			LOGGER.info("礼包未找到,用户ID{}",userId);
			return;
		}
		try {
			UserGiftGrant userGiftGrant = new UserGiftGrant(userGift,userId,giftLevel);
			userGiftGrant.preInsert();
			dao.insert(userGiftGrant);
			LOGGER.info("发放{}礼包,用户ID{}",userGift.getGiftName(),userId);
		} catch (Exception e) {
			LOGGER.error("用户{}礼包发放失败");
		}
	}

	/**
	 * 更新未发放生日礼包
	 * @author fxl
	 * @date 2017年5月11日
	 */
	@Override
	public void reFreshBirthGift(UserGiftGrant userGiftGrant,UserGift userGift,User user,String vipLevel) {
		// 将原生日礼包置为作废
		if(updateGiftStatus(userGiftGrant.getUuid(), UserGiftGrant.GIFT_STATUS_CANCLE, UserGiftGrant.GIFT_STATUS_NO_RECEIVE)){
			// 发放新礼包
			grantGift(userGift, user.getUuid(),vipLevel);
		}
	}
	
	/**
	 * 获取可领取礼包
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	@Override
	public UserGiftGrant getAvailableUserGift(User user,String giftType) {
		return dao.getAvailableUserGift(user.getUuid(),giftType);
	}
	
	/**
	 * 更新礼包状态
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	@Override
	public boolean updateGiftStatus(String uuid, String status, String preStatus) {
		int count = dao.updateGiftStatus(uuid,status,preStatus);
		return count > 0?true:false;
	}
	
	
	/**
	 * 处理过期专享礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	@Override
	public void doOverDueExclusiveGift() {
		// 前一个月
		Date month = DateUtils.rollMon(DateUtils.getTodayEnd(), Constant.INT_ONE_NEGA);
		Date lastMonth = DateUtils.rollMon(month, Constant.INT_ONE_NEGA);
		dao.overdueExclusiveGift(lastMonth,month);
	}
	
	/**
	 * 每月初处理生日礼包
	 * @author fxl
	 * @date 2017年5月10日
	 */
	@Override
	public void doOverDueBirthGift() {
		// 将上个月未使用礼包置为失效
		dao.overdueBirthdayGift();
		// 给本月生日用户发放生日礼包
		List<UserVip> userVipList = userVipService.findBirthVipUser();
		if(CollectionUtils.isNotEmpty(userVipList)){
			for (UserVip userVip : userVipList) {
				// 礼包发放
				UserVipLevel level = userVipLevelService.getByVipLevel(userVip.getVipLevel());
				final UserGift userGift = userGiftService.get(level.getBirthdayGiftId());
				grantGift(userGift, userVip.getUserId(),level.getVipLevel());
			}
		}
	}
	
}
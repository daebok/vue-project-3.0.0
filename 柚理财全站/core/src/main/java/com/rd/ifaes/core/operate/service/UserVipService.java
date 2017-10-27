package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.UserGiftGrant;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 * 用户vip
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-8-2
 */
public interface UserVipService extends BaseService<UserVip> {
	/**
	 * 根据投资金额 加vip成长值
	 * @author wyw
	 * @date 2016-8-2
	 * @param user 用户
	 * @param tenderMoney 投资金额
	 */
	void addVipGrowthByTender(User user, BigDecimal tenderMoney, String remark);

	/**
	 * 加vip成长值
	 * @author wyw
	 * @date 2016-8-3
	 * @param user
	 * @param value
	 * @param remark
	 */
	void addVipGrowthByValue(User user, BigDecimal value, String remark);

	/**
	 * 根据用户id获取userVip
	 * @author wyw
	 * @date 2016-8-2
	 * @param userId
	 * @return
	 */
	UserVip getUserVip(String userId);

	/**
	 * 发放vip礼包
	 * @author wyw
	 * @date 2016-8-29
	 * @param user
	 */
	Map<String, Object> grantVipGift(User user);

	/**
	 * 判断用户是vip礼包是否可以领取
	 * @author wyw
	 * @date 2016-8-30
	 * @param userVip
	 * @param giftType
	 * @return
	 */
	void checkAvailableGift(UserVip userVip, final UserGiftGrant userGiftGrant);
	
	/**
	 * 查询本月生日的用户
	 * @author fxl
	 * @date 2017年5月10日
	 * @return
	 */
	List<UserVip> findBirthVipUser();
	
	/**
	 * 判断是否显示领取vip礼包
	 * @author ZhangBiao
	 * @date 2016年11月9日
	 * @param userVip
	 * @return
	 */
	boolean checkShowVipGift(User user);

	/**
	 * 扣除VIP成长值
	 * @author ZhangBiao
	 * @date 2016年11月23日
	 */
	void deductionVipGrowthValue();
}
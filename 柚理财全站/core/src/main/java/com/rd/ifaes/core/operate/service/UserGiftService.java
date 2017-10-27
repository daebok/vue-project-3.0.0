package com.rd.ifaes.core.operate.service;

import java.util.Map;

import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:UserGiftService
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
public interface UserGiftService extends BaseService<UserGift>{
	
	/**
	 * 发放生日礼包
	 * @author wyw
	 * @param userGift
	 * @param user
	 * @return
	 */
	Map<String, Object> grantBirthGift(UserGift userGift, User user);

	/**
	 * 增加礼包
	 * @author ywt
	 * @param userGift
	 * @return
	 */
	void addUserGift(UserGift userGift);

	/**
	 * 后台列表展示
	 * @author ZhangBiao
	 * @date 2016年10月28日
	 * @param userGift
	 * @return
	 */
	Object findManageList(UserGift userGift);
}
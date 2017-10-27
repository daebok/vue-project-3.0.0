package com.rd.ifaes.core.score.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.score.domain.ReceivingInfo;

public interface ReceivingInfoService extends BaseService<ReceivingInfo>{
	
	
	/**
	 * 设置用户的默认收货地址
	 * @param userId
	 * @param uuid
	 */
	void setDefaultReceivingInfo(String uuid,String userId);
	
	/**
	 * 清除用户的默认收货地址
	 * @param userId
	 * @param uuid
	 */
	void clearDefaultReceivingInfoSetting(String userId);
	
	/**
	 * 删除用户收货信息
	 * @param userId
	 * @param uuid
	 */
	void deleteReceivingInfo(String uuid,String userId);
	
	/**
	 * 统计用户已设置的收货地址数量
	 * @param userId
	 */
	int countUserReceivingInfoNum(String userId);

}

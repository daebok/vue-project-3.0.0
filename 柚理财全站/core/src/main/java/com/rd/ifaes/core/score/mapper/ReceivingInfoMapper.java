package com.rd.ifaes.core.score.mapper;


import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.score.domain.ReceivingInfo;

/**
 * Mapper Interface:商品管理Mapper
 * @author ywt
 * @version 3.0
 * @date 2017-03-03
 */
public interface ReceivingInfoMapper extends BaseMapper<ReceivingInfo>{
	
	/**
	 * 设置用户的默认收货地址
	 * @param userId
	 * @param uuid
	 */
	void setDefaultReceivingInfo(String uuid);
	
	/**
	 * 清除用户的默认收货地址
	 * @param userId
	 * @param uuid
	 */
	void clearDefaultReceivingInfoSetting(String userId);
	
	/**
	 * 统计用户已设置的收货地址数量
	 * @param userId
	 */
	int countUserReceivingInfoNum(String userId);
}

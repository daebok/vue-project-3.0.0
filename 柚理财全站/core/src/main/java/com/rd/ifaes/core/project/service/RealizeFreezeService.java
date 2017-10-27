package com.rd.ifaes.core.project.service;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.RealizeFreeze;

/**
 * 变现冻结信息
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public interface RealizeFreezeService extends BaseService<RealizeFreeze>{

	/**
	 * 统计冻结金额总和
	 * @author fxl
	 * @date 2016年11月22日
	 * @param userId
	 * @param collectionId
	 * @param freezeType
	 * @return
	 */
	BigDecimal getSumFreezeMoney(String userId,String collectionId,String freezeType);
	
	/**
	 * 根据待收ID查询待冻结列表
	 * @author fxl
	 * @date 2016年12月5日
	 * @param collectionId
	 * @param freezeType
	 * @return
	 */
	List<RealizeFreeze> getFreezeListByCollection(String collectionId);
	/**
	 * 更新变现冻结信息
	 * @author fxl
	 * @date 2016年11月23日
	 * @param collectionId
	 * @param freezeType
	 * @param freezeNo
	 * @param orderNo
	 */
	void updateFreeze(String collectionId,String realizeId,String freezeType,String freezeNo,String orderNo);
	
	/**
	 * 撤销冻结
	 * @author fxl
	 * @date 2016年11月23日
	 * @param realizeId
	 */
	void cancelFreeze(String realizeId);
	
	/**
	 * 根据变现ID和状态获取冻结列表
	 * @author fxl
	 * @date 2016年11月23日
	 * @param realizeId
	 * @param status
	 * @return
	 */
	List<RealizeFreeze> getFreezedListByRealizeId(String realizeId,String status);
	
}
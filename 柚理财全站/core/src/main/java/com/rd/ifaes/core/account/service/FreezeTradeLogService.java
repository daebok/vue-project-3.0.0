package com.rd.ifaes.core.account.service;

import java.util.List;

import com.rd.ifaes.core.account.domain.FreezeTradeLog;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:FreezeTradeLogService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public interface FreezeTradeLogService extends BaseService<FreezeTradeLog>{

	/**
	 * 根据订单号和冻结类型查询记录
	 * @param freezeType
	 * @param orderNo
	 * @return
	 */
	FreezeTradeLog findByOrderNo(String freezeType, String orderNo);
	
	/**
	 * 根据项目ID和用户ID查询
	 * @author fxl
	 * @date 2016年8月20日
	 * @param projectId
	 * @param userId
	 * @return
	 */
	List<FreezeTradeLog> findByInvestNoAndUser(String investId,String userId);
}
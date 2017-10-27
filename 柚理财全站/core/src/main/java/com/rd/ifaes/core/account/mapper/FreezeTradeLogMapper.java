package com.rd.ifaes.core.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.account.domain.FreezeTradeLog;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:FreezeTradeLogMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
public interface FreezeTradeLogMapper extends BaseMapper<FreezeTradeLog> {

	/**
	 * 根据订单号和冻结类型查询记录
	 * @param freezeType
	 * @param orderNo
	 * @return
	 */
	FreezeTradeLog findByOrderNo(FreezeTradeLog freezeTradeLog);
	
	/**
	 * 根据项目ID和用户ID查询
	 * @author fxl
	 * @date 2016年8月20日
	 * @param projectId
	 * @param userId
	 * @return
	 */
	List<FreezeTradeLog> findByInvestNoAndUser(@Param("investId") String investId,@Param("userId") String userId);
}
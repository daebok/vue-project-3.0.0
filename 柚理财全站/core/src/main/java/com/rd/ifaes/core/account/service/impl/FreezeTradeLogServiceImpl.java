package com.rd.ifaes.core.account.service.impl;

import java.util.List;

import com.rd.ifaes.core.account.service.FreezeTradeLogService;
import com.rd.ifaes.core.account.mapper.FreezeTradeLogMapper;
import com.rd.ifaes.core.account.domain.FreezeTradeLog;
import com.rd.ifaes.core.base.service.*;

import org.springframework.stereotype.Service;

/**
 * ServiceImpl:FreezeTradeLogServiceImpl
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
@Service("freezeTradeLogService") 
public class FreezeTradeLogServiceImpl  extends BaseServiceImpl<FreezeTradeLogMapper, FreezeTradeLog> implements FreezeTradeLogService{
	
    //@Resource
    //private FreezeTradeLogMapper freezeTradeLogMapper;
	
	public FreezeTradeLog findByOrderNo(String freezeType, String orderNo){
		FreezeTradeLog freezeTradeLog = new FreezeTradeLog();
		freezeTradeLog.setFreezeType(freezeType);
		freezeTradeLog.setOrderNo(orderNo);
		return dao.findByOrderNo(freezeTradeLog);
	}

	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.account.service.FreezeTradeLogService#findByInvestNoAndUser(java.lang.String, java.lang.String)
	 */
	@Override
	public List<FreezeTradeLog> findByInvestNoAndUser(String investId, String userId) {
		return dao.findByInvestNoAndUser(investId,userId);
	}

}
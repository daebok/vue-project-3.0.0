package com.rd.ifaes.core.account.service;
import java.math.BigDecimal;

import com.rd.ifaes.core.account.domain.CashFeeMarkLog;


/**
 * CashFeeMarkLogService
 * @author zhangxj
 * @version 3.0
 * @date 2017-09-18
 */
public interface CashFeeMarkLogService {
	
	//根据资金交易类型获取提示语
	String getRemarkByAccountCode(CashFeeMarkLog log);
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	void save(CashFeeMarkLog entity);
	/**
	 * @param userId 用户id
	 * @param accountType 资金交易类型
	 * @param bigDecimal 交易金额
	 * @param investid 选填，投资ID
	 */
	void preSave(String userId, String accountType, BigDecimal money, String cashId, String investId);
	/**
	 * 根据id获取model
	 */
	CashFeeMarkLog get(String uuid);
	
	CashFeeMarkLog findOne(CashFeeMarkLog cashFeeMarkLog);

}

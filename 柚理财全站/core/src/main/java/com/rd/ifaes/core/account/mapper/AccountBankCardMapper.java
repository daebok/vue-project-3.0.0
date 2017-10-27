/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * 银行卡接口类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月1日
 */
public interface AccountBankCardMapper extends BaseMapper<AccountBankCard>{
	
	/**
	 * 获取个人的银行卡
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @param userId
	 * @return
	 */
	List<AccountBankCard> getCardByUserId(@Param("userId") String userId);
	
	/**
	 * 根据用户ID，卡号ID，状态来查询单个用户的单个卡号
	 * @author QianPengZhan
	 * @date 2017年3月20日
	 * @param userId
	 * @param cardId
	 * @param status
	 * @return
	 */
	AccountBankCard getCardByUserIdAndCardId(@Param("userId")String userId,@Param("cardId")String cardId,@Param("status")String status);
}

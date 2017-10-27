/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.account.service;

import java.util.List;

import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindCardWebModel;

/**
 * 银行卡接口业务员类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月1日
 */
public interface AccountBankCardService  extends BaseService<AccountBankCard>{
	
	
	/**
	 * 根据用户ID，卡号ID，状态来查询单个用户的单个卡号
	 * @author QianPengZhan
	 * @date 2017年3月20日
	 * @param userId
	 * @param cardId
	 * @param status
	 * @return
	 */
	AccountBankCard getCardByUserIdAndCardId(String userId,String cardId,String status);
	
	/**
	 * 获取银行卡列表
	 * @author QianPengZhan
	 * @date 2017年3月2日
	 * @return
	 */
	List<AccountBankCard> findList();
	
	/**
	 * 获取银行卡列表
	 * @author QianPengZhan
	 * @date 2017年3月2日
	 * @return
	 */
	List<AccountBankCard> findList(String userId);
	/**
	 * 添加银行卡
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @param card
	 */
	void insertBankCard(AccountBankCard card);
	
	
	/**
	 * 获取个人的银行卡
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @param userId
	 * @return
	 */
	List<AccountBankCard> getCardByUserId(String userId);
	
	/**
	 * 修改银行卡异步回调业务处理
	 * @author QianPengZhan
	 * @date 2017年3月2日
	 * @param cardModel
	 */
	void bindCardWebHandle(CbhbBindCardWebModel cardModel);
}

package com.rd.account.service;


import java.math.BigDecimal;
import java.util.Map;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.model.AccountRegisterModel;
import com.rd.ifaes.common.orm.Page;

/**
 * Service Interface:AccountService
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
public interface AccountService{
	
	Account get(String uuid);
	/**
	 * 开户
	 * @param userId		用户ID(M)
	 * @param accountCode	账户编号(M)
	 * @param accountType	细分账户类别(O)
	 * @param parentCode	上级账户编码(O)
	 * @return
	 */
	void register(AccountRegisterModel model);

	
	
	/*************************查询*************************/
	/**
	 * 取得账户信息
	 * @param userId		用户ID(M)
	 * @param accountCode	账户编号(M)
	 * @param accountType	细分账户类别(O)
	 * @return
	 */
	Account findAccount(AccountQueryModel model);
	
	/**
	 * 查询分页数据
	 * @param entity
	 * @return
	 */
	Page<Account> findPage(Account entity);
	
	/**
	 * 根据userId和accountCode查询对象
	 * @param userId
	 * @param accountCode
	 * @return
	 */
	Account getByUserId(AccountQueryModel model);

	/**
	 * 批量修改用户账户、保存资金记录 
	 * @author  FangJun
	 * @date 2016年8月1日
	 * @param logList 资金记录列表
	 */
	void saveBatch(AccountBatchModel model);//List<AccountModel> accountList,List<AccountLog> logList 
	/**
	 * 修改账户资金、保存资金记录
	 * @author  FangJun
	 * @date 2016年8月9日
	 */
	void saveAccountChange(AccountModel model,AccountLog log);
	
	/**
	 * 获取可用余额
	 * @author xhf
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	BigDecimal getTotalUseMoney();
	
	/**
	 * 获得昨日的可用余额
	 * @return
	 */
	BigDecimal sumUseByDate(String dateStart, String dateEnd) ;
	
	/**
	 * 根据userId和accountCode查询账户可用/不可用金额
	 * @param userId
	 * @param accountCode
	 * @return
	 */
	Account getMoneyByUserId(AccountQueryModel aqmodel);

	/**
	 * 渤海银行 -- 企业 担保账户 只保存资金记录
	 * @author ZhangBiao
	 * @date 2017年3月21日
	 * @param accountLogList
	 */
	void saveLogs(AccountBatchModel model);
	
	/**
	 * 获得冻结总额
	 * @return
	 */
	BigDecimal getNoUseTotalMoney();
	
	/**
	 * 根据日期获取冻结总额
	 * @return
	 */
	BigDecimal sumNoUseMoneyByDate(String startDate, String endDate) ;
	
	void updateOneByModel(final Account account);
	
	//企业类型账号（个人账号之外的用户同步第三方资金信息）
	Map<String, Object> updateCompanyUserMoney(final Account account);
}
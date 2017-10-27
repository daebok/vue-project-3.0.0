package com.rd.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:AccountMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
public interface AccountMapper extends BaseMapper<Account> {

	/**
	 * 
	 * @Title: findManageList 
	 * @Description:后台查询用户资金
	 * @param @param model
	 * @param @return 
	 * @return List<Account>
	 * @throws
	 */
	List<Account> findManageList(Account model);
	
	/**
	 *根据用户ID、账户编号，更新用户可用余额、不可用余额
	 *    参数限制：
	 *             1）可用余额、不可用余额数值不可以同时都为零、正数、负数 
	 * @author  FangJun
	 * @date 2016年8月25日
	 * @param model 目前传递参数：可用余额、不可用余额、用户ID、账户编号
	 * @return 修改记录条数
	 */
	int updateAccount(Account model);
	
	
	String getByUserId(Account model);
	
	/**
	 * 取得账户金额（可用/不可用)
	 * @param model
	 * @return
	 */
	Account getMoneyByUserId(AccountModel model);
	
	/**
	 * 获得可用余额
	 * @author xhf
	 * @date 2017年2月27日
	 * @param model
	 * @return
	 */
	BigDecimal getTotalUseMoney();
	
	/**
	 * 获得冻结总额
	 * @return
	 */
	BigDecimal getNoUseTotalMoney();
	
	/**
	 * 根据日期获取冻结总额
	 * @return
	 */
	BigDecimal sumNoUseMoneyByDate(@Param("startDate") String startDate, @Param("endDate") String endDate) ;
	
	/**
	 * 获得昨日的可用余额
	 * @return
	 */
	BigDecimal sumUseByDate(@Param("startDate") String startDate, @Param("endDate") String endDate) ;

	void updateOneByModel(Account account);

	void updateCompanyUserMoney(Account account);
}
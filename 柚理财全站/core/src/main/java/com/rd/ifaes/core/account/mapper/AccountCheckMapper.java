package com.rd.ifaes.core.account.mapper;

import java.util.List;

import com.rd.ifaes.core.account.domain.AccountCheck;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * 
 * @ClassName: AccountCheckMapper 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 上午10:21:27 
 *
 */
public interface AccountCheckMapper extends BaseMapper<AccountCheck> {
	
	/**
	 * 删除所有资金不平衡资金信息 
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 */
	void deleteAccountCheck();
	
	/**
	 * 删除所有资金不平衡资金信息 
	 * 		为使用主键重新计数，使用truncate进行删除数据
	 */
	void truncateAccountCheck();
	

	/**
	 * 
	 * @Title: findManageList 
	 * @Description:后台余额对账
	 * @param @param model
	 * @param @return 
	 * @return List<AccountCheck>
	 * @throws
	 */
	List<AccountCheck> findManageList(AccountCheck model);
}
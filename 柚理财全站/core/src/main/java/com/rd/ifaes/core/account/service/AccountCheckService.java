package com.rd.ifaes.core.account.service;

import java.util.List;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.account.domain.AccountCheck;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 * @ClassName: AccountCheckService 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 上午10:22:25 
 *
 */
public interface AccountCheckService extends BaseService<AccountCheck>{
	
	/**
	 * 删除数据
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 */
	void deleteAccountCheck();
	
	void truncateAccountCheck();
	
	List<User> getOpenAccountUserList(User model);
	
	/**
	 * 
	 * @Title: accountCheck 
	 * @Description: 资金对账
	 * @param @param userList 
	 * @return void
	 * @throws
	 */
	void accountCheck(List<User> userList);
	
	/**
	 * 
	 * @Title: findManagePage 
	 * @Description: 后台余额对账记录
	 * @param @param entity
	 * @param @return 
	 * @return Page<Recharge>
	 * @throws
	 */
	Page<AccountCheck> findManagePage(AccountCheck entity);
	
	Object doAutoQueryErrorBalance();
}
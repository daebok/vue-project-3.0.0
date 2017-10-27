package com.rd.ifaes.core.credit.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.credit.domain.UserCredit;
import com.rd.ifaes.core.credit.domain.UserCreditLine;

public interface UserCreditService extends BaseService<UserCredit>{
	/**
	 * 额度申请
	 * @author wzy
	 * @date 2017年9月8日
	 */
	void applyCredit(String userId, String account, String content) ;
	
	/**
	 * 成立审核
	 * @param model
	 * @return
	 */
	void establishVerify(UserCredit model);
	
	
}

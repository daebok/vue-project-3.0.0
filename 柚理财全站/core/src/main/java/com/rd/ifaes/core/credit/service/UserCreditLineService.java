package com.rd.ifaes.core.credit.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.credit.domain.UserCreditLine;

public interface UserCreditLineService extends BaseService<UserCreditLine>{
	/**
	 * 根据userId获取数据
	 * @param userId
	 * @return
	 */
	UserCreditLine getByUserId(String userId) ;
	/**
	 * 检查用户信用额度是否足够,并且扣除
	 * @param userId 需要扣除的用户
	 * @param account 需要扣除的信用额度
	 * @return true表示信用额度足够，并且扣除成功  false表示信用额度不足
	 */
	boolean subCreditByAccount(final String userId, final BigDecimal account, final String projectName);
	/**
	 * 添加信用额度
	 * @param userId 需要添加的用户
	 * @param account 需要添加的信用额度
	 */
	void addCreditByAccount(final String userId, final BigDecimal account, final String projectName);
}

package com.rd.ifaes.core.credit.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.credit.domain.UserCreditLine;

public interface UserCreditLineMapper extends BaseMapper<UserCreditLine>{
	/**
	 * 根据userId获取数据
	 * @param userId
	 * @return
	 */
	UserCreditLine getByUserId(String userId) ;
}

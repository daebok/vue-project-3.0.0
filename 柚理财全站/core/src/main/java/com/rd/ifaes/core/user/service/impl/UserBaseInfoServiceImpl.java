package com.rd.ifaes.core.user.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.mapper.UserBaseInfoMapper;
import com.rd.ifaes.core.user.service.UserBaseInfoService;

/**
 * 基本用户信息处理类
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userBaseInfoService") 
public class UserBaseInfoServiceImpl  extends BaseServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements UserBaseInfoService{
   
	/**
	 * 根据userId查询基本用户信息
	 */
	@Override
	public UserBaseInfo findByUserId(final String userId) {
		return dao.findByUserId(userId);
	}

}
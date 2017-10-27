package com.rd.ifaes.core.user.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserBaseInfo;

/**
 * Dao Interface:UserBaseInfoMapper
 * @author zb
 * @version 3.0
 * @date 2016-7-22
 */
public interface UserBaseInfoMapper extends BaseMapper<UserBaseInfo> {

	/**
	 * 根据userId查找用户基本信息
	 * @author xhf
	 * @date 2016年8月31日
	 * @param userId
	 * @return
	 */
	UserBaseInfo findByUserId(String userId);
}
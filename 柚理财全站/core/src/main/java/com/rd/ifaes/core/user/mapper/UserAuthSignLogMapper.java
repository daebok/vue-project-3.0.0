package com.rd.ifaes.core.user.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.user.domain.UserAuthSignLog;

/**
 * Dao Interface:UserAuthSignLogMapper
 * @author zb
 * @version 3.0
 * @date 2016-7-22
 */
public interface UserAuthSignLogMapper extends BaseMapper<UserAuthSignLog> {

	/**
	 * 根据订单更新结果
	 * @param respDesc
	 * @param orderNo
	 */
	void updateRespDescByOrder(UserAuthSignLog userAuthSignLog);

}
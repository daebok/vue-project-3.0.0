package com.rd.ifaes.core.sys.mapper;

import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.UserMessageConfig;

/**
 * Dao Interface:UserMessageConfigMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface UserMessageConfigMapper extends BaseMapper<UserMessageConfig> {
	
	/**
	 * 根据参数列表查询记录
	 * @param params
	 * @return
	 */
	UserMessageConfig findByParams(Map<String, String> params);
	
}
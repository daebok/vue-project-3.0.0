package com.rd.ifaes.core.sys.service;

import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.UserMessageConfig;

/**
 * Service Interface:UserMessageConfigService
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface UserMessageConfigService extends BaseService<UserMessageConfig>{
	
	/**
	 * 根据userId查找对象
	 */
	UserMessageConfig findByUserId(final String userId);

	/**
	 * 根据参数查找对象
	 * @return
	 */
	UserMessageConfig findByParams(final Map<String, String> params);
}
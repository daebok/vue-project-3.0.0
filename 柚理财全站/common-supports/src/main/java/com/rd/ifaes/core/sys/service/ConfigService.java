package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Config;

/**
 * Service Interface:ConfigService
 * @author lh
 * @version 3.0
 * @date 2016-5-17
 */
public interface ConfigService extends BaseService<Config>{
	
	/**
	 * 根据标识查询
	 * @param code
	 * @return
	 */
	Config findByCode(String code);
	
	/**
	 * 根据Nid修改value
	 * @param code
	 * @param value
	 * @return
	 */
	int updateValueByCode(String code, String value);
	
	/**
	 * 校验nid
	 * @param config
	 * 		&nid [ !id ]
	 * @return
	 */
	int checkCode(Config config);
	
}
package com.rd.ifaes.core.sys.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Config;

/**
 * Dao Interface:ConfigMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-17
 */
public interface ConfigMapper extends BaseMapper<Config> {

	/**
	 * 校验nid
	 * @param config
	 * 		&code [ !id ]
	 * @return
	 */
	int checkCode(Config config);
	
	/**
	 * findByCode
	 * @param code
	 * @return
	 */
	Config getByCode(String code);
	
}
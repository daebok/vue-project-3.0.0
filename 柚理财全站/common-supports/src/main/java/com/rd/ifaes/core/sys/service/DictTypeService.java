package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.DictType;

/**
 * Service Interface:DictTypeService
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public interface DictTypeService extends BaseService<DictType>{

	/**
	 * 根据dictType查找对象
	 * @param dictType
	 * @return
	 */
	DictType findByDictType(String dictType);

}
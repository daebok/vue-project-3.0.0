package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.DictData;

/**
 * Service Interface:DictDataService
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public interface DictDataService extends BaseService<DictData>{
	
	/**
	 * 取该类型对应的字典集合
	 * @param dictType
	 * @return
	 */
	List<DictData> findListByDictType(String dictType);
	
	
	/**
	 * 取指定类型和值得字典项
	 * @param dictType
	 * @param itemValue
	 * @return
	 */
	DictData get(String dictType, String itemValue);
}
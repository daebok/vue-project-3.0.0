package com.rd.ifaes.core.sys.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.DictType;

/**
 * Dao Interface:DictTypeMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public interface DictTypeMapper extends BaseMapper<DictType> {
	
	/**
	 * 根据字典类型查找记录
	 * @author xhf
	 * @date 2016年8月31日
	 * @param dictType
	 * @return
	 */
	DictType findByDictType(final String dictType);
}
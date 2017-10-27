package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.DictType;
import com.rd.ifaes.core.sys.mapper.DictTypeMapper;
import com.rd.ifaes.core.sys.service.DictTypeService;

/**
 * 字典类型业务处理
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
@Service("dictTypeService") 
public class DictTypeServiceImpl  extends BaseServiceImpl<DictTypeMapper, DictType> implements DictTypeService{
    
	/**
	 * 根据字典类型查找记录
	 */
    @Override
    public DictType findByDictType(final String dictType){
    	return dao.findByDictType(dictType);
    }

}
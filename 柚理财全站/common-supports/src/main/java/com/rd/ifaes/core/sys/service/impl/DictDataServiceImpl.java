package com.rd.ifaes.core.sys.service.impl;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.mapper.DictDataMapper;
import com.rd.ifaes.core.sys.service.DictDataService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典处理类
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
@Service("dictDataService") 
public class DictDataServiceImpl  extends BaseServiceImpl<DictDataMapper, DictData> implements DictDataService{
	
	/**
	 * 根据字典类型查询字典集合
	 */
	@Override
	@Cacheable(key=CacheConstant.KEY_PREFIX_DICT_DATA_DICT_TYPE+"{dictType}",expire = ExpireTime.ONE_DAY)
	public List<DictData> findListByDictType(final String dictType) {
		final DictData model = new DictData();
		model.setDictType(dictType);
		model.setStatus(CommonEnum.YES.getValue());
		return dao.findList(model);
	}
	
	/**
	 * 根据字典类型和字典值查询记录
	 */
	@Override
	@Cacheable(key=CacheConstant.KEY_PREFIX_DICT_DATA_DICT_TYPE+"{dictType}:{itemValue}",expire = ExpireTime.ONE_DAY)
	public DictData get(final String dictType, final String itemValue) {
		final DictData model = new DictData();
		model.setDictType(dictType);
		model.setItemValue(itemValue);
		
		DictData dictData = null;
		final List<DictData> list = dao.findList(model);
		if(list != null && list.size() > 0){
			dictData = list.get(0);
		}
		return dictData;
	}
	
	/**
	 * 保存对象
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final DictData entity) {
		final String key = CacheConstant.KEY_PREFIX_DICT_DATA_DICT_TYPE.concat(entity.getDictType());
		CacheUtils.batchDel(key);
		super.insert(entity);
	}
	
	/**
	 * 更新对象
	 */
	@Override
	@Transactional(readOnly = false)
	public void update(final DictData entity) {
		if(StringUtils.isNotBlank(entity.getDictType())){
			final String key = CacheConstant.KEY_PREFIX_DICT_DATA_DICT_TYPE.concat(entity.getDictType());
			CacheUtils.batchDel(key);			
		}
		dao.update(entity);
	}
	
}
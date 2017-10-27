package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Resource;
import com.rd.ifaes.core.sys.mapper.ResourceMapper;
import com.rd.ifaes.core.sys.service.ResourceService;

/**
 * ServiceImpl:ResourceServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-7-27
 */
@Service("resourceService") 
public class ResourceServiceImpl  extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService{
	
	@Override
	@Transactional
	public void insert(final Resource entity) {
		entity.preInsert();
		ResourceUtils.put(entity.getResName(), entity.getResLanguage(), entity.getResText());
		dao.insert(entity);
	}
	
	@Override
	public void update(final Resource entity) {
		entity.preUpdate();		
		ResourceUtils.put(entity.getResName(), entity.getResLanguage(), entity.getResText());
		super.update(entity);
	}
	
	@Override
	public Resource getByName(final String name, final String language) {
		return dao.getByName(name, language);
	}
	
}
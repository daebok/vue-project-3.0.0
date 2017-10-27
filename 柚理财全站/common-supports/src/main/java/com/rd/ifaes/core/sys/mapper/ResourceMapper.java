package com.rd.ifaes.core.sys.mapper;

import com.rd.ifaes.core.sys.domain.Resource;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:ResourceMapper
 * @author lh
 * @version 3.0
 * @date 2016-7-27
 */
public interface ResourceMapper extends BaseMapper<Resource> {
	
	/**
	 * 根据name查找记录
	 * @return
	 */
	Resource getByName(@Param("name") String name, @Param("language") String language);

}
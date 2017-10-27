package com.rd.ifaes.core.sys.mapper;

import com.rd.ifaes.core.sys.domain.Area;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:AreaMapper
 * @author xhf
 * @version 3.0
 * @date 2016-7-29
 */
public interface AreaMapper extends BaseMapper<Area> {

	/**
	 * 根据areaCode查询名称
	 * @author ZhangBiao
	 * @date 2016年8月1日
	 * @param areaCode
	 * @return
	 */
	String getNameByCode(String areaCode);

}
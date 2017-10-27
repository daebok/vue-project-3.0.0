package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.core.sys.domain.Area;
import com.rd.ifaes.core.sys.model.AreaModel;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:AreaService
 * @author xhf
 * @version 3.0
 * @date 2016-7-29
 */
public interface AreaService extends BaseService<Area>{

	/**
	 * 
	 * 根据areaCode查询area
	 * @author xhf
	 * @date 2016年7月29日
	 * @param areaCode
	 * @return
	 */
	AreaModel findByAreaCode(String areaCode);
	
	/**
	 * 
	 * 根据parentCode查询area列表
	 * @author xhf
	 * @date 2016年7月29日
	 * @param parentCode
	 * @return
	 */
	List<AreaModel> findByParentCode(String parentCode);

	/**
	 * 根据areaCode查询名称
	 * @author ZhangBiao
	 * @date 2016年8月1日
	 * @param province
	 * @return
	 */
	String getNameByCode(String areaCode);
}
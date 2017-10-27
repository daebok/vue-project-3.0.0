package com.rd.ifaes.core.column.service;

import java.util.List;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.model.SectionModel;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:SectionService
 * @author WengYaWen
 * @version 3.0
 * @date 2016-7-21
 */
public interface SectionService extends BaseService<Section>{
	/**
	 * 查找子类包括父类
	 * @param model
	 * @return
	 */
	Page<Section> findListByPage(Section model);
	/**
	 * 查询所有
	 * @param parentId
	 * @return
	 */
	 List<SectionModel> getSectionTree(String parentId);
	
	/**
	 * 
	 * 根据栏目标识查询对象
	 * @author xhf
	 * @date 2016年8月4日
	 * @param sectionCode
	 * @return
	 */
	Section getByCode(String sectionCode);
	
	/**
	 * 
	 *  获取子节点列表
	 * @param  
	 * @return List<Section>
	 * @author xxb
	 * @date 2016年11月1日
	 */
	public List<Section> findChildSection(String parentId);
	
	/**
	 * 根据
	 * @author ZhangBiao
	 * @date 2016年11月8日
	 * @param sectionCode
	 * @param uuid
	 * @return
	 */
	Section getByCodeId(String sectionCode, String uuid);
}
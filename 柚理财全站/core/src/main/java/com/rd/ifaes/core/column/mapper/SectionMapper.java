package com.rd.ifaes.core.column.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.column.domain.Section;

/**
 * Dao Interface:SectionMapper
 * @author WengYaWen
 * @version 3.0
 * @date 2016-7-21
 */
public interface SectionMapper extends BaseMapper<Section> {

	/**
	 * 
	 * 根据code查询对象
	 * @author xhf
	 * @date 2016年8月5日
	 * @param model
	 * @return
	 */
	Section getByCode(Section section);
}
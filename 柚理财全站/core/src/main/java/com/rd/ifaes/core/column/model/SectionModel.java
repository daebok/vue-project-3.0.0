package com.rd.ifaes.core.column.model;

import com.rd.ifaes.common.entity.TreeEntity;
import com.rd.ifaes.core.column.domain.Section;

/**
 * 树节点model 
 * @author wyw
 * @version 3.0
 * @date 2016-7-7
 */
public class SectionModel extends TreeEntity<SectionModel> {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1944250331411366214L;
	/**
	 * 构造器
	 */
	public SectionModel() {
		super();
	}
	/**
	 * 构造器
	 */
	public SectionModel(final  String nodeId, final String text,final int sort) {
		super();
		super.id = nodeId;
		super.text = text;
		super.sort = sort;
	}
	/**
	 * 实例化SectionModel
	 */
	public static SectionModel instance(final Section section){
		final SectionModel sectionModel = new SectionModel(section.getUuid(),section.getSectionName(),section.getSort());
		return sectionModel;
	}
}

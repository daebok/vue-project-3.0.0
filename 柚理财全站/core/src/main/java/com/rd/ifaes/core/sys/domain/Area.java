package com.rd.ifaes.core.sys.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Area
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-7-29
 */
public class Area extends BaseEntity<Area> {
	
	private static final long serialVersionUID = 1L;
	/** 区域编码 */
	private String	areaCode;	
	/** 区域名称 */
	private String	areaName;	
	/** 层级（根节点 中国 0，省 1 市 2 区、县3） */ 
	private Integer	areaLevel;	
	/** 排序 */ 
	private Integer	sort;	
	/** 上一级 */ 
	private String	parentCode;		 

	//其他自定义属性
	

	/**
	 * 空构造函数
	 */
	public Area() {
		super();
	}

	/**
	 * 构造函数
	 * @param areaCode
	 * @param parentCode
	 */
	public Area(final String areaCode, final String parentCode) {
		super();
		this.areaCode = areaCode;
		this.parentCode = parentCode;
	}

	/**
	 * 构造函数
	 */
	public Area(final String uuid, final String areaCode, final String areaName, final Integer areaLevel, final Integer sort, final String parentCode) {
		super();
		setUuid(uuid);
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.areaLevel = areaLevel;
		this.sort = sort;
		this.parentCode = parentCode;
	}

	/**
	 * 获取区域编码
	 * @return areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * 设置区域编码
	 * @param  areaCode
	 */
	public void setAreaCode(final String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 获取区域名称
	 * @return areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置区域名称
	 * @param  areaName
	 */
	public void setAreaName(final String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取层级（根节点中国0，省1市2区、县3）
	 * @return areaLevel
	 */
	public Integer getAreaLevel() {
		return areaLevel;
	}

	/**
	 * 设置层级（根节点中国0，省1市2区、县3）
	 * @param  areaLevel
	 */
	public void setAreaLevel(final Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	/**
	 * 获取排序
	 * @return sort
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * @param  sort
	 */
	public void setSort(final Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取上一级
	 * @return parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * 设置上一级
	 * @param  parentCode
	 */
	public void setParentCode(final String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * 覆盖toString()方法
	 */
	@Override
	public String toString() {
		return "Area [" + "uuid=" + uuid + ", areaCode=" + areaCode + ", areaName=" + areaName + ", areaLevel=" + areaLevel + ", sort=" + sort + ", parentCode=" + parentCode +  "]";
	}
}

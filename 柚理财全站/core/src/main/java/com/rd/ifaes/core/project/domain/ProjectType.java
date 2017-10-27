/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.domain;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ProjectType
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-8-2
 */
public class ProjectType extends BaseEntity<ProjectType> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类别最小层级为0 
	 */
	public static final int  TYPE_LEVEL_MIN=0;
	/**
	 * 类别最大层级为2 
	 */
	public static final int  TYPE_LEVEL_MAX=2;
	
	@Length( max = 5, message = "{manage.projectType.typeName.lengthError}")
	private String	typeName;		 /* 分类名称 */ 
	@NotBlank(message="{projectType.parentId.notEmpty}")
	private String	parentId;		 /* 父类ID */ 
	private Integer	sort;		 /* 排序 */ 
	private String	protocolId;		 /* 协议模板标识 */ 
	@Length( max = 15, message = "{manage.projectType.features.lengthError}")
	private String	features;		 /* 特点 */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private Integer	typeLevel;		 /* 层级(根目录为0层，最大2） */ 
	private String nid;  /*分类标识*/
	//其他自定义属性
	private String isLeaf;   // 是否叶子节点
	private String protocolName; //协议名称
	private Integer exceptTypeLevel;//排除某一层级
	// Constructor
	public ProjectType() {
	}
 
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(Integer typeLevel) {
		this.typeLevel = typeLevel;
	}
	/**
	 * 获取协议名称
	 * @return 协议名称
	 */
	public String getProtocolName() {
		return protocolName;
	}

	/**
	 * 设置协议名称
	 * @param 协议名称
	 */
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public Integer getExceptTypeLevel() {
		return exceptTypeLevel;
	}

	public void setExceptTypeLevel(Integer exceptTypeLevel) {
		this.exceptTypeLevel = exceptTypeLevel;
	}

	
	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	@Override
	public String toString() {
		return "ProjectType [" + "uuid=" + uuid + ", typeName=" + typeName + ", parentId=" + parentId + ", sort=" + sort + ", protocolId=" + protocolId + ", features=" + features + ", deleteFlag=" + deleteFlag + ", typeLevel=" + typeLevel +  "]";
	}
}

package com.rd.ifaes.mobile.model.project;

/**
 * @authorlgx
 * 分类列表
 */
public class projectitemTypeModel {
	/**
	 * 类别的UUID
	 */
	private String uuid;
	/**
	 * 分类名称
	 */
	private String typeName;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	 * 父类id
	 */
	private String parentId;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 协议模板标识
	 */
	private String protocolId;
	/**
	 * 特点
	 */
	private String features;
	/**
	 * 0 未删除，1 已删除
	 */
	private String deleteFlag;
	/**
	 * 层级(根目录为0层，最大2）
	 */
	private Integer typeLevel;
	
	
}

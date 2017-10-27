package com.rd.ifaes.core.column.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:Section
 * 
 * @author WengYaWen
 * @version 3.0
 * @date 2016-8-4
 */
public class Section extends BaseEntity<Section> {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8026667895097168673L;
	/** 栏目名称 */ 
	private String	sectionName;
	/** 栏目标识 */ 
	private String	sectionCode;		
	/** 栏目类型，1：列表，2：单页  */ 
	private String	sectionType;		
	/** 上一级栏目ID  */ 
	private String	parentId;		
	/** 状态 0 未删除，1 已删除 */ 
	private String	deleteFlag;		
	/** 排序  */ 
	private Integer	sort;		
	/** 描述   */ 
	private String	remark;		
	/** 创建日期   */ 
	private Date	createTime;		
	/** 图片个数   */
	private Integer	picNumber;	

	//其他自定义属性
	

	/** Constructor   */
	public Section() {
		super();
	}
	
	/** 持久化前执行的操作   */
	@Override
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = DeleteFlagEnum.NO.getValue();
		this.createTime = DateUtils.getNow();
	}

	/**
	 * full Constructor
	 */
	public Section(final String uuid,final  String sectionName, final String sectionCode, final String sectionType,
			final String parentId,final  String deleteFlag, final Integer sort,final  String remark,
			final Date createTime, final Integer picNumber) {
		super();
		setUuid(uuid);
		this.sectionName = sectionName;
		this.sectionCode = sectionCode;
		this.sectionType = sectionType;
		this.parentId = parentId;
		this.deleteFlag = deleteFlag;
		this.sort = sort;
		this.remark = remark;
		this.createTime = createTime;
		this.picNumber = picNumber;
	}
	/** 获取栏目名称   */
	public String getSectionName() {
		return sectionName;
	}
	/** 设置栏目名称   */
	public void setSectionName(final String sectionName) {
		this.sectionName = sectionName;
	}
	/** 获取栏目标识  */
	public String getSectionCode() {
		return sectionCode;
	}
	/** 设置栏目标识  */
	public void setSectionCode(final String sectionCode) {
		this.sectionCode = sectionCode;
	}
	/** 获取栏目类型  */
	public String getSectionType() {
		return sectionType;
	}
	/** 设置栏目类型  */
	public void setSectionType(final String sectionType) {
		this.sectionType = sectionType;
	}
	/** 获取父节点id  */
	public String getParentId() {
		return parentId;
	}
	/** 设置父节点id  */
	public void setParentId(final String parentId) {
		this.parentId = parentId;
	}
	/** 获取删除标示  */
	public String getDeleteFlag() {
		return deleteFlag;
	}
	/** 设置删除标示  */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/** 获取排序值  */
	public Integer getSort() {
		return sort;
	}
	/** 设置排序值  */
	public void setSort(final Integer sort) {
		this.sort = sort;
	}
	/** 获取备注描述  */
	public String getRemark() {
		return remark;
	}
	/** 设置备注描述  */
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取创建日期  */
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置创建日期  */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取栏目显示图片个数  */
	public Integer getPicNumber() {
		return picNumber;
	}
	/** 设置栏目显示图片个数  */
	public void setPicNumber(final Integer picNumber) {
		this.picNumber = picNumber;
	}
	/** toString  */
	@Override
	public String toString() {
		return "Section [" + "uuid=" + uuid + ", sectionName=" + sectionName + ", sectionCode=" + sectionCode + ", sectionType=" + sectionType + ", parentId=" + parentId + ", deleteFlag=" + deleteFlag + ", sort=" + sort + ", remark=" + remark + ", createTime=" + createTime + ", picNumber=" + picNumber +  "]";
	}
}

package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.constant.ResourceConstant;

/**
 * entity:DictType
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public class DictType extends BaseEntity<DictType> {
	
	private static final long serialVersionUID = 1L;
	/** 字典类型 */
	@Size(max=64,message="{"+ResourceConstant.DICT_TYPE_OVER_LENGTH+"}")
	private String	dictType;	
	/** 类型名称 */
	@Size(max=40,message="{"+ResourceConstant.TYPE_NAME_OVER_LENGTH+"}")
	private String	typeName;	
	/** 创建时间 */ 
	private Date	createTime;	
	/** 备注 */ 
	@Size(max=512,message="{"+ResourceConstant.REMARK_OVER_LENGTH+"}")
	private String	remark;		 

	//其他自定义属性
	

	/**
	 * 空构造函数
	 */
	public DictType() {
		super();
	}

	/**
	 * 全参构造
	 */
	public DictType(final String uuid, final String dictType, final String typeName, final Date createTime, final String remark) {
		super();
		setUuid(uuid);
		this.dictType = dictType;
		this.typeName = typeName;
		this.createTime = createTime;
		this.remark = remark;
	}

	/**
	 * 获取字典类型
	 * @return dictType
	 */
	public String getDictType() {
		return dictType;
	}

	/**
	 * 设置字典类型
	 * @param  dictType
	 */
	public void setDictType(final String dictType) {
		this.dictType = dictType;
	}

	/**
	 * 获取类型名称
	 * @return typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * 设置类型名称
	 * @param  typeName
	 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	/**
	 * 获取创建时间
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取备注
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * @param  remark
	 */
	public void setRemark(final String remark) {
		this.remark = remark;
	}

	/**
	 * 重写preInsert()
	 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = (this.createTime == null)?new Date():this.createTime;
	}

	/**
	 * 重写toString()
	 */
	@Override
	public String toString() {
		return "DictType [" + "uuid=" + uuid + ", dictType=" + dictType + ", typeName=" + typeName + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}

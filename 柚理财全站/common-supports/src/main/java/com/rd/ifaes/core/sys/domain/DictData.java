package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ResourceConstant;

/**
 * entity:DictData
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
public class DictData extends BaseEntity<DictData> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_STATUS_ENABLE = "1";//默认启用状态标识，启用
	public static final String DEFAULT_DELETE_FLAG = "0";//删除状态默认值，未删除
	
	/** 字典类型 */ 
	private String	dictType;	
	/** 数据项名称 */ 
	@Size(max=64,message="{"+ResourceConstant.ITEM_NAME_OVER_LENGTH+"}")
	private String	itemName;	
	/** 数据项值 */ 
	@Size(max=128,message="{"+ResourceConstant.ITEM_VALUE_OVER_LENGTH+"}")
	private String	itemValue;	
	/** 创建时间 */ 
	private Date	createTime;	
	/**  排序 */
	private Integer	sort;
	/** 启用状态(1 启用，0停用) */ 
	private String	status;		 
	/** 表达式*/
	private String expression;	
	/** 备注 */ 
	@Size(max=512,message="{"+ResourceConstant.REMARK_OVER_LENGTH+"}")
	private String	remark;		 

	//其他参数
	/** 最小值 */ 
	private String minValue;
	/** 最大值 */ 
	private String maxValue;
	/**
	 * 空构造函数
	 */
	public DictData() {
		super();
	}

	public DictData(String itemName, String itemValue) {
		super();
		this.itemName = itemName;
		this.itemValue = itemValue;
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
	 * 获取数据项名称
	 * @return itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * 设置数据项名称
	 * @param  itemName
	 */
	public void setItemName(final String itemName) {
		this.itemName = itemName;
	}

	/**
	 * 获取数据项值
	 * @return itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * 设置数据项值
	 * @param  itemValue
	 */
	public void setItemValue(final String itemValue) {
		this.itemValue = itemValue;
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
	 * 获取启用状态(1启用，0停用)
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置启用状态(1启用，0停用)
	 * @param  status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获取表达式
	 * @return expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * 设置表达式
	 * @param  expression
	 */
	public void setExpression(final String expression) {
		this.expression = expression;
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
	 * 获取属性minValue的值
	 * @return minValue属性值
	 */
	public String getMinValue() {
		return minValue;
	}

	/**
	 * 设置属性minValue的值
	 * @param  minValue属性值
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	/**
	 * 获取属性maxValue的值
	 * @return maxValue属性值
	 */
	public String getMaxValue() {
		return maxValue;
	}

	/**
	 * 设置属性maxValue的值
	 * @param  maxValue属性值
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "DictData [" + "uuid=" + uuid + ", dictType=" + dictType + ", itemName=" + itemName + ", itemValue=" + itemValue + ", createTime=" + createTime + ", sort=" + sort + ", status=" + status + ", expression=" + expression + ", remark=" + remark +  "]";
	}
	
	/**
	 * 重写preInsert()方法
	 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.status = StringUtils.isBlank(this.status)?DEFAULT_STATUS_ENABLE:this.status;
	}

}

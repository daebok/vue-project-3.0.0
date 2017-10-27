package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.StringUtils;

/**
 * entity:Menu
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public class Menu extends BaseEntity<Menu> {
	
	private static final long serialVersionUID = 1L;
	
	private String	code;		 /* 菜单编号 */ 
	private String	menuName;		 /* 菜单名称 */ 
	private String	url;		 /* url */ 
	private String	parentId;		 /* 父级ID */ 
	private String	parentIds;		 /* 链接地址 */ 
	private String	iconCss;		 /* 图标 */ 
	private Integer	sort;		
	private Date	createTime;		 /* 添加时间 */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private String	remark;		 /* 备注 */ 
	private String  isLeaf;		 /*是否叶子节点：1 是；0 否*/
	
	//其他自定义属性
	

	// Constructor
	public Menu() {
	}
	
	public Menu(String parentId) {
		this.parentId=parentId;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getIconCss() {
		return iconCss;
	}

	public void setIconCss(String iconCss) {
		this.iconCss = iconCss;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public void preInsert() {
		super.preInsert();
		this.deleteFlag = StringUtils.isBlank(this.deleteFlag)?CommonEnum.NO.getValue():this.deleteFlag;
		this.isLeaf = StringUtils.isBlank(this.isLeaf)?CommonEnum.NO.getValue():this.isLeaf;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Override
	public String toString() {
		return "Menu [" + "uuid=" + uuid + ", code=" + code + ", menuName=" + menuName + ", url=" + url + ", parentId=" + parentId + ", parentIds=" + parentIds + ", iconCss=" + iconCss + ", sort=" + sort + ", createTime=" + createTime + ", deleteFlag=" + deleteFlag + ", remark=" + remark +  "]";
	}

}

package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Org
 * 
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
public class Org extends BaseEntity<Org> {
	
	private static final long serialVersionUID = 1L;
	
	private String	orgNo;		 /* 机构(部门)编号 */ 
	@Length(min = 2, max = 50, message = "{sys.org.orgName.lengthError}")
	private String	orgName;		 /* 机构(部门)名称 */ 
	@NotEmpty(message="{sys.org.parentId.emptyMsg}")
	private String	parentId;		 /* 父级ID */ 
	private String	parentIds;		 /* 父级ID集 */ 
	private String	orgType;		 /* 类型(机构、部门) */ 
	private Integer	orgLevel;		 /* 层级，如： 根节点 0 一级部门 1  二级部门 2 */ 
	@NotNull(message="{sort.value.not.null}")
	private Integer	sort;			 /* 排序 */
	private Date	createTime;		 /* 添加时间 */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private String	remark;		 /* 备注 */ 

	//其他自定义属性
	

	// Constructor
	public Org() {
	}

	/**
	 * full Constructor
	 */
	

	public String getOrgNo() {
		return orgNo;
	}

	public Org(String uuid, String orgNo, String orgName, String parentId, String parentIds, String orgType, Integer orgLevel,
			Integer sort, Date createTime, String deleteFlag, String remark) {
		super(uuid);
		this.orgNo = orgNo;
		this.orgName = orgName;
		this.parentId = parentId;
		this.parentIds = parentIds;
		this.orgType = orgType;
		this.orgLevel = orgLevel;
		this.sort = sort;
		this.createTime = createTime;
		this.deleteFlag = deleteFlag;
		this.remark = remark;
	}

		
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Integer getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(Integer orgLevel) {
		this.orgLevel = orgLevel;
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

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
		this.deleteFlag = CommonEnum.NO.getValue();
	}

	@Override
	public String toString() {
		return "Org [uuid=" + uuid + ", orgNo=" + orgNo + ", orgName=" + orgName + ", parentId=" + parentId + ", parentIds=" + parentIds
				+ ", orgType=" + orgType + ", orgLevel=" + orgLevel + ", sort=" + sort + ", createTime=" + createTime
				+ ", deleteFlag=" + deleteFlag + ", remark=" + remark + "]";
	}

	
	
}

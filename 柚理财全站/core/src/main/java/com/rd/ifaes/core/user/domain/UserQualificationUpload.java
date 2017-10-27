package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserQualificationUpload
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-8-3
 */
public class UserQualificationUpload extends BaseEntity<UserQualificationUpload> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String	userId;		 
	/** 审核申请ID */ 
	private String	qualificationApplyId;	
	/** 文件路径 */ 
	private String	filePath;
	/** 添加时间 */ 
	private Date	createTime;	
	/** 删除标识 */ 
	private String	deleteFlag;		 

	//其他自定义属性
	

	/**
	 * 构造函数
	 */
	public UserQualificationUpload() {
		super();
	}
	
	/**
	 * 构造函数
	 */
	public UserQualificationUpload(final String qualificationApplyId) {
		super();
		this.qualificationApplyId = qualificationApplyId;
	}


	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取审核申请ID
	 * @return qualificationApplyId
	 */
	public String getQualificationApplyId() {
		return qualificationApplyId;
	}

	/**
	 * 设置审核申请ID
	 * @param  qualificationApplyId
	 */
	public void setQualificationApplyId(final String qualificationApplyId) {
		this.qualificationApplyId = qualificationApplyId;
	}

	/**
	 * 获取文件路径
	 * @return filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * 设置文件路径
	 * @param  filePath
	 */
	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 获取添加时间
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置添加时间
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取删除标识
	 * @return deleteFlag
	 */
	public String getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * 设置删除标识
	 * @param  deleteFlag
	 */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserQualificationUpload [" + "uuid=" + uuid + ", userId=" + userId + ", qualificationApplyId=" + qualificationApplyId + ", filePath=" + filePath + ", createTime=" + createTime + ", deleteFlag=" + deleteFlag +  "]";
	}
}

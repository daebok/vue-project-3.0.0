package com.rd.ifaes.core.project.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:BorrowUpload
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-6-22
 */
public class BorrowUpload extends BaseEntity<BorrowUpload> {
	
	private static final long serialVersionUID = 1L;
	
	private String	projectId;		 /* 借款基本信息ID */ 
	private String	fileType;		 /* 上传文件类型:1、借款资料；2、抵押物资料；3、其他 */ 
	private String	filePath;		 /* 上传文件路径 */ 
	private Date	addTime;		 /* 上传时间 */ 
	private Integer	sort;		 /* 显示顺序 */ 

	//其他自定义属性
	public static final String FILE_TYPE_BORROW = "1";//借款资料
	public static final String FILE_TYPE_MORTGAGE = "2";//抵押物资料
	public static final String FILE_TYPE_BORROWFOLLOW="3";//贷后跟踪资料

	// Constructor
	public BorrowUpload() {
	}

	/**
	 * full Constructor
	 */
	public BorrowUpload(String uuid, String projectId, String fileType, String filePath, Date addTime, Integer sort) {
		setUuid(uuid);
		this.projectId = projectId;
		this.fileType = fileType;
		this.filePath = filePath;
		this.addTime = addTime;
		this.sort = sort;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "BorrowUpload [" + "uuid=" + uuid + ", projectId=" + projectId + ", fileType=" + fileType + ", filePath=" + filePath + ", addTime=" + addTime + ", sort=" + sort +  "]";
	}
}

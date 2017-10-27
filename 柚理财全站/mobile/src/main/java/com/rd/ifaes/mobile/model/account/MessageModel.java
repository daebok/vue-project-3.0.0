package com.rd.ifaes.mobile.model.account;

import java.util.Date;

public class MessageModel {
    /**
     * 批量处理类型: 1  批量删除 2 批量已读 3 批量未读
     */
	private int batchType;
	/**
	 * 消息内容
	 */
	private String	content;
	/**
	 * 添加时间
	 */
	private Date createTime;
	 /**
	  * 删除标识：0 未删除，1 已删除 
	  */
	private String	deleteFlag;	
	 /**
	  * 阅读标识(1已阅读，0未阅读，默认0)
	  */ 
	private String	readFlag;
	/**
	 * 接收用户ID 
	 */ 
	private String	receiveUser;
	 /**
	  * 消息标题 
	  */ 
	private String	title;
	/**
	 * uuid
	 */
	
	private String uuid;
	public int getBatchType() {
		return batchType;
	}
	public void setBatchType(int batchType) {
		this.batchType = batchType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	public String getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}

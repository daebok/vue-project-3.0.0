package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;

/**
 * entity:Letter
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public class Letter extends BaseEntity<Letter> {
	
	private static final long serialVersionUID = 1L;
	
	private String	sendUser;		 /* 发送用户ID */ 
	private String	receiveUser;		 /* 接收用户ID */ 
	private String	title;		 /* 消息标题 */ 
	private String	content;		 /* 内容 */ 
	private String	status;		 /* 状态（0 新建，1发送成功 2发送失败） */ 
	private String	readFlag;		 /* 阅读标识(1已阅读，0未阅读，默认0) */ 
	private String	deleteFlag;		 /* 0 未删除，1 已删除 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	remark;		 /* 备注: 发送结果信息 */ 

	//其他自定义属性
	private String[] ids; // id数组(批量使用)
	private int batchType;// 批量处理类型
	// Constructor
	public Letter() {
	}

	/**
	 * full Constructor
	 */
	public Letter(String uuid, String sendUser, String receiveUser, String title, String content, String status, String readFlag, String deleteFlag, Date createTime, String remark) {
		setUuid(uuid);
		this.sendUser = sendUser;
		this.receiveUser = receiveUser;
		this.title = title;
		this.content = content;
		this.status = status;
		this.readFlag = readFlag;
		this.deleteFlag = deleteFlag;
		this.createTime = createTime;
		this.remark = remark;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取属性ids的值
	 * @return ids属性值
	 */
	public String[] getIds() {
		return ids;
	}

	/**
	 * 设置属性ids的值
	 * @param  ids属性值
	 */
	public void setIds(String[] ids) {
		this.ids = ids;
	}

	/**
	 * 获取属性batchType的值
	 * @return batchType属性值
	 */
	public int getBatchType() {
		return batchType;
	}

	/**
	 * 设置属性batchType的值
	 * @param  batchType属性值
	 */
	public void setBatchType(int batchType) {
		this.batchType = batchType;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime =  (this.createTime == null? DateUtils.getNow():this.createTime);
		this.readFlag = (StringUtils.isNotBlank(this.readFlag)?this.readFlag:"0");
		this.deleteFlag = (StringUtils.isNotBlank(this.deleteFlag)?this.deleteFlag:"0");
		
	}
	
	@Override
	public String toString() {
		return "Letter [" + "uuid=" + uuid + ", sendUser=" + sendUser + ", receiveUser=" + receiveUser + ", title=" + title + ", content=" + content + ", status=" + status + ", readFlag=" + readFlag + ", deleteFlag=" + deleteFlag + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}

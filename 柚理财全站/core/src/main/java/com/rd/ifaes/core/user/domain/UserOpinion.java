package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserOpinion
 * 用户意见反馈记录实体
 * @author xxb
 * @version 3.0
 * @date 2016-10-10
 */
public class UserOpinion extends BaseEntity<UserOpinion> {

	private static final long serialVersionUID = -2755449623415715472L;
	
	private String title;
	private String attachmentPath;
	private String userId;
	private String remark;
	private Date createTime;
	private String addIp;
	/**
	 * 增加状态和内容字段
	 */
	private String status ;
	private String content ;
	
	private String userName;
	private String realName;
	private String mobile;
	
	public UserOpinion(){
		
	}
	
	public UserOpinion(final String uuid, final String title, final String attachmentPath, final String userId, final String remark, final Date createTime, final String addIp){
		super.setUuid(uuid);
		this.title = title;
		this.attachmentPath = attachmentPath;
		this.userId = userId;
		this.remark = remark;
		this.createTime = createTime;
		this.addIp = addIp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}

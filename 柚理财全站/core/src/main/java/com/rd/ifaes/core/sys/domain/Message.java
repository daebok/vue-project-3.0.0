package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;

/**
 * entity:Message
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-21
 */
public class Message extends BaseEntity<Message> {
	
	private static final long serialVersionUID = 1L;
	/** 消息模板标识 */ 
	private String	messageType;
	/** 发送用户ID */ 
	private String	sendUser;	
	/** 接收用户ID */ 
	private String	receiveUser;	
	/** 接收地址(1短信 ：填写接收人手机号；2邮件： 接收人邮箱 ；3站内信，填写用户名) */
	private String	receiveAddr;
	/** 通知类型（1短信 2邮件 3站内信） */ 
	private String	sendType;	
	/** 消息标题 */
	private String	title;	
	/** 内容 */ 
	private String	content;	
	/** 状态（0 新建，1发送成功 2发送失败） */ 
	private String	status;		 
	/** 添加时间 */ 
	private Date	createTime;	
	/** 备注: 发送结果信息 */ 
	private String	remark;
	
	/** 短信发送的模板 阿里大于 */
	private String templateCode;
	/** 短信模板需要的json信息 阿里大于 */
	private String templateContent;
	//其他自定义属性
	private String receiveUserName;

	/**
	 * 构造函数
	 */
	public Message() {
		super();
	}

	/**
	 * 构造函数
	 * @param messageType
	 * @param receiveAddr
	 */
	public Message(final String messageType, final String receiveAddr) {
		super();
		this.messageType = messageType;
		this.receiveAddr = receiveAddr;
	}

	/**
	 * 构造函数
	 */
	public Message(final String uuid, final String messageType, final String sendUser,
			final String receiveUser, final String receiveAddr, final String sendType,
			final String title, final String content, final String status, final Date createTime,
			final String remark) {
		super();
		setUuid(uuid);
		this.messageType = messageType;
		this.sendUser = sendUser;
		this.receiveUser = receiveUser;
		this.receiveAddr = receiveAddr;
		this.sendType = sendType;
		this.title = title;
		this.content = content;
		this.status = status;
		this.createTime = createTime;
		this.remark = remark;
	}
	
	/**
	 * 获取消息模板标识
	 * @return messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * 设置消息模板标识
	 * @param  messageType
	 */
	public void setMessageType(final String messageType) {
		this.messageType = messageType;
	}

	/**
	 * 获取发送用户ID
	 * @return sendUser
	 */
	public String getSendUser() {
		return sendUser;
	}

	/**
	 * 设置发送用户ID
	 * @param  sendUser
	 */
	public void setSendUser(final String sendUser) {
		this.sendUser = sendUser;
	}

	/**
	 * 获取接收用户ID
	 * @return receiveUser
	 */
	public String getReceiveUser() {
		return receiveUser;
	}

	/**
	 * 设置接收用户ID
	 * @param  receiveUser
	 */
	public void setReceiveUser(final String receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取接收地址(1短信：填写接收人手机号；2邮件：接收人邮箱；3站内信，填写用户名)
	 * @return receiveAddr
	 */
	public String getReceiveAddr() {
		return receiveAddr;
	}

	/**
	 * 设置接收地址(1短信：填写接收人手机号；2邮件：接收人邮箱；3站内信，填写用户名)
	 * @param  receiveAddr
	 */
	public void setReceiveAddr(final String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	/**
	 * 获取通知类型（1短信2邮件3站内信）
	 * @return sendType
	 */
	public String getSendType() {
		return sendType;
	}

	/**
	 * 设置通知类型（1短信2邮件3站内信）
	 * @param  sendType
	 */
	public void setSendType(final String sendType) {
		this.sendType = sendType;
	}

	/**
	 * 获取消息标题
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置消息标题
	 * @param  title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * 获取内容
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * @param  content
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * 获取状态（0新建，1发送成功，2发送失败）
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置状态（0新建，1发送成功，2发送失败）
	 * @param  status
	 */
	public void setStatus(final String status) {
		this.status = status;
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
	 * 获取备注:发送结果信息
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注:发送结果信息
	 * @param  remark
	 */
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	
	/**
	 * 获取属性receiveUserName的值
	 * @return receiveUserName属性值
	 */
	public String getReceiveUserName() {
		return receiveUserName;
	}
	
	/**
	 * 设置属性receiveUserName的值
	 * @param  receiveUserName属性值
	 */
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	/**
	 * 重写preInsert()
	 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = (this.createTime != null? this.createTime:DateUtils.getNow());
		this.status = (StringUtils.isNotBlank(status)?this.status:"0");
	}

	

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	/**
	 * 重写toString()
	 */
	@Override
	public String toString() {
		return "Message [" + "uuid=" + uuid + ", messageType=" + messageType + ", sendUser=" + sendUser + ", receiveUser=" + receiveUser + ", receiveAddr=" + receiveAddr + ", sendType=" + sendType + ", title=" + title + ", content=" + content + ", status=" + status + ", createTime=" + createTime + ", remark=" + remark + ", templateCode=" + templateCode +  "]";
	}
}

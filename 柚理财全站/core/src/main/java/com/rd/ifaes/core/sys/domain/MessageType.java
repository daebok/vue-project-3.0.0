package com.rd.ifaes.core.sys.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:MessageType
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public class MessageType extends BaseEntity<MessageType> {
	
	private static final long serialVersionUID = 1L;
	/** 消息类型标识 */ 
	private String	messageType;	
	/** 消息类型名称 */ 
	private String	messageName;	
	/** 通知类型（1短信 2邮件 3站内信） */ 
	private String	sendType;		 
	/** 消息标题模板(freemark模板格式) */ 
	private String	messageTitle;	
	/** 消息内容模板(freemark模板格式) */ 
	private String	messageContent;		 
	/** 是否启用: 0 禁用，1 启用 */ 
	private String send;		
	/** 添加时间 */ 
	private Date createTime;	
	/** 备注 */ 
	private String remark;		 
	private String templateCode;
	//其他自定义属性
	

	/**
	 * 空构造函数
	 */
	public MessageType() {
		super();
	}

	/**
	 * full Constructor
	 */
	public MessageType(final String uuid, final String messageType, final String sendType, 
			final String messageName, final String messageTitle, final String messageContent, 
			final String send, final Date createTime, final String remark) {
		super();
		setUuid(uuid);
		this.messageType = messageType;
		this.sendType = sendType;
		this.messageName = messageName;
		this.messageTitle = messageTitle;
		this.messageContent = messageContent;
		this.send = send;
		this.createTime = createTime;
		this.remark = remark;
	}

	/**
	 * 获取消息类型标识
	 * @return messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * 设置消息类型标识
	 * @param  messageType
	 */
	public void setMessageType(final String messageType) {
		this.messageType = messageType;
	}

	/**
	 * 获取消息类型名称
	 * @return messageName
	 */
	public String getMessageName() {
		return messageName;
	}

	/**
	 * 设置消息类型名称
	 * @param  messageName
	 */
	public void setMessageName(final String messageName) {
		this.messageName = messageName;
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
	 * 获取消息标题模板(freemark模板格式)
	 * @return messageTitle
	 */
	public String getMessageTitle() {
		return messageTitle;
	}

	/**
	 * 设置消息标题模板(freemark模板格式)
	 * @param  messageTitle
	 */
	public void setMessageTitle(final String messageTitle) {
		this.messageTitle = messageTitle;
	}

	/**
	 * 获取消息内容模板(freemark模板格式)
	 * @return messageContent
	 */
	public String getMessageContent() {
		return messageContent;
	}

	/**
	 * 设置消息内容模板(freemark模板格式)
	 * @param  messageContent
	 */
	public void setMessageContent(final String messageContent) {
		this.messageContent = messageContent;
	}

	/**
	 * 获取是否启用:0禁用，1启用
	 * @return send
	 */
	public String getSend() {
		return send;
	}

	/**
	 * 设置是否启用:0禁用，1启用
	 * @param  send
	 */
	public void setSend(final String send) {
		this.send = send;
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
	
	

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "MessageType [" + "uuid=" + uuid + ", messageType=" + messageType + ", sendType=" + sendType + ", messageName=" + messageName + ", messageTitle=" + messageTitle + ", messageContent=" + messageContent + ", send=" + send + ", createTime=" + createTime + ", remark=" + remark +  "]";
	}
}

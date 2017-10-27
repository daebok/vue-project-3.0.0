package com.rd.ifaes.core.sys.domain;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserMessageConfig
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public class UserMessageConfig extends BaseEntity<UserMessageConfig> {
	
	private static final long serialVersionUID = 1L;
	
	private String	userId;		 /* 用户 */ 
	private String	messageType;		 /* 消息类型 */ 
	private String	sms;		 /* 短信接收配置( 1 接收，0不接收) */ 
	private String	email;		 /* 邮件接收配置( 1 接收，0不接收) */ 
	private String	letter;		 /* 站内信接收配置( 1 接收，0不接收) */ 

	//其他自定义属性
	

	// Constructor
	public UserMessageConfig() {
	}

	/**
	 * full Constructor
	 */
	public UserMessageConfig(String uuid, String userId, String messageType, String sms, String email, String letter) {
		setUuid(uuid);
		this.userId = userId;
		this.messageType = messageType;
		this.sms = sms;
		this.email = email;
		this.letter = letter;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	@Override
	public String toString() {
		return "UserMessageConfig [" + "uuid=" + uuid + ", userId=" + userId + ", messageType=" + messageType + ", sms=" + sms + ", email=" + email + ", letter=" + letter +  "]";
	}
}

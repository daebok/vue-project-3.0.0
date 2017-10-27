package com.rd.ifaes.core.core.sms.msg;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.core.sms.VerifCodeForSMSUtils;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 短信验证码业务处理类
 * @version 3.0
 * @author xhf
 * @date 2016年8月30日
 */
public class GetCodeLog implements MsgEvent, Serializable {
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(GetCodeLog.class);

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	
	/** 发送通知service */
	protected transient MessageService messageService;

	/** 用户 */
	private User user;
	
	/** 接收地址 */
	private String receiveAddr;
	
	/** 模块编码 */
	protected String messageType;
	
	/**
	 * 构造方法
	 */
	public GetCodeLog() {
		super();
		messageService = (MessageService) SpringContextHolder.getBean("messageService");
	}
	
	/**
	 * 构造方法
	 * @param messageType
	 * @param userName
	 * @param email
	 * @param mobilePhone
	 */
	public GetCodeLog(final User user, final String messageType) {
		this();
		this.user = user;
		this.messageType = messageType;
	}
	
	/**
	 * 构造方法
	 * @param messageType
	 * @param userName
	 * @param email
	 * @param mobilePhone
	 */
	public GetCodeLog(final User user, final String messageType, final String receiveAddr){
		this();
		this.user = user;
		this.messageType = messageType;
		this.receiveAddr = receiveAddr;
	}

	/**
	 * 事件处理
	 */
	@Override
	public void doEvent() {
		// 邮件、短信、站内信
		sendMsg();
	}

	/**
	 * 发送短信
	 */
	@Override
	public void sendMsg() {
		if (StringUtils.isNotBlank(messageType)) {
	        //生成验证码
			final String code = VerifCodeForSMSUtils.buildVerifCode(messageType, receiveAddr);
			LOGGER.debug("messageType:{} --> receiveAddr:{} --> 验证码：{}" ,messageType, receiveAddr, code);
			
			final Map<String, Object> transferMap = Maps.newHashMap();
			transferMap.put("messageType", messageType);
			transferMap.put("receiveAddr", receiveAddr);
			transferMap.put("code", code);
			transferMap.put("user", user);
			messageService.sendMessage(messageType, transferMap);
		}
	}
	
	/**
	 * 发送短信
	 */
	public void sendMsg(String receiveAddr, String messageType, String route) {
		final String code = VerifCodeForSMSUtils.buildVerifCode(messageType, receiveAddr);
		LOGGER.debug("messageType {} --> receiveAddr:{} --> 验证码：{}" ,messageType, receiveAddr , code);
		messageService.sendMessage(messageType, receiveAddr, code, route);
	}

	/**
	 * 获得消息类型
	 * @return
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * 设置消息类型
	 * @param messageType
	 */
	public void setMessageType(final String messageType) {
		this.messageType = messageType;
	}

	/**
	 * 获得接收地址
	 * @return
	 */
	public String getReceiveAddr() {
		return receiveAddr;
	}

	/**
	 * 设置接收地址
	 * @author xhf
	 * @date 2016年8月30日
	 * @param receiveAddr
	 */
	public void setReceiveAddr(final String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	/**
	 * 获得发送用户
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置发送用户
	 * @param user
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * 初始化操作
	 */
	@Override
	public void initCode(final String messageType) {		
	}
}

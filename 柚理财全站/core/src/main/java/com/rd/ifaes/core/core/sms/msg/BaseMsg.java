package com.rd.ifaes.core.core.sms.msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.MessageService;

/**
 * 消息发送基础类
 * @version 3.0
 * @author xhf
 * @date 2016年8月30日
 */
public class BaseMsg implements MsgEvent, Serializable {
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseMsg.class);
	
	/** 序列化 */
	private static final long serialVersionUID = 1L;
	
	/** 发送通知service */
	protected transient MessageService messageService;
	
	/** 模块编码 */
	protected String messageType;
	
	/** 短信单发-参数 */
	private HashMap<String, Object> params;
	
	/** 短信群发-参数列表*/
	private ArrayList<HashMap<String, Object>> paramsList;

	/**
	 * 构造方法
	 */
	public BaseMsg() {
		messageService = (MessageService) SpringContextHolder.getBean("messageService");
	}

	/**
	 * 构造方法
	 * 
	 * @param nid 模块ID
	 */
	public BaseMsg(final String messageType, final HashMap<String, Object> params) {
		this();
		this.messageType = messageType;
		this.params = params;
	}
	
	/**
	 * 构造方法
	 * 
	 * @param nid 模块ID
	 */
	public BaseMsg(final String messageType, final ArrayList<HashMap<String, Object>>paramsList) {
		this();
		this.messageType = messageType;
		this.paramsList = paramsList;
	}
	
	/**
	 * 事件执行
	 */
	@Override
	public void doEvent() {
		// 邮件、短信、站内信
		sendMsg();
	}

	/**
	 * 发送消息
	 */
	@Override
	public void sendMsg() {
		if (StringUtils.isNotBlank(messageType)) { //单发
			if(params!=null && params.size()>0){
				sendMessage(params);
				
			} else if(!CollectionUtils.isEmpty(paramsList)){ //群发
				for (final Map<String, Object> map : paramsList) {
					sendMessage(map);
				}
			}
		}
	}
	
	/**
	 * 发送消息
	 * @param params
	 */
	private void sendMessage(final Map<String, Object> params){
		try {
			messageService.sendMessage(messageType, params);
		} catch (Exception e) {
				LOGGER.error("消息发送失败，消息类型：{},参数：{}",messageType,params.toString());
				LOGGER.error("错误信息：{}",e.getMessage(), e);
		}
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
	 * 获得消息参数
	 * @return
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * 设置消息参数
	 * @return
	 */
	public void setParams(final HashMap<String, Object> params) {
		this.params = params;
	}

	/**
	 * 获得消息列表
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getParamsList() {
		return paramsList;
	}

	/**
	 * 设置消息列表
	 * @param paramsList
	 */
	public void setParamsList(final ArrayList<HashMap<String, Object>> paramsList) {
		this.paramsList = paramsList;
	}

	/**
	 * 初始化处理
	 */
	@Override
	public void initCode(final String todo) {
	}

}

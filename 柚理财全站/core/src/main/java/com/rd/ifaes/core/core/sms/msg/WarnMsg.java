package com.rd.ifaes.core.core.sms.msg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;

/**
 * 发送预警消息
 * @version 3.0
 * @author xhf
 * @date 2016年8月30日
 */
public class WarnMsg implements MsgEvent, Serializable {
	
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WarnMsg.class);
	
	/** 序列化 */
	private static final long serialVersionUID = 1L;
	
	/** 发送通知service */
	@Resource
	private transient MessageService messageService;
	
	/** 短信类型service */
	@Resource
	private transient MessageTypeService messageTypeService;
	
	/** 模块编码 */
	protected String messageType;
	
	/** 短信单发-参数 */
	private Map<String, Object> params;

	/**
	 * 构造方法
	 */
	public WarnMsg() {
		messageService = (MessageService) SpringContextHolder.getBean("messageService");
		messageTypeService = (MessageTypeService) SpringContextHolder.getBean("messageTypeService");
	}

	/**
	 * 构造方法
	 * 
	 * @param nid 模块ID
	 */
	public WarnMsg(final String messageType, final Map<String, Object> params) {
		this();
		this.messageType = messageType;
		this.params = params;
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
		if (StringUtils.isNotBlank(messageType)) { 
			if(params!=null && params.size()>0){
				params.put("createTime", DateUtils.getNow());
				sendMessage();
			}
		}
	}
	
	/**
	 * 发送消息
	 * @param params
	 */
	private void sendMessage(){
		try {
			final String ReceiveAddr = ConfigUtils.getValue(ConfigConstant.ACCOUNT_WARN_CONTACTS);
			MessageType msgType = messageTypeService.find(messageType, MessageConstant.MESSAGE_SMS);
			if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
				Message msg = new Message();
				msg.setMessageType(messageType);
				msg.setCreateTime(DateUtils.getNow());
				msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
				msg.setSendUser(MessageConstant.USER_ID_ADMIN);
				msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), params));
				msg.setContent(FreemarkerUtil.renderTemplate(msgType.getMessageContent(), params));
				msg.setReceiveAddr(ReceiveAddr);
				//内容不为空
				if ( StringUtils.isNotBlank(msg.getReceiveAddr()) && StringUtils.isNotBlank(msg.getContent())) {
					if(ConfigUtils.isOpenMq()){
						//加入队列
						RabbitUtils.sendMessage(msg);
					}else{
						//不加入队列
						messageService.sendMessage(msg);
					}
				}
			}
			
		
			
		} catch (Exception e) {
			LOGGER.info("消息发送失败，消息类型：{},参数：{}",messageType,params.toString());
			LOGGER.info("错误信息：{}",e.getMessage(), e);
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
	 * 初始化处理
	 */
	@Override
	public void initCode(final String todo) {
	}

}

package com.rd.ifaes.core.sys.service;

import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Message;

/**
 * Service Interface:MessageService
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface MessageService extends BaseService<Message>{
	
	/**
	 * 发送消息，仅供队列调用
	 * @param message
	 */
	void sendMessage(Message message);
	
	/**
	 * 发送消息，对外接口调用
	 * @param messageType
	 * @param sendData
	 */
	void sendMessage(String messageType, Map<String, Object> sendData);
	
	/**
	 * 发送消息，对外接口调用
	 * @author xxb 2016-10-17
	 * @param messageType 业务点
	 * @param receiveAddr 手机号或邮箱
	 * @param code        动态验证码
	 * @param route       发送方式（请求控制驱动方式）
	 */
	void sendMessage(String messageType, String receiveAddr, String code, String route);
	
	/**
	 * 
	 * 获得最新消息发送时间
	 * @author xhf
	 * @date 2016年7月28日
	 * @param message
	 * @return
	 */
	Message getNewestSendTime(String messageType, String receiveAddr);
	
	/**
	 * 查询后台消息记录
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	Page<Message> findMessageList(Message model);
}
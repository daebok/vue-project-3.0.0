package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.mq.MqListener;
/**
 * 消息队列监听
 * @author lh
 * @version 3.0
 * @since 2016-7-15
 *
 */
@Component("messageListener")
public class MessageListener implements MqListener<Message> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
	@Autowired
	private MessageService messageService;
	
	@Override
	public void listen(Message t) {
		LOGGER.info("消息队列监听服务已开启");
		messageService.sendMessage(t);
	}

}

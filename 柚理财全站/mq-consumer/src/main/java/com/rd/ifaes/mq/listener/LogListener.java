package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqLogModel;
import com.rd.ifaes.mq.MqListener;

/**
 * 日志队列监听
 * @author lh
 * @version 3.0
 * @since 2016-7-15
 *
 */
@Component
public class LogListener implements MqListener<MqLogModel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogListener.class);

	@Override
	public void listen(MqLogModel t) {
		LOGGER.debug("logger ……{}",t.getLogTime());
	}

}

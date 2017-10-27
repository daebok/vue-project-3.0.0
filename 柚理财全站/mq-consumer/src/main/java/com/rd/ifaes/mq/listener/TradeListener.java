package com.rd.ifaes.mq.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.rabbit.model.MqTradeModel;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.mq.MqListener;

/**
 * 第三方交易 队列监听
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月4日
 */
@Component
public class TradeListener implements MqListener<MqTradeModel>{

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeListener.class);
	@Autowired
	private TppTradeService tppTradeService;
	/**
	 * 第三方业务处理
	 * 
	 * @author lh
	 * @date 2016年8月4日
	 * @param taskList
	 */
	@Override
	public void listen(MqTradeModel model) {
		LOGGER.info("第三方业务处理队列监听已开启");
		 LOGGER.debug(model.toString());
		tppTradeService.doTppTask(model.getTradeList());
	}

}

package com.rd.ifaes.mq.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.ifaes.core.account.domain.CashFeeMarkLog;
import com.rd.ifaes.core.account.service.CashFeeMarkLogService;
import com.rd.ifaes.mq.MqListener;

/**
 * 账户日志队列监听服务
 * 
 * @author zhangxj
 * @version 3.0
 * @since 2017-09-18
 */
@Component
public class CashFeeMarkLogListener implements MqListener<CashFeeMarkLog> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CashFeeMarkLogListener.class);
	
	@Resource
	private CashFeeMarkLogService cashFeeMarkLogService;

	@Override
	public void listen(CashFeeMarkLog model) {
		LOGGER.info("免提现手续费标记日志队列监听服务, model ="+model.toString());
		cashFeeMarkLogService.save(model);
	}
	

}

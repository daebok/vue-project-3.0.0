package com.rd.ifaes.mq.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rd.account.domain.AccountLog;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.mq.MqListener;

/**
 * 账户日志队列监听服务
 * 
 * @author lh
 * @version 3.0
 * @since 2017-2-13
 */
@Component
public class AccountLogListener implements MqListener<AccountLog> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccountLogListener.class);
	
	@Resource
	private AccountLogService accountLogService;

	@Override
	public void listen(AccountLog model) {
		LOGGER.info("账户日志队列监听服务, model ="+model.toString());
		accountLogService.save(model);
	}
	

}

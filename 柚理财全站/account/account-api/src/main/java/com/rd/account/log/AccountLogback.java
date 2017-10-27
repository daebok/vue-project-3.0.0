package com.rd.account.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>资金相关的日志采集</p>
 * 	不暴露接口，只供日志记录功能<br>
 * 
 * <p>使用：</p>
 * 	com.rd.account.log.AccountLog.ACCOUNT_LOGGER.info("xxx");
 * 
 * @author lihua
 * @version 3.0
 * @since 2016-7-28
 *
 */
public interface AccountLogback {
	
	final Logger LOGGER = LoggerFactory.getLogger(AccountLogback.class);

}

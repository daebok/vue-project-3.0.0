/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.job.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.core.core.sign.factory.BaseFactory;
import com.rd.ifaes.core.core.sign.factory.SignFactory;

/**
 * 网站监听器mq
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年10月20日
 */
public class WebContextListener  extends ContextLoaderListener{
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebContextListener.class);

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		 
		final StringBuilder strBuilder = new StringBuilder(500);
		strBuilder.append("\r\n======================================================================\r\n");
		strBuilder.append("\r\n    欢迎使用JOB-  "+PropertiesUtils.getValue("productName")+"     - Powered By http://erongdu.com \r\n");
		strBuilder.append("\r\n======================================================================\r\n");
		LOGGER.info(strBuilder.toString());
		
		return super.initWebApplicationContext(servletContext);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("协议签章启动");
		final BaseFactory factory = new SignFactory();
		factory.prepare();
	}
}

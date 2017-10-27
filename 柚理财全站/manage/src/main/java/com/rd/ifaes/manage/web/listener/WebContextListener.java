package com.rd.ifaes.manage.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.sign.factory.BaseFactory;
import com.rd.ifaes.core.core.sign.factory.SignFactory;
import com.rd.ifaes.core.sys.service.ConfigService;

public class WebContextListener extends ContextLoaderListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebContextListener.class);

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		 
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用  "+PropertiesUtils.getValue("productName")+"     - Powered By http://erongdu.com \r\n");
		sb.append("\r\n======================================================================\r\n");
		LOGGER.info(sb.toString());
				
		return super.initWebApplicationContext(servletContext);
	}
	
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//协议签章启动
		final BaseFactory factory = new SignFactory();
		factory.prepare();
		ServletContext context = event.getServletContext();
//		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		
		ConfigService configService = (ConfigService) SpringContextHolder.getBean("configService");
		context.setAttribute(ConfigConstant.IMAGE_SERVER_URL, configService.findByCode(ConfigConstant.IMAGE_SERVER_URL).getConfigValue());
		context.setAttribute(ConfigConstant.TPP_NAME, configService.findByCode(ConfigConstant.TPP_NAME).getConfigValue());
		context.setAttribute(ConfigConstant.OPEN_PRODUCT, configService.findByCode(ConfigConstant.OPEN_PRODUCT).getConfigValue());
//		
//		//------------------------------------------------------------
//        // 设置国际化多语言支持
//        //------------------------------------------------------------
//		MessageResource messageSource = ctx.getBean(MessageResource.class);
//		messageSource.reload();
	}
	
    
}

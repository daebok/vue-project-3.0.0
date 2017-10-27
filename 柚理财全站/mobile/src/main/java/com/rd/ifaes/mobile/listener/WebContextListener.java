package com.rd.ifaes.mobile.listener;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.JsonMapper;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.resource.MessageResource;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.sign.factory.BaseFactory;
import com.rd.ifaes.core.core.sign.factory.SignFactory;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.model.AreaModel;
import com.rd.ifaes.core.sys.service.AreaService;
import com.rd.ifaes.core.sys.service.LogTemplateService;

/**
 * 
 *  项目监听器，项目启动时调用
 * @version 3.0
 * @author xxb
 * @date 2016年8月10日
 */
public class WebContextListener extends ContextLoaderListener {
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebContextListener.class);

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		 
		final StringBuilder strBuilder = new StringBuilder(500);
		strBuilder.append("\r\n======================================================================\r\n");
		strBuilder.append("\r\n    欢迎使用  "+PropertiesUtils.getValue("productName")+"     - Powered By http://erongdu.com \r\n");
		strBuilder.append("\r\n======================================================================\r\n");
		LOGGER.info(strBuilder.toString());
		
		return super.initWebApplicationContext(servletContext);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		final ServletContext context = event.getServletContext();
		final ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		
		//资金账户、积分账户日志模板信息
		final LogTemplateService logTService = (LogTemplateService) ctx.getBean("logTemplateService");
		final Map<String, String> logTemplateMap = Maps.newHashMap();
		final List<LogTemplate> logTemplateList = logTService.findAll();
		for (int i = 0; i < logTemplateList.size(); i++) {
			LogTemplate logT = logTemplateList.get(i);
			if (logT != null) {
				String mapKey = logT.getLogType() + "_" + logT.getCode();
				logTemplateMap.put(mapKey, logT.getTemplateContent());
			}
		}
		CacheUtils.setMap(CacheConstant.KEY_LOG_TEMPLATE, logTemplateMap, ExpireTime.NONE);
		
		//资源数据重新加载
		final MessageResource messageResource = SpringContextHolder.getBean(MessageResource.class);
		if(messageResource!=null){
			messageResource.reload();
		}
		
		//协议签章启动
		final BaseFactory factory = new SignFactory();
		factory.prepare();
		
		//地区表
		final AreaService areaService = (AreaService)ctx.getBean("areaService");
		final List<AreaModel> provinceList = areaService.findByParentCode(Constant.AREA_CHINA);
		for (final AreaModel province : provinceList) { //省
			final List<AreaModel> cityList = areaService.findByParentCode(province.getValue());
			for (final AreaModel city : cityList) { //市
				 final List<AreaModel> areaModelList = areaService.findByParentCode(city.getValue());
				 city.setChildren(areaModelList);
			}
			province.setChildren(cityList);
		}
		final String areaJson = JsonMapper.toJsonString(provinceList);
		CacheUtils.set(CacheConstant.KEY_AREA_JSON, areaJson, ExpireTime.NONE);
	}
}

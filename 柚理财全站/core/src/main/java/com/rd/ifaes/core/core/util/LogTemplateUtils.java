package com.rd.ifaes.core.core.util;

import java.util.Map;

import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.LogTemplateService;

/**
 * 日志模板工具类
 * @author xhf
 * @version 3.0
 * @since 2016-8-11
 *
 */
public class LogTemplateUtils {

	private static LogTemplateService logTemplateService;
	
	/**
	 * 
	 * 获得资金日志模板内容
	 * @author xhf
	 * @date 2016年8月11日
	 * @param code
	 * @param param
	 * @return
	 */
	public static String getAccountTemplate(String code, Map<String, Object> param){
		if(logTemplateService == null){
			logTemplateService = (LogTemplateService)SpringContextHolder.getBean("logTemplateService");
		}
		return logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT, code, param);
	}
	
	/**
	 * 
	 * 获得积分日志模板内容
	 * @author xhf
	 * @date 2016年8月11日
	 * @param code
	 * @param param
	 * @return
	 */
	public static String getScoreTemplate(String code, Map<String, Object> param){
		if(logTemplateService == null){
			logTemplateService = (LogTemplateService)SpringContextHolder.getBean("logTemplateService");
		}
		return logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_SCORE, code, param);
	}
	
}

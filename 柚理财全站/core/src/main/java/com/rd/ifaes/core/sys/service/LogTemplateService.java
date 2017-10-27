package com.rd.ifaes.core.sys.service;

import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.LogTemplate;

/**
 * 日志模板接口
 * @author lh
 * @version 3.0
 * @date 2016-5-18
 */
public interface LogTemplateService extends BaseService<LogTemplate>{
	
	
	/**
	 *   获取模板内容,生成资金记账内容
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年8月10日
	 */
	String getTemplateContent(int logType, String code , Map<String, Object> param);
	
}
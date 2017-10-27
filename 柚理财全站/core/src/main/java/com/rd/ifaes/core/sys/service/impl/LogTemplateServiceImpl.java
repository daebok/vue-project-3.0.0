package com.rd.ifaes.core.sys.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.mapper.LogTemplateMapper;
import com.rd.ifaes.core.sys.service.LogTemplateService;

/**
 * 日志模板业务处理
 * @author xhf
 * @version 3.0
 * @date 2016-5-18
 */
@Service("logTemplateService") 
public class LogTemplateServiceImpl  extends BaseServiceImpl<LogTemplateMapper, LogTemplate> implements LogTemplateService{
	
	
	
	@Override
	@Transactional(readOnly = false)
	public void update(LogTemplate entity) {
		super.update(entity);
		LogTemplate logTemplate = get(entity.getUuid());
		final Map<String, String> logTemplateMap = Maps.newHashMap();
		logTemplateMap.put(logTemplate.getLogType() + "_" + logTemplate.getCode(), logTemplate.getTemplateContent());
		CacheUtils.addMap(CacheConstant.KEY_LOG_TEMPLATE, logTemplateMap);
	}

	/**
	 * 保存
	 */
    @Override
    @Transactional(readOnly = false)
    public void insert(final LogTemplate entity) {
    	entity.preInsert();
    	dao.insert(entity);
    	final Map<String, String> logTemplateMap = Maps.newHashMap();
		logTemplateMap.put(entity.getLogType() + "_" + entity.getCode(), entity.getTemplateContent());
		CacheUtils.addMap(CacheConstant.KEY_LOG_TEMPLATE, logTemplateMap);
    }
    
    /**
     * 获得日志模板
     */
	@Override
	public String getTemplateContent(final int logType, final String code, final Map<String, Object> param) {
		final Map<String, String> logTemplateMap = CacheUtils.mget(CacheConstant.KEY_LOG_TEMPLATE, String.class);
		String templateContent = null;
		String accountContent = null;
		if (logTemplateMap != null) {
			templateContent = logTemplateMap.get(logType + "_" + code);
		}
		if (StringUtils.isNotBlank(templateContent)) {
			accountContent = FreemarkerUtil.renderTemplate(templateContent, param);
		}
		return accountContent;
	}
	
	
}
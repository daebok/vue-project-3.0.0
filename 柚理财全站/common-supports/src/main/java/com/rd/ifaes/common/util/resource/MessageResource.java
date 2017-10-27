package com.rd.ifaes.common.util.resource;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.Resource;
import com.rd.ifaes.core.sys.service.ResourceService;


/**
 * 取得资源数据
 * @author Robin
 *
 */
@Component
public class MessageResource extends AbstractMessageSource implements ResourceLoaderAware {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageResource.class);

    @SuppressWarnings("unused")
    private ResourceLoader resourceLoader;
    
    @Autowired
    private ResourceService resourceService;
    
    /**
     * Map切分字符
     */
    public static final String MAP_SPLIT_CODE = "|";
    
    public static final String DB_SPLIT_CODE = "_";
    
    public MessageResource() {
    	ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
    	rbms.setBasenames(new String[]{"ValidationMessages"});
    	this.setParentMessageSource(rbms);    	
    }
    
    public void reload() {
    	CacheUtils.setMap(CacheConstant.KEY_RESOURCE, loadTexts(), ExpireTime.NONE);
    }

    protected Map<String, String> loadTexts() {
        Map<String, String> mapResource = new HashMap<String, String>();
        List<com.rd.ifaes.core.sys.domain.Resource> resources = resourceService.findAll();
        for (com.rd.ifaes.core.sys.domain.Resource item : resources) {
			String code = item.getResName() + MAP_SPLIT_CODE + item.getResLanguage();
            mapResource.put(code, item.getResText());
        }
        return mapResource;
    }
    
    /**
     * 重新载入资源
     * @param name	名称
     * @param language	语言
     * @param text	显示文本
     */
    public void put(String name, String language, String text){
    	String code = name + MAP_SPLIT_CODE + language;
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(code, text);
    	CacheUtils.addMap(CacheConstant.KEY_RESOURCE, map);
    }
        
    private String getText(String code, Locale locale) {
        String localeCode = locale.getLanguage() + DB_SPLIT_CODE + locale.getCountry();
        String key = code + MAP_SPLIT_CODE + localeCode;
        String localeText = CacheUtils.getMapField(CacheConstant.KEY_RESOURCE, key, String.class);
        String resourceText = code;
        
        if(localeText != null) {
            resourceText = localeText;
        } else {
        	Resource res = resourceService.getByName(code, localeCode);
			if (res != null) {
				resourceText = res.getResText();
				put(code, localeCode, resourceText);
			} else {
				LOGGER.warn("Cannot find message with code: {}", code);
			}
        }
        return resourceText;
    }
    
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = getText(code, locale);
        MessageFormat result = createMessageFormat(msg, locale);
        return result;
    }
    
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result = getText(code, locale);
        return result;
    }
    

}
package com.rd.ifaes.shiro.freemarker;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;
/**
 * 自定义一个ShiroTagFreeMarkerConfigurer继承Spring本身提供的FreeMarkerConfigurer
 * 在FreeMarker的Configuration中添加shiro的配置
 * @author lh
 * @version 3.0
 * @since 2016-05-11
 */
public class ShiroTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
	
	@Override
	public void afterPropertiesSet() throws IOException, TemplateException {
		super.afterPropertiesSet();
		this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}

}

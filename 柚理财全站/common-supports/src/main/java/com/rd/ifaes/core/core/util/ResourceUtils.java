package com.rd.ifaes.core.core.util;

import java.util.Locale;

import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.MessageResource;
import com.rd.ifaes.core.core.constant.ConfigConstant;

/**
 * 国际化资源工具类
 * @author lh
 * @version 3.0
 * @since 2016-7-28
 *
 */
public class ResourceUtils {

	private static MessageResource messageResource = SpringContextHolder.getBean(MessageResource.class);
	private static final String DEFAULT_LOCALE = "zh_CN";
	
	private static String getDefaultLocaleCode() {
		String locale = ConfigUtils.getValue(ConfigConstant.MESSAGE_RESOURCE_DEFAULT_LANGUAGE);
		return StringUtils.isBlank(locale) ? DEFAULT_LOCALE : locale;
	}
	
	private static Locale getDefaultLocale(){
		String[] lan = getDefaultLocaleCode().split("_");
		return new Locale(lan[0], lan[1]);
	}
	/**
	 * 获取资源信息
	 * 
	 * @param name
	 *            名称
	 * @return
	 */
	public static String get(String name) {
		return messageResource.getMessage(name, null, getDefaultLocale());
	}
	
	
	 /**
	  *  获取资源信息
	  * @author  FangJun
	  * @date 2016年7月28日
	  * @param code 资源标识
	  * @param args  对应占位符具体内容
	  * @return 替换占位符后的资源信息
	  */
	public static String get(String code,Object... args) {
		return messageResource.getMessage(code, args, getDefaultLocale());
	}

	/**
	 * 设置资源信息
	 * 
	 * @param name
	 *            名称
	 * @param language
	 *            国际化语言
	 * @param text
	 *            显示值
	 */
	public static void put(String name, String language, String text) {
		messageResource.put(name, language, text);
	}

	/**
	 * 设置资源信息
	 * 
	 * @param name
	 *            名称
	 * @param text
	 *            显示值
	 */
	public static void put(String name, String text) {
		messageResource.put(name, getDefaultLocaleCode(), text);
	}

}

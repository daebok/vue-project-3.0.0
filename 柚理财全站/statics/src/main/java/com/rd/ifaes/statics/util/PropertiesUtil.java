package com.rd.ifaes.statics.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 工具类-文件上传路径以及文件模块名称
 * 
 * @author：zyz
 * @version 1.0
 * @since 2014年3月19日
 */
public final class PropertiesUtil {

	/** 日志 */
	private static final Logger LOG = Logger.getLogger(PropertiesUtil.class);

	/** 资源属性 */
	private static Properties properties;

	/**
	 * 私有构造方法
	 */
	private PropertiesUtil() {
	}

	static {
		properties = new Properties();
		try {
			// 读取配置文件
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("uploadUrl.properties"));
		} catch (IOException e) {
			LOG.error("读取配置文件出错，请确认uploadUrl.properties文件已经放到src目录下。");
		}
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key 键
	 * @return 配置信息
	 */
	public static String getMessage(String key) {
		String value = null;
		value = properties.getProperty(key);
		if (null == value || "".equals(value)) {
			LOG.info("没有获取指定key的值，请确认资源文件中是否存在【" + key + "】");
		}
		return value;
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key 键
	 * @param param 参数
	 * @return 配置信息
	 */
	public static String getMessage(String key, Object[] param) {
		String value = getMessage(key);
		return MessageFormat.format(value, param);
	}

}

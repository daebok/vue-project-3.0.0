package com.rd.ifaes.mobile.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yusj
 * 2016年2月24日 下午2:28:06
 */
public final class InterfaceVersionPropertyUtil {

	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InterfaceVersionPropertyUtil.class);

	/** 资源属性 */
	private static Properties properties;

	/**
	 * 私有构造方法
	 */
	private InterfaceVersionPropertyUtil() {
	}

	static {
		properties = new Properties();
		try {
			// 读取配置文件
			properties.load(InterfaceVersionPropertyUtil.class.getClassLoader().getResourceAsStream("interface_version.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("读取配置文件出错，请确认interface_version.properties文件已经放到src目录下。");
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
			LOGGER.info("没有获取指定key的值，请确认资源文件中是否存在【" + key + "】");
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

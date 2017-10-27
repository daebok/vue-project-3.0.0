package com.rd.ifaes.mobile.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取工具类
 * 
 * @author Kunkka
 *
 */
public class AppProperties {
	private static AppProperties inst = null;

	private AppProperties() throws IOException {
	}

	private static synchronized AppProperties getInstance() throws IOException {
		if (inst == null) {
			inst = new AppProperties();
		}
		return inst;
	}

	private Properties getPropertyFile(String filePath) throws IOException {
		InputStream in = AppProperties.class.getClassLoader()
				.getResourceAsStream(filePath);
		Properties p = new Properties();
		p.load(in);
		return p;
	}

	public static String getProperty(String filePath, String key) {
		AppProperties config = null;
		String value = null;
		try {
			config = getInstance();
			value = config.getPropertyFile(filePath).getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}

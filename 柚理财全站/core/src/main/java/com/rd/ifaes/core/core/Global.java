package com.rd.ifaes.core.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.sys.domain.LogTemplate;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {
	
	private Global() {
	}
	
	
	public static final ThreadLocal<String> IP_THREADLOCAL = new ThreadLocal<>();
	
    //接口回调处理结果
	private static final Map<String, String> RESULT_MAP=Collections.synchronizedMap(new HashMap<String,String>());
	private static final Map<String, LogTemplate> LOG_TEMPLATE_MAP = new HashMap<>();

	public static LogTemplate getLogTempLate(int logType, String code) {
		if (logType <= 0 || StringUtils.isBlank(code)) {
			return null;
		}
		return Global.LOG_TEMPLATE_MAP.get(logType + "_" + code);
	}
	
	public static String getLogTempValue(int logType, String code) {
		final LogTemplate temp = getLogTempLate(logType, code);
		return temp == null? null:temp.getTemplateContent();
	}

	public static String getIP() {
		final Object retObj = Global.IP_THREADLOCAL.get();
		return retObj == null ? "" : retObj.toString();
	}
	
	public static void setResultMap(String key, String value){
		RESULT_MAP.put(key, value);
	}
	
	public static void delResultMap(String key){
		RESULT_MAP.remove(key);
	}

}

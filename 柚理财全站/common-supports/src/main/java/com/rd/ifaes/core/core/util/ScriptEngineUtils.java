package com.rd.ifaes.core.core.util;

import java.util.Iterator;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

/**
 * 脚本引擎工具类
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月15日
 */
public class ScriptEngineUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngineUtils.class); 

	/**
	 * Executes the specified script
	 * @author lh
	 * @date 2016年8月15日
	 * @param expression	表达式
	 * @param params	参数
	 * @return	执行结果
	 */
	public static Object eval(String expression, Map<String, Object> params) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			engine.put(key, params.get(key));
		}
		Object result = null;
		try {
			result = engine.eval(HtmlUtils.htmlUnescape(expression));
		} catch (ScriptException e) {
			LOGGER.warn(e.getMessage(), e);
		}
		return result;
	}


	/**
	 * 判断是否匹配
	 * @author lh
	 * @date 2016年8月15日
	 * @param expression
	 * @param n
	 * @return
	 */
	public static boolean match(String expression, Object n) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		engine.put("n", n);
		Object result = null;
		try {
			result = engine.eval(HtmlUtils.htmlUnescape(expression));
		} catch (ScriptException e) {
			LOGGER.warn(e.getMessage(), e);
		}
		return result==null?false:result.equals(Boolean.TRUE);
	}
	
}

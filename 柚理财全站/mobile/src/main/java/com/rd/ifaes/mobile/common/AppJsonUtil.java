package com.rd.ifaes.mobile.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * json工具类
 * 
 * @author Kunkka
 *
 */
public class AppJsonUtil {
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AppJsonUtil.class);

	private static ValueFilter filter = new ValueFilter() {
	    @Override
	    public Object process(Object obj, String s, Object v) {
	    if(v==null)
	        return "";
	    return v;
	    }
	};
	
	public static void outputJson(String res_code, String res_msg) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(AppResponseParams.RES_CODE, res_code);
		map.put(AppResponseParams.RES_MSG, res_msg);
		map.put(AppResponseParams.RES_DATA, "");
		printJson(JSON.toJSONString(map,filter));
	}

	public static void printJson(String str) {
		HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		print(response, str);
	}

	public static void print(HttpServletResponse response, String str) {
		PrintWriter write = null;
		try {
			write = response.getWriter();
			LOGGER.info(str);
			write.print(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (write != null)
				write.close();
		}
	}

	public static String toJSONString(String key, Object object) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(key, object);

		return JSON.toJSONString(map,filter);
	}

	public static String toJSONMap(Map<String, Object> map) {
		return JSON.toJSONString(map,filter);
	}

	public static void print(String str) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter write = null;
		try {
			write = response.getWriter();
			write.print(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (write != null)
				write.close();
		}
	}
}

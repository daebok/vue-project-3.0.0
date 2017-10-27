package com.rd.ifaes.mobile.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

public class JsonPrinter {
	
	private Logger logger;
	
	private static ValueFilter filter = new ValueFilter() {
	    @Override
	    public Object process(Object obj, String s, Object v) {
			if (v == null)
				return "";
			else if (v instanceof java.sql.Date) {
				return ((java.sql.Date) v).getTime();
			} else if (v instanceof java.util.Date) {
					return ((java.util.Date) v).getTime();
			} else if (v instanceof Timestamp) {
				return ((Timestamp) v).getTime();
			}

			return v;
	    }
	};
	
	public JsonPrinter( Logger logger ) {
		this.logger = logger;
	}

	public void printBeanJson(Object obj,HttpServletResponse response) {
		printJson(JSONObject.toJSONString(obj,filter),response);
	}

	public void printJson(String str,HttpServletResponse response) {
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		print(response, str);
	}

	public void print(HttpServletResponse response, String str) {
		PrintWriter write = null;
		try {
			write = response.getWriter();
			write.print(str);
			logger.info( str );
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (write != null)
				write.close();
		}
	}
}

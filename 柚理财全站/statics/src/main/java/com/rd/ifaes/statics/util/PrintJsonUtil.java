package com.rd.ifaes.statics.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class PrintJsonUtil {
	
	public static void printSuccessJson(Object obj, String code, String msg) throws IOException {
		if(code == null){ code = "39321"; }
		if(msg == null){ msg = "SUCCESS"; }
		Map<String, Object> map = new HashMap<>();
		map.put("resCode", code);
		map.put("resMsg", msg);
		map.put("resData", obj);
		printJson(buildJSON(map));
	}
	
	public static String buildJSON(Map<String, Object> params)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if ((param.getKey() != null) && (!"".equals(param.getKey()))) {
				jsonObject.put((String) param.getKey(),
						param.getValue() == null ? "" : param.getValue());
			}
		}
		return jsonObject.toString();
	}
	
	public static void printJson(String str) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("text/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		print(response, str);
	}
	
	public static void print(HttpServletResponse response, String str) {
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

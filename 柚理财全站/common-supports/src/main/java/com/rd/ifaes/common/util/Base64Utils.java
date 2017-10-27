package com.rd.ifaes.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.code.BASE64Decoder;
import com.rd.ifaes.common.util.code.BASE64Encoder;


public class Base64Utils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);
	
	private Base64Utils() {	}
	/**
	 * 字符串Base64位加密
	 * @param str
	 * @return
	 */
	public static String base64Encode(String str) {
		BASE64Encoder encoder = new BASE64Encoder();
		String result = encoder.encode(str.getBytes());
		return result;
	}
	
	/**
	 * 字符串Base64位解密
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String base64Decode(String str) {
		try {
			if (StringUtils.isNotBlank(str)) {
				BASE64Decoder decoder = new BASE64Decoder();
				String result = decoder.decodeStr(str);
				return result;
			} else {
				return null;
			}
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
		}
		return "";
	}
}

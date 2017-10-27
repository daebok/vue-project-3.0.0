package com.rd.ifaes.mobile.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Kunkka
 *
 */
public class AppDateUtil {

	/**
	 * 获取时间戳 单位秒
	 * 
	 * @param d
	 * @return
	 */
	public static String getTimes(Date d) {
		if (d == null) {
			return "0";
		} else {
			return String.valueOf(d.getTime() / 1000);
		}
	}

	public static String getYYYYMMDDHHMMSSdate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}

	public static String getYYYYMMDDdate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}
}

package com.rd.ifaes.core.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;

/**
 * JAVA版本的自动生成有规则的订单号(或编号) 生成的格式是: 200908010001 前面几位为当前的日期,后面五位为系统自增长类型的编号 原理: 1.获取当前日期格式化值; 2.读取文件,上次编号的值+1最为当前此次编号的值
 * (新的一天会重新从1开始编号)
 */

public class OrderNoUtils {
	private OrderNoUtils() {}

	/**
	 * 产生唯一 的序列号。
	 * 
	 * @return String
	 */
	public static String getSerialNumber() {
		int hashCode = UUID.randomUUID().toString().hashCode();
		if (hashCode < 0) {
			hashCode = -hashCode;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(DateUtils.getNow()).substring(2, 8) + String.format("%010d", hashCode);
	}
	
	/**
	 * 产生批量序列号
	 * @param count
	 * @return
	 */
	public static List<String> getBatchSerialNumber(String batchCount) {
		int count = NumberUtils.getInt(batchCount);
		List<String> orderNoList = new ArrayList<String>();
		if (count <= 1) {
			orderNoList.add(getSerialNumber());
			return orderNoList;
		}
		
		for (int i = 0; i < count; i++) {
			orderNoList.add(getSerialNumber());
		}
		
		return orderNoList;
	}
	
	/**
     * 获取随机数字
     * @param len
     * @return
     */
	public static String getRandomStr(int len) {
		double d1 = Math.pow(10, len - 1);
		double d2 = d1 * Math.random() / 2;
		long n = new Double(d1 + d2).longValue();
		return String.valueOf(n);
	}
}

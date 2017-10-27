package com.rd.ifaes.mobile.common;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * 数据转化工具类
 * 
 * @author Kunkka
 *
 */
public class AppDataUtil {
	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			return "md5 加密异常";
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	/**
	 * 保留两位小数（四舍五入）
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatNum2(double num) {
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(num);
	}

	/**
	 * 保留1位小数（四舍五入）
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatNum1(double num) {
		DecimalFormat format = new DecimalFormat("0.0");
		return format.format(num);
	}

	/**
	 * 保留两位小数（截取）
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatNum2Floor(double num) {
		DecimalFormat format = new DecimalFormat("0.00");
		format.setRoundingMode(RoundingMode.FLOOR);
		return format.format(num);
	}

	/**
	 * 保留1位小数（截取）
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatNum1Floor(double num) {
		DecimalFormat format = new DecimalFormat("0.0");
		format.setRoundingMode(RoundingMode.FLOOR);
		return format.format(num);
	}

	/**
	 * 去除小数部分（截取）
	 * 
	 * @param num
	 * @return
	 */
	public static String getFormatNum0Floor(double num) {
		DecimalFormat format = new DecimalFormat("0");
		format.setRoundingMode(RoundingMode.FLOOR);
		return format.format(num);
	}
	/**
     * 数值格式化 - 12,345.00
     */
    public static String doubleFormat(Object args) {
        if (args != null && !"".equals(args.toString())) {
            String number = args.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (Double.valueOf(number) < 1) {
                    df.applyPattern("0.00");
                } else {
                    df.applyPattern("##,###,###,###.00");
                }
                return df.format(Double.valueOf(number));
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0.00";
        }
    }
}

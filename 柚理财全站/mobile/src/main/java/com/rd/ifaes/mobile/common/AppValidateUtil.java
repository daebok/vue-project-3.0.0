package com.rd.ifaes.mobile.common;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rd.ifaes.common.util.StringUtils;
public class AppValidateUtil {

	/**
	 * 校验字符串是否为空不为空返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkStrNull(String str) {
		if (str != null && str.length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 校验集合是否为空 不为空返回true
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean checkCollectionNull(Collection c) {
		if (c != null && c.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 校验是否为手机号
	 */
	public static boolean isPhone(String phone) {
		String phoneFormat = "^((13[\\d])|(15[0-35-9])|(18[\\d])|(147)|(17[034678]))\\d{8}$";
		Pattern p = Pattern.compile(phoneFormat);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 校验是否为邮箱
	 */
	public static boolean isEmail(String email) {
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(StringUtils.isNull(email));
		return matcher.matches();
	}
	
	/**
	 * 密码格式校验
	 * 
	 * @param pwd
	 * @return
	 */
	public static String isPwd(String pwd) {
		if (pwd.length() < 8 || pwd.length() > 16) {
			return "密码长度必须是8-16位！";
		}
		for( int i = 0 ; i < pwd.length() ; ++i ) {
			if( "';<>\"&%\\*^+|$ ".indexOf( pwd.charAt(i) ) >= 0 )
				return "密码不能包含空格或';<>\"&%\\*^+|$";
		}
		boolean b1 = Pattern.compile("[0-9]").matcher(pwd).find();
		boolean b2 = Pattern.compile("(?i)[a-zA-Z]").matcher(pwd).find();
		boolean b3 = Pattern.compile("^[0-9A-Za-z]{8,16}$").matcher(pwd).matches();
		if (b1 && b2 && b3) {
			return "";
		} else {
			return "密码必须是数字+字母组成";
		}
	}
}

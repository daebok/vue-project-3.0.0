package com.rd.ifaes.mobile.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 一些数据池，以及包括appkey之类的一些数据
 * 
 * @author Kunkka
 *
 */
public class AppCommons {
	public static final Map<String, String> APPKEY_MAP = new HashMap<String, String>();

	public static final String APPKEY = AppProperties.getProperty(
			"app_common.properties", "appkey");

	public static final String APPSECRET = AppProperties.getProperty(
			"app_common.properties", "appsecret");

	public static final String TOKEN_EFFECTIVE_TIME = AppProperties
			.getProperty("app_common.properties", "token_effective_time");

	public static final String APP_IOS_VERSION = AppProperties
			.getProperty("app_common.properties", "app_ios_version");
	
	public static final String APP_ANDROID_VERSION = AppProperties
			.getProperty("app_common.properties", "app_android_version");

	public static final Integer APP_REPAYMENT_TIME_PEROID = Integer.parseInt( AppProperties
			.getProperty("app_common.properties", "repayment_time_peroid").trim() );
	
	static {
		APPKEY_MAP.put(APPKEY, APPSECRET);
	}
	
	/**
	 * 移动端注册短信号码缓存
	 */
    public static final String KEY_APP_PHONECODE_PHONENUM = "app_phoneCode:phoneNum:";
}

package com.rd.ifaes.core.sys.constant;

import com.rd.ifaes.core.core.constant.ConfigConstant;

/**
 * context刷新常量类
 * @author lh
 *
 */
public class RefreshContextConstant {
	
	/**
	 * context刷新地址
	 */
	public static final String REFRESH_CONTEXT_URL = "/index/refreshContext.html";
	
	/**
	 * 需刷新的key前缀
	 */
	public static String[] REFRESH_KEY_PREFIX = {"web_", 
			ConfigConstant.IMAGE_SERVER_URL, 
			ConfigConstant.CUSTOMER_HOTLINE,
			ConfigConstant.TPP_NAME,
			ConfigConstant.OPEN_PRODUCT,
			ConfigConstant.TASK_CENTER_ONLINE};
	
	
}

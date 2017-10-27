package com.rd.ifaes.core.core.sign.factory;

import java.util.Map;

/**
 * 
 *  工厂接口类
 * @version 3.0
 * @author jxx
 * @date 2016年8月1日
 */
public interface BaseFactory {
	
	/**
	 * 
	 * 初始化
	 * @author jxx
	 * @date 2016年8月1日
	 */
	void prepare();
	
	/**
	 * 
	 * 开户
	 * @author jxx
	 * @date 2016年8月1日
	 * @param map
	 * @return
	 */
	String register(Map<String,Object> map);
	
	/**
	 * 
	 * 根据模板获取签章
	 * @author jxx
	 * @date 2016年8月1日
	 * @param map
	 * @return
	 */
	String createSeal(Map<String,Object> map);
	
	/**
	 * 
	 * 画图获取签章
	 * @author jxx
	 * @date 2016年8月1日
	 * @param map
	 * @return
	 */
	String drawSeal(Map<String,Object> map);
	
	/**
	 * 
	 * 签署
	 * @author jxx
	 * @date 2016年8月1日
	 * @param map
	 */
	void execute(Map<String,Object> map);
	
	/**
	 * 
	 * 发送短信
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String tokenSendCode(Map<String,Object> map);
	
	/**
	 * 
	 * 短信验证
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String tokenCheckCode(Map<String,Object> map);
}

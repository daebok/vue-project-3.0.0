package com.rd.ifaes.core.core.sms;

import java.util.Map;

/**
 * 短信通道
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
public interface SmsPortal {

	public String getSPName();

	/**
	 * 发送消息
	 * 
	 * @param phone 手机号码
	 * @param content 短信内容
	 * @return 发送结果
	 */
	public String send(String phone, String content);
	
	/**
	 * 通过返回码，得到返回消息
	 */

	String getReturnMessage(String code);
	/**
	 * 发送消息
	 * 
	 * @param phone 手机号码
	 * @param content 短信内容
	 * @param templateCode 模板名称
	 * @return 发送结果
	 */
	public String send(String phone, String content, String templateCode);

	public Map<String, Integer> getUseInfo();
}

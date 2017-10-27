package com.rd.ifaes.mq.exception;

import com.rd.ifaes.common.exception.BussinessException;

/**
 * 消息队列处理异常类
 * @author lh
 * @version 3.0
 *
 */
public class MqException extends BussinessException {

	private static final long serialVersionUID = 1L;

	/**
	 * 无参的构造函数
	 */
	public MqException() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param message 异常消息
	 */
	public MqException(String message) {
		super(message);
	}
	
	/**
	 * 构造函数
	 * @param message	异常消息
	 * @param url		跳转地址
	 */
	public MqException(String message, String url) {
		super(message, url);
	}
	
	/**
	 * 构造函数
	 * @param message	异常消息
	 * @param url		跳转地址
	 * @param button	按钮
	 */
	public MqException(String message, String url,String button) {
		super(message, url , button);
	}
	

	/**
	 * 构造函数
	 * @param message	异常消息
	 * @param type		类型
	 */
	public MqException(String message, int type) {
		super(message, type);
	}
}

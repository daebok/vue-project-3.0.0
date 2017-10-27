package com.rd.ifaes.common.exception;

/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 *  令牌校验异常
 * @version 3.0
 * @author FangJun
 * @date 2016年8月3日
 */
public class TokenException extends BussinessException {
 
	private static final long serialVersionUID = -819278916160073746L;
	/**
	 * 默认构造方法
	 */
	public TokenException() {
		super();
	}
	/**
	 * 含错误信息的构造方法
	 * 
	 * @param message 错误信息
	 * @param type 指定返回错误展示方式
	 */
	public TokenException(String message, int type) {
		super(message, type);
	}

	
}

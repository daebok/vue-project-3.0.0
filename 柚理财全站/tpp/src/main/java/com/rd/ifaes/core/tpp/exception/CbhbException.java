/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.exception;

/**
 * Cbhb 异常类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月22日
 */
@SuppressWarnings("serial")
public class CbhbException extends TppException{
	
	public CbhbException() {
		super();
	}

	public CbhbException(String message) {
		super(message);
	}
	
	public CbhbException(String message, String url) {
		super(message, url);
	}
	
	public CbhbException(String message, String url,String button) {
		super(message, url , button);
	}
	

	public CbhbException(String message, int type) {
		super(message, type);
	}
}

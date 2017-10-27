package com.rd.ifaes.common.exception;

/**
 * 
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 * 系统异常：系统初始化、组件等错误（jdbc,log,公用组件等）
 * 
 * @author FangJun
 * @date 2016年7月25日
 */
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = -6412848421126927289L;

	/**
	 * 默认构造方法
	 */
	public SystemException() {
		super();
	}

	/**
	 * 含错误信息、异常堆栈信息的构造方法
	 */
	public SystemException(final String message,final  Throwable cause) {
		super(message, cause);
	}

	/**
	 * 含错误信息的构造方法
	 */
	public SystemException(final String message) {
		super(message);
	}

	/**
	 * 含异常堆栈信息的构造方法
	 */
	public SystemException(final Throwable cause) {
		super(cause);
	}

}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.exception;

import com.rd.ifaes.common.exception.BussinessException;

/**
 * 产品业务，功能模块涉及异常,抽象对象
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年7月29日
 */
public class ProjectException extends BussinessException {

	private static final long serialVersionUID = -6970117439604148142L;

	/** 默认构造方法 */
	public ProjectException() {
		super();
	}

	/**
	 * 含错误信息的构造方法
	 * 
	 * @param msg 错误信息
	 */
	public ProjectException(String message) {
		super(message);
	}

	/**
	 * 含错误信息、返回地址的构造方法
	 * 
	 * @param msg 错误信息
	 * @param url 返回地址
	 */
	public ProjectException(String message, String url) {
		super(message, url);
	}

	/**
	 * 含错误信息、返回地址的构造方法
	 * 
	 * @param msg 错误信息
	 * @param url 返回地址
	 * @param buttonName 提示按钮名称
	 */
	public ProjectException(String message, String url, String button) {
		super(message, url, button);
	}

	/**
	 * 含错误信息的构造方法
	 * 
	 * @param msg 错误信息
	 * @param type 指定返回错误展示方式
	 */
	public ProjectException(String message, int type) {
		super(message, type);
	}

	/**
	 * @param message 提示信息
	 * @param url 错误跳转url
	 * @param type 错误提示类型
	 */
	public ProjectException(String message, String url, int type) {
		super(message, url, type);
	}

}
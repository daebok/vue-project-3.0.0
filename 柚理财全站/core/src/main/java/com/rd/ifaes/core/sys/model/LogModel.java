package com.rd.ifaes.core.sys.model;

import com.rd.ifaes.core.sys.domain.Log;

public class LogModel extends Log {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 日志类型（1：接入日志） */
	public static final String TYPE_ACCESS = "1";
	/** 日志类型（2：错误日志） */
	public static final String TYPE_EXCEPTION = "2";
	/** 日志类型（3：操作日志） */
	public static final String TYPE_OPERATOR = "3";
	
	
}

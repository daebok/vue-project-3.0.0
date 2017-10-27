/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 * 协议类资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年10月19日
 */
public class ProtocolResource {
	
	/**
	 * 协议不存在
	 */
	public static final String PROTOCL_NOT_EXISTS = "protocol.message.isnot.exists";
	/**
	 * 同一类型的债权/变现有效协议只能有一个,故不能启用
	 */
	public static final String PROTOCOL_STATUS_VALID_ONLY_ONE = "protocol.message.status_valid.only.one";
	/**
	 * 有效的协议剩余一个,不能禁用
	 */
	public static final String PROTOCOL_STATUS_VALID_REMAIN_ONE = "protocol.message.status_valid.remain.one";
}

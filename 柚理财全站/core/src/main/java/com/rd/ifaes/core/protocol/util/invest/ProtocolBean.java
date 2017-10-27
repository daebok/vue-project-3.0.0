package com.rd.ifaes.core.protocol.util.invest;

import com.rd.ifaes.core.protocol.domain.Protocol;



/**
 * 处理协议
 * 
 * @author qj
 * @version 2.0
 * @since 2014-05-22
 */
public interface ProtocolBean {

	/**
	 * 业务核心处理方法
	 * @param projectId 借款ID
	 * @param protocol 协议实体
	 * @param userId 用户ID
	 */
	void executer(String projectId, Protocol protocol, String userId);

	/**
	 * 业务核心处理方法
	 * @param projectId 借款ID
	 * @param tenderId 投资ID
	 * @param protocol 协议实体
	 * @param userId 用户ID
	 */
	void executer(String projectId, String tenderId, Protocol protocol, String userId);

	/**
	 * 逻辑执行之前的准备工作
	 */
	void validDownload();

	/**
	 * 逻辑执行之前的准备工作
	 */
	void prepare();

	/**
	 * 初始化协议需要参数
	 */
	void initData();

	/**
	 * 创建协议pdf
	 */
	void createPdf();

}

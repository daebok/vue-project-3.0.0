/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.protocol.util.bond;

import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.util.invest.ProtocolBean;

/**
 * 债权协议接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月29日
 */
public interface BondProtocolBean extends ProtocolBean {
	/**
	 * 业务核心处理方法
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param bondId
	 * @param bondProtocol
	 * @param userId
	 */
	void executer(String bondId, Protocol bondProtocol, String userId);

	/**
	 * 业务核心处理方法
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param bondId
	 * @param bondInvestId
	 * @param bondProtocol
	 * @param userId
	 */
	void executer(String bondId, String bondInvestId, Protocol bondProtocol, String userId);
	
}

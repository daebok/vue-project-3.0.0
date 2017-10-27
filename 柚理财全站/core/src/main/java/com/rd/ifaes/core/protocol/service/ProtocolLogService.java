package com.rd.ifaes.core.protocol.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.protocol.domain.ProtocolLog;

public interface ProtocolLogService extends BaseService<ProtocolLog> {

	/**
	 * 
	 * 后台协议列表
	 * @author jxx
	 * @date 2016年7月28日
	 * @param entity
	 * @return
	 */
	Page<ProtocolLog> findManagePage(ProtocolLog entity);
}

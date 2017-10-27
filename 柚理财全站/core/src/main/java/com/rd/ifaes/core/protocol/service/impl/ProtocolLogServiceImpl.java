package com.rd.ifaes.core.protocol.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.protocol.domain.ProtocolLog;
import com.rd.ifaes.core.protocol.mapper.ProtocolLogMapper;
import com.rd.ifaes.core.protocol.service.ProtocolLogService;

@Service("protocolLogService")
public class ProtocolLogServiceImpl extends BaseServiceImpl<ProtocolLogMapper, ProtocolLog> implements ProtocolLogService {

	/**
	 * 
	 * 后台协议列表
	 * @author jxx
	 * @date 2016年7月28日
	 * @param entity
	 * @return
	 */
	@Override
	public Page<ProtocolLog> findManagePage(ProtocolLog entity) {
		Page<ProtocolLog> page = entity.getPage();
		page.setRows(dao.findManageList(entity));
		return page;
	}

	
}

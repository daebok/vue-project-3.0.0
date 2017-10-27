package com.rd.ifaes.core.protocol.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.protocol.domain.ProtocolLog;

/**
 * 
 *  协议接口
 * @version 3.0
 * @author jxx
 * @date 2016年7月28日
 */
public interface ProtocolLogMapper extends BaseMapper<ProtocolLog> {
	
	/**
	 * 
	 * 后台协议列表
	 * @author jxx
	 * @date 2016年7月28日
	 * @param model
	 * @return
	 */
	List<ProtocolLog> findManageList(ProtocolLog model);
}

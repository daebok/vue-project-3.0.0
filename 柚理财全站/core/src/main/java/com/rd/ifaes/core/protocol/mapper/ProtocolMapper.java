package com.rd.ifaes.core.protocol.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.protocol.domain.Protocol;

/**
 * 
 * @ClassName: ProtocolMapper 
 * @Description: 
 * @author jxx 
 * @date 2016年7月14日 下午2:43:52 
 *
 */
public interface ProtocolMapper extends BaseMapper<Protocol> {
	
	/**
	 * 
	 * 根据协议类型获取协议模板
	 * @author jxx
	 * @date 2016年8月2日
	 * @param type
	 * @return
	 */
	List<Protocol> getProtocolListByType(Protocol model);

}
package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.MessageType;

/**
 * Service Interface:MessageTypeService
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface MessageTypeService extends BaseService<MessageType>{

	/**
	 * 根据messageType和sendType查找记录
	 * @author xhf
	 * @date 2016年8月31日
	 * @param messageType
	 * @param sendType
	 * @return
	 */
	MessageType find(final String messageType, String sendType);
	
	void update(final MessageType entity);
	
}
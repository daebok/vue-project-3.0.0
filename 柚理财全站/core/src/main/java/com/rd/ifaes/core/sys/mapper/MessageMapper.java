package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Message;

/**
 * Dao Interface:MessageMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface MessageMapper extends BaseMapper<Message> {

	/**
	 * 
	 * 获得最新消息发送时间
	 * @author xhf
	 * @date 2016年7月28日
	 * @param message
	 * @return
	 */
	Message getNewestSendTime(Message message);
	
	
	/**
	 * 查询后台消息记录
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	List<Message> findMessageList(Message model);
}
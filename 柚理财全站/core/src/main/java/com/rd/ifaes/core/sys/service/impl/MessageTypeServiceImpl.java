package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.mapper.MessageTypeMapper;
import com.rd.ifaes.core.sys.service.MessageTypeService;

import java.util.List;

/**
 * ServiceImpl:MessageTypeServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
@Service("messageTypeService") 
public class MessageTypeServiceImpl  extends BaseServiceImpl<MessageTypeMapper, MessageType> implements MessageTypeService{
	
	/**
	 * 从缓存中获取消息类型，如果缓存中没有再从数据库获取
	 */
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_MESSAGE_TYPE+"{messageType}:{sendType}", expire = ExpireTime.ONE_DAY)
	public MessageType find(final String messageType, final String sendType) {
		final MessageType model = new MessageType();
		model.setMessageType(messageType);
		model.setSendType(sendType);
		final List<MessageType> list = dao.findList(model);
		if(list!=null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 插入
	 */
	@Override
	public void insert(final MessageType entity) {
		super.insert(entity);
		CacheUtils.del(CacheConstant.KEY_PREFIX_MESSAGE_TYPE.concat(entity.getMessageType()).concat(":").concat(entity.getSendType()));
	}
	
	/**
	 * 更新
	 */
	@Override
	public void update(final MessageType entity) {
		super.update(entity);
		CacheUtils.del(CacheConstant.KEY_PREFIX_MESSAGE_TYPE.concat(entity.getMessageType()).concat(":").concat(entity.getSendType()));
	}

}
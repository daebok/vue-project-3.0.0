package com.rd.ifaes.mq;

/**
 * 消息队列监听接口
 * @author lh
 * @version 3.0
 * @since 2016-7-14
 * @param <T>
 */
public interface MqListener<T> {
	
	void listen(T t);

}

package com.rd.ifaes.core.core.sms.msg;

/**
 * 通知监听建接口
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2014-04-03
 */
public interface MsgEvent {
	
	/**
	 * 
	 * 初始化事件
	 * @author xhf
	 * @date 2016年7月26日
	 * @param todo
	 */
	void initCode(String todo);
	/**
	 * 事件执行
	 */
	void doEvent();

	/**
	 * 通知发送（邮件、站内行、短信）
	 */
	void sendMsg();
}

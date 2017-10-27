package com.rd.ifaes.core.credit.service.impl;



import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.credit.domain.UserCredit;
import com.rd.ifaes.core.credit.domain.UserCreditLine;
import com.rd.ifaes.core.credit.mapper.UserCreditMapper;
import com.rd.ifaes.core.credit.service.UserCreditLineService;
import com.rd.ifaes.core.credit.service.UserCreditService;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.core.user.service.impl.UserServiceImpl;

@Service("userCreditService") 
@Transactional
public class UserCreditServiceImpl extends BaseServiceImpl<UserCreditMapper, UserCredit> implements UserCreditService {

	/**
	 * 日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserCreditLineService userCreditLineService ;
	@Resource
	private UserService userService ;
	 /**
     * 短信类型
     */
    @Resource
    private transient MessageTypeService msgTypeService;
    /**
     * Rabbit操作类
     */
    @Autowired
    private transient RabbitProducer rabbitProducer;
    /**
     * 短信业务处理类
     */
    @Resource
    private transient MessageService messageService;
    
    
	/**
	 * 额度申请
	 */
	public void applyCredit(String userId, String account, String content) {
		LOGGER.info("userId:{}  account:{}  content:{}",userId, account, content);
		UserCredit userCredit = new UserCredit() ;
		userCredit.setUserId(userId);
		userCredit.setAccount(Integer.parseInt(account));
		userCredit.setContent(content);
		userCredit.setCreateTime(DateUtils.getNow());
		userCredit.setStatus("0");
		userCredit.setDeduct(0);
		insert(userCredit);
	}

	public void establishVerify(UserCredit model) {
		model.setStatus("1");
		LOGGER.info("Model:{}", model);
		update(model);
		User user = userService.get(model.getUserId()) ;
		UserCreditLine userCreditLine = userCreditLineService.getByUserId(model.getUserId()) ;
		LOGGER.info("account:{}", model.getAccount());
		//站内信告知
	    String functionType = null ;
        final Map<String, Object> sendData = new HashMap();
        sendData.put("user", user) ;
		if(model.getDeduct() > 0) {
			userCreditLine.setTotalCredit(userCreditLine.getTotalCredit() - model.getDeduct());
			userCreditLine.setUseCredit(userCreditLine.getUseCredit() - model.getDeduct());
			userCreditLineService.update(userCreditLine);
			functionType = MessageConstant.CREDIT_LETTER; ;
	        sendData.put("userCredit", model) ;
		}else {
			if(userCreditLine == null) {
				UserCreditLine userCreditLine2 = new UserCreditLine() ;
				userCreditLine2.setUserId(model.getUserId());
				userCreditLine2.setTotalCredit(model.getAccount());
				userCreditLine2.setUseCredit(model.getAccount());
				userCreditLine2.setCreateTime(DateUtils.getNow());
				userCreditLineService.save(userCreditLine2);
				sendData.put("totalCredit", userCreditLine2.getTotalCredit()) ;
			}else {
				userCreditLine.setTotalCredit(userCreditLine.getTotalCredit() + model.getAccount());
				userCreditLine.setUseCredit(userCreditLine.getUseCredit() + model.getAccount());
				userCreditLineService.update(userCreditLine);
				sendData.put("totalCredit", userCreditLine.getTotalCredit()) ;
			}
			functionType = MessageConstant.ADD_CREDIT_LETTER; ;
			
		}
		 final MessageType msgType = msgTypeService.find(functionType, MessageConstant.MESSAGE_LETTER);
	        if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
	            final Message msg = new Message();
	            msg.setMessageType(functionType);
	            msg.setSendType(String.valueOf(MessageConstant.MESSAGE_LETTER));
	            msg.setSendUser(MessageConstant.USER_ID_ADMIN);
	            msg.setReceiveUser(user.getUuid());
	            msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), sendData));
	            msg.setContent(FreemarkerUtil.renderTemplate(msgType.getMessageContent(), sendData));
	            //内容不为空
	            if (StringUtils.isNotBlank(msg.getContent())) {
	                if (ConfigUtils.isOpenMq()) {
	                    //加入队列
	                    rabbitProducer.send(MqConstant.ROUTING_KEY_MESSAGE, msg);
	                } else {
	                    //不加入队列
	                    messageService.sendMessage(msg);
	                }
	            }
	        }
	}
	
}

package com.rd.ifaes.core.sys.service.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.event.MessageCountEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.sms.AldySmsPortalImpl;
import com.rd.ifaes.core.core.sms.SmsPortal;
import com.rd.ifaes.core.core.sms.ZtSmsPortalImpl;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.mail.Mail;
import com.rd.ifaes.core.sys.domain.Letter;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.domain.UserMessageConfig;
import com.rd.ifaes.core.sys.mapper.MessageMapper;
import com.rd.ifaes.core.sys.service.LetterService;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.sys.service.UserMessageConfigService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 消息处理类
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
@Service("messageService") 
public class MessageServiceImpl  extends BaseServiceImpl<MessageMapper, Message> implements MessageService{

	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	private static final int MESSAGE_MAX_LEN = 300;//短信最大可发送字符数，请酌情配置该参数，长短信不能超过350个字符

	/**
	 * 站内信处理类
	 */
	@Resource
	private transient LetterService letterService;
	/**
	 * 消息配置处理类
	 */
	@Resource
	private transient UserMessageConfigService msgConfigService;
	/**
	 * 消息类型处理类
	 */
	@Resource
	private transient MessageTypeService messageTypeService;
	
	private SmsPortal smsPortal;
	
	/**
	 * 发送消息
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendMessage(final Message message) {
		switch (message.getSendType()) {
			case MessageConstant.MESSAGE_SMS:
				this.sendSms(message);
				break;
			case MessageConstant.MESSAGE_EMAIL:
				this.sendEmail(message);
				break;
			case MessageConstant.MESSAGE_LETTER:
				this.sendLetter(message);
				break;
		}
		
	}

	/**
	 * 根据消息类型发送消息
	 */
	@Override
	@Transactional(readOnly = false)
	public void sendMessage(final String messageType,final Map<String, Object> sendData) {
        if (sendData.isEmpty()) {
          LOGGER.error("there is no data to send message");
          return;
        }
        sendData.put(ConfigConstant.WEB_URL, ConfigUtils.getValue(ConfigConstant.WEB_URL));
        sendData.put(ConfigConstant.WEB_NAME, ConfigUtils.getValue(ConfigConstant.WEB_NAME));
        sendData.put(ConfigConstant.CUSTOMER_HOTLINE, ConfigUtils.getValue(ConfigConstant.CUSTOMER_HOTLINE));
        sendData.put(ConfigConstant.IMAGE_SERVER_URL, ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
        sendData.put(ConfigConstant.WEB_INDEX_COPYRIGHT, ConfigUtils.getValue(ConfigConstant.WEB_INDEX_COPYRIGHT));
		sendData.put(ConfigConstant.WEB_RECORD_NUMBER, ConfigUtils.getValue(ConfigConstant.WEB_RECORD_NUMBER));
		sendData.put("sendTime", DateUtils.formatDate(DateUtils.getNow(),"yyyy-MM-dd HH:mm:ss"));
		sendData.put("noticeTime", DateUtils.dateStr(DateUtils.getNow(), "MM月dd日 HH时mm分ss秒"));
		sendData.put("vtime", ConfigUtils.getValue(ConfigConstant.VERIFY_CODE_TIME));
		sendData.put("sendEmail",ConfigUtils.getValue(ConfigConstant.EMAIL_EMAIL));
		User user = (User) sendData.get("user");
		sendData.put("user", user);
		final String userId = (user!=null)?user.getUuid():"";
		final Map<String, String> params = Maps.newHashMap();
		params.put("userId", userId);
		params.put("messageType", messageType);
		UserMessageConfig unConfig = msgConfigService.findByParams(params);
		// 没有强制用户通知配置必须与用户对应，所以当取不到这个配置的时候，初始化一个配置对象，且发送标志为真
		if (unConfig == null) {
			unConfig = new UserMessageConfig();
			unConfig.setSms(CommonEnum.YES.getValue());
			unConfig.setEmail(CommonEnum.YES.getValue());
			unConfig.setLetter(CommonEnum.YES.getValue());
		}
		// 生成短信
		if (CommonEnum.YES.eq(unConfig.getSms())) {
			this.sendMsg(messageType,MessageConstant.MESSAGE_SMS,sendData);
		}
		// 生成邮件
		if (CommonEnum.YES.eq(unConfig.getEmail())) {
			this.sendMsg(messageType,MessageConstant.MESSAGE_EMAIL,sendData);
		}
		// 生成站内信
		if (CommonEnum.YES.eq(unConfig.getLetter())) {
			this.sendMsg(messageType,MessageConstant.MESSAGE_LETTER,sendData);
		}		
		sendData.clear();
	}
	
	 /**
	  * 发送消息
	  * @author  FangJun
	  * @date 2016年10月19日
	  * @param msg 消息模型
	  */
	private  void  sendMsg(final String messageType,final String sendType,Map<String,Object> sendData){
		final MessageType msgType = messageTypeService.find(messageType, sendType);
		if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
			final Message msg = new Message();
			final User user = (User) sendData.get("user");
			final String userId = user.getUuid();
			msg.setMessageType(messageType);
			msg.setCreateTime(DateUtils.getNow());
			msg.setSendUser(MessageConstant.USER_ID_ADMIN);
			msg.setReceiveUser(userId);
			msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), sendData));
			msg.setContent(FreemarkerUtil.renderTemplate(msgType.getMessageContent(), sendData));
			msg.setSendType(String.valueOf(sendType));
			//接收地址
			String receiveAddr = (String) sendData.get("receiveAddr");
			if(MessageConstant.MESSAGE_SMS.equals(sendType)){
				msg.setReceiveAddr(StringUtils.isNotBlank(receiveAddr) ? receiveAddr : user.getMobile());
			}else if(MessageConstant.MESSAGE_EMAIL.equals(sendType)){
				msg.setReceiveAddr(StringUtils.isNotBlank(receiveAddr) ? receiveAddr : user.getEmail());
			}else{
				msg.setReceiveAddr(userId);
			}

			// 接收地址、标题、内容，非空校验
			if (StringUtils.isNotBlank(msg.getReceiveAddr()) && StringUtils.isNotBlank(msg.getTitle())
					&& StringUtils.isNotBlank(msg.getContent())) {
				if(ConfigUtils.isOpenMq()){
					// 加入队列
					RabbitUtils.sendMessage(msg);
				} else {
					// 不加入队列
					sendMessage(msg);
				}
			}
		}
	}
	
	
	/**
	 * 发送短信
	 * @author xhf
	 * @date 2016年8月30日
	 * @param s
	 */
	@Transactional(readOnly = false)
	private void sendSms(final Message message) {
		final String mobile = message.getReceiveAddr();
		final String content = StringUtils.isNull(message.getContent());
		final String templateContent = StringUtils.isNull(message.getTemplateContent());
		final String templateCode = message.getTemplateCode();
		LOGGER.debug("mobile=========" + mobile);
		LOGGER.debug("contentString=========" + content);		
		LOGGER.debug("templateContent=========" + templateContent);		
		smsPortal = new AldySmsPortalImpl();
		String code  = smsPortal.send(mobile, templateContent, templateCode);
		if(AldySmsPortalImpl.SUCCESS_CODE.equals(code)){
			//短信发送成功
			LOGGER.debug("短信发送成功！！");
			message.setStatus(Constant.MESSAGE_STATUS_SUCCESS);
		}else{
			LOGGER.info("message remark:{}", message.getRemark());
			message.setStatus(Constant.MESSAGE_STATUS_FAIL);
		}
		message.setRemark(smsPortal.getReturnMessage(code));
		this.insert(message);
	}
	
		
	public void insert(final Message message) {
		message.preInsert();
		dao.insert(message);
	}
	
	/**
	 * 发送邮件
	 * @author xhf
	 * @date 2016年8月30日
	 * @param email
	 */
	@Transactional(readOnly = false)
	private void sendEmail(final Message email) {
		if(validMailAddress(email.getReceiveAddr())){
			final Mail mail = Mail.getInstance();
			mail.setTo(email.getReceiveAddr());
			mail.setSubject(email.getTitle());
			mail.setBody(email.getContent());
			try {
				mail.sendMail();
				email.setStatus(MessageConstant.SEND_STATUS_SUCCESS);
				email.setRemark("ok");
				LOGGER.info(ResourceUtils.get("sys.message.email.sendSuccess"));		// "发送邮件成功！！"			
			} catch (Exception e) {
				email.setStatus(MessageConstant.SEND_STATUS_FAIL);
				email.setRemark("fail");
				LOGGER.error(ResourceUtils.get("sys.message.email.sendFail"), e);//"发送邮件失败"
			}
			this.insert(email);			
		}else{
			LOGGER.warn(ResourceUtils.get("sys.message.email.addressError"));//	"邮箱地址错误！"	
		}
	}
	
	/**
	 * 校验邮箱地址
	 * @author xhf
	 * @date 2016年8月30日
	 * @param mailAddress
	 * @return
	 */
	private static boolean validMailAddress(final String mailAddress){
		final Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		final Matcher matcher = pattern.matcher(mailAddress);
		return matcher.matches();
	}	
	
	/**
	 * 发送站内信
	 * @author xhf
	 * @date 2016年8月30日
	 * @param message
	 */
	@Transactional(readOnly = false)
	private void sendLetter(final Message message) {
		//站内信入库
		Letter letter = new Letter();
		letter.setTitle(message.getTitle());
		letter.setSendUser(message.getSendUser());
		letter.setReceiveUser(message.getReceiveUser());
		letter.setContent(message.getContent());
		letter.setDeleteFlag("0");
		letter.setReadFlag("0");
		letter.setStatus(MessageConstant.SEND_STATUS_SUCCESS);
		letter.setCreateTime(DateUtils.getNow());
		letter.preInsert();
		letterService.insert(letter);
		
		//消息入库
		message.setStatus(MessageConstant.SEND_STATUS_SUCCESS);
		message.setRemark("ok");
		message.setReceiveAddr("-");
		this.insert(message);
	}
	
	/**
	 * 获得最新发送时间
	 */
	@Override
	public Message getNewestSendTime(final String messageType, final String receiveAddr){
		final Message model = new Message();
		model.setMessageType(messageType);
		model.setReceiveAddr(receiveAddr);
		return dao.getNewestSendTime(model);
	}

	@Override
	public Page<Message> findMessageList(Message model) {
		Page<Message> page = model.getPage();
		if(page==null){
			page=new Page<Message>();
		}
		page.setRows(dao.findMessageList(model));
		return page;
	}
	
	/**
	 * 非在线用户，验证码发送
	 * @author xxb
	 * @date 2016年10月17日
	 * @param messageType
	 * @param receiveAddr
	 * @param code
	 */
	@Override
	public void sendMessage(String messageType, String receiveAddr, String code, String route) {
		
			Map<String, Object> sendData = Maps.newConcurrentMap();
	        sendData.put(ConfigConstant.WEB_URL, ConfigUtils.getValue(ConfigConstant.WEB_URL));
	        sendData.put(ConfigConstant.WEB_NAME, ConfigUtils.getValue(ConfigConstant.WEB_NAME));
	        sendData.put(ConfigConstant.CUSTOMER_HOTLINE, ConfigUtils.getValue(ConfigConstant.CUSTOMER_HOTLINE));
	        sendData.put(ConfigConstant.WEB_INDEX_COPYRIGHT, ConfigUtils.getValue(ConfigConstant.WEB_INDEX_COPYRIGHT));
			sendData.put(ConfigConstant.WEB_RECORD_NUMBER, ConfigUtils.getValue(ConfigConstant.WEB_RECORD_NUMBER));
			sendData.put(ConfigConstant.IMAGE_SERVER_URL, ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
			sendData.put("sendTime", DateUtils.formatDate(DateUtils.getNow(),"yyyy-MM-dd HH:mm:ss"));
			sendData.put("noticeTime", DateUtils.dateStr(DateUtils.getNow(), "MM月dd日 HH时mm分ss秒"));
			sendData.put("vtime", ConfigUtils.getValue(ConfigConstant.VERIFY_CODE_TIME));
			sendData.put("receiveAddr", receiveAddr);
			sendData.put("code", code);
			sendData.put("messageType", messageType);
			sendData.put("sendEmail",ConfigUtils.getValue(ConfigConstant.EMAIL_EMAIL));
			
			// 生成短信
			if (StringUtils.equals(route, MessageConstant.MESSAGE_SMS + "")) {
				final MessageType msgType = messageTypeService.find(messageType, MessageConstant.MESSAGE_SMS);
				if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
					final Message msg = new Message();
					msg.setMessageType(messageType);
					msg.setCreateTime(DateUtils.getNow());
					msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
					msg.setSendUser(MessageConstant.USER_ID_ADMIN);
					msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), sendData));
					msg.setContent(FreemarkerUtil.renderTemplate(msgType.getMessageContent(), sendData));
					msg.setReceiveAddr(receiveAddr);
					if(MessageConstant.MESSAGE_RETRIEVE_PASS_PHONECODE.equals(messageType)){
						msg.setTemplateCode(msgType.getTemplateCode());
						Map<String, Object> templateContent = Maps.newHashMap();
						templateContent.put("code", code);
						msg.setTemplateContent(JSON.toJSONString(templateContent));
					}
					//内容不为空
					if ( StringUtils.isNotBlank(msg.getReceiveAddr()) && StringUtils.isNotBlank(msg.getContent())) {
						
						if(ConfigUtils.isOpenMq()){
							//加入队列
							RabbitUtils.sendMessage(msg);						
						}else{
							//不加入队列
							sendMessage(msg);
						}
					}
				}
			}

			// 生成邮件
			if (StringUtils.equals(route, MessageConstant.MESSAGE_EMAIL + "")) {
				final MessageType emailType = messageTypeService.find(messageType, MessageConstant.MESSAGE_EMAIL);
				if (emailType != null && CommonEnum.YES.eq(emailType.getSend())) {
					final Message email = new Message();
					email.setMessageType(messageType);
					email.setSendType(String.valueOf(MessageConstant.MESSAGE_EMAIL));
					email.setSendUser(MessageConstant.USER_ID_ADMIN);
					email.setTitle(FreemarkerUtil.renderTemplate(emailType.getMessageTitle(), sendData));
					email.setContent(FreemarkerUtil.renderTemplate(emailType.getMessageContent(), sendData));
					email.setCreateTime(DateUtils.getNow());
					email.setReceiveAddr(receiveAddr);
					//邮件非空校验
					if (StringUtils.isNotBlank(email.getReceiveAddr()) && StringUtils.isNotBlank(email.getTitle())
							&& StringUtils.isNotBlank(email.getContent())) {
						
					 	if(ConfigUtils.isOpenMq()){
							//加入队列
							RabbitUtils.sendMessage(email);						
						}else{
							//不加入队列
							sendMessage(email);
						}
					}
				}
			}
			sendData.clear();
	}
	
}
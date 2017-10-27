package com.rd.ifaes.manage.operate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class UserSendMessageManageController extends SystemController{
	
	@Resource
	private UserService userService ;
	@Resource
	private transient ActivityPlanService activityPlanService;
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;
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
	 * 消息群发页面
	 */
	@RequestMapping(value = "/operate/sendMessage/sendUserMessage")
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RED)
	public String grantUserRed(final Model model) {
		ActivityPlan actp=activityPlanService.findActivityPlanByCode(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue());
		if (actp==null || actp.getStatus().equals(OperateEnum.STATUS_FORBIDDEN.getValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ACTIVITY_GRANT_BAN));
		}
		return "/operate/sendMessage/sendUserMessage";
	}

	
	
	/**
	 * 消息群发页面选择群发用户
	 */
	@RequestMapping(value = "/operate/sendMessage/selectedSendUser")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_LIST)
	public Object selectedSendUser(final UserModel userModel, final String userIds) {
		Page<User> userPage = null;
		if(StringUtils.isBlank(userIds)){
			userPage = new Page<User>();
			List<User> userList = new ArrayList<User>(); 
			userPage.setRows(userList);
		}else{
			userModel.setUuids(userIds.split(","));
			userModel.setIsVouch(String.valueOf(Constant.INT_ZERO));
			userPage = userService.findUserForRed(userModel);
		}
		return userPage;
	}
	
	
	/**
	 * 消息群发页面 可选择用户列表数据
	 * 
	 */
	@RequestMapping(value = "/operate/sendMessage/selectableSendUser")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_LIST)
	public Object selectableGrantUser(final UserModel userModel, final String userIds) {
		// 非担保机构
		userModel.setIsVouch(String.valueOf(Constant.INT_ZERO));
		if (StringUtils.isNotBlank(userIds)) {
			userModel.setUuids(userIds.split(","));
		}
		return userService.findUserForRed(userModel);
	}
	
	/**
	 * 短信群发操作
	 */
	@RequiresPermissions("oper:message:mass")
	@RequestMapping(value = "/operate/sendMessage/doSendMessage")
	@ResponseBody
	@SystemLog(method=SysLogConstant.GRANT,content=SysLogConstant.USER_RED)
	@TokenValid(value = TokenConstants.TOKEN_SEND_MESSAGE, dispatch = true)
	public Object doSendMessage(final String[] userIds, final String sendType, final String content) {
		if(userIds!=null && userIds.length > Constant.INT_TWO_HUNDRED){//发放用户数量太多，直接入队列
			//队列短信发放限制发放频率，防止队列消息大量堆积
			final String intervalKey = CacheConstant.KEY_SEND_MESSAGE;
			if (CacheUtils.incr(intervalKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				return renderResult(false, ResourceUtils.get(ResourceConstant.USER_RED_GRANT_TOO_OFTEN));
			}
			CacheUtils.expire(intervalKey, ExpireTime.TWO_MIN);
		}else{
		}
		LOGGER.info("待处理：{}",userIds[0]);
		LOGGER.info("待处理：{}",sendType);
		LOGGER.info("待处理：{}",content);
		for(String userId : userIds) {
			User user = userService.get(userId) ;
			String mobile = user.getMobile() ;
			String email = user.getEmail() ;
			
			Message msg = new Message() ;
			if(sendType.equals("1")) {
				//短信
				msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
				msg.setTitle("短信提醒");
				msg.setReceiveAddr(mobile);
			}else if(sendType.equals("2")) {
				//邮件
				msg.setSendType(String.valueOf(MessageConstant.MESSAGE_EMAIL));
				msg.setTitle("邮件提醒");
				msg.setReceiveAddr(email);
			}else if(sendType.equals("3")) {
				//站内信
				msg.setSendType(String.valueOf(MessageConstant.MESSAGE_LETTER));
				msg.setTitle("站内信提醒");
			}
			msg.setContent(content);
			msg.setSendUser(MessageConstant.USER_ID_ADMIN);
			msg.setReceiveUser(userId);
			//内容不为空
			if (StringUtils.isNotBlank(msg.getContent())) {
				if(ConfigUtils.isOpenMq()){
					//加入队列
					rabbitProducer.send(MqConstant.ROUTING_KEY_MESSAGE, msg);						
				}else{
					//不加入队列
					messageService.sendMessage(msg);
				}
			}
		}
		return renderResult(true,"发送成功");
	}
}

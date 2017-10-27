package com.rd.ifaes.manage.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserOpinion;
import com.rd.ifaes.core.user.model.UserOpinionModel;
import com.rd.ifaes.core.user.service.UserOpinionService;
import com.rd.ifaes.core.user.service.UserService;
/**
 * 前台用户意见反馈 Controller
 * @author xxb
 * @since 2016-10-13
 * @version 3.0
 */
@Controller
public class UserOpinionController extends SystemController {
	
	@Resource
	private UserOpinionService userOpinionService;
	@Resource
	private UserService userService ;
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
	 * 
	 * 进入管理列表页面
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年10月13日
	 */
	@RequestMapping(value = "/sys/opinion/opinionManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPINION)
	public String manage(Model model){
		model.addAttribute(ConfigConstant.WEB_IMAGE_SERVER_URL, ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL));
		return "/sys/opinion/opinionManage";
	}
		
	/**
	 * 
	 * 管理页面加载数据
	 * @param  
	 * @return Page<UserOpinion>
	 * @author xxb
	 * @date 2016年10月13日
	 */
	@RequestMapping(value = "/sys/opinion/opinionList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPINION)
	public Page<UserOpinion> list(UserOpinionModel userOpinion){
		return userOpinionService.findPage(userOpinion);
	}
	
	/**
	 * 
	 * 查看反馈信息
	 * @param  
	 * @return 
	 * @author ywt
	 * @date 2016年10月21日
	 */
	@RequestMapping(value = "/sys/opinion/checkOpinionPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.OPINION)
	public String checkOpinionPage(String id,final Model model) {
		UserOpinionModel opinion=new UserOpinionModel();
		opinion.setUuid(id);
		UserOpinion userOpinion=null;
		List<UserOpinion> userOpinionList=userOpinionService.findList(opinion);
		if (!userOpinionList.isEmpty()) {
			userOpinion=userOpinionList.get(0);
			if (!StringUtils.isBlank(userOpinion.getAttachmentPath())) {
				List<String> picList = new ArrayList<>();
				Collections.addAll(picList, userOpinion.getAttachmentPath().split(","));
				 model.addAttribute("picList",picList);
			}
			
		}
		 model.addAttribute("userOpinion",userOpinion);
		return "/sys/opinion/checkOpinionPage";
	}

	/**
	 * 忽略用户反馈
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/sys/opinion/opinionIgnore")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPINION)
	public Object ignoreUserOpinion(String uuid) {
		LOGGER.info("待处理:{}", uuid);
		UserOpinion uo = userOpinionService.get(uuid) ;
		LOGGER.info("待处理:{}", uo.getUuid());
		uo.setStatus("2");
		userOpinionService.updateById(uo);
		return renderResult(true, "忽略成功") ;
	}
	
	/**
	 * 用户答复窗口
	 * @param uuid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sys/opinion/opinionReply")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPINION)
	public String replyContent(String uuid, final Model model) {
		LOGGER.info("待处理id:{}", uuid);
		UserOpinionModel opinion=new UserOpinionModel();
		opinion.setUuid(uuid);
		UserOpinion userOpinion=null;
		List<UserOpinion> userOpinionList=userOpinionService.findList(opinion);
		if (!userOpinionList.isEmpty()) {
			userOpinion=userOpinionList.get(0);
			if (!StringUtils.isBlank(userOpinion.getAttachmentPath())) {
				List<String> picList = new ArrayList<>();
				Collections.addAll(picList, userOpinion.getAttachmentPath().split(","));
				 model.addAttribute("picList",picList);
			}
			
		}
		model.addAttribute("userOpinion", userOpinion) ;
		return "/sys/opinion/opinionReply";
	}
	
	@RequestMapping(value = "/sys/opinion/opinionReplySubmit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPINION)
	public Object replySubmit(String uuid, String content) {
		LOGGER.info("待处理:{}", uuid);
		LOGGER.info("待处理:{}", content);
		if(content == null) {
			return renderResult(true, "答复内容不能为空") ;
		}
		UserOpinion userOPinion = userOpinionService.get(uuid) ;
		userOPinion.setContent(content);
		userOPinion.setStatus("1");
		userOpinionService.updateById(userOPinion);
		String userId = userOPinion.getUserId() ;
		User user = userService.get(userId) ;
		Message msg = new Message();
		msg.setContent(content);
		msg.setSendType(String.valueOf(MessageConstant.MESSAGE_LETTER));
		msg.setTitle("反馈答复");
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
		return renderResult(true, "发送成功") ;
	}
	
}

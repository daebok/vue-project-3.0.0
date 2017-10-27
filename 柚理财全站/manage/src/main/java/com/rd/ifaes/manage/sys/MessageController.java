package com.rd.ifaes.manage.sys;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.service.MessageService;

@Controller
public class MessageController extends SystemController {
	
	@Resource
	private MessageService messageService;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sys/message/messageManage")
	@RequiresPermissions("set:msg:msg:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MESSAGE)
	public String manage(){
		return "/sys/message/messageManage";
	}
	
	@RequestMapping(value = "/sys/message/list")
	@RequiresPermissions("set:msg:msg:query")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MESSAGE)
	public Object list(Message model){
		if("99".equalsIgnoreCase(model.getStatus())){
			model.setStatus(null);
		}
		return messageService.findMessageList(model);
	}
	
}

package com.rd.ifaes.manage.sys;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
/**
 * 消息模板管理
 * @author lh
 * @version 3.0
 * @since 2016-6-22
 *
 */
@Controller
public class MessageTypeController extends SystemController {

	
	@Resource
	private MessageTypeService messageTypeService;
	@Resource
	private DictDataService dictDataService;
	
	/**
	 * 管理页面
	 * @return
	 */
	@RequestMapping(value = "/sys/messageType/messageTypeManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MESSAGE_TYPE)
	public String manage(){
		return "/sys/messageType/messageTypeManage";
	}
	
	@RequestMapping(value = "/sys/messageType/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MESSAGE_TYPE)
	public Object list(MessageType model){
		return messageTypeService.findPage(model);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:msg:msgType:add")
	@RequestMapping(value = "/sys/messageType/messageTypeAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.MESSAGE_TYPE)
	public String addPage(){		
		return "/sys/messageType/messageTypeAddPage";
	}
	
	/**
	 * 添加
	 */
	@RequiresPermissions("set:msg:msgType:add")
	@RequestMapping(value = "/sys/messageType/messageTypeAdd")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.MESSAGE_TYPE)
	public Map<String, Object> add( MessageType messageType, BindingResult bindingResult){
		BeanValidators.validate(messageType);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  
	    	messageTypeService.insert(messageType);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:msg:msgType:edit")
	@RequestMapping(value = "/sys/messageType/messageTypeEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.MESSAGE_TYPE)
	public String editPage(final String id, final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("messageType", messageTypeService.get(id));
		}
		return "/sys/messageType/messageTypeEditPage";
	}
	
	/**
	 * 编辑
	 */
	@RequiresPermissions("set:msg:msgType:edit")
	@RequestMapping(value = "/sys/messageType/messageTypeEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.MESSAGE_TYPE)
	public Map<String, Object> edit( MessageType messageType, BindingResult bindingResult){
		BeanValidators.validate(messageType);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	messageTypeService.update(messageType);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	
	
		
}

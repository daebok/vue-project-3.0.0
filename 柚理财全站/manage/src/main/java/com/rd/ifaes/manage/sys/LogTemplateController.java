package com.rd.ifaes.manage.sys;

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.LogTemplateService;
/**
 * 消息模板 Controller
 * @author XHF
 * @since 2016-5-27
 * @version 3.0
 */
@Controller
public class LogTemplateController extends SystemController {
	
	@Resource
	private LogTemplateService logTemplateService;
	
	@RequestMapping(value = "/sys/logTemplate/logTemplateManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG_TEMPLATE)
	public String manage(){
		return "/sys/logTemplate/logTemplateManage";
	}
	
	@RequestMapping(value = "/sys/logTemplate/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG_TEMPLATE)
	public Object list(LogTemplate logTemplate){
		return logTemplateService.findPage(logTemplate);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:logtemplate:add")
	@RequestMapping(value = "/sys/logTemplate/logTemplateAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.LOG_TEMPLATE)
	public String addPage(){		
		return "/sys/logTemplate/logTemplateAddPage";
	}
	
	/**
	 * 添加消息模板
	 */
	@RequiresPermissions("set:logtemplate:add")
	@RequestMapping(value = "/sys/logTemplate/logTemplateAdd")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.LOG_TEMPLATE)
	public Map<String, Object> add(LogTemplate logTemplate, BindingResult bindingResult){
		BeanValidators.validate(logTemplate);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  
	    	logTemplateService.insert(logTemplate);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:logtemplate:edit")
	@RequestMapping(value = "/sys/logTemplate/logTemplateEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.LOG_TEMPLATE)
	public String editPage(String id, final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("logTemplate", logTemplateService.get(id));
		}
		return "/sys/logTemplate/logTemplateEditPage";
	}
	
	/**
	 * 编辑消息模板
	 */
	@RequiresPermissions("set:logtemplate:edit")
	@RequestMapping(value = "/sys/logTemplate/logTemplateEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.LOG_TEMPLATE)
	public Map<String, Object> edit( LogTemplate logTemplate, BindingResult bindingResult){
		BeanValidators.validate(logTemplate);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  
	    	logTemplateService.update(logTemplate);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
		
	/**
	 * 删除消息模板
	 * @param ids
	 * @param response
	 */
	@RequestMapping(value = "/sys/logTemplate/logTemplateDel")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.LOG_TEMPLATE)
	public Map<String, Object> del(@NotNull(message="{sys.config.code.exists}") String [] ids){
		logTemplateService.deleteBatch(ids);
    	return renderSuccessResult();
	}
	

}

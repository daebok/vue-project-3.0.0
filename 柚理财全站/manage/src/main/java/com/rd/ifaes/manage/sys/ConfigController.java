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
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Config;
import com.rd.ifaes.core.sys.service.ConfigService;
/**
 * 参数配置 Controller
 * @author lh
 * @since 2016年5月17日 
 * @version 3.0
 */
@Controller
public class ConfigController extends SystemController {
	
	@Resource
	private ConfigService configService;
	
	@RequestMapping(value = "/sys/config/configManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CONFIG)
	public String manage(){
		return "/sys/config/configManage";
	}
	
	@RequestMapping(value = "/sys/config/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CONFIG)
	public Page<Config> list(Config config){
		return configService.findPage(config);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:config:add")
	@RequestMapping(value = "/sys/config/configAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.CONFIG)
	public String addPage(){		
		return "/sys/config/configAddPage";
	}
	
	/**
	 * 添加参数配置
	 */
	@RequiresPermissions("set:config:add")
	@RequestMapping(value = "/sys/config/configAdd")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.CONFIG)
	public Map<String, Object> add(Config config, BindingResult bindingResult){
		BeanValidators.validate(config);
		config.checkConfig();
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (resultMap == null) {  
	    	configService.insert(config);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:config:edit")
	@RequestMapping(value = "/sys/config/configEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.CONFIG)
	public String editPage(String id ,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("config", configService.get(id));
		}
		return "/sys/config/configEditPage";
	}
	
	/**
	 * 编辑参数配置
	 */
	@RequiresPermissions("set:config:edit")
	@RequestMapping(value = "/sys/config/configEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.CONFIG)
	public Map<String, Object> edit(Config config, BindingResult bindingResult){
		BeanValidators.validate(config);
		config.checkConfig();
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	configService.update(config);
	    	//清除前台缓存
	    	this.doCleanWebCache(config);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
		
	/**
	 * 校验code是否存在
	 */
	@RequestMapping(value = "/sys/config/checkCode")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.CONFIG)
	public Map<String, Object> checkCode(Config config){
		boolean result = configService.checkCode(config) > 0;
		return renderResult(result, result?ResourceUtils.get("global.code.exists", config.getCode()):"");
	}
	

}

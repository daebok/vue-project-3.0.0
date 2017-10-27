package com.rd.ifaes.manage.sys;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Resource;
import com.rd.ifaes.core.sys.service.ResourceService;
/**
 * 国际化资源 Controller
 * @author lh
 * @since 2016年7月28日 
 * @version 3.0
 */
@Controller
public class ResourceController extends SystemController {
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/sys/resource/resourceManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RESOURCE)
	public String manage(){
		return "/sys/resource/resourceManage";
	}
	
	@RequestMapping(value = "/sys/resource/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RESOURCE)
	public Page<Resource> list(Resource resource){
		return resourceService.findPage(resource);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:resource:add")
	@RequestMapping(value = "/sys/resource/resourceAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RESOURCE)
	public String addPage(){		
		return "/sys/resource/resourceAddPage";
	}
	
	/**
	 * 添加
	 */
	@RequiresPermissions("set:resource:add")
	@RequestMapping(value = "/sys/resource/resourceAdd")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RESOURCE)
	@ResponseBody
	public Map<String, Object> add( Resource resource, BindingResult bindingResult){
		BeanValidators.validate(resource);
		if (!bindingResult.hasErrors()) {  
	    	resourceService.insert(resource);
	    	return renderSuccessResult();	    	
	    }
	    return renderBindingResult(bindingResult);
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:resource:edit")
	@RequestMapping(value = "/sys/resource/resourceEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RESOURCE)
	public String editPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("resource", resourceService.get(id));
		}
		return "/sys/resource/resourceEditPage";
	}
	
	/**
	 * 编辑
	 */
	@RequiresPermissions("set:resource:edit")
	@RequestMapping(value = "/sys/resource/resourceEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RESOURCE)
	public Map<String, Object> edit( Resource resource, BindingResult bindingResult){
		BeanValidators.validate(resource);
		if (!bindingResult.hasErrors()) {  
	    	resourceService.update(resource);
	    	return renderSuccessResult();	    	
	    }else{
	    	return renderBindingResult(bindingResult);
	    }
	}
	
	/**
	 * 删除
	 * @return 
	 */
	@RequiresPermissions("set:resource:del")
	@RequestMapping(value = "/sys/resource/resourceDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RESOURCE)
	public Map<String, Object> del(String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		resourceService.deleteBatch(ids);
		return renderSuccessResult();
	}
	
		
}

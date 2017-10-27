package com.rd.ifaes.manage.sys;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.rd.ifaes.core.sys.domain.Role;
import com.rd.ifaes.core.sys.service.RoleService;

@Controller
public class RoleController extends SystemController {

	@Resource
	private RoleService roleService;

	
	@RequestMapping(value = "/sys/role/roleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ROLE)
	public String manage(){
		return "/sys/role/roleManage";
	}
	
	@RequestMapping(value = "/sys/role/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ROLE)
	public Page<Role> list(Role role){
		return roleService.findPage(role);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:sys:role:add")
	@RequestMapping(value = "/sys/role/roleAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ROLE)
	public String addPage(){		
		return "/sys/role/roleAddPage";
	}
	
	/**
	 * 添加角色
	 */
	@RequiresPermissions("set:sys:role:add")
	@RequestMapping(value = "/sys/role/roleAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ROLE)
	public Map<String, Object> add(Role role, BindingResult bindingResult,HttpServletRequest request){
		role.checkRole();
		BeanValidators.validate(role);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		if (!bindingResult.hasErrors()) {
			//TODO TOKEN 
			roleService.insert(role);
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:sys:role:edit")
	@RequestMapping(value = "/sys/role/roleEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ROLE)
	public String editPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("role", roleService.get(id));
		}
		return "/sys/role/roleEditPage";
	}
	
	/**
	 * 编辑角色
	 */
	//
	@RequiresPermissions("set:sys:role:edit")
	@RequestMapping(value = "/sys/role/roleEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ROLE)
	public Map<String, Object> edit( Role role, BindingResult bindingResult,HttpServletRequest request){
		role.checkRole();
		BeanValidators.validate(role);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		if (!bindingResult.hasErrors()) {
			//TODO token
			roleService.update(role);
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 删除角色
	 */
	@RequiresPermissions("set:sys:role:del")
	@RequestMapping(value = "/sys/role/roleDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.ROLE)
	public Map<String, Object> del(String[] ids){
		if(ids == null || ids.length == 0){
			return renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		roleService.deleteChildren(ids);
		return renderSuccessResult();
	}
	
	
	/*********************************角色授权*********************************/
	/**
	 * 角色授权页
	 * @param id
	 * @param request
	 * @return
	 */
	@RequiresPermissions("set:sys:role:auth")
	@RequestMapping(value="/sys/role/rolePermsPage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ROLE_PERMS)
	public String permsPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("role", roleService.get(id));
		}
		 model.addAttribute("id", id);
		return "/sys/role/rolePermsPage";
	}
	
	/**
	 * 角色权限树
	 * @param id
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value="/sys/role/rolePermsTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ROLE_PERMS)
	public Object rolePermsTree(String id){		
		return roleService.findRolePermTree(id);
	}
	*/
	
	/**
	 * 构建角色权限树
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sys/role/rolePermsTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ROLE_PERMS)
	public Object rolePermsTree(String id){		
		return roleService.findAuthTree(id);
	}
	
	/**
	 * 角色授权
	 * @param roleId
	 * @param nids
	 * @param response
	 */
	@RequiresPermissions("set:sys:role:auth")
	@RequestMapping(value="/sys/role/saveRolePerms")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ROLE_PERMS)
	public Map<String, Object> saveRolePerms(String id,String [] ids){
		if(StringUtils.isBlank(id)){
			return renderResult( false, "角色不能为空");
		}
		roleService.saveRolePermissions(id, ids);
		return renderSuccessResult();
	}
	
	/*********************************用户分配角色*********************************/
	
	/**
	 * 用户分配角色页
	 * @param id
	 * @param request
	 * @return
	 */
	@RequiresPermissions("set:sys:role:user:mgr")
	@RequestMapping(value="/sys/role/roleOperatorsPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ROLE_PERMS)
	public String roleOperatorsPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("role", roleService.get(id));
		}
		 model.addAttribute("id", id);
		return "/sys/role/roleOperatorsPage";
	}
	
	
	
}

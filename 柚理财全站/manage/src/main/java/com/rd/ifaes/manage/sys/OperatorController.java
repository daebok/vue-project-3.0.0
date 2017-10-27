package com.rd.ifaes.manage.sys;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.domain.OperatorRole;
import com.rd.ifaes.core.sys.service.OperatorRoleService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.sys.service.RoleService;


/**
 * 
 *  TODO 用户管理controller
 * @version 3.0
 * @author xxb
 * @date 2016年8月20日
 */
@Controller
public class OperatorController extends SystemController {


	@Resource
	private OperatorService operatorService;
	@Resource
	private OperatorRoleService operatorRoleService;
	@Resource
	private RoleService roleService;
	
	@RequestMapping(value = "/sys/operator/operatorManage")
	@SystemLog(method=SysLogConstant.OPERATOR,content=SysLogConstant.OPERATOR)
	public String manage(){
		return "/sys/operator/operatorManage";
	}
	
	@RequestMapping(value = "/sys/operator/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.OPERATOR,content=SysLogConstant.OPERATOR)
	public Page<Operator> list(Operator operator){
		return operatorService.findPage(operator);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:sys:user:add")
	@RequestMapping(value = "/sys/operator/operatorAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.OPERATOR)
	public String addPage(final Model model) {
		 model.addAttribute("roleList", roleService.findAll());
		return "/sys/operator/operatorAddPage";
	}
	
	/**
	 * 添加后台用户
	 */
	@RequiresPermissions("set:sys:user:add")
	@RequestMapping(value = "/sys/operator/operatorAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.OPERATOR)
	public Object add( Operator operator){
		operator.setDeleteFlag(Constant.STRING_ZERO);
		operatorService.insert(operator);
		return renderSuccessResult();			
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:sys:user:edit")
	@RequestMapping(value = "/sys/operator/operatorEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public String editPage(String id, final Model model) {
		 model.addAttribute("roleList", roleService.findAll());
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("operator", operatorService.get(id));
			OperatorRole ur = new OperatorRole();
			ur.setOperatorId(id);
			 model.addAttribute("operatorRoleList", operatorRoleService.findList(ur));
		}
		return "/sys/operator/operatorEditPage";
	}
	
	/**
	 * 禁用或解禁后台用户
	 */
	@RequiresPermissions("vip:member:freeze")
	@RequestMapping(value="/sys/operator/operatorForbidden")
	@ResponseBody
	@SystemLog(method=SysLogConstant.FREEZE,content=SysLogConstant.USER)
	public Object freeze(String[] ids) {
		if(ids==null || ids.length==0){
			return renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		for(String id:ids) {
			Operator operator=operatorService.get(id);
			if(operator.getStatus().equals(Operator.USER_STATUS_LOCKED)) {
				operator.setStatus(Operator.DEFAULT_USER_STATUS);
				Map<String, AtomicInteger> loginFailMap =  (Map<String, AtomicInteger>)CacheUtils.get("loginFailMap", Map.class);
				loginFailMap.remove(operator.getLoginName());//清掉最大登录次数
				CacheUtils.set("loginFailMap", loginFailMap, ExpireTime.ONE_DAY);
			}
			else {
				operator.setStatus(Operator.USER_STATUS_LOCKED);
			}
			operatorService.updateStatus(operator);
		}
		return renderSuccessResult();
	}
	/**
	 * 编辑后台用户
	 */
	@RequiresPermissions("set:sys:user:edit")
	@RequestMapping(value = "/sys/operator/operatorEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public Object edit( Operator operator){
		operatorService.update(operator);
		return renderSuccessResult();			
	}
	
	/**
	 * 删除后台用户
	 */
	@RequiresPermissions("set:sys:user:del")
	@RequestMapping(value = "/sys/operator/operatorDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.OPERATOR)
	public Object del(String[] ids){
		if(ids==null || ids.length==0){
			return renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		operatorService.deleteBatch(ids);
		return renderSuccessResult();
	}
	
	/**
	 * 重置后台用户密码页面
	 */
	@RequestMapping(value = "/sys/operator/resetOperatorPwdPage", method = RequestMethod.POST)
	public String resetOperatorPwdPage(String id,final Model model){
		model.addAttribute("id", id);
		return "/sys/operator/resetOperatorPwdPage";
	}
	
	/**
	 * 重置后台用户密码
	 */
	@RequiresPermissions("set:sys:user:resetPwd")
	@RequestMapping(value = "/sys/operator/resetOperatorPwd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public Object resetOperatorPwd(final Operator operator){
		operatorService.resetOperatorPwd(operator);
		return renderSuccessResult();
	}
	
	/******************************** 个人设置 ********************************/
	/**
	 * 修改后台用户页面
	 */
	@RequestMapping(value = "/sys/operator/operatorLoginEditPage")
	@RequiresPermissions("set:operator:login:edit")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public String operatorLoginEditPage(final Model model) {
		Operator operator = getUser();
		 model.addAttribute("operator", operatorService.get(operator.getUuid()));
		 model.addAttribute(ConfigConstant.WEB_URL, ConfigUtils.getValue(ConfigConstant.WEB_URL));
		 model.addAttribute("roleList", roleService.findListByOperatorId(operator.getUuid()));
		return "/sys/operator/operatorLoginEditPage";
	}
	
	/**
	 * 修改后台用户
	 */
	@RequestMapping(value = "/sys/operator/operatorLoginEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public Object operatorLoginEdit(Operator operator){
		operatorService.update(operator);
		return renderSuccessResult();
	}
	
	/**
	 * 修改后台用户密码页面
	 */
	@RequestMapping(value = "/sys/operator/operatorChangePwdPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public String operatorChangePwdPage(){
		return "/sys/operator/operatorChangePwdPage";
	}
	
	/**
	 * 修改后台用户密码
	 */
	@RequestMapping(value = "/sys/operator/operatorChangePwd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public Object operatorChangePwd(final Operator operator){
		operatorService.updatePwd(operator);
		return renderSuccessResult();
	}
	/**
	 * 后台用户重置密码后修改密码页面
	 */
	@RequestMapping(value = "/sys/operator/operatorChangePasswordPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public String operatorChangePasswordPage(){
		return "/sys/operator/operatorChangePasswordPage";
	}
	
	/**
	 * 后台用户重置密码后修改后台用户密码
	 */
	@RequestMapping(value = "/sys/operator/operatorChangePassword", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.OPERATOR)
	public String operatorChangePassword(final Operator operator){
		operatorService.updatePwd(operator);
		return "/main";
	}
	/******************************** 角色用户 ********************************/
	
	@RequestMapping(value = "/sys/operator/findRoleOperators")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.OPERATOR_ROLE)
	public Object findRoleOperators(Operator operator){
		if(StringUtils.isBlank(operator.getRoleEq())){
			operator.setRoleEq(Operator.ROLE_EQ);
		}
		return operatorService.findRoleOperators(operator);
	}
	
	/**
	 * 保存角色用户
	 * @param id
	 * @param userIds
	 * @param response
	 */
	@RequestMapping(value = "/sys/operator/saveRoleOperators")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.OPERATOR_ROLE)
	public Object saveRoleOperators(String id, String [] userIds){
		if(StringUtils.isBlank(id)){
			return renderResult(false, "角色不能为空");
		}
		operatorRoleService.saveRoleOperators(id, userIds);
		return renderSuccessResult();
	}
	
	/**
	 * 删除角色用户
	 * @param id	角色id
	 * @param ids	用户Ids
	 */
	@RequiresPermissions("set:sys:role:user:del")
	@RequestMapping(value = "/sys/operator/deleteSelected")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.OPERATOR_ROLE)
	public Object deleteSelected(String id, String [] ids){
		if(StringUtils.isBlank(id)){
			return renderResult(false, "角色不能为空");
		}
		operatorRoleService.deleteSelected(id, ids);
		return renderSuccessResult();
	}
	
	
	
}

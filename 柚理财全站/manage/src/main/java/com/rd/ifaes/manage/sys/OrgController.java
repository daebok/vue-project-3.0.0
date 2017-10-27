package com.rd.ifaes.manage.sys;

import java.util.List;
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
import com.rd.ifaes.core.sys.domain.Org;
import com.rd.ifaes.core.sys.model.OrgModel;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.sys.service.OrgService;

/**
 * 组织机构管理
 * @author lh
 * @since 2016年5月19日 
 * @version 3.0
 */
@Controller
public class OrgController extends SystemController {
	
	public static final String ORG_ID_FIELD = "orgIdField";
	public static final String ORG_NAME_FIELD = "orgNameField";
	
	@Resource
	private OrgService orgService;
	@Resource
	private OperatorService operatorService;
	
	@RequestMapping(value = "/sys/org/orgManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ORG)
	public String manage(){
		return "/sys/org/orgManage";
	}
	
	@RequestMapping(value = "/sys/org/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ORG)
	public Page<Org> list(Org org){
		return  orgService.findPage(org);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:sys:org:add")
	@RequestMapping(value = "/sys/org/orgAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ORG)
	public String addPage(String parentId,final Model model) {
		 model.addAttribute("parentId", parentId);
		return "/sys/org/orgAddPage";
	}
	
	/**
	 * 添加组织机构
	 */
	@RequiresPermissions("set:sys:org:add")
	@RequestMapping(value = "/sys/org/orgAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.ORG)
	public Map<String, Object> add( Org org, BindingResult bindingResult){
		BeanValidators.validate(org);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		if(!bindingResult.hasErrors()){
			orgService.save(org);
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:sys:org:edit")
	@RequestMapping(value = "/sys/org/orgEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ORG)
	public String editPage(String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("org", orgService.get(id));			
		}
		return "/sys/org/orgEditPage";
	}
	
	/**
	 * 编辑组织机构
	 */
	@RequiresPermissions("set:sys:org:edit")
	@RequestMapping(value = "/sys/org/orgEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.ORG)
	public Map<String, Object> edit( Org org, BindingResult bindingResult){
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		BeanValidators.validate(org);
		if(!bindingResult.hasErrors()){
			orgService.update(org);
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 删除组织机构
	 */
	@RequiresPermissions("set:sys:org:del")
	@RequestMapping(value = "/sys/org/orgDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.ORG)
	public Map<String, Object> del(String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		if(orgService.validHasChildren(ids) > 0){
			return renderResult( false, "组织机构下还有子机构，请先删除子机构");
		}
		// 判断选中的组织机构中是否存在用户
		int num = operatorService.getNumByOrgIds(ids);
		if(num > 0){
			return renderResult( false, "组织机构下有用户存在，不能删除");
		}
		orgService.deleteBatch(ids);
		return renderSuccessResult();
	}
	
	/**
	 * 取得组织机构树
	 * @param response
	 */
	@RequestMapping(value = "/sys/org/getOrgTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ORG)
	public List<OrgModel> getOrgTree(String parentId){
		return orgService.getOrgTree(parentId);
	}
	
	/**
	 * 进入组织树页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sys/org/orgTreePage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.ORG)
	public String orgTreePage(HttpServletRequest request){
		request.setAttribute(ORG_ID_FIELD, request.getParameter(ORG_ID_FIELD));
		request.setAttribute(ORG_NAME_FIELD, request.getParameter(ORG_NAME_FIELD));
		return "/sys/org/orgTreePage";
	}
	

}

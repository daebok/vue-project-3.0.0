package com.rd.ifaes.manage.sys;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TreeNode;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.domain.Menu;
import com.rd.ifaes.core.sys.domain.Operation;
import com.rd.ifaes.core.sys.model.MenuModel;
import com.rd.ifaes.core.sys.service.MenuService;
import com.rd.ifaes.core.sys.service.OperationService;
import com.rd.ifaes.core.sys.service.PermissionService;

/**
 * 菜单管理 controller
 * @author Administrator
 *
 */
@Controller
public class MenuController extends SystemController {
	
	@Resource
	private MenuService menuService;
	@Resource
	private PermissionService permissionService;
	@Resource
	private OperationService operationService;
	
	/**
	 * 菜单管理
	 * @return
	 */
	@RequestMapping(value = "/sys/menu/menuManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public String manage(){		
		return "/sys/menu/menuManage";
	}
	
	@RequestMapping(value = "/sys/menu/getMenuUseList")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public Object getMenuUseList(Menu model){
		return menuService.getMenuUseList(model);
	}
	
	/**
	 * 获取菜单集合
	 */
	@RequestMapping(value = "/sys/menu/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public Object list(Menu menu){
		return menuService.findListByPage(menu);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:sys:menu:add")
	@RequestMapping(value = "/sys/menu/menuAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.MENU)
	public String addPage(String parentId, final Model model)throws Exception{	
		if (!StringUtils.isBlank(parentId)) {
			 model.addAttribute("parentId", parentId);
			 model.addAttribute("parentName", menuService.get(parentId).getMenuName());
			List<DictData> dictDataList = DictUtils.list("menuIcon");
			StringBuilder dictValue = new StringBuilder();
			if (CollectionUtils.isNotEmpty(dictDataList)) {
				for (DictData data : dictDataList) {
					dictValue.append(data.getItemValue() + Constant.SPEC_CHAR);
				}
			}
			 model.addAttribute("iconfont", dictValue);
		}
		return "/sys/menu/menuAddPage";
	}
	
	/**
	 * 添加菜单项
	 * @return 
	 */
	@RequiresPermissions("set:sys:menu:add")
	@RequestMapping(value = "/sys/menu/menuAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.MENU)
	public Map<String, Object> add( Menu menu,BindingResult bindingResult){		
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		BeanValidators.validate(menu);
		if (!bindingResult.hasErrors()) {
			//TODO token
			menuService.insert(menu);
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:sys:menu:edit")
	@RequestMapping(value = "/sys/menu/menuEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.MENU)
	public String editPage(String id, final Model model){
		if (StringUtils.isNotBlank(id)) {
			// 菜单详情
			Menu menu = menuService.get(id);
			 model.addAttribute("menu", menu);
			// 父节点名称
			if (!StringUtils.isBlank(menu.getParentId())) {
				List<DictData> dictDataList = DictUtils.list("menuIcon");
				StringBuffer dictValue = new StringBuffer();
				if (CollectionUtils.isNotEmpty(dictDataList)) {
					for (DictData data : dictDataList) {
						dictValue.append(data.getItemValue() + Constant.SPEC_CHAR);
					}
				}
				 model.addAttribute("iconfont", dictValue);
				 model.addAttribute("parentName", menuService.get(menu.getParentId()).getMenuName());
			}
			// 授权
			List<Operation> operationList = operationService.findByMenuId(id);
			 model.addAttribute("operationList", operationList);
		}
		return "/sys/menu/menuEditPage";
	}
	
	/**
	 * 编辑菜单项
	 * @return 
	 */
	@RequiresPermissions("set:sys:menu:edit")
	@RequestMapping(value = "/sys/menu/menuEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.MENU)
	public Map<String, Object> edit( Menu menu, BindingResult bindingResult,HttpServletRequest request){
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		BeanValidators.validate(menu);
		if (!bindingResult.hasErrors()) {
			//TODO token
			menuService.update(menu, request.getParameterValues(menu.getUuid()));
			resultMap = renderSuccessResult();			
		}
		return resultMap;
	}
	
	/**
	 * 删除菜单项
	 * @return 
	 */
	@RequiresPermissions("set:sys:menu:del")
	@RequestMapping(value = "/sys/menu/menuDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.MENU)
	public Map<String, Object> del(String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
		menuService.deleteBatch(ids);
		return renderSuccessResult();
	}
	
	/**
	 * 查询子菜单
	 * @param parentId
	 * @param response
	 * @return 
	 */
	public List<Menu> getChildren(String parentId, HttpServletResponse response){
		Menu model = new Menu();
		model.setParentId(parentId);
		return menuService.findList(model);
	}
	
	/**
	 * 取得菜单树
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "/sys/menu/getMenuTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public List<MenuModel> getMenuTree(String parentId, HttpServletResponse response){
		return menuService.getMenuTree(parentId);
	}
	
	/**
	 * 进入组织树页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sys/menu/menuTreePage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public String menuTreePage(final String parentId,final Model model){
		 model.addAttribute("parentId", parentId);
		return "/sys/menu/menuTreePage";
	}
	
	/**
	 * 异步加载已授权菜单
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/menu/findAuthMenu")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.MENU)
	public List<TreeNode> findAuthMenu(final String modelId){
		String operatorId = super.getUserId();
		return menuService.findAuthMenuList(operatorId, modelId);
	}

}

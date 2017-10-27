/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.manage.loan;

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
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.model.ProjectTypeModel;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 产品-类别管理
 * 
 * @author FangJun
 * @date 2016年7月14日
 */
@Controller
public class ProjectTypeManageController extends SystemController {

	@Resource
	private ProjectTypeService projectTypeService;
	
	/**
	 * 产品分类管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/project/type/typeManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROJECT_TYPE)
	public String manage() {
		return "/project/type/typeManage";
	}

	/**
	 * 根据传入所属类别查询列表
	 * 
	 * @param model 含所属类别
	 * @return 分类列表
	 */
	@RequestMapping(value = "/project/type/list")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROJECT_TYPE)
	public Object list(ProjectType model) {
		return projectTypeService.findListByPage(model);
	}

	/**
	 * 取得类别树
	 * 
	 * @return
	 */
	@RequestMapping(value = "/project/type/getTypeTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.PROJECT_TYPE)
	public List<ProjectTypeModel> getTypeTree(String parentId) {
		return projectTypeService.getTypeTree(parentId);
	}

	/**
	 * 添加页面
	 * 
	 * @return
	 */
	@RequiresPermissions("project:typeMan:add")
	@RequestMapping(value = "/project/type/typeAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PROJECT_TYPE)
	public String addPage(String parentId, Model model) {
		if (StringUtils.isNotBlank(parentId)) {
			model.addAttribute("parentId", parentId);
			ProjectType type = projectTypeService.get(parentId);
			if (type != null) {
				model.addAttribute("parentName", type.getTypeName());
			}
		}
		return "/project/type/typeAddPage";
	}

	/**
	 * 添加类别项
	 * 
	 * @return
	 */
	@RequiresPermissions("project:typeMan:add")
	@RequestMapping(value = "/project/type/typeAdd", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PROJECT_TYPE_ADD, dispatch = true)
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.PROJECT_TYPE)
	public Map<String, Object> add(final ProjectType model, BindingResult bindingResult) {
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		if (!bindingResult.hasErrors()) {
			projectTypeService.insert(model);
			resultMap = renderSuccessResult();
		}
		return resultMap;
	}

	/**
	 * 编辑页面
	 * 
	 * @return
	 */
	@RequiresPermissions("project:typeMan:edit")
	@RequestMapping(value = "/project/type/typeEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PROJECT_TYPE)
	public String editPage(String id, Model model) {
		if (StringUtils.isNotBlank(id)) {
			// 菜单详情
			ProjectType node = projectTypeService.get(id);
			model.addAttribute("node", node);
			// 父节点名称
			if (StringUtils.isNotBlank(node.getParentId())) {
				model.addAttribute("parentName", projectTypeService.get(node.getParentId()).getTypeName());
			}
			final int childNum= projectTypeService.countChilds(id);
		   model.addAttribute("childNum", childNum);
		}
		return "/project/type/typeEditPage";
	}

	/**
	 * 编辑菜单项
	 * 
	 * @return
	 */
	@RequiresPermissions("project:typeMan:edit")
	@RequestMapping(value = "/project/type/typeEdit", method = RequestMethod.POST)
	@ResponseBody
	@TokenValid(value = TokenConstants.TOKEN_PROJECT_TYPE_EDIT, dispatch = true)
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.PROJECT_TYPE)
	public Map<String, Object> edit(final ProjectType t, BindingResult bindingResult, HttpServletRequest request) {
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
		if (!bindingResult.hasErrors()) {
			projectTypeService.update(t);
			resultMap = renderSuccessResult();
		}
		return resultMap;
	}

	@RequiresPermissions("project:typeMan:del")
	@RequestMapping(value = "/project/type/typeDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.PROJECT_TYPE)
	public Map<String, Object> del(String[] ids) {
		projectTypeService.deleteBatch(ids);
		return renderSuccessResult();
	}

	/**
	 * 进入类别选择页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/project/type/typeTreePage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.PROJECT_TYPE)
	public String typeTreePage(String pageFlag,Model model) {
		// 页面标识（产品分类添加页面 parent 产品上架管理projectType)
		model.addAttribute("pageFlag", pageFlag);
		return "/project/type/typeTreePage";
	}
}

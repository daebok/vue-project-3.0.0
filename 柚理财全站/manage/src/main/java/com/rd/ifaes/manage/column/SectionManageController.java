package com.rd.ifaes.manage.column;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.model.SectionModel;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.manage.sys.SystemController;

@Controller
public class SectionManageController  extends SystemController {
	@Resource
	private SectionService sectionService;
	
	/**
	 * 栏目管理
	 * @return
	 */
	@RequestMapping(value = "/column/section/sectionManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SECTION)
	public String sectionManage(){	
		return "/column/section/sectionManage";
	}
	/**
	 * 添加栏目页面
	 * @return
	 */
	@RequestMapping(value = "/column/section/sectionAddPage")
	@RequiresPermissions("set:website:section:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.SECTION)
	public String addPage(String parentId,Model model){	
		if(!StringUtils.isBlank(parentId)){
			model.addAttribute("parentId", parentId);
			Section type= sectionService.get(parentId);
			if(type!=null){
			      model.addAttribute("parentName",type.getSectionName());
			}
		}
		return "/column/section/sectionAddPage";
	}
	/**
	 * 添加栏目
	 * @return 
	 */
	@RequiresPermissions("set:website:section:add")
	@RequestMapping(value = "/column/section/sectionAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.SECTION)
	public Map<String, Object> sectionAdd( Section model , HttpServletRequest request){	
			Section section = sectionService.getByCode(model.getSectionCode());
			if (section != null) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.SECTION_CODE_HAS_EXIST));
			}
			sectionService.insert(model);
			return renderSuccessResult();			
	}
	/**
	 * 取得栏目类别树
	 * @return 
	 */
	@RequestMapping(value = "/column/section/getSectionTree")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SECTION)
	public List<SectionModel> getSectionTree(String parentId){
		return sectionService.getSectionTree(parentId);
	}
	/**
	 * 根据传入所属类别查询列表
	 * @param model 含所属类别
	 * @return 分类列表
	 */
	@RequestMapping(value = "/column/section/list")
	@ResponseBody
	@RequiresPermissions("set:website:section:query")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SECTION)
	public Object list(Section model){
		return sectionService.findListByPage(model);
	}
	/**
	 * 进入类别树选择页面
	 * @return
	 */
	@RequestMapping(value = "/column/section/sectionTreePage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.SECTION)
	public String sectionTreePage(String parentId,Model model){
		model.addAttribute("parentId", parentId);
		return "/column/section/sectionTreePage";
	}
	/**
	 * 栏目编辑页
	 * @return
	 */
	@RequiresPermissions("set:website:section:add")
	@RequestMapping(value = "/column/section/sectionEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.SECTION)
	public String sectionEditPage(String id,Model model){
		if(StringUtils.isNotBlank(id)){
			//栏目详情
			Section section= sectionService.get(id);
			model.addAttribute("section",section);		
			//父节点名称
			if(!StringUtils.isBlank(section.getParentId())){
				model.addAttribute("parentName",sectionService.get(section.getParentId()).getSectionName());
			}
		}
		return "/column/section/sectionEditPage";
	}
	/**
	 * 编辑菜单项
	 * @return 
	 */
	@RequiresPermissions("set:website:section:add")
	@RequestMapping(value = "/column/section/sectionEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.SECTION)
	public Map<String, Object> edit( Section model ,HttpServletRequest request){
		Section section = sectionService.getByCodeId(model.getSectionCode(),model.getUuid());
		if (section != null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SECTION_CODE_HAS_EXIST));
		}
		sectionService.update(model);
		return renderSuccessResult();			
	}

	/**
	 * 删除栏目
	 * @return 
	 */
	@RequiresPermissions("set:website:section:add")
	@RequestMapping(value = "/column/section/sectionDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.SECTION)
	public Map<String, Object> sectionDel(String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult( false, "请勾选需要删除的记录");
		}
		sectionService.deleteBatch(ids);
		return renderSuccessResult();
	}
}

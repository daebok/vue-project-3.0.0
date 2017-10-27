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
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.domain.DictType;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.sys.service.DictTypeService;
/**
 * 字典管理 Controller
 * @author lh
 * @since 2016-5-24
 * @version 3.0
 */
@Controller
public class DictController extends SystemController {
	
	@Resource
	private DictTypeService dictTypeService;
	@Resource
	private DictDataService dictDataService;
	
	
	@RequestMapping(value = "/sys/dict/dictManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.DICT)
	public String manage(){
		return "/sys/dict/dictManage";
	}
		
	@RequestMapping(value = "/sys/dict/dictTypeList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.DICT)
	public Page<DictType> dictTypeList(DictType dictType){
		return dictTypeService.findPage(dictType);
	}
	
	@RequestMapping(value = "/sys/dict/dictDataList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.DICT)
	public Page<DictData> dictDataList(DictData dictData){
		return dictDataService.findPage(dictData);
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:dict:add")
	@RequestMapping(value = "/sys/dict/dictTypeAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.DICT)
	public String dictTypeAddPage(){		
		return "/sys/dict/dictTypeAddPage";
	}
	
	/**
	 * 添加页面
	 * @return
	 */
	@RequiresPermissions("set:dict:contentadd")
	@RequestMapping(value = "/sys/dict/dictDataAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.DICT)
	public String addPage(String dictType,final Model model) {
		if(StringUtils.isBlank(dictType)){
			throw new BussinessException("请先选择字典类型");
		}
		 model.addAttribute("dictTypeModel", dictTypeService.findByDictType(dictType));
		return "/sys/dict/dictDataAddPage";
	}
	
	/**
	 * 添加字典
	 */
	@RequiresPermissions("set:dict:add")
	@RequestMapping(value = "/sys/dict/dictTypeAdd")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.DICT)
	public Map<String, Object> dictTypeAdd(DictType dictType, BindingResult bindingResult){
		BeanValidators.validate(dictType);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	DictType dbDictType = dictTypeService.findByDictType(dictType.getDictType());
	    	if(dbDictType!=null){
	    		throw new BussinessException("类型标识已存在", BussinessException.TYPE_JSON);
	    	}
	    	dictTypeService.insert(dictType);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 添加字典
	 */
	@RequiresPermissions("set:dict:contentadd")
	@RequestMapping(value = "/sys/dict/dictDataAdd")
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.DICT)
	public Map<String, Object> add(DictData dictData, BindingResult bindingResult){
		BeanValidators.validate(dictData);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	DictData dbDictData = dictDataService.get(dictData.getDictType(), dictData.getItemValue());
	    	if(dbDictData != null){
	    		throw new BussinessException("该字典类型下的字典值已存在",BussinessException.TYPE_JSON);
	    	}
	    	
	    	dictDataService.insert(dictData);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:dict:edit")
	@RequestMapping(value = "/sys/dict/dictTypeEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.DICT)
	public String dictTypeEditPage(String id, final Model model) {
		if(StringUtils.isNotBlank(id)){
			 model.addAttribute("dict", dictTypeService.get(id));
		}
		return "/sys/dict/dictTypeEditPage";
	}
	
	/**
	 * 编辑页面
	 * @return
	 */
	@RequiresPermissions("set:dict:contentedit")
	@RequestMapping(value = "/sys/dict/dictDataEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.DICT)
	public String editPage(String id, final Model model) {
		if(StringUtils.isNotBlank(id)){
			DictData dictData = dictDataService.get(id);
			 model.addAttribute("dictData", dictData);
			
			DictType dictType = dictTypeService.findByDictType(dictData.getDictType());
			 model.addAttribute("dictTypeModel", dictType);
		}
		return "/sys/dict/dictDataEditPage";
	}
	
	/**
	 * 编辑字典
	 */
	@RequiresPermissions("set:dict:edit")
	@RequestMapping(value = "/sys/dict/dictTypeEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.DICT)
	public Map<String, Object> dictTypeEdit(DictType dictType, BindingResult bindingResult){
		BeanValidators.validate(dictType);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	dictTypeService.update(dictType);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
	
	/**
	 * 编辑字典
	 */
	@RequiresPermissions("set:dict:contentedit")
	@RequestMapping(value = "/sys/dict/dictDataEdit")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.DICT)
	public Map<String, Object> edit(DictData dictData, BindingResult bindingResult){
		BeanValidators.validate(dictData);
		Map<String, Object> resultMap = renderBindingResult(bindingResult);
	    if (!bindingResult.hasErrors()) {  
	    	dictDataService.update(dictData);
	    	resultMap = renderSuccessResult();	    	
	    }
	    return resultMap;
	}
		
	/**
	 * 删除字典
	 * @param ids
	 * @param response
	 */
/*	@RequestMapping(value = "/sys/dict/dictDel")
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.DICT)
	public Map<String, Object> del(String [] ids){
		if(ids == null || ids.length == 0){
			return renderResult(false, "请选择需要删除的记录");
		}
		dictDataService.deleteBatch(ids);
    	return renderSuccessResult();		
	}*/
	

	

}

/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.mapper.ProjectMapper;
import com.rd.ifaes.core.project.mapper.ProjectTypeMapper;
import com.rd.ifaes.core.project.model.ProjectTypeModel;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.sys.service.DictDataService;

/**
 * ServiceImpl:ProjectTypeServiceImpl
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("projectTypeService")
public class ProjectTypeServiceImpl extends BaseServiceImpl<ProjectTypeMapper, ProjectType> implements ProjectTypeService {
	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private ProjectService projectService;
	@Resource
	private DictDataService dictDataService;
	
	private static final String typeNamesKey = CacheConstant.KEY_PREFIX_PROJECT_TYPE + "typeNames";
	
	@Override
	public void deleteBatch(String[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD), BussinessException.TYPE_JSON);
		}
		for (String uuid : ids) {
			final ProjectType typeTemp = dao.get(uuid);
			// a) 当选择的类别下方有子类别时，点击“删除”提示“该类别下存在子类别不允许删除”
			final ProjectType typeModel = new ProjectType();
			typeModel.setParentId(uuid);
			typeModel.setDeleteFlag(DeleteFlagEnum.NO.getValue());
			final int childTypeNum = dao.getCount(typeModel);
			if (childTypeNum > 0) {
				throw new BussinessException(
						ResourceUtils.get("manage.projectType.deleteParentNodeError", typeTemp.getTypeName()),
						BussinessException.TYPE_JSON);
			}
			// b) 当选择的类别下方有产品时，点击“删除”提示“该类别下已有产品不允许删除”
			final Project projectModel = new Project();
			projectModel.setProjectTypeId(uuid);
			final int projectNum = projectMapper.getCount(projectModel);
			if (projectNum > 0) {
				throw new BussinessException(ResourceUtils.get("manage.projectType.deleteUsingError", typeTemp.getTypeName()),
						BussinessException.TYPE_JSON);
			}
		}

		super.deleteBatch(ids);
		for (String uuid : ids) {
			delFromCache(uuid);
		}	 	
	}

	@Override
	public void insert(ProjectType model) {
		BeanValidators.validate(model);
		model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		model.setNid(StringUtils.numToLetter(DateUtils.dateStr3(DateUtils.getNow())));
		checkProjectType(model);
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PROJECT_TYPE_ADD);
		super.insert(model);
	   addToCache(model);
	}

	/**
	 * 校验类别
	 * 
	 * @author FangJun
	 * @date 2016年8月2日
	 * @param model 类别
	 */
	private void checkProjectType(final ProjectType model) {
		String parentId = model.getParentId();
		if (StringUtils.isBlank(parentId)) {
			throw new BussinessException(ResourceUtils.get("project.projectTypeId.notEmpty"), BussinessException.TYPE_JSON);
		}
		ProjectType parent = dao.get(parentId);
		if (parent == null || DeleteFlagEnum.YES.eq(parent.getDeleteFlag())) {
			throw new BussinessException(ResourceUtils.get("project.projectType.noExists"), BussinessException.TYPE_JSON);
		}
		if (parent.getTypeLevel() >= ProjectType.TYPE_LEVEL_MAX) {
			throw new BussinessException(ResourceUtils.get("manage.projectType.typeLevel.bigError"),
					BussinessException.TYPE_JSON);
		}
		// 一级菜单查询排序是否重复 
		if(parent.getTypeLevel() == ProjectType.TYPE_LEVEL_MIN && dao.countSort(model.getSort(),parent.getTypeLevel()+1,model.getUuid())>0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_TYPE_SORT_CANNOT_REPEAT),
					BussinessException.TYPE_JSON);
		}
		model.setTypeLevel(parent.getTypeLevel() + 1);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(ProjectType model) {
		BeanValidators.validate(model);
		checkProjectType(model);
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PROJECT_TYPE_EDIT);
		 super.update(model);
       if(CommonEnum.YES.eq(model.getDeleteFlag())){
			delFromCache(model.getUuid());
		}else{
			addToCache(model);
		}
	}
	
	/**
	 * 项目类型添加到缓存
	 * @param model
	 */
	private void addToCache(ProjectType model) {
		boolean keyExists = CacheUtils.exists(typeNamesKey);
		CacheUtils.addMap(typeNamesKey, model.getUuid(), model.getTypeName());	
		if(!keyExists){//首次使用时添加缓存时效
			CacheUtils.expire(typeNamesKey, ExpireTime.ONE_HOUR.getTime());			
		}
	}

	/**
	 * 删除项目类型缓存
	 * @param uuid
	 */
	private void delFromCache(String uuid) {
		CacheUtils.delMapField(typeNamesKey, uuid);
	}
	
	@Override
	public Page<ProjectType> findListByPage(ProjectType model) {
		Page<ProjectType> page = model.getPage();
		page.setRows(dao.getTypeTree(model.getParentId()));
		return page;
	}

	@Override
	public List<ProjectTypeModel> getTypeTree(String parentId) {
		List<ProjectTypeModel> list = new ArrayList<ProjectTypeModel>();
		// 一级菜单集合
		List<ProjectType> rootChildNodes = dao.getCurrType(null);
		
		// 获取需要展开的所有id
		List<String> listExpanded = new ArrayList<>();
		if(StringUtils.isNotBlank(parentId)){
			listExpanded.add(parentId);
			getExpandedId(listExpanded,parentId);
		}

		// 递归迭代菜单集合
		for (ProjectType type : rootChildNodes) {
			ProjectTypeModel model = ProjectTypeModel.getInstance(type);
			model.setNodes(getAllChildren(type.getUuid(),listExpanded));
			model.setPid(type.getParentId());
			for(String expandedId : listExpanded){
    			if(expandedId.equals(type.getUuid())){
    				model.setExpanded(true);
    			}
    		}
			list.add(model);
		}
		return list;
	}
	
	/**获取所有的展开id集合*/
	private List<String> getExpandedId(List<String> listExpanded,final String id) {
		ProjectType temp = dao.get(id);
		if(temp != null && temp.getParentId() != null){
			listExpanded.add(temp.getParentId());
			listExpanded = getExpandedId(listExpanded,temp.getParentId());
		}
		return listExpanded;
	}

	public List<ProjectTypeModel> getAllChildren(String parentId, List<String> listExpanded) {
		List<ProjectTypeModel> list = new ArrayList<ProjectTypeModel>();
		ProjectType temp = new ProjectType();
		temp.setParentId(parentId);
		temp.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		temp.setPage(null);
		List<ProjectType> children = this.findList(temp);
		for (ProjectType t : children) {
			ProjectTypeModel currModel = ProjectTypeModel.getInstance(t);
			list.add(currModel);
			ProjectType current = new ProjectType();
			current.setParentId(t.getUuid());
			current.setPage(null);
			for(String expandedId : listExpanded){
    			if(expandedId.equals(t.getUuid())){
    				currModel.setExpanded(true);
    			}
    		}
			currModel.setPid(t.getParentId());
			if (this.getCount(current) > 0) {
				currModel.setNodes(getAllChildren(t.getUuid(),listExpanded));
			}
		}
		return list;
	}

	@Override
	public void setAllParentList(String uuid, List<ProjectType> list) {
		// ProjectType
		ProjectType temp = this.get(uuid);
		if (temp != null && StringUtils.isNotBlank(temp.getParentId())) {
			setAllParentList(temp.getParentId(), list);
			list.add(temp);
		}
	}

	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_PROJECT_TYPE_CHILD , expire = ExpireTime.TEN_MIN)
	public List<ProjectType> findChilds(String parentId) {
		if (StringUtils.isBlank(parentId)) {
			throw new BussinessException(ResourceUtils.get("project.projectTypeId.notEmpty"), BussinessException.TYPE_JSON);
		}
		ProjectType parent = dao.get(parentId);
		if (parent == null || DeleteFlagEnum.YES.eq(parent.getDeleteFlag())) {
			throw new BussinessException(ResourceUtils.get("project.projectType.noExists"), BussinessException.TYPE_JSON);
		}
		final List<ProjectType> list = new ArrayList<ProjectType>();
		if (StringUtils.isNotBlank(parentId)) {
			final ProjectType model = new ProjectType();
			model.setParentId(parentId);
			model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
			list.addAll(this.findList(model));
		}

		return list;
	}

	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_PROJECT_TYPE_QUERY_CONDITION , expire = ExpireTime.TEN_MIN)
	public  Map<String,Object> queryCondition(String  projectTypeId) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("projectTypeList", findChilds(projectTypeId));
		conditionMap.put("amountCondition", dictDataService.findListByDictType("amountCondition"));
		conditionMap.put("timeCondition", dictDataService.findListByDictType("timeCondition"));
		conditionMap.put("repayStyle", dictDataService.findListByDictType("repayStyle"));
		return conditionMap;
	}

	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_ORGPROJECT_FEATURES , expire = ExpireTime.TEN_MIN)
	public String getOrgProjectFeatures(String projectId) {
		final Project project = projectService.get(projectId);
		final ProjectType type = dao.get(project.getProjectTypeId());
		return type.getFeatures();
	}
 
	@Override
	public int countChilds(String projectTypeId) {
		return dao.countChilds(projectTypeId);
	}
	
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_PROJECT_TYPE+":typeName:{uuid}" , expire = ExpireTime.TEN_MIN)
	public String getProjectTypeName(String uuid) {
		return dao.getProjectTypeName(uuid);
	}
	
@Override
	public Map<String, String> findProjectTypeNames() {		
		Map<String, String> typeNames = CacheUtils.mget(typeNamesKey, String.class);
		if(typeNames!=null && typeNames.size() > 0){
			return typeNames;
		}
		typeNames = new HashMap<>();
		ProjectType model = new ProjectType();
		model.setDeleteFlag(CommonEnum.NO.getValue());
		List<ProjectType> list = this.findList(model);
		for (ProjectType projectType : list) {
			typeNames.put(projectType.getUuid(), projectType.getTypeName());
		}
		CacheUtils.addMap(typeNamesKey, typeNames);
		//设置缓存时效，防止缓存和数据库长期不一致
		CacheUtils.expire(typeNamesKey, ExpireTime.ONE_HOUR.getTime());
		return typeNames;
	}

	@Override
	public List<ProjectType> findByTypeLevel(int typeLevel){
		return dao.findByTypeLevel(typeLevel);
	}
	
	@Override
	public List<ProjectType> findUseProjectType(){
		return dao.findUseProjectType();
	}

}
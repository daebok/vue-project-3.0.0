package com.rd.ifaes.core.project.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.model.ProjectTypeModel;

/**
 * Service Interface:ProjectTypeService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectTypeService extends BaseService<ProjectType>{
 
	/**
	 * 查找子类包括父类
	 * @param model
	 * @return
	 */
	Page<ProjectType> findListByPage(ProjectType model);
	/**
	 * 类别树查询
	 * @param parentId 父类ID
	 * @return
	 */
	  List<ProjectTypeModel> getTypeTree(String parentId) ;
	/**
	 * 
	 * 查询所有父节点id
	 * @author wyw
	 * @date 2016-7-29
	 * @param uuid
	 * @param list
	 */
	   void setAllParentList(String uuid,List<ProjectType> list);
	
	  /**
	   * 查询指定项目分类的子分类
	   * @author  FangJun
	   * @date 2016年8月10日
	   * @param parentId 项目分类（父节点）ID
	   * @return 子分类列表
	   */
	  List<ProjectType> findChilds(String parentId);
	  /**
	   * 根据项目分类ID，获取产品列表查询条件
	   * @author  FangJun
	   * @date 2016年8月11日
	   * @param projectTypeId 项目分类ID
	   * @return 产品列表查询条件
	   */
      Map<String,Object> queryCondition(String  projectTypeId);
      
      
    /**
     * 查询变现原产品特点(缓存)
     * @author fxl
     * @date 2016年8月29日
     * @param projectId
     * @return
     */
    String getOrgProjectFeatures(String projectId);
    
    /**
     * 查询指定分类的子分类（未删除）个数
     * @author  FangJun
     * @date 2016年9月19日
     * @param projectTypeId 指定分类
     * @return 子分类（未删除）个数
     */
    int countChilds(String  projectTypeId);
    
    /**
     * 取得类型名称
     * @param uuid
     * @return
     */
    String getProjectTypeName(String uuid);
    
	/**
	 * 查找一级类别列表
	 * @param typeLevel
	 * @return
	 */
	 List<ProjectType> findByTypeLevel(int typeLevel);
	  /**
     * 查询项目类型名称
     * @return
     */
    Map<String, String> findProjectTypeNames();
    
    /**
     * 查找有效产品类型
     * @return
     */
	List<ProjectType> findUseProjectType();
}
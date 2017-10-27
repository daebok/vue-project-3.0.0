package com.rd.ifaes.core.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectType;

/**
 * Dao Interface:ProjectTypeMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectTypeMapper extends BaseMapper<ProjectType> {
	
	/**
	 * 取得菜单结构树
	 * @param id
	 * @return
	 */
	List<ProjectType> getTypeTree(String id);
	
	/**
	 * 获得本级菜单
	 * @param id
	 * @return
	 */
	List<ProjectType> getCurrType(String id);
	  /**
     * 查询指定分类的子分类（未删除）个数
     * @author  FangJun
     * @date 2016年9月19日
     * @param projectTypeId 指定分类
     * @return 子分类（未删除）个数
     */
    int countChilds(String  projectTypeId);
    
    /**
     * 查询排序是否有重复
     * @author fxl
     * @date 2016年12月28日
     * @param sort
     * @param typeLevel
     * @param uuid
     * @return
     */
    int countSort(@Param("sort")Integer sort,@Param("typeLevel")Integer typeLevel,@Param("uuid")String uuid);
    
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
	  * 查找有效产品类型
	  * @return
	  */
	 List<ProjectType> findUseProjectType();
}
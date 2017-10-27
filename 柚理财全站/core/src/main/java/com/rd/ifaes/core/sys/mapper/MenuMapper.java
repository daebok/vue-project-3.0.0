package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Menu;

/**
 * Dao Interface:MenuMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface MenuMapper extends BaseMapper<Menu> {
	
	/**
	 * 用户授权菜单
	 * @param model
	 * @return
	 */
	List<Menu> getMenuUseList(Menu model);
	
	/**
	 * 取得菜单结构树
	 * @param id
	 * @return
	 */
	List<Menu> getMenuTree(String id);
	
	/**
	 * 获得本级菜单
	 * @param id
	 * @return
	 */
	List<Menu> getCurrMenu(String id);
	
	/**
	 * 查子菜单
	 * @param parentId
	 * @return
	 */
	List<Menu> findChildren(String parentId);
	
	int getCountByCode(Menu menu);
	
	
	/**
	 *  查询用户已授权的菜单 
	 * @param operatorId parentId
	 * @return List<Menu>
	 * @author xxb
	 * @date 2016年8月19日
	 */
	List<Menu> findAuthMeun(@Param("operatorId") String operatorId, @Param("parentId") String parentId);
	
	/**
	 *  查询用户已授权模块
	 * @param  operatorId
	 * @return List<Menu>
	 * @author xxb
	 * @date 2016年8月21日
	 */
	List<Menu> findAuthModel(@Param("operatorId") String operatorId);
	
}
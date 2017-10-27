package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.TreeNode;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Menu;
import com.rd.ifaes.core.sys.model.MenuModel;

/**
 * Service Interface:MenuService
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
public interface MenuService extends BaseService<Menu>{
	
	/**
	 * 用户授权菜单
	 * @param model
	 * @return
	 */
	List<Menu> getMenuUseList(Menu model);

	/**
	 * 菜单组织树
	 * @param id
	 * @return
	 */
	List<MenuModel> getMenuTree(String id);

	/**
	 * 查找子类包括父类
	 * @param menu
	 * @return
	 */
	Page<Menu> findListByPage(Menu menu);

	/**
	 * 批量删除menu和permission
	 * @param ids
	 * @return
	 */
	void deleteBatchByMenuIds(String[] ids);

	/**
	 * 更新menu
	 * @param menu
	 * @param permissionIds
	 * @return
	 */
	void update(Menu menu, String[] permissionIds);
	
	
	/**
	 * 
	 * 查询用户授权分级菜单
	 * @param  operatorId
	 * @param  menuId
	 * @return List<Menu>
	 * @author xxb
	 * @date 2016年8月20日
	 */
	List<Menu> findSubMenuList(String operatorId, String menuId);
	
	
	List<Menu> findChildrenMenu(String parentId);
	
	/**
	 *  根据模块获取已授权的菜单
	 * @param  
	 * @return List<TreeNode>
	 * @author xxb
	 * @date 2016年8月21日
	 */
	List<TreeNode> findAuthMenuList(String operatorId, String modelId);
	
	
	/**
	 *  根据模块获取已授权的模块（注意：各模块下的菜单层级为两层或三层 ， 统计时需要考虑三层的情形）
	 * @param  
	 * @return List<TreeNode>
	 * @author xxb
	 * @date 2016年8月21日
	 */
	public List<Menu> findAuthModel(String operatorId);

}
package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.Node;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TreeNode;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Menu;
import com.rd.ifaes.core.sys.mapper.MenuMapper;
import com.rd.ifaes.core.sys.mapper.PermissionMapper;
import com.rd.ifaes.core.sys.mapper.RolePermissionMapper;
import com.rd.ifaes.core.sys.model.MenuModel;
import com.rd.ifaes.core.sys.service.MenuService;

/**
 * ServiceImpl:MenuServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
@Service("menuService") 
public class MenuServiceImpl  extends BaseServiceImpl<MenuMapper, Menu> implements MenuService{
	
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    
    @Override
    public List<Menu> findAll() {
    	return super.findAll();
    }
    
    @Override
    @Transactional(readOnly = false)
    public void save(Menu entity) {
    	preInsUpd(entity);
    	super.save(entity);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void insert(Menu entity) {
    	//check
    	int count = menuMapper.getCountByCode(entity);
    	if(count>0){
    		throw new BussinessException(ResourceUtils.get("global.code.exists", entity.getCode()));
    	}
    	//insert
    	preInsUpd(entity);
    	entity.preInsert();
    	menuMapper.insert(entity);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void update(Menu entity) {
    	//check
    	int count = menuMapper.getCountByCode(entity);
    	if(count>0){
    		throw new BussinessException(ResourceUtils.get("global.code.exists", entity.getCode()));
    	}
    	//insert    	
    	preInsUpd(entity);
    	super.update(entity);
    }
    
    private void preInsUpd(Menu entity){
    	if(StringUtils.isNotBlank(entity.getParentId())){
    		Menu parent = get(entity.getParentId());
    		String parentIds = StringUtils.isBlank(parent.getParentIds()) ? parent.getUuid() : 
    				( parent.getParentIds().concat(":").concat(parent.getUuid()) );
    		entity.setParentIds(parentIds);
    	}
    }

    @Override
    public List<Menu> getMenuUseList(Menu model) {
    	return menuMapper.getMenuUseList(model);
    }
    
    @Override
    public List<MenuModel> getMenuTree(String selectId) {
    	List<MenuModel> list = new ArrayList<MenuModel>();
    	//一级菜单集合
    	List<Menu> rootMenus = menuMapper.getCurrMenu(null);
    	
    	// 获取需要展开的所有id
		List<String> listExpanded = new ArrayList<>();
		if(StringUtils.isNotBlank(selectId)){
			listExpanded.add(selectId);
			getExpandedId(listExpanded,selectId);
		}
    	
    	//递归迭代菜单集合
    	for (Menu menu : rootMenus) {
    		MenuModel model=new MenuModel();
    		model.setId(menu.getUuid());
    		model.setCode(menu.getCode());
    		model.setText(menu.getMenuName());
    		model.setFlag(MenuModel.MENU_HAS_NODES_YES);
    		model.setNodes(getAllChildren(menu.getUuid(), selectId,listExpanded));
    		model.setSelected(menu.getUuid().equals(selectId)?true:false);
    		model.setPid(menu.getParentId());
    		for(String expandedId : listExpanded){
    			if(expandedId.equals(menu.getUuid())){
    				model.setExpanded(true);
    			}
    		}
    		list.add(model);
		}
    	return list;
    }
    
    /**获取所有的展开id集合*/
	private List<String> getExpandedId(List<String> listExpanded,final String id) {
		Menu temp = dao.get(id);
		if(temp != null && temp.getParentId() != null){
			listExpanded.add(temp.getParentId());
			listExpanded = getExpandedId(listExpanded,temp.getParentId());
		}
		return listExpanded;
	}
    
    public List<MenuModel> getAllChildren(String parentId, String selectId, List<String> listExpanded){
    	List<MenuModel> list = new ArrayList<MenuModel>();
    	List<Menu> children = findList(new Menu(parentId));
    	for (Menu menu : children) {
    		MenuModel model=new MenuModel();
    		model.setId(menu.getUuid());
    		model.setCode(menu.getCode());
    		model.setText(menu.getMenuName());
    		model.setFlag(MenuModel.MENU_HAS_NODES_NO);
    		model.setNodes(getAllChildren(menu.getUuid(), selectId,listExpanded));
    		model.setSelected(menu.getUuid().equals(selectId)?true:false);
    		model.setPid(menu.getParentId());
    		for(String expandedId : listExpanded){
    			if(expandedId.equals(menu.getUuid())){
    				model.setExpanded(true);
    			}
    		}
    		list.add(model);
		}
    	return list;
    }
    
    @Override
    public Page<Menu> findListByPage(Menu menu) {
		Page<Menu> page = menu.getPage();
		page.setRows(menuMapper.getMenuTree(menu.getParentId()));
		return page;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void deleteBatchByMenuIds(String[] ids) {
    	//删除菜单权限
//    	permissionMapper.deleteBatchByMenuIds(ids);
    	rolePermissionMapper.deleteBatchByMenuIds(ids);
    	//删除菜单
    	super.deleteBatch(ids);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void update(Menu menu,String[] codes) {
    	//删除菜单权限
/*		Permission model= new Permission();
		model.setMenuId(menu.getUuid());
		List<Permission> permissionList = permissionMapper.findList(model);
		for (Permission permission : permissionList) {
			boolean checked=false;
			if(codes!=null && codes.length > 0){ //至少选中一个
				for(String code:codes){
					if(code.equals(permission.getCode())){ //启用
						checked=true;
						break;
					}
				}
			}
			permission.setIsChecked(checked);
			if(checked){
				permission.setStatus("1");
			}else{
				permission.setStatus("-1");
			}
			permissionMapper.update(permission);
		}*/
    	//删除菜单
    	super.update(menu);
    }

    
	@Override
	public List<Menu> findSubMenuList(String operatorId, String menuId) {
		return menuMapper.findAuthMeun(operatorId, menuId);
	}

	@Override
	public List<Menu> findChildrenMenu(String parentId) {
		return menuMapper.findChildren(parentId);
	}

	@Override
	public List<TreeNode> findAuthMenuList(String operatorId, String modelId) {
		
		List<TreeNode> treeNodeList = Lists.newLinkedList();
		// 查询默认模块下所有直接子菜单
		List<Menu> topMenuList = findChildrenMenu(modelId);
		if (CollectionUtils.isEmpty(topMenuList)) {
			return treeNodeList;
		}
		
		// 查询默认模块下已授权的菜单
		List<Menu> authMenuList = findSubMenuList(operatorId, modelId);
		if(CollectionUtils.isEmpty(authMenuList)){
			return treeNodeList;
		}
		
		for (Menu topMenu : topMenuList) {
			TreeNode parentNode = new TreeNode();
			Node node = new Node(topMenu.getUuid(), topMenu.getMenuName(), topMenu.getUrl(), topMenu.getIconCss());
			parentNode.setNode(node);
			
			if(Constant.LEAF_VALUE.equals(topMenu.getIsLeaf())){  //叶子节点
				for (Menu authMenu : authMenuList) {
					if (topMenu.getUuid().equals(authMenu.getUuid())) {
						parentNode.setIsLeaf(Constant.LEAF_VALUE);
					}
				}
			}else{
				for (Menu authMenu : authMenuList) {
					if (topMenu.getUuid().equals(authMenu.getParentId())) {
						Node authNode = new Node(authMenu.getUuid(), authMenu.getMenuName(), authMenu.getUrl(), topMenu.getIconCss());
						parentNode.addChildNode(authNode);
					}
				}
			}
			
			//叶子节点(二级菜单)或有子节点(三级菜单)
			if (Constant.LEAF_VALUE.equals(parentNode.getIsLeaf()) || parentNode.getChildNode().size() != 0) {
				treeNodeList.add(parentNode);
			}
		}
		return treeNodeList;
	}

	
	@Override
	public List<Menu> findAuthModel(String operatorId) {
		return menuMapper.findAuthModel(operatorId);
	}
}
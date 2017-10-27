package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.ZTreeNode;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Menu;
import com.rd.ifaes.core.sys.domain.Operation;
import com.rd.ifaes.core.sys.domain.Permission;
import com.rd.ifaes.core.sys.domain.Role;
import com.rd.ifaes.core.sys.domain.RolePermission;
import com.rd.ifaes.core.sys.mapper.MenuMapper;
import com.rd.ifaes.core.sys.mapper.OperationMapper;
import com.rd.ifaes.core.sys.mapper.OperatorMapper;
import com.rd.ifaes.core.sys.mapper.PermissionMapper;
import com.rd.ifaes.core.sys.mapper.RoleMapper;
import com.rd.ifaes.core.sys.mapper.RolePermissionMapper;
import com.rd.ifaes.core.sys.model.RolePermModel;
import com.rd.ifaes.core.sys.service.RoleService;
import com.rd.ifaes.manage.security.SystemAuthorizingRealm;

/**
 * ServiceImpl:RoleServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
@Service("roleService") 
public class RoleServiceImpl  extends BaseServiceImpl<RoleMapper, Role> implements RoleService{
	
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private OperatorMapper operatorMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private MenuMapper menuMapper;
    
    
    /************************************构建权限树************************************/    
    
//    /********************
//     * list实现
//     ********************/
//    @Override
//    public List<RolePermModel> findRolePermTree(String id) {
//    	List<RolePermModel> list = new ArrayList<RolePermModel>();
//    	//角色已授权限集合
//    	List<String> rolePermissions = findRolePermissions(id);
//    	List<Menu> menuList = menuMapper.findAll();
//    	for (Menu menu : menuList) {
//    		RolePermModel menuModel = new RolePermModel(menu.getId(),menu.getParentId(),menu.getNid(),
//    				RolePermModel.ROLE_PERM_FLAG_MENU,false,menu.getSort(),menu.getName());
//    		list.add(menuModel);
//    		List<Operation> operationList = operationMapper.findListByMenuId(menu.getId());
//    		if(operationList == null || operationList.size() == 0){
//    			continue;
//    		}
//    		for (Operation operation : operationList) {
//    			String nid = menu.getNid().concat(":").concat(operation.getNid());
//    			String _id = menu.getId().concat(":").concat(operation.getId());
//    			RolePermModel operationModel = new RolePermModel(_id, menu.getId(),nid,
//    					RolePermModel.ROLE_PERM_FLAG_OPERATION,
//    					rolePermissions.contains(nid), 
//    					operation.getSort(), operation.getName());
//        		list.add(operationModel);
//			}
//		}
//    	return list;
//    }
    
    
    /********************
     * tree 实现
     ********************/
    @Override
    public List<RolePermModel> findRolePermTree(final String id) {
    	final List<RolePermModel> list = new ArrayList<RolePermModel>();
    	
    	//一级菜单集合
    	final List<Menu> rootMenus = menuMapper.getCurrMenu(null);
    	
    	//角色已授权限集合
    	final List<String> rolePermissionIds = findRolePermissionIds(id);   
    	
    	//递归迭代菜单集合
    	for (Menu menu : rootMenus) {
    		RolePermModel model = new RolePermModel();
    		model.setId(menu.getUuid());
    		model.setCode(menu.getCode());
    		model.setFlag(RolePermModel.ROLE_PERM_FLAG_MENU);
    		model.setText(menu.getMenuName());
    		model.setNodes(getMenuChildren(menu.getUuid(), rolePermissionIds));
    		list.add(model);
		}
    	return list;
    }
    
    /**
     * 查找角色对应的权限集合
     * @param id
     * @return
     */
    private List<String> findRolePermissionIds(final String id){
    	List<String> permissions = permissionMapper.findIdListByRoleId(id);
    	if(permissions == null) permissions = new ArrayList<String>();
    	return permissions;
    }
    
    /**
     * 取得所有子菜单
     * @param parentId
     * @param rolePermissions
     * @return
     */
    private List<RolePermModel> getMenuChildren(final String parentId,  final List<String> rolePermissionIds){
    	final List<RolePermModel> list = new ArrayList<RolePermModel>();
    	final List<Menu> children = menuMapper.findChildren(parentId);
    	for (Menu menu : children) {
    		RolePermModel model = new RolePermModel();
    		model.setId(menu.getUuid());
    		model.setCode(menu.getCode());
    		model.setFlag(RolePermModel.ROLE_PERM_FLAG_MENU);
    		model.setText(menu.getMenuName());
			Menu current = new Menu();
			current.setParentId(menu.getUuid());
			int subCount = menuMapper.getCount(current);
			if( subCount > 0){
				//子菜单
				model.setNodes(getMenuChildren(menu.getUuid(), rolePermissionIds));
			}else{
				//操作集合
				model.setNodes(getOperationChildren(menu, rolePermissionIds));	
				subCount = model.getNodes().size();
			}		
			if(subCount == 0){
				continue;
			}
			list.add(model);
		}
    	return list;
    }
    
    /**
     * 取得菜单下的操作
     * @param menu
     * @param rolePermissions
     * @return
     */
    private List<RolePermModel> getOperationChildren(final Menu menu, final List<String> rolePermissionIds){
    	final List<RolePermModel> list = new ArrayList<RolePermModel>();
    	//TODO 尚未进行测试
    	
    	final String userId = PrincipalUtils.getSubject().hasRole(Constant.ROLE_CODE_ADMIN)?null:PrincipalUtils.getCurrUserId();
    	final List<Operation> children = operationMapper.findListByMenuId(menu.getUuid(), userId);
    	
    	
    	for (Operation operation : children) {
    		RolePermModel model = new RolePermModel();
    		model.setId(operation.getPermissionId());
    		String code =  menu.getCode().concat(":").concat(operation.getCode());
    		model.setCode(code);
    		model.setFlag(RolePermModel.ROLE_PERM_FLAG_OPERATION);
    		model.setSort(operation.getSort());
    		model.setText(operation.getOperationName());
    		model.setState(rolePermissionIds.contains( operation.getPermissionId() ));
			list.add(model);			
		}
    	return list;
    }
    
    /************************************保存角色权限************************************/    
    @Override
    @Transactional(readOnly = false)
    public void saveRolePermissions(final String roleId, final String[] ids) {
    	//删除原有授权
    	RolePermission model = new RolePermission();
    	model.setRoleId(roleId);
    	rolePermissionMapper.deleteByRoleId(roleId);
    	//清除授权信息
//    	AbstractJedisSessionDAO sessionDAO = SpringContextHolder.getBean("sessionDAO");
//		CacheUtils.del(sessionDAO.getSessionKeyPrefix() + Constant.CACHE_STRING);    	
    	SystemAuthorizingRealm authRealm = SpringContextHolder.getBean(SystemAuthorizingRealm.class);
    	authRealm.clearAllCachedAuthorizationInfo();
    	
    	String pids [] = arrayUnique(ids);
    	//添加现有授权
    	if(pids != null && pids.length > 0){
    		List<RolePermission> list = new ArrayList<RolePermission>();
    		for (String permissionId : pids) {
    			list.add(new RolePermission(IdGen.uuid(), roleId, permissionId));
    		}
    		if(!list.isEmpty()){
    			 rolePermissionMapper.insertBatch(list);    		
    		}
    	}
    }
    
    //去除数组中重复的记录
    public static String[] arrayUnique(String[] a) {
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < a.length; i++) {
            if(StringUtils.isNotBlank(a[i]) && !list.contains(a[i])) {
                list.add(a[i]);
            }
        }
        if(list.isEmpty()){
        	return null;
        }
        return list.toArray(new String[list.size()]);
    }

	@Override
	public Role findRoleByCode(String code) {
		Role role=new Role();
		role.setCode(StringUtils.isNull(code));
		List<Role> list=dao.findList(role);
		if (!list.isEmpty()) {
			role=list.get(0);
		}
		return role;
	}
	
	@Override
	public void insert(Role model){
		List<Role> list=dao.findByRoalName(model.getRoleName());
		if (!list.isEmpty()) {   //如果要增加的角色类型已存在但被逻辑删除，则覆盖原来的并修改删除状态
			Role role = list.get(0);
			role.setRemark(model.getRemark());
			role.setDeleteFlag(Constant.FLAG_NO);
			dao.update(role);
		}
		else{
			super.insert(model);
		}
		
	}
	
	@Override
	public void deleteChildren(String[] ids) {
		if (dao.checkDefaultRole(ids)>0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ROLE_DELETE_ERROR));
		}
		dao.deleteChildren(ids);
		super.deleteBatch(ids);
	}

	@Override
	public List<Role> findListByOperatorId(String uuid) {
		return dao.findListByOperatorId(uuid);
	}

	
	@Override
	public List<ZTreeNode> findAuthTree(String roleId) {
		
		// 虚拟根节点
		ZTreeNode rootNode = new ZTreeNode();
		rootNode.setId(ZTreeNode.TREE_ROOT_ID);
		rootNode.setName("权限");
		rootNode.setOpen(true);
		rootNode.setpId(ZTreeNode.TREE_ROOT_PID);
		rootNode.setType(ZTreeNode.TYPE_V);

		// 角色已授权限集合
		final List<Permission> permissions = permissionMapper.findListByRole(roleId);
		Set<String> menuSet = new HashSet<String>();
		Set<String> perssionSet = new HashSet<String>();
		for (Permission p : permissions) {
			menuSet.add(p.getMenuId());
			perssionSet.add(p.getUuid());
		}

		// 处理菜单
		Menu model = new Menu();
		model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		final List<Menu> menuList = menuMapper.findList(model);
		ZTreeNode node = null;
		List<ZTreeNode> zTreeList = new ArrayList<ZTreeNode>();
		for (Menu menu : menuList) {
			node = new ZTreeNode();
			node.setId(menu.getUuid());
			node.setName(menu.getMenuName());
			node.setpId(ObjectUtils.defaultIfNull(menu.getParentId(), ZTreeNode.TREE_ROOT_ID));
			node.setChecked(!menuSet.add(menu.getUuid()));
			node.setType(ZTreeNode.TYPE_M);
			zTreeList.add(node);
		}

		// 处理权限（按钮）
		final List<Permission> permissionList = permissionMapper.findAllOperation();
		for (Permission perm : permissionList) {
			node = new ZTreeNode();
			node.setId(perm.getUuid());
			node.setName(perm.getOperationName());
			node.setpId(perm.getMenuId());
			node.setChecked(!perssionSet.add(perm.getUuid()));
			node.setType(ZTreeNode.TYPE_P);
			zTreeList.add(node);
		}
		zTreeList.add(rootNode);
		return zTreeList;
	}
    
}
package com.rd.ifaes.core.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.Operation;
import com.rd.ifaes.core.sys.domain.Permission;
import com.rd.ifaes.core.sys.mapper.PermissionMapper;
import com.rd.ifaes.core.sys.service.PermissionService;

/**
 * ServiceImpl:PermissionServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
@Service("permissionService") 
public class PermissionServiceImpl  extends BaseServiceImpl<PermissionMapper, Permission> implements PermissionService{
	
	public List<Operation> findOperationByMenuId(String menuId){
		return null;
	}
}
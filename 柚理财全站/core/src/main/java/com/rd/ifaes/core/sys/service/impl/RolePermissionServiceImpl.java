package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.RolePermission;
import com.rd.ifaes.core.sys.mapper.RolePermissionMapper;
import com.rd.ifaes.core.sys.service.RolePermissionService;

import javax.annotation.Resource;

/**
 * ServiceImpl:RolePermissionServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-12
 */
@Service("rolePermissionService") 
public class RolePermissionServiceImpl  extends BaseServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService{
	
    @Resource
    private RolePermissionMapper rolePermissionMapper;

}
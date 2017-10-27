package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.sys.domain.OperatorRole;
import com.rd.ifaes.core.sys.mapper.OperatorRoleMapper;
import com.rd.ifaes.core.sys.service.OperatorRoleService;
import com.rd.ifaes.manage.security.SystemAuthorizingRealm;

/**
 * ServiceImpl:operatorRoleServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-31
 */
@Service("operatorRoleService") 
public class OperatorRoleServiceImpl  extends BaseServiceImpl<OperatorRoleMapper, OperatorRole> implements OperatorRoleService{
	
    
	@Override
	@Transactional(readOnly = false)
	public int saveRoleOperators(String roleId, String[] operatorIds) {
		if(StringUtils.isBlank(roleId)){
			return 0;
		}
		
		dao.deleteRoleOperators(roleId);
		//清除权限缓存
//		AbstractJedisSessionDAO sessionDAO = SpringContextHolder.getBean("sessionDAO");
//		CacheUtils.del(sessionDAO.getSessionKeyPrefix() + Constant.CACHE_STRING);		
		SystemAuthorizingRealm authRealm = SpringContextHolder.getBean(SystemAuthorizingRealm.class);
    	authRealm.clearAllCachedAuthorizationInfo();
    	
		if(operatorIds != null && operatorIds.length > 0){
			List<OperatorRole> selList = new ArrayList<OperatorRole>();
			for (String userId : operatorIds) {
				if(StringUtils.isBlank(userId)){
					continue;
				}
				selList.add(new OperatorRole(IdGen.uuid(), userId, roleId));		
			}
			if(selList.size() > 0){
				return dao.insertBatch(selList);		
			}
		}
		return 1;
	}
	
	@Override
	@Transactional(readOnly = false)
	public int deleteSelected(String roleId, String[] operatorIds) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("roleId", roleId);
		params.put("operatorIds", operatorIds);
		return dao.deleteSelected(params);
	}
	
	

}
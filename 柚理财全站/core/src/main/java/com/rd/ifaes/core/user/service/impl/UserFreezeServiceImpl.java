package com.rd.ifaes.core.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserFreeze;
import com.rd.ifaes.core.user.mapper.UserFreezeMapper;
import com.rd.ifaes.core.user.model.UserModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:UserFreezeServiceImpl
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userFreezeService") 
public class UserFreezeServiceImpl  extends BaseServiceImpl<UserFreezeMapper, UserFreeze> implements UserFreezeService{
	
	/**
	 * 用户操作类
	 */
    @Resource
    private UserService userService;
    /**
     * 用户附属信息类
     */
    @Resource
    private UserCacheService userCacheService;
	
    /**
     * 是否被冻结
     */
    @Override
	public boolean isFreezed(final String userId, final String operation){
    	//如果用户账户被冻结，任何功能都为冻结状态
    	final User user= userService.get(userId);
    	if(User.USER_STATUS_LOCKED.equals(user.getStatus())){
    		return true;
    	}else{
        	//是否有指定功能的冻结设置
        	return getCount(new UserFreeze(userId,operation)) > 0;
    	}
	}
    
    /**
     * 根据userId查询操作列表
     */
    @Override
    public List<String> findOperationByUserId(final String userId){
    	return dao.findOperationByUserId(userId);
    }
    
    /**
     * 冻结
     */
    @Override
    @Transactional
    public void freeze(final UserModel model){
    	if(StringUtils.isBlank(model.getUuid())){
    		throw new BussinessException(ResourceUtils.get(ResourceConstant.NEED_SELECT_ONE_RECORD), BussinessException.TYPE_JSON);
    	}
    	//1、先删除原来的功能列表
    	dao.deleteByUserId(model.getUuid());
    	
    	//2、保存新功能列表
    	final String[] operations = model.getOperation();
		if (operations != null && operations.length > 0) {
    		List<UserFreeze> list = new ArrayList<>();
	    	for (String operation : operations) {
	    		UserFreeze userFreeze = new UserFreeze();
	    		userFreeze.setUserId(model.getUuid());
	    		userFreeze.setOperation(operation);
	    		userFreeze.setCreateTime(DateUtils.getNow());
	    		list.add(userFreeze);
			}
	    	super.insertBatch(list);
    	}
    	
    	//账户冻结
    	final User user = userService.get(model.getUuid());
    	final UserCache userCache = userCacheService.findByUserId(user.getUuid());
    	if(StringUtils.isBlank(model.getStatus())){ //未冻结
    		//重置失败登录次数
    		if(User.USER_STATUS_LOCKED.equals(user.getStatus())){
    			CacheUtils.set(CacheConstant.KEY_PREFIX_USER_LOGIN_FAIL_NUM + user.getUuid(), 0, ConfigUtils.getInt(Constant.LOGIN_ERROR_FALSH_TIME));
    		}
    		
    		user.setStatus(User.USER_STATUS_NORMAL);
    		userCache.setLockType(null);
    		userCache.setLockTime(null);
    		userCache.setLockRemark(null);
    	}else{
    		user.setStatus(User.USER_STATUS_LOCKED);
    		userCache.setLockType(Constant.LOCK_TYPE_MAN_HANDLE);
    		userCache.setLockTime(DateUtils.getNow());
    		userCache.setLockRemark(model.getLockRemark());
    	}
    	userService.update(user);
    	userCacheService.updateLockRemark(userCache);
    }

	@Override
	public String[] getUserFreezeByUserList(List<User> list) {
		return dao.getUserFreezeByUserList(list);
	}

}
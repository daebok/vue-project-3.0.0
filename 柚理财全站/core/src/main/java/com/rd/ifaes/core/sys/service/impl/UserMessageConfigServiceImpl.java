package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.UserMessageConfig;
import com.rd.ifaes.core.sys.mapper.UserMessageConfigMapper;
import com.rd.ifaes.core.sys.service.UserMessageConfigService;

import java.util.List;
import java.util.Map;


/**
 * ServiceImpl:UserMessageConfigServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
@Service("userMessageConfigService") 
public class UserMessageConfigServiceImpl  extends BaseServiceImpl<UserMessageConfigMapper, UserMessageConfig> implements UserMessageConfigService{
	
	/**
	 * 根据userId查找对象
	 */
	@Override
	public UserMessageConfig findByUserId(final String userId) {
		final UserMessageConfig model = new UserMessageConfig();
		model.setUserId(userId);
		final List<UserMessageConfig> list = dao.findList(model);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据参数查找对象
	 * @author xhf
	 * @date 2016年8月30日
	 * @param params
	 * @return
	 */
	@Override
	public UserMessageConfig findByParams(final Map<String, String> params){
		return dao.findByParams(params);
	}
	

}
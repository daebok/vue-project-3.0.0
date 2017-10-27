package com.rd.ifaes.core.score.service.impl;

import org.springframework.stereotype.Service;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.score.domain.UserScoreLog;
import com.rd.ifaes.core.score.mapper.UserScoreLogMapper;
import com.rd.ifaes.core.score.service.UserScoreLogService;
import com.rd.ifaes.core.user.domain.User;

/**
 * ServiceImpl:UserScoreLogServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
@Service("userScoreLogService") 
public class UserScoreLogServiceImpl  extends BaseServiceImpl<UserScoreLogMapper, UserScoreLog> implements UserScoreLogService{
	
    //@Resource
    //private UserScoreLogMapper userScoreLogMapper;
	/**
	 * 根据时间查询积分日志
	 */
	@Override
	public Page<UserScoreLog> findByDate(final UserScoreLog userScoreLog, final User user) {
		if(user!=null){
			userScoreLog.setUserId(user.getUuid());
		}
		Page<UserScoreLog> page = userScoreLog.getPage();
		if(page==null){
			page = new Page<UserScoreLog>();
		}
		page.setRows(dao.findByDate(userScoreLog));
		return page;
	}
	
	@Override
	public int getListCount(UserScoreLog model) {
		return dao.getListCount(model);
	}
	
	@Override
	public Page<UserScoreLog> findPage(UserScoreLog entity) {
		if(StringUtils.isNotBlank(entity.getCreateTimeEnd())){
			entity.setCreateTimeEnd(entity.getCreateTimeEnd().concat(DateUtils.DATE_END));
		}
		return super.findPage(entity);
	}
}
package com.rd.ifaes.core.user.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.user.mapper.UserInviteMapper;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:UserInviteServiceImpl
 * @author wyw
 * @version 3.0
 * @date 2016-8-17
 */
@Service("userInviteService") 
public class UserInviteServiceImpl  extends BaseServiceImpl<UserInviteMapper, UserInvite> implements UserInviteService{
	@Resource
	private transient UserService userService;
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	@Override
	public Page<UserInvite> findFirstInvite(UserInvite userInvite) {
		
		final UserActivityAwardLog query = new UserActivityAwardLog();
		query.setUserId(userInvite.getInviteUserId());
		Page<UserInvite> page = userInvite.getPage();
		List<UserInvite> list= dao.findList(userInvite);
		for (UserInvite invite : list) {
			query.setAwardType(OperateEnum.AWARD_TYPE_RED.getValue());
			query.setInviteUserId(invite.getInviteeUserId());
			invite.setAwardRedTotal(userActivityAwardLogService.getAwardSum(query));
			query.setAwardType(OperateEnum.AWARD_TYPE_RATE.getValue());
			invite.setAwardRateTotal(userActivityAwardLogService.countRate(query));			
		}
		page.setRows(list);
		return page;
	}

	@Override
	public Page<UserInvite> findSecondInvite(UserInvite userInvite) {
		final UserActivityAwardLog query = new UserActivityAwardLog();
		query.setUserId(userInvite.getInviteUserId());
		Page<UserInvite> page = userInvite.getPage();
		List<UserInvite> list= dao.findSecondInvite(userInvite);
		for (UserInvite invite : list) {
			query.setAwardType(OperateEnum.AWARD_TYPE_RED.getValue());
			query.setInviteUserId(invite.getInviteeUserId());
			invite.setAwardRedTotal(userActivityAwardLogService.getAwardSum(query));
			query.setAwardType(OperateEnum.AWARD_TYPE_RATE.getValue());
			invite.setAwardRateTotal(userActivityAwardLogService.countRate(query));			
		}
		page.setRows(list);
		return page;
	}

	@Override
	public User getInviteUser(String userId) {
		// 获取InviteUser对象
		return userService.getByInvitee(userId);
	}

	@Override
	public Page<UserInvite> findInvitestatistics(UserInvite userInvite) {
		Page<UserInvite> page = userInvite.getPage();
		List<UserInvite> list= dao.findInvitestatistics(userInvite);
		page.setRows(list);
		return page;
	}
	
}
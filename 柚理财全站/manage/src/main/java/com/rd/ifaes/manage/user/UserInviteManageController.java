package com.rd.ifaes.manage.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * 好友邀请管理
 * @version 3.0
 * @author wyw
 * @date 2016-8-22
 */
@Controller
public class UserInviteManageController extends SystemController {
	@Resource
	private UserInviteService userInviteService;
	@Resource
	private UserActivityAwardLogService userActivityAwardLogService;
	
	/**
	 * 
	 * 邀请统计页面
	 * @author wyw
	 * @date 2016-8-22
	 * @return
	 */
	@RequestMapping(value = "/user/invite/statisticsManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public String statisticsManage(){
		return "/user/invite/statisticsManage";
	} 
	/**
	 * 邀请统计列表数据
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/user/invite/statisticsList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public Page<UserInvite> statisticsList(UserInvite userInvite){
		return userInviteService.findInvitestatistics(userInvite);
	}
	/**
	 * 
	 * 一级邀请管理页面
	 * @author wyw
	 * @date 2016-8-22
	 * @return
	 */
	@RequestMapping(value = "/user/invite/inviteFirstManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public String inviteFirstManage( final String inviteUserId,final Model model) {
		 model.addAttribute("inviteUserId", inviteUserId);
		return "/user/invite/inviteFirstManage";
	}
	
	/**
	 * 
	 * 一级人脉邀请列表数据
	 * @author wyw
	 * @date 2016-8-22
	 * @param userInvite
	 * @param inviteLevel 
	 * @return
	 */
	@RequestMapping(value = "/user/invite/inviteFirstList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public Page<UserInvite> inviteFirstList(UserInvite userInvite){
		return userInviteService.findPage(userInvite);
	}
	/**
	 * 
	 * 二级好友邀请列表
	 * @author wyw
	 * @date 2016-8-22
	 * @return
	 */
	@RequestMapping(value = "/user/invite/inviteSecondManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public String inviteSecondManage(final String inviteUserId ,final Model model) {
		 model.addAttribute("inviteUserId", inviteUserId);
		return "/user/invite/inviteSecondManage";
	}
	/**
	 * 
	 * 二级好友邀请列表数据
	 * @author wyw
	 * @date 2016-8-22
	 * @param userInvite
	 * @return
	 */
	@RequestMapping(value = "/user/invite/inviteSecondList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE)
	public Page<UserInvite> inviteSecondList(UserInvite userInvite){
		return userInviteService.findSecondInvite(userInvite);
	}
	/**
	 * 
	 * 邀请奖励页面
	 * @author wyw
	 * @date 2016-8-22
	 * @return
	 */
	@RequestMapping(value = "/user/invite/userInviteAward")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE_AWARD)
	public String userInviteAward(){
		return "/user/invite/userInviteAward";
	}
	/**
	 * 
	 * 邀请奖励列表数据
	 * @author wyw
	 * @date 2016-8-22
	 * @param userActivityAwardLog
	 * @return
	 */
	@RequestMapping(value = "/user/invite/userInviteAwardList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.USER_INVITE_AWARD)
	public Page<UserActivityAwardLog> userInviteAwardList(UserActivityAwardLog userActivityAwardLog){
		String[] codeSet={OperateEnum.ACTIVITYPLAN_INVITE_REGISTER.getValue(),
							OperateEnum.ACTIVITYPLAN_FIRST_INVITE_FIRST_TENDER.getValue(),
							OperateEnum.ACTIVITYPLAN_SECOND_INVITE_FIRST_TENDER.getValue(),
							OperateEnum.ACTIVITYPLAN_FIRST_INVITE_TENDER_SUC.getValue(),
							OperateEnum.ACTIVITYPLAN_SECOND_INVITE_TENDER_SUC.getValue()};
		userActivityAwardLog.setCodeSet(codeSet);
		return userActivityAwardLogService.findPage(userActivityAwardLog);
	}
}

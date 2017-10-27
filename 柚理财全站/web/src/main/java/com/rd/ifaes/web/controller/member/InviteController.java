package com.rd.ifaes.web.controller.member;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.web.controller.WebController;

/**
 * 
 *  账户管理-好友邀请
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class InviteController extends WebController {
	
	/**好友邀请业务处理*/
	@Resource
	private transient UserInviteService userInviteService;
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	/**返回数据*/
	private transient Map<String, Object> data;

	/**
	 * 好友邀请页面
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/invite/index")
	public String view(final Model model) {
		User user = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		final UserActivityAwardLog query = new UserActivityAwardLog();
		//邀请好友相关活动
		final String[] codeSet ={OperateEnum.ACTIVITYPLAN_INVITE_REGISTER.getValue(),
								 OperateEnum.ACTIVITYPLAN_FIRST_INVITE_FIRST_TENDER.getValue(),
								 OperateEnum.ACTIVITYPLAN_SECOND_INVITE_FIRST_TENDER.getValue(),
								 OperateEnum.ACTIVITYPLAN_FIRST_INVITE_TENDER_SUC.getValue(),
								 OperateEnum.ACTIVITYPLAN_SECOND_INVITE_TENDER_SUC.getValue()};
		
		query.setAwardType(OperateEnum.AWARD_TYPE_RED.getValue());
		query.setUserId(user.getUuid());
		query.setCodeSet(codeSet);
		//红包奖励数目 
		 model.addAttribute("redCount", userActivityAwardLogService.getCount(query));
		//红包金额
		 model.addAttribute("redAmount", userActivityAwardLogService.getAwardSum(query));
		//体验券数目
		query.setAwardType(OperateEnum.AWARD_TYPE_RATE.getValue());
		 model.addAttribute("rateCount", userActivityAwardLogService.getCount(query));
		//邀请链接
		StringBuilder  inviteUrl= new StringBuilder();
		inviteUrl.append(ConfigUtils.getValue("web_url")).append("/user/register.html?ui=").append(user.getUuid());
		model.addAttribute("inviteUrl", inviteUrl);
		return "/member/invite/index";
	}
	/**
	 * 好友邀请页面
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping(value = "/member/invite/getLogList")
	@ResponseBody
	public Object getLogList(final UserInvite userInvite,final String inviteLevel){
		data = new HashMap<>();
		//用户信息
		final User user = SessionUtils.getSessionUser();
		Page<UserInvite> page=null;
		userInvite.setInviteUserId(user.getUuid());
		if("1".equalsIgnoreCase(inviteLevel)){//一级人脉
			page=userInviteService.findFirstInvite(userInvite);
		}else if("2".equalsIgnoreCase(inviteLevel)){
			page=userInviteService.findSecondInvite(userInvite);//二级人脉
		}
		data.put("data", page);
		data.put(RESULT_NAME, true);
		return data;
	}	

}

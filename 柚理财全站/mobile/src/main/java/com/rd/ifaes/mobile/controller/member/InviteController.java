package com.rd.ifaes.mobile.controller.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserInvite;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.extra.AppUserInviteModel;
import com.rd.ifaes.mobile.model.extra.InviteeItemModel;
import com.rd.ifaes.mobile.wechat.util.SHA1;
import com.rd.ifaes.mobile.wechat.util.WeixinHttpUtil;
import com.rd.ifaes.mobile.wechat.util.WeixinUtils;

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
	/**
	 * 移动端 好友邀请页面
	 * @author lgx
	 * @date 2016年11月17日
	 * @return
	 */
	@RequestMapping(value = "/app/member/invite/userInvite")
	@ResponseBody
	public Object userInvite(HttpServletRequest request) {
		Object obj=null;
		try{
			User user = getAppSessionUser(request);
			AppUserInviteModel model=new AppUserInviteModel();
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
		model.setRedCount(userActivityAwardLogService.getCount(query));
		//红包金额
		model.setRedAmount(userActivityAwardLogService.getAwardSum(query));
		//体验券数目
		query.setAwardType(OperateEnum.AWARD_TYPE_RATE.getValue());
		model.setRateCount(userActivityAwardLogService.getCount(query));
		//邀请链接
		StringBuilder  inviteUrl= new StringBuilder();
		inviteUrl.append(ConfigUtils.getValue("mobile_url")).append("/app/open/wechat/register.html?ui=").append(user.getMobile());
		 model.setInviteUrl(inviteUrl);
		 model.setShareTitle("测试分享标题");//分享活动标题
		 model.setShareContent("标题重复三遍，测试分享标题，测试分享标题，测试分享标题");//分享活动内容
		 model.setDrawRuleStr("1、每邀请1位用户，累积投资5000元以上（含5000元），您可获得30元奖励，邀请人数越多赚得越多。"+"\n"+"2、奖励将以投资红包形式发放，投资红包等同现金，可在投资时选择抵扣，但不能用于提现。"+"\n"+"3、只有您的好友通过您的专属邀请链接注册投资，您才能获得投资红包。"+"\n"+"4、请不要将邀请链接发给不熟悉的人，避免给他人带来不必要的困扰。");//奖励规则说明
		 obj=createSuccessAppResponse(model);
	} catch (Exception e) {
		obj=dealException(e);
	}
	return obj;
	}	
	/**
	 * 移动端 好友邀请页面
	 * @author lgx
	 * @date 2016年11月17日
	 * @return
	 */
	@RequestMapping(value = "/app/member/invite/getLogList")
	@ResponseBody
	public Object getLogList(final UserInvite userInvite,final String inviteLevel, HttpServletRequest request){
		Object obj=null;
		try{
		//用户信息
			User user = getAppSessionUser(request);
			PagedData<InviteeItemModel> invitelist=new PagedData<InviteeItemModel>();
			Page<UserInvite> page=null;
		userInvite.setInviteUserId(user.getUuid());
		if("1".equalsIgnoreCase(inviteLevel)){//一级人脉
			page=userInviteService.findFirstInvite(userInvite);
		}else if("2".equalsIgnoreCase(inviteLevel)){
			page=userInviteService.findSecondInvite(userInvite);//二级人脉
		}
		
		int pages=userInvite.getPage().getPage();
		 List<UserInvite>  projectInvestRecordList=page.getRows();
		 page.setPage(pages);
		 fillPageData(invitelist, page);
		 
			for(UserInvite userInvitemodel:projectInvestRecordList){
				InviteeItemModel model=new InviteeItemModel();
				model.setInviteTime(userInvitemodel.getInviteeUserTime());// 邀请时间
				model.setInviteUserMobile(userInvitemodel.getInviteUserMobile());//邀请人手机号
				model.setInviteeUserMobile(userInvitemodel.getInviteeUserMobile());//被邀请人手机号
				model.setAwardRateTotal(userInvitemodel.getAwardRateTotal());//加息券总个数
				model.setAwardRedTotal(userInvitemodel.getAwardRedTotal());//红包奖励总额
				invitelist.getList().add(model);
			}
				obj= createSuccessAppResponse(invitelist);
	} catch (Exception e) {
		obj=dealException(e);
	}
		return obj;
	}	
	
	/**
	 * 微信-分享
	 * @author lgx
	 * @date 2016年11月17日
	 * @return
	 */
	@RequestMapping(value = "/app/member/invite/wxInvite")
	@ResponseBody
	public Object wxInvite(final UserInvite userInvite,final String inviteLevel, HttpServletRequest request){
		Object obj=null;
		final Map<String, Object> data = new HashMap<>();
		try{
			//用户信息
			User user = getAppSessionUser(request);
			// 微信 add jdd 2015-10-28
			String ticket = WeixinUtils.loadJSApiTicket();
			String url = "http://10.10.1.206:8088/mine/invite";
			String noncestr = WeixinHttpUtil.getRandomStr();
			long timestamp = new Date().getTime() / 1000L;
			String string1 = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
			String signature = new SHA1().getDigestOfString(string1.getBytes()).toLowerCase();
			data.put("noncestr", noncestr);
			data.put("timestamp", Long.valueOf(timestamp));
			data.put("url", url);
			data.put("ticket", ticket);
			data.put("token", WeixinUtils.loadAccessToken());
			data.put("signature", signature);
					obj= createSuccessAppResponse(data);
		} catch (Exception e) {
			obj=dealException(e);
		}
			return obj;
		}	
}

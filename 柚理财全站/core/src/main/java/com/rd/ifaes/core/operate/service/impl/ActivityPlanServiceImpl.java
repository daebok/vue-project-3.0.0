package com.rd.ifaes.core.operate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.mapper.ActivityPlanMapper;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserInviteService;

/**
 * 
 * 活动方案
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
@Service("activityPlanService")
public class ActivityPlanServiceImpl extends BaseServiceImpl<ActivityPlanMapper, ActivityPlan> implements ActivityPlanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityPlanServiceImpl.class);
	/** 红包规则业务处理*/
	@Resource
	private transient  RedenvelopeRuleService redenvelopeRuleService;
	/** 加息券规则业务处理*/
	@Resource
	private transient  RateCouponRuleService rateCouponRuleService;
	/** 好友邀请业务处理*/
	@Resource
	private transient  UserInviteService userInviteService;
	@Resource 
	private transient ProjectService projectService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	/** 用户红包业务处理 */
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/** 奖励日志业务处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	/** 用户加息券业务处理 */
	@Resource
	private transient UserRateCouponService userRateCouponService;
	
	@Override
	public void grantAllAwardByActivityCode(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter,boolean redFlag) {
		// 查询活动方案
		LOGGER.info("活动奖励发放：{},{}",operateEnum.getName(),redFlag==true?"发红包":"发加息券");
		final ActivityPlan activityPlanQuery = new ActivityPlan();
		activityPlanQuery.setActivityCode(operateEnum.getValue());
		activityPlanQuery.setStatus(CommonEnum.YES.getValue());
		final List<ActivityPlan> activityList = this.findList(activityPlanQuery);
		if (!CollectionUtils.isEmpty(activityList)) {
			if (CommonEnum.YES.eq(activityList.get(0).getStatus())) {  //如果活动开启则发放奖励
				if(redFlag){
					grantRedenvelope(awardUser, operateEnum, invest,inviter);
				}else{
					grantRatecoupon(awardUser, operateEnum, invest,inviter);
				}
			}
		}else{
			LOGGER.info("未查到该活动方案：{}", operateEnum.getName());
		}
				
	}
	
	/**
	 * 发放注册相关奖励
	 */
	@Transactional
	@Override
	public void grantRegisterAward(final User user) {
		grantAwardByActivityCode(user, OperateEnum.ACTIVITYPLAN_REGISTER, null,null);
		
		User inviter=userInviteService.getInviteUser(user.getUuid());
		
		if(inviter!=null){//好友注册发放奖励
			grantAwardByActivityCode(inviter, OperateEnum.ACTIVITYPLAN_INVITE_REGISTER, null,user);
		}
	}
	
	/**
	 * 发放首投相关奖励
	 */
	@Override
	public void grantUserInvestFirstAward(User user, ProjectInvest projectInvest) {
		grantAwardByActivityCode(user, OperateEnum.ACTIVITYPLAN_FIRST_TENDER, projectInvest,null);
		
		//一级好友相关奖励-- 首投奖励
		//获取用户的一级好友
		final User firstInviteUser = userInviteService.getInviteUser(user.getUuid());
		if(firstInviteUser!=null){
			grantAwardByActivityCode(firstInviteUser, OperateEnum.ACTIVITYPLAN_FIRST_INVITE_FIRST_TENDER, projectInvest,user);//一级好友首次投资
			//二级还有相关奖励-- 首投奖励
			final User secondInviteUser = userInviteService.getInviteUser(firstInviteUser.getUuid());
			if(secondInviteUser!=null){
				grantAwardByActivityCode(secondInviteUser, OperateEnum.ACTIVITYPLAN_SECOND_INVITE_FIRST_TENDER, projectInvest,user);// 二级首次投资
			}
		}
	}
	
	/**
	 * 发放投资相关奖励
	 */
	//@Transactional
	@Override
	public void grantUserInvestAward(final User user, final ProjectInvest projectInvest) {
		//用户投资相关奖励 -- 投资奖励
		grantAwardByActivityCode(user, OperateEnum.ACTIVITYPLAN_TENDER_SUC, projectInvest,null);
		//一级好友相关奖励-- 投资奖励
		//获取用户的一级好友
		final User firstInviteUser = userInviteService.getInviteUser(user.getUuid());
		if(firstInviteUser!=null){
			grantAwardByActivityCode(firstInviteUser, OperateEnum.ACTIVITYPLAN_FIRST_INVITE_TENDER_SUC, projectInvest,user);// 一级好友投资奖励
			//二级还有相关奖励-- 投资奖励
			final User secondInviteUser = userInviteService.getInviteUser(firstInviteUser.getUuid());
			if(secondInviteUser!=null){
				grantAwardByActivityCode(secondInviteUser, OperateEnum.ACTIVITYPLAN_SECOND_INVITE_TENDER_SUC, projectInvest,user); // 二级好友投资奖励
			}
		}
	}
	
	/**
	 * 发放奖励
	 * @author ywt
	 * @date 2016-11-04
	 * @param 发放对象
	 * @param 活动标识
	 * @param 投资记录
	 * @param 被邀请者
	 * 
	 */
	public void grantAwardByActivityCode(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter){
		// 查询活动方案
		LOGGER.info("活动奖励发放：{}",operateEnum.getName());
		final ActivityPlan activityPlanQuery = new ActivityPlan();
		activityPlanQuery.setActivityCode(operateEnum.getValue());
		activityPlanQuery.setStatus(CommonEnum.YES.getValue());
		final List<ActivityPlan> activityList = this.findList(activityPlanQuery);
		if (!CollectionUtils.isEmpty(activityList)) {
			if (CommonEnum.YES.eq(activityList.get(0).getStatus())) {  //如果活动开启则发放奖励
				grantRedenvelope(awardUser, operateEnum, invest,inviter);
				grantRatecoupon(awardUser, operateEnum, invest,inviter);
			}
		}else{
			LOGGER.info("未查到该活动方案：{}", operateEnum.getName());
		}
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	
	
	/**
	 * 发放红包
	 * @author ywt
	 * @date 2016-11-04
	 */
	public void grantRedenvelope(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter){
		List<RedenvelopeRule> redenvelopeRules=null;
		if (OperateEnum.ACTIVITYPLAN_TENDER_SUC.eq(operateEnum.getValue())) {  //如果是投资活动特殊查询
			if (null==invest) {
				LOGGER.info("投资记录为空，发放投资奖励失败！awardUser={}",awardUser.getUuid());
				return;
			}else{
				final Project project=projectService.get(invest.getProjectId());
				final String redenvelopeRuleId =project.getRedenvelopeRuleId();
				//上架指定投资红包奖励规则
				if(StringUtils.isNotBlank(redenvelopeRuleId) && CommonConstants.NO.equals(redenvelopeRuleId)){
					//不发放红包
					LOGGER.info("投资记录(uuid:{})不发放红包（上架设置）",invest.getUuid());
					return;
				}else if(StringUtils.isNotBlank(redenvelopeRuleId)){
					RedenvelopeRule rule=redenvelopeRuleService.get(redenvelopeRuleId);
					if(rule==null){
						LOGGER.error("投资记录(uuid:{})发放红包奖励失败,红包规则（ruleId:{}）为空。",invest.getUuid(),redenvelopeRuleId);
					}else{
						redenvelopeRules=new ArrayList<RedenvelopeRule>(1);
						redenvelopeRules.add(rule);
					}
			 	}else{
			 		//取默认匹配项目类别的红包规则
			 		final String projectTypeId=project.getProjectTypeId();
					LOGGER.info("投资记录(uuid:{}),投资红包奖励，projectTypeId:{}",invest.getUuid(),projectTypeId);
					if (StringUtils.isBlank(projectTypeId)) {
						LOGGER.error("投资奖励发放---->查询项目类型失败！投资记录id:{}",invest.getProjectId());
						return;
					}
					else {
						RedenvelopeRule	rule=redenvelopeRuleService.findInvestRedenvelopeRuleByProjectTypeId(projectTypeId);
						if(rule==null){
							LOGGER.error("投资记录(uuid:{})发放红包奖励失败,项目类别未配置奖励规则。",invest.getUuid());
							return;
						}else{
						    redenvelopeRules=new ArrayList<RedenvelopeRule>(1);
					    	redenvelopeRules.add(rule);
					 	}
					}
				}
			}
		}
		else{
			//取出可用的红包规则，sql筛选条件为：delete_flag=0、status = 1、有剩余发放数量、未过期
			redenvelopeRules= redenvelopeRuleService.findByActivityCode(operateEnum.getValue());
		}
		List<UserRedenvelope> urvs = new ArrayList<>();
		List<UserActivityAwardLog> logs = new ArrayList<>();
		long time1 = System.currentTimeMillis();
		for(RedenvelopeRule redenvelopeRule:redenvelopeRules){
			redenvelopeRule.setActivityName(operateEnum.getName());
			redenvelopeRuleService.grantRedenvelope(awardUser, redenvelopeRule, invest,inviter,urvs,logs);
		}
		long time2 = System.currentTimeMillis();
		LOGGER.info("红包循环，耗时:{}",(time2-time1));
		if(!CollectionUtils.isEmpty(urvs)){
			userRedenvelopeService.insertBatch(urvs);
		}
		long time3 = System.currentTimeMillis();
		LOGGER.info("红包insert，耗时:{}",(time3-time2));
		if(!CollectionUtils.isEmpty(logs)){
			userActivityAwardLogService.insertBatch(logs);				
		}
		long time4 = System.currentTimeMillis();
		LOGGER.info("红包日志insert，耗时:{}",(time4-time3));
	}
	
	/**
	 * 发放加息券
	 * @author ywt
	 * @date 2016-11-04
	 */
	public void grantRatecoupon(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter){
		
		List<RateCouponRule> rateCouponRules=null;
		if (OperateEnum.ACTIVITYPLAN_TENDER_SUC.eq(operateEnum.getValue())) {  //如果是投资活动特殊查询
			if (null==invest) {
				LOGGER.info("投资记录为空，发放投资奖励失败！awardUser={}",awardUser.getUuid());
				return;
			}
			else{
				final Project project=projectService.get(invest.getProjectId());
				final String rateCouponRuleId =project.getRateCouponRuleId();
				//上架指定投资加息劵奖励规则
				if(StringUtils.isNotBlank(rateCouponRuleId) && CommonConstants.NO.equals(rateCouponRuleId)){
					//不发放加息劵
					LOGGER.info("投资记录(uuid:{})不发放加息劵（上架设置）",invest.getUuid());
					return;
				}else if(StringUtils.isNotBlank(rateCouponRuleId)){
					RateCouponRule rule=rateCouponRuleService.get(rateCouponRuleId);
					if(rule==null){
						LOGGER.error("投资记录(uuid:{})发放加息劵奖励失败，加息劵规则（ruleId:{}）为空。",invest.getUuid(),rateCouponRuleId);
					}else{
						rateCouponRules=new ArrayList<RateCouponRule>(1);
						rateCouponRules.add(rule);
					}
			 	}else{
			 		final String projectTypeId=projectService.get(invest.getProjectId()).getProjectTypeId();
					if (null==projectTypeId || "".equals(projectTypeId)) {
						LOGGER.info("投资奖励发放---->查询项目类型失败！投资记录id="+invest.getProjectId());
						return;
					}
					else {
						RateCouponRule	rule=rateCouponRuleService.findInvestRateCouponRuleByProjectTypeId(projectTypeId);
						if(rule==null){
							LOGGER.error("投资记录(uuid:{})发放加息劵奖励失败,项目类别未配置奖励规则。",invest.getUuid());
							return;
						}else{
							rateCouponRules=new ArrayList<RateCouponRule>(1);
							rateCouponRules.add(rule);
					 	}
					}
		     	}//rule 
			}//invest  
		}
		else{
			//取出可用的加息券规则，sql筛选条件为：delete_flag=0、status = 1、有剩余发放数量、未过期
			rateCouponRules= rateCouponRuleService.findByActivityCode(operateEnum.getValue());
		}
		List<UserRateCoupon> urcs = new ArrayList<>();
		List<UserActivityAwardLog> logs = new ArrayList<>();
		for(RateCouponRule rateCouponRule:rateCouponRules){
			rateCouponRule.setActivityName(operateEnum.getName());
			rateCouponRuleService.grantRateCoupon(awardUser, rateCouponRule, invest,inviter,urcs,logs);
		}
		if(!CollectionUtils.isEmpty(urcs)){
			userRateCouponService.insertBatch(urcs);
		}
		if(!CollectionUtils.isEmpty(logs)){
			userActivityAwardLogService.insertBatch(logs);				
		}
	}

	@Override
	public ActivityPlan findActivityPlanByCode(String code) {
		return dao.findActivityPlanByCode(code);
	}
	
	public void update(ActivityPlan activityPlan){
		if (OperateEnum.STATUS_FORBIDDEN.eq(activityPlan.getStatus())) {
			String[] rules=findRuleByActivityCode(activityPlan.getActivityCode());
			if(rules!=null && rules.length>0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.ACTIVITY_PLAN_BAN_ERROR));
			}
		}
		super.update(activityPlan);
	}

	@Override
	public String[] findRuleByActivityCode(String code) {
		return dao.findRuleByActivityCode(code);
	}

}
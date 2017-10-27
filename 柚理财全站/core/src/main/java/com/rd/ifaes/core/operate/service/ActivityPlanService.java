package com.rd.ifaes.core.operate.service;


import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 *   类说明 活动方案
 * @version 3.0
 * @author wyw
 * @date 2016-7-26
 */
public interface ActivityPlanService extends BaseService<ActivityPlan>{

	/**
	 * 独立发放红包或者加息券奖励
	 * @author QianPengZhan
	 * @date 2017年1月12日
	 * @param awardUser
	 * @param operateEnum
	 * @param invest
	 * @param inviter
	 * @param redFlag
	 */
	void grantAllAwardByActivityCode(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter,boolean redFlag);
	
	/**
	 * 发放注册相关奖励，
	 * @author ywt
	 * @date 2016-11-05
	 * @param user
	 */
	 void grantRegisterAward(User user);
	
	
	/**
	 * 
	 * 发放用户投资的奖励
	 * 用户投资奖励，一级好友投资，二级好友投资。
	 * @author ywt
	 * @date 2016-11-05
	 * @param user
	 * @param projectInvest
	 */
	 void grantUserInvestAward( final User user,final ProjectInvest projectInvest);
	 
	 /**
	 * 
	 * 发放用户首次投资的奖励
	 * 用户首次投资奖励，一级好友首次投资，二级好友首次投资。
	 * @author ywt
	 * @date 2016-8-23
	 * @param user
	 * @param projectInvest
	 */
	 void grantUserInvestFirstAward( final User user,final ProjectInvest projectInvest);
	 
	 /**
	 * 
	 * 根据活动标识发放奖励
	 * @author ywt
	 * @date 2016-11-05
	 * @param awardUser
	 * @param activityCode
	 * @param invest
	 * @param inviter
	 */
	 void grantAwardByActivityCode(User awardUser,OperateEnum operateEnum,ProjectInvest invest,User inviter);
	 
	 /**
	 * 通过code查询活动方案
	 * @author ywt
     * @date 2016-11-05
	 * @param user
	 */
	 ActivityPlan findActivityPlanByCode(String code);
	 
	 /**
	  * 通过code查询活动方案
	  * @author ywt
	  * @date 2016-11-05
      * @param user
	 */
	 String[] findRuleByActivityCode(String code);
}
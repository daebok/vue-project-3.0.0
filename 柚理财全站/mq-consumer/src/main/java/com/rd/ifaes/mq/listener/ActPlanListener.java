package com.rd.ifaes.mq.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.operate.model.BatchUserRedModel;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserInviteService;
import com.rd.ifaes.mq.MqListener;

/**
 * 活动方案队列监听服务
 * 
 * @author lh
 * @version 3.0
 * @since 2016-10-18
 */
@Component
public class ActPlanListener implements MqListener<MqActPlanModel> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActPlanListener.class);

	@Autowired
	private transient ActivityPlanService actPlanService;
	@Autowired
	private transient UserScoreService userScoreService;
	/** 好友邀请业务处理*/
	@Resource
	private transient  UserInviteService userInviteService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private transient RedenvelopeRuleService redenvelopeRuleService;

	@Override
	public void listen(MqActPlanModel model) {
		LOGGER.info("活动方案队列监听已开启");
		if(model.getOperate().equals(MqConstant.OPERATE_ACTPLAN_BATCH_USER_RED_GRANT)){
			LOGGER.debug("批量用户红包发放----");
			BatchUserRedModel bur = model.getBatchUserRed();
			redenvelopeRuleService.grantSelectUserRed(bur.getUserIds(), bur.getRedenvelopeRuleId(), bur.getType());	
		}else if (model.getUser()==null) {
			LOGGER.warn("用户为空,活动奖励发放失败！");
		}else{
			grantAwardByActivityType(model);
		}
	}

	/**
	 * 发放奖励入口
	 * 
	 * @author ywt
	 * @date 2016-11-04
	 * @param ActivityType 活动大类
	 * @param AwardUser 活动奖励发放对象
	 * @param invitee 被邀请者
	 * @param invest 投资记录
	 */
	private void grantAwardByActivityType(MqActPlanModel model) {
		User user = model.getUser();
		User inviter=userInviteService.getInviteUser(user.getUuid());
		switch (model.getOperate()) {
			case MqConstant.OPERATE_ACTPLAN_INVEST_FIRST: //发放首投奖励、一级好友首投奖励、二级好友首投奖励
				actPlanService.grantUserInvestFirstAward(model.getUser(), model.getInvest());
				break;
			case MqConstant.OPERATE_ACTPLAN_INVEST: //发放投资奖励、一级好友投资奖励、二级好友投资奖励
				actPlanService.grantUserInvestAward(model.getUser(), model.getInvest());
				// 发放积分
				userScoreService.grantScore(model.getUser(), ScoreEnum.SCORE_INVEST_SUCCESS.getValue(), model.getInvest().getUuid());
				// 用户vip处理
				userVipService.addVipGrowthByTender(model.getUser(), model.getInvest().getAmount(),  "投资成功");
				break;
			case MqConstant.OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_RATECOUPON://发放注册好友送好礼--送加息券
				long time1 = System.currentTimeMillis();
				if(inviter != null){
					actPlanService.grantAllAwardByActivityCode(inviter, OperateEnum.ACTIVITYPLAN_INVITE_REGISTER, null, user, false);
				}
				long time2 = System.currentTimeMillis();
				LOGGER.info("发放注册好友送好礼--送加息券，耗时：{}",(time2-time1));
				break;
			case MqConstant.OPERATE_ACTPLAN_REGISTER_FRIEND_GIFT_REDPACKET://发放注册好友送好礼--送红包
				long time3 = System.currentTimeMillis();
				if(inviter != null){
					actPlanService.grantAllAwardByActivityCode(inviter, OperateEnum.ACTIVITYPLAN_INVITE_REGISTER, null, user, true);
				}
				long time4 = System.currentTimeMillis();
				LOGGER.info("发放注册好友送好礼--送红包，耗时：{}",(time4-time3));
				break;
			case MqConstant.OPERATE_ACTPLAN_REGISTER_GIFT_RATECOUPON://发放注册送好礼--送加息券
				long time5 = System.currentTimeMillis();
				actPlanService.grantAllAwardByActivityCode(user, OperateEnum.ACTIVITYPLAN_REGISTER, null, null, false);
				long time6 = System.currentTimeMillis();
				LOGGER.info("发放注册送好礼--送加息券，耗时：{}",(time6-time5));
				break;
			case MqConstant.OPERATE_ACTPLAN_REGISTER_GIFT_REDPACKET://发放注册送好礼--送红包
				long time7 = System.currentTimeMillis();
				actPlanService.grantAllAwardByActivityCode(user, OperateEnum.ACTIVITYPLAN_REGISTER, null, null, true);
				long time8 = System.currentTimeMillis();
				LOGGER.info("发放注册送好礼--送红包，耗时：{}",(time8-time7));
				break;
			case MqConstant.OPERATE_ACTPLAN_REGISTER_SCORE://注册送注册积分和手机认证积分
				long time9 = System.currentTimeMillis();
				// 送注册积分
				userScoreService.grantScore(model.getUser(), ScoreEnum.SCORE_USER_REGISTER.getValue(), null);
				// 送手机认证积分
				userScoreService.grantScore(model.getUser(), ScoreEnum.SCORE_PHONE.getValue(), null);
				long time10 = System.currentTimeMillis();
				LOGGER.info("注册送注册积分和手机认证积分，耗时：{}",(time10-time9));
				break;
			case MqConstant.OPERATE_ACTPLAN_LOGIN: //登录送积分
				// 登录送积分
				userScoreService.grantScore(model.getUser(), ScoreEnum.SCORE_USER_LOGIN.getValue(), null);
			default:
				break;
			}
	}

}

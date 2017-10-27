package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.RedEnvelopeRuleResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.RandomUtil;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.mapper.RateCouponRuleMapper;
import com.rd.ifaes.core.operate.model.RateCouponRuleModel;
import com.rd.ifaes.core.operate.model.RedenvelopeRuleModel;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.supports.spring.BeanSelfAware;

/**
 * 
 * 加息券规则业务处理类
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
@Service("rateCouponRuleService")
public class RateCouponRuleServiceImpl extends BaseServiceImpl<RateCouponRuleMapper, RateCouponRule>
		implements RateCouponRuleService,BeanSelfAware {
	/** 日志处理 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RateCouponRuleServiceImpl.class);
	/** 加息券规则明细业务处理 */
	@Resource
	private transient RateCouponRuleDetailService rateCouponRuleDetailService;
	/** 用户加息券业务处理 */
	@Resource
	private transient UserRateCouponService userRateCouponService;
	/** 用户业务处理 */
	@Resource
	private transient UserService userService;
	/** 用户奖励日志处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	
	private RateCouponRuleService proxySelf;
	@Override
	public void setSelf(Object proxyBean) {
		this.proxySelf = (RateCouponRuleService)proxyBean;
	}

	/**
	 * 验证时间的正确性
	 * 
	 * @author QianPengZhan
	 * @date 2016年9月23日
	 * @param model
	 */
	private void validTime(final RateCouponRuleModel model) {
		if (RateCouponRuleModel.TIME_STATUS_YES.equalsIgnoreCase(model.getGrantTimestatus())) {// 限制
			Date now = DateUtils.getNow();
			if (DateUtils.rollMinute(model.getGrantStartTime(), Constant.INT_ONE).getTime() < DateUtils.valueOf(DateUtils.getDateStart(now)).getTime()) {
				throw new BussinessException(ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_STARTTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
			if (model.getGrantEndTime().getTime() < now.getTime()) {
				throw new BussinessException(ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_ENDTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
			if (model.getGrantEndTime().before(model.getGrantStartTime())) {
				throw new BussinessException(
						ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_STARTTIME_ENDTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
		}
	}

	/**
	 * 保存规则
	 */
	@Override
	@Transactional(readOnly = false)
	public Boolean saveRule(final RateCouponRuleModel ruleModel, final List<RateCouponRuleDetail> detaillist,
			final String userIds) {
		// 验证数据有效性
		BeanValidators.validate(ruleModel);
		ruleModel.packageRatecoupon();
		ruleModel.checkModel();
		validTime(ruleModel);
		// 生成规则的uuid
		final String uuid = IdGen.uuid();
		ruleModel.setUuid(uuid);
		ruleModel.setNewRecord(true);
		// 保存规则
		save(ruleModel);
		for (int i = 0; i < detaillist.size(); i++) {
			detaillist.get(i).setRuleId(uuid);
			if (detaillist.get(i).getUpApr().compareTo(RateCouponRuleModel.UPAPRMAX) > 0
					|| detaillist.get(i).getUpApr().compareTo(RateCouponRuleModel.UPAPRMIN) < 0) {
				throw new BussinessException(
						ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_STARTTIME_UPRATE_ERROR),
						BussinessException.TYPE_CLOSE);
			}
		}
		if (!CollectionUtils.isEmpty(detaillist)) {
			rateCouponRuleDetailService.insertBatch(detaillist);
		}
		if (StringUtils.isNotBlank(userIds)) {
			proxySelf.grantSelectUserRate(userIds.split(","), ruleModel.getUuid(), Constant.INT_TWO);
		}
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_RATE_RULE);
		return true;
	}

	/**
	 * 更新加息券规则
	 */
	@Transactional
	@Override
	public Boolean updateRule(final RateCouponRuleModel model, final List<RateCouponRuleDetail> detaillist) {
		// 验证数据有效性
		BeanValidators.validate(model);
		model.packageRatecoupon();
		model.setUpdateTime(new Date());
		validTime(model);
		model.setStatus(RedenvelopeRuleModel.STATUS_SERVICE);
		model.setUuid(null);
		// 更新加息券规则表
		this.save(model);
		// 重新组装明细
		for (int i = 0; i < detaillist.size(); i++) {
			detaillist.get(i).setRuleId(model.getUuid());
		}
		if (!CollectionUtils.isEmpty(detaillist)) {
			rateCouponRuleDetailService.insertBatch(detaillist);
		}
		return true;
	}

	/**
	 * 启用或则禁用规则
	 */
	@Override
	public int updateStatus(final RateCouponRule ratecouponRule) {
		if (OperateEnum.STATUS_FORBIDDEN.eq(ratecouponRule.getStatus())) {
			String[] calls1 = dao.findRateCouponRuleCall(ratecouponRule.getUuid(), "user_gift");
			String[] calls2 = dao.findRateCouponRuleCall(ratecouponRule.getUuid(), "user_vip_level");
			String callsList = "";
			if (calls1 != null && calls1.length > 0) {
				for (String call : calls1) {
					callsList += call + " ";
				}
				throw new BussinessException(
						StringUtils.format(ResourceUtils.get(ResourceConstant.RULE_BAN_ERROR_FOR_GIFT), callsList));
			}
			if (calls2 != null && calls2.length > 0) {
				for (String call : calls2) {
					callsList += call + " ";
				}
				throw new BussinessException(StringUtils
						.format(ResourceUtils.get(ResourceConstant.RULE_BAN_ERROR_FOR_VIP_LEVEL), callsList));
			}
		}else{
			RateCouponRule rule = get(ratecouponRule.getUuid());
			if(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue().equals(rule.getActivityCode()) && StringUtils.isNotBlank(rule.getGrantProjectType())){
				final String[] projectTypeSet = rule.getGrantProjectType().split(",");//发放类型集合
				for (String projectTypeId : projectTypeSet) {
					if(StringUtils.isNotBlank(projectTypeId) && checkGrantProjectType(projectTypeId)){
						ProjectType projectType = projectTypeService.get(projectTypeId);
						throw new BussinessException(ResourceUtils.get(ResourceConstant.PORJECT_TYPE_IS_ASSOCIATED,projectType.getTypeName()));
					}
				}
			}
		}
		return dao.updateStatus(ratecouponRule);
	}

	/**
	 * 根据规则发放加息券
	 * 
	 * @author ywt
	 * @date 2016-11-05
	 * @param awardUser
	 * @param rateCouponRule
	 * @param projectInvest
	 * @param inviter
	 */
	@Override
	@Transactional(readOnly = false)
	public List<BigDecimal> grantRateCoupon(final User awardUser, final RateCouponRule rateCouponRule,
			final ProjectInvest projectInvest, final User inviteUser,final List<UserRateCoupon> urcs,final List<UserActivityAwardLog> logs) {
		List<BigDecimal> rateCouponList = new ArrayList<BigDecimal>();
		if (null == rateCouponRule) {
			LOGGER.info("发放规则为空，发放失败");
			return rateCouponList;
		}
		if (null == awardUser) {
			LOGGER.info("发放对象为空，发放失败");
			return rateCouponList;
		}
		rateCouponRule.validRule();
		BigDecimal upApr = null;
		List<RateCouponRuleDetail> ruleDetailList = rateCouponRuleDetailService.getListByRuleId(rateCouponRule.getUuid());// 查询所有规则明细
		rateCouponRule.setDetailList(ruleDetailList);
		// 根据类型不同进行发放
		boolean bl = false; // 是否生成加息券,发放次数加1
		if (OperateEnum.GRANT_TYPE_FIXED_UPARP_RATE.eq(rateCouponRule.getGrantType())) {// 固定值发放方式
			for (int i = 0; i < ruleDetailList.size(); i++) {
				// 发放并保存记录
				upApr = ruleDetailList.get(i).getUpApr();
				fillGrantRecord(ruleDetailList.get(i), rateCouponRule, awardUser, projectInvest, inviteUser,urcs,logs);
				if(upApr != null && upApr.doubleValue() > 0){
					bl = true;
					rateCouponList.add(upApr);
				}
			}
		} else {// 金额满返
			List<RateCouponRuleDetail> ruleDetailLists = calRateIntervalApr(projectInvest, rateCouponRule, ruleDetailList); // 计算加息券金额
			for(RateCouponRuleDetail rateCouponRuleDetail : ruleDetailLists){
				// 发放并保存记录
				fillGrantRecord(rateCouponRuleDetail, rateCouponRule, awardUser, projectInvest, inviteUser,urcs,logs);
				if(rateCouponRuleDetail.getUpApr().doubleValue() > 0){
					bl = true;
					rateCouponList.add(rateCouponRuleDetail.getUpApr());
				}
				
			}
		}
		// 增加发放次数
		if (bl) {
			dao.updateLssueNum(rateCouponRule);
		}
		return rateCouponList;
	}

	/**
	 * 计算区间比例发放方式下的加息利率
	 */
	private List<RateCouponRuleDetail> calRateIntervalApr(final ProjectInvest projectInvest, final RateCouponRule rule,
			final List<RateCouponRuleDetail> ruleDetailList) {
		List<RateCouponRuleDetail> ruleDetailLists = new ArrayList<RateCouponRuleDetail>();
		// 计算加息券
		for (int i = 0; i < ruleDetailList.size(); i++) {
			RateCouponRuleDetail rateCouponRuleDetail = ruleDetailList.get(i);
			// 检查是否满足要求
			if (checkDetail(projectInvest, rateCouponRuleDetail) ) { // 满足规则条件或者投资金额大于最大的区间值
				ruleDetailLists.add(rateCouponRuleDetail);
			}

		}
		return ruleDetailLists;
	}

	/**
	 * 判定投资金额是否满足的区间规则
	 */
	private Boolean checkDetail(final ProjectInvest projectInvest, final RateCouponRuleDetail rateCouponRuleDetail) {
		final Double money = projectInvest.getMoney().doubleValue();
		final Double tend_min = rateCouponRuleDetail.getTenderMin().doubleValue();
		final Double tend_max = rateCouponRuleDetail.getTenderMax().doubleValue();
		boolean minCheck = true;
		boolean maxCheck = true;
		if (tend_max > 0) {
			maxCheck = money <= tend_max;
		}
		if (tend_min > 0) {
			minCheck = money >= tend_min;
		}
		return minCheck && maxCheck;
	}

	private UserActivityAwardLog initAwardLog(final User user, final RateCouponRule rateCouponRule,
			final ProjectInvest projectInvest, final User inviteUser, final double award, final String rateUuid) {
		final UserActivityAwardLog userActivityAwardLog = new UserActivityAwardLog();
		userActivityAwardLog.setUserId(user.getUuid());
		userActivityAwardLog.setAward(String.valueOf(award));
		userActivityAwardLog.setRateRuleId(rateCouponRule.getUuid());
		userActivityAwardLog.setUserRateId(rateUuid);
		if (projectInvest != null) {
			userActivityAwardLog.setTenderId(projectInvest.getUuid());
		}
		if (inviteUser != null) {
			userActivityAwardLog.setInviteUserId(inviteUser.getUuid());
		}
		userActivityAwardLog.setAwardType(OperateEnum.AWARD_TYPE_RATE.getValue());
		userActivityAwardLog.setCreateTime(DateUtils.getNow());
		userActivityAwardLog.setRuleName(rateCouponRule.getRuleName());
		userActivityAwardLog.setActivityName(rateCouponRule.getActivityName());
		userActivityAwardLog.setActivityCode(rateCouponRule.getActivityCode());
		return userActivityAwardLog;
	}

	private UserRateCoupon initUserRateCoupon(final User user, final RateCouponRule rateCouponRule,
			final BigDecimal amount, final RateCouponRuleDetail ruleDetail) {
		final UserRateCoupon userRateCoupon = new UserRateCoupon(rateCouponRule, amount, ruleDetail);
		userRateCoupon.setUserId(user.getUuid());
		final String useRedUuid = IdGen.uuid();
		userRateCoupon.setUuid(useRedUuid);
		userRateCoupon.setRuleId(rateCouponRule.getUuid());
		userRateCoupon.setRateCouponUuid(DateUtils.dateStr7(DateUtils.getNow()) + RandomUtil.getRandomString(8));
		return userRateCoupon;
	}

	/**
	 * 发放加息券 选择用户
	 */
	//@Transactional
	@Override
	public void grantSelectUserRate(final String[] userIds, final String rateCouponRuleId, int type) {
		// 调用加息券放规则 发放加息券
		final RateCouponRule rateCouponRule = get(rateCouponRuleId);
		List<RateCouponRuleDetail> details = rateCouponRuleDetailService.getListByRuleId(rateCouponRuleId);
		rateCouponRule.setDetailList(details);
		// 校验发放数量
		if (rateCouponRule.getTotalNum()!=null) {
			if (rateCouponRule.getLssueNum() + userIds.length
					* (details == null || details.size() == Constant.INT_ZERO ? Constant.INT_ONE : details.size()) > rateCouponRule.getTotalNum()) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REMAINING_RULE_NUM_INSUFFICIENT));
			}
		}
		rateCouponRule.setActivityName(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getName());
		User user = null;
		if (type == Constant.INT_ONE) {
			TokenUtils.validShiroToken(TokenConstants.TOKEN_GRANT_RATECOUPON);
		}
		List<UserRateCoupon> urcs = new ArrayList<>();
		List<UserActivityAwardLog> logs = new ArrayList<>();
		// 循环发放加息券
		for (int i = 0; i < userIds.length; i++) {
			user = userService.get(userIds[i]);
			proxySelf.grantRateCoupon(user, rateCouponRule, null, null,urcs,logs);
		}
		if(!CollectionUtils.isEmpty(urcs)){
			userRateCouponService.insertBatch(urcs);
		}
		if(!CollectionUtils.isEmpty(logs)){
			userActivityAwardLogService.insertBatch(logs);				
		}
	}

	@Override
	public List<RateCouponRule> findListForGrant(RateCouponRule rateCouponRule) {
		return dao.findListForGrant(rateCouponRule);
	}

	@Override
	public List<RateCouponRule> findByActivityCode(String activityCode) {
		return dao.findByActivityCode(activityCode);
	}

	@Override
	public String findRateCouponRuleIdByUrl(String url) {
		return dao.findRateCouponRuleIdByUrl(url);
	}

	@Override
	public void checkAndDeleteBatch(String[] ids) {
		if (dao.checkRateCouponRuleBeforeDelete(ids) > 0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ONLY_BAN_RULE_CAN_DELETE));
		}
		super.deleteBatch(ids);
	}

	/**
	 * 构造加息券实体类 并保存
	 */
	private void fillGrantRecord(RateCouponRuleDetail rateCouponRuleDetail, RateCouponRule rateCouponRule, User user,
			ProjectInvest projectInvest, User inviteUser, List<UserRateCoupon> urcs, List<UserActivityAwardLog> logs) {
		BigDecimal upApr = rateCouponRuleDetail.getUpApr();
		if (upApr.doubleValue() > 0) {
			UserRateCoupon userRateCoupon = initUserRateCoupon(user, rateCouponRule, upApr,
					rateCouponRuleDetail);
			LOGGER.info("发放的加息卷ID：" + userRateCoupon.getUuid() + "   加息卷名称:" + userRateCoupon.getRuleName());
			urcs.add(userRateCoupon);
			//userRateCouponService.insert(userRateCoupon);
			final UserActivityAwardLog userActivityAwardLog = initAwardLog(user, rateCouponRule, projectInvest,
					inviteUser, upApr.doubleValue(), userRateCoupon.getUuid());
			logs.add(userActivityAwardLog);
			//userActivityAwardLogService.insert(userActivityAwardLog);
		} else {
			LOGGER.info("加息券值错误，保存失败！");
		}
	}

	@Override
	public RateCouponRule findInvestRateCouponRuleByProjectTypeId(String projectTypeId) {
		return dao.findInvestRateCouponRuleByProjectTypeId(projectTypeId);
	}
	
	@Override
	public boolean checkGrantProjectType(String projectTypeId) {
		if(dao.countGrantProjectType(projectTypeId)>0){
			return true;
		}
		return false;
	}

}
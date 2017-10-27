package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
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
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.mapper.RedenvelopeRuleMapper;
import com.rd.ifaes.core.operate.model.RedenvelopeRuleModel;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.supports.spring.BeanSelfAware;

/**
 * ServiceImpl:RedenvelopeRuleServiceImpl
 * 
 * @author
 * @version 3.0
 * @date 2016-7-22
 */
@Service("redenvelopeRuleService")
public class RedenvelopeRuleServiceImpl extends BaseServiceImpl<RedenvelopeRuleMapper, RedenvelopeRule>
		implements RedenvelopeRuleService,BeanSelfAware {

	/** 日志记录 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RedenvelopeRuleServiceImpl.class);

	/** 可重复发放的奖励活动类型 */
	@SuppressWarnings("unused")
	private static final String[] REPEAT_GRANT = { OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue(),
			OperateEnum.ACTIVITYPLAN_FIRST_INVITE_TENDER_SUC.getValue(),
			OperateEnum.ACTIVITYPLAN_SECOND_INVITE_TENDER_SUC.getValue() };
	/** 红包规则明细业务处理 */
	@Resource
	private transient RedenvelopeRuleDetailService redenvelopeRuleDetailService;
	/** 用户红包业务处理 */
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	/** 奖励日志业务处理 */
	@Resource
	private transient UserActivityAwardLogService userActivityAwardLogService;
	/** 用户业务处理 */
	@Resource
	private transient UserService userService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	
	/** 匹配规则key值 */
	public static final String MATCH_RULE_DETAIL_KEY = "ruleDetail";
	/** 匹配规则红包金额key值 */
	public static final String RED_AMOUNT = "amount";
	
	private RedenvelopeRuleService proxySelf;
	
	@Override
	public void setSelf(Object proxyBean) {	
		this.proxySelf = (RedenvelopeRuleService)proxyBean;
	}
	
	
	/**
	 * 验证时间的正确性
	 * 
	 * @author QianPengZhan
	 * @date 2016年9月23日
	 * @param model
	 */
	private void validTime(final RedenvelopeRuleModel model) {
		if (RedenvelopeRuleModel.TIME_STATUS_YES.equals(model.getGrantTimeStatus())) {// 限制
			Date now = DateUtils.getNow();
			if (DateUtils.rollMinute(model.getGrantStartTime(), Constant.INT_ONE).getTime() < DateUtils.valueOf(DateUtils.getDateStart(now)).getTime()) {
				throw new BussinessException(ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_STARTTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
			if (model.getGrantEndTime().getTime() < now.getTime()) {
				throw new BussinessException(ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_ENDTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
			if (model.getGrantEndTime().getTime() < model.getGrantStartTime().getTime()) {
				throw new BussinessException(
						ResourceUtils.get(RedEnvelopeRuleResource.REDENVELOPERULE_STARTTIME_ENDTIME_ERROR),
						BussinessException.TYPE_CLOSE);
			}
		}
	}

	/** 保存规则 */
	@Override
	@Transactional
	public Boolean saveRule(final RedenvelopeRuleModel model, final List<RedenvelopeRuleDetail> list) {
		// 验证数据有效性
		BeanValidators.validate(model);
		model.packageRedEnvelope();
		model.checkAddModel();
		validTime(model);
		// 生成规则的uuid
		final String uuid = IdGen.uuid();
		model.setUuid(uuid);
		model.setNewRecord(true);
		// 保存规则
		save(model);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRuleId(uuid);
		}
		if (!CollectionUtils.isEmpty(list)) {
			redenvelopeRuleDetailService.insertBatch(list);
		}
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_RED_RULE);
		return true;
	}

	/**
	 * 修改红包规则
	 */
	@Override
	public Boolean updateRule(final RedenvelopeRuleModel model, final List<RedenvelopeRuleDetail> list) {
		// 验证数据有效性
		BeanValidators.validate(model);
		model.checkAddModel();
		model.packageRedEnvelope();
		model.setUpdateTime(new Date());
		validTime(model);
		model.setStatus(RedenvelopeRuleModel.STATUS_SERVICE);
		model.setUuid(null);
		// 新增红包规则表
		this.save(model);
		// 保存明细
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setRuleId(model.getUuid());
		}
		if (!CollectionUtils.isEmpty(list)) {
			redenvelopeRuleDetailService.insertBatch(list);
		}
		return true;
	}

	/**
	 * 根据红包规则发放红包
	 * 
	 * @author ywt
	 * @date 2016-11-05
	 * @param awardUser
	 * @param redenvelopeRule
	 * @param projectInvest
	 * @param inviter
	 */
	@Transactional
	@Override
	public List<BigDecimal> grantRedenvelope(final User awardUser, final RedenvelopeRule redenvelopeRule,
			final ProjectInvest projectInvest, User inviter,final List<UserRedenvelope> urvs,final List<UserActivityAwardLog> logs) {
		List<BigDecimal> redenvelopeList = new ArrayList<BigDecimal>();
		if (null == redenvelopeRule) {
			LOGGER.info("发放规则为空，发放失败");
			return redenvelopeList;
		}
		if (null == awardUser) {
			LOGGER.info("发放对象为空，发放失败");
			return redenvelopeList;
		}
		redenvelopeRule.validRule();
		BigDecimal amount = null;
		List<RedenvelopeRuleDetail> ruleDetailList = redenvelopeRuleDetailService.getListByRuleId(redenvelopeRule.getUuid());// 查询所有规则明细
		redenvelopeRule.setDetailList(ruleDetailList);
		// 根据类型不同进行发放
		boolean bl = false; // 是否生成红包,发放次数加1
		if (RedenvelopeRuleModel.GRANT_TYPE_FIXED_AMOUNT.equals(redenvelopeRule.getGrantType())) { // 固定金额
			for (RedenvelopeRuleDetail ruleDetail : ruleDetailList) {
				amount = BigDecimalUtils.round(ruleDetail.getAmount(), 0);
				if(amount.doubleValue() > 0){
					bl = true;
					redenvelopeList.add(amount);
				}
				// 生成记录
				fillGrantRecord(awardUser, redenvelopeRule, amount, ruleDetail, inviter, projectInvest, urvs, logs);
			}
		} else if(RedenvelopeRuleModel.GRANT_TYPE_FIXED_RATE.equals(redenvelopeRule.getGrantType())){ // 固定比例
			amount = calRedAmount(projectInvest,redenvelopeRule,null);
			if(amount.doubleValue() > 0){
				bl = true;
				redenvelopeList.add(amount);
			}
			// 生成记录
			fillGrantRecord(awardUser, redenvelopeRule, amount, null, inviter, projectInvest, urvs, logs);
		} else { // 比例满返、金额满返
			// 查询有效规则明细
			List<RedenvelopeRuleDetail> ruleDetailLists = calRedIntervalAmount(projectInvest, ruleDetailList);
			// 循环发放红包
			for(RedenvelopeRuleDetail ruleDetail : ruleDetailLists){
				amount = calRedAmount(projectInvest,redenvelopeRule,ruleDetail);
				if(amount.doubleValue() > 0){
					bl = true;
					redenvelopeList.add(amount);
				}
				// 生成记录
				fillGrantRecord(awardUser, redenvelopeRule, amount, ruleDetail, inviter, projectInvest, urvs, logs);
			}
		}
		// 判断发放数目是否增加
		if(bl){
			dao.updateLssueNum(redenvelopeRule);
		}
		return redenvelopeList;
	}

	/**
	 * 计算红包发放金额
	 * 
	 * @author ywt
	 * @return MAP amount红包金额 ruleDetail匹配的规则明细
	 */
	private BigDecimal calRedAmount(final ProjectInvest projectInvest, final RedenvelopeRule rule,
			final RedenvelopeRuleDetail redenvelopeRuleDetail) {
		// 红包发放金额
		BigDecimal amount = BigDecimal.valueOf(0.00d);
		// 红包发放的最小金额
		final BigDecimal minAmount = rule.getGrantMin();
		// 红包发放的最大金额
		final BigDecimal maxAmount = rule.getGrantMax();
		if (RedenvelopeRuleModel.GRANT_TYPE_FIXED_RATE.equals(rule.getGrantType())) {// 固定比例
			amount = BigDecimalUtils.mul(projectInvest.getMoney(),
					BigDecimalUtils.div(rule.getGrantRate(), BigDecimal.valueOf(100), Constant.INT_FOUR));
		} else if (RedenvelopeRuleModel.GRANT_TYPE_INTERVAL_AMOUNT.equals(rule.getGrantType())) {// 金额满返
			amount = redenvelopeRuleDetail.getAmount();
		} else if (RedenvelopeRuleModel.GRANT_TYPE_INTERVAL_RATE.equals(rule.getGrantType())) {// 比例满返
			amount = BigDecimalUtils.mul(projectInvest.getMoney(), BigDecimalUtils.div(redenvelopeRuleDetail.getGrantRate(),
					BigDecimal.valueOf(100), Constant.INT_FOUR));// 比例带%，要除以100
		}
		// 如果配置有红包最大与最小上限，则根据计算的红包金额进行处理
		if (amount.doubleValue() > 0) {
			if (minAmount.doubleValue() > 0 && amount.doubleValue() < minAmount.doubleValue()) {
				amount = minAmount;
			}
			if (maxAmount.doubleValue() > 0 && amount.doubleValue() > maxAmount.doubleValue()) {
				amount = maxAmount;
			}
		}
		return amount;
	}

	/**
	 * 金额满返、满返比例方式计算红包金额
	 * 
	 * @author ywt
	 * @date 2016-11-05
	 * @param projectInvest
	 * @param ruleDetailList
	 * @return
	 */
	public List<RedenvelopeRuleDetail> calRedIntervalAmount(final ProjectInvest projectInvest,
			final List<RedenvelopeRuleDetail> ruleDetailList) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<RedenvelopeRuleDetail> ruleDetailLists = new ArrayList<RedenvelopeRuleDetail>();
		// 计算红包金额
		for (int i = 0; i < ruleDetailList.size(); i++) {
			RedenvelopeRuleDetail redenvelopeRuleDetail = ruleDetailList.get(i);
			// 检查是否满足要求
			if (checkDetail(projectInvest, redenvelopeRuleDetail)) { // 满足规则条件
				ruleDetailLists.add(redenvelopeRuleDetail);
			}

		}
		dataMap.put(MATCH_RULE_DETAIL_KEY, ruleDetailLists);
		return ruleDetailLists;
	}

	/**
	 * 检查是否满足设置的区间
	 */
	private Boolean checkDetail(final ProjectInvest projectInvest, final RedenvelopeRuleDetail pedenvelopeRuleDetail) {
		final Double money = projectInvest.getMoney().doubleValue();
		final Double tend_min = pedenvelopeRuleDetail.getTenderMin().doubleValue();
		final Double tend_max = pedenvelopeRuleDetail.getTenderMax().doubleValue();
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

	/**
	 * 启用或则禁用规则
	 */
	@Override
	public int updateStatus(final RedenvelopeRule redenvelopeRule) {
		if (OperateEnum.STATUS_FORBIDDEN.eq(redenvelopeRule.getStatus())) {
			String[] calls1 = dao.findRedenvelopeRuleCall(redenvelopeRule.getUuid(), "user_gift");
			String[] calls2 = dao.findRedenvelopeRuleCall(redenvelopeRule.getUuid(), "user_vip_level");
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
			RedenvelopeRule rule = get(redenvelopeRule.getUuid());
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
		return dao.updateStatus(redenvelopeRule);
	}

	/**
	 * 构造奖励发放日志
	 * 
	 * @author wyw
	 * @return
	 */
	private UserActivityAwardLog initAwardLog(final User user, final RedenvelopeRule redenvelopeRule,
			final ProjectInvest projectInvest, final User inviteUser, final double award, final String redUuid) {
		final UserActivityAwardLog userActivityAwardLog = new UserActivityAwardLog();
		userActivityAwardLog.setUserId(user.getUuid());
		userActivityAwardLog.setAward(String.valueOf(award));
		userActivityAwardLog.setRedRuleId(redenvelopeRule.getUuid());
		userActivityAwardLog.setUserRedId(redUuid);
		if (projectInvest != null) {
			userActivityAwardLog.setTenderId(projectInvest.getUuid());
		}
		if (inviteUser != null) {
			userActivityAwardLog.setInviteUserId(inviteUser.getUuid());
		}
		userActivityAwardLog.setAwardType(OperateEnum.AWARD_TYPE_RED.getValue());
		userActivityAwardLog.setCreateTime(DateUtils.getNow());
		userActivityAwardLog.setRuleName(redenvelopeRule.getRuleName());
		userActivityAwardLog.setActivityName(redenvelopeRule.getActivityName());
		userActivityAwardLog.setActivityCode(redenvelopeRule.getActivityCode());
		return userActivityAwardLog;
	}

	private UserRedenvelope initUserRedenvelope(final User user, final RedenvelopeRule redenvelopeRule,
			final BigDecimal amount, final RedenvelopeRuleDetail ruleDetail) {
		final UserRedenvelope userRedenvelope = new UserRedenvelope(redenvelopeRule, amount, ruleDetail);
		userRedenvelope.setUserId(user.getUuid());
		final String useRedUuid = IdGen.uuid();
		userRedenvelope.setUuid(useRedUuid);
		userRedenvelope.setRuleId(redenvelopeRule.getUuid());
		userRedenvelope.setRedenvelopeUuid(DateUtils.dateStr7(DateUtils.getNow()) + RandomUtil.getRandomString(8));
		return userRedenvelope;
	}

	/**
	 * 手动发放红包 选择用户
	 */
	//@Transactional
	@Override
	public void grantSelectUserRed(final String[] userIds, final String redenvelopeRuleId, int type) {
		// 调用红包发放规则 发放红包
		final RedenvelopeRule redenvelopeRule = get(redenvelopeRuleId);
		List<RedenvelopeRuleDetail> details = redenvelopeRuleDetailService.getListByRuleId(redenvelopeRuleId);
		redenvelopeRule.setDetailList(details);
		redenvelopeRule.setActivityName(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getName());
		if (OperateEnum.STATUS_FORBIDDEN.getValue().equals(redenvelopeRule.getStatus())
				|| DeleteFlagEnum.YES.getValue().equals(redenvelopeRule.getDeleteFlag())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RULE_IS_BANED));
		}
		// 校验发放数量
		if (redenvelopeRule.getTotalNum()!=null) {
			if (userIds != null && redenvelopeRule.getLssueNum() + userIds.length * (details == null || details.size() == 0 ? Constant.INT_ONE : details.size()) > redenvelopeRule.getTotalNum()) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REMAINING_RULE_NUM_INSUFFICIENT));
			}
		}
		if (type == Constant.INT_ONE) {
			TokenUtils.validShiroToken(TokenConstants.TOKEN_GRANT_REDENVELOPE);
		}
		User user = null;
		List<UserRedenvelope> urvs = new ArrayList<>();
		List<UserActivityAwardLog> logs = new ArrayList<>();
		// 循环发放红包
		for (int i = 0; i < userIds.length; i++) {
			user = userService.get(userIds[i]);
			proxySelf.grantRedenvelope(user, redenvelopeRule, null, null,urvs,logs);
		}
		if(!CollectionUtils.isEmpty(urvs)){
			userRedenvelopeService.insertBatch(urvs);
		}
		if(!CollectionUtils.isEmpty(logs)){
			userActivityAwardLogService.insertBatch(logs);				
		}
	}

	@Override
	public List<RedenvelopeRule> findListForGrant(RedenvelopeRule redenvelopeRule) {
		return dao.findListForGrant(redenvelopeRule);
	}

	@Override
	public List<RedenvelopeRule> findByActivityCode(String activityCode) {
		return dao.findByActivityCode(activityCode);
	}

	@Override
	public String findRedenvelopeRuleIdByUrl(String url) {
		return dao.findRedenvelopeRuleIdByUrl(url);
	}

	@Override
	public void checkAndDeleteBatch(String[] ids) {
		if (dao.checkRedenvelopeRuleBeforeDelete(ids) > 0) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.ONLY_BAN_RULE_CAN_DELETE));
		}
		super.deleteBatch(ids);
	}

	// 构造红包实体对象
	private void fillGrantRecord(User awardUser, RedenvelopeRule redenvelopeRule, BigDecimal amount,
			RedenvelopeRuleDetail ruleDetail, User inviter, ProjectInvest invest, List<UserRedenvelope> urvs, List<UserActivityAwardLog> logs) {
		if (amount.doubleValue() > 0) {// 发放的金额大于零
			UserRedenvelope userRedenvelope = initUserRedenvelope(awardUser, redenvelopeRule, amount, ruleDetail);
			LOGGER.info("发放的红包ID：" + userRedenvelope.getUuid() + "   发放的红包名称：" + userRedenvelope.getRuleName());
			urvs.add(userRedenvelope);
			// 添加发放日志
			final UserActivityAwardLog userActivityAwardLog = initAwardLog(awardUser, redenvelopeRule, invest, inviter,
					amount.doubleValue(), userRedenvelope.getUuid());
			logs.add(userActivityAwardLog);
		} else {
			LOGGER.info("红包金额错误，保存失败！");
		}

	}

	@Override
	public RedenvelopeRule findInvestRedenvelopeRuleByProjectTypeId(String ProjectTypeId) {
		return dao.findInvestRedenvelopeRuleByProjectTypeId(ProjectTypeId);
	}


	@Override
	public boolean checkGrantProjectType(String projectTypeId) {
		if(dao.countGrantProjectType(projectTypeId)>0){
			return true;
		}
		return false;
	}

}
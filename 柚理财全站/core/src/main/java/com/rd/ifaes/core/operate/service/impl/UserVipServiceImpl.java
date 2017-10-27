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

import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.operate.domain.UserActivityAwardLog;
import com.rd.ifaes.core.operate.domain.UserGift;
import com.rd.ifaes.core.operate.domain.UserGiftGrant;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.domain.UserVip;
import com.rd.ifaes.core.operate.domain.UserVipGrowthLog;
import com.rd.ifaes.core.operate.domain.UserVipLevel;
import com.rd.ifaes.core.operate.mapper.UserVipMapper;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.operate.service.UserActivityAwardLogService;
import com.rd.ifaes.core.operate.service.UserGiftGrantService;
import com.rd.ifaes.core.operate.service.UserGiftService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.operate.service.UserVipGrowthLogService;
import com.rd.ifaes.core.operate.service.UserVipLevelService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserBaseInfo;
import com.rd.ifaes.core.user.service.UserBaseInfoService;
import com.rd.ifaes.supports.spring.BeanSelfAware;

/**
 * UserVipServiceImpl
 *
 * @author wyw
 * @version 3.0
 * @date 2016-8-2
 */
@Service("userVipService")
public class UserVipServiceImpl extends BaseServiceImpl<UserVipMapper, UserVip> implements UserVipService, BeanSelfAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserVipServiceImpl.class);
    /**
     * 红包规则业务处理
     */
    @Resource
    private transient RedenvelopeRuleService redenvelopeRuleService;
    /**
     * 红包规则明细业务处理
     */
    @Resource
    private transient RedenvelopeRuleDetailService redenvelopeRuleDetailService;
    /**
     * 加息券规则业务处理
     */
    @Resource
    private transient RateCouponRuleService rateCouponRuleService;
    /**
     * 加息券规则明细业务处理
     */
    @Resource
    private transient RateCouponRuleDetailService rateCouponRuleDetailService;
    /**
     * 用户礼包业务处理
     */
    @Resource
    private transient UserGiftService userGiftService;
    /**
     * vip等级设计业务处理
     */
    @Resource
    private transient UserVipLevelService userVipLevelService;
    /**
     * vip等级成长日志业务处理
     */
    @Resource
    private transient UserVipGrowthLogService userVipGrowthLogService;
    /**
     * 用户红包业务处理
     */
    @Resource
    private transient UserRedenvelopeService userRedenvelopeService;
    /**
     * 奖励日志业务处理
     */
    @Resource
    private transient UserActivityAwardLogService userActivityAwardLogService;
    /**
     * 用户加息券业务处理
     */
    @Resource
    private transient UserRateCouponService userRateCouponService;
    /**
     * 用户附属信息类
     */
    @Resource
    private transient UserBaseInfoService userBaseInfoService;
    /**
     * VIP礼包发放业务处理
     */
    @Resource
    private transient UserGiftGrantService userGiftGrantService;

    private UserVipService proxySelf;

    @Override
    public void setSelf(Object proxyBean) {
        this.proxySelf = (UserVipService) proxyBean;
    }

    /**
     * 根据投资金额 加vip成长值
     */

    @Override
    public void addVipGrowthByTender(final User user, final BigDecimal tenderMoney, final String remark) {
        if (ConfigUtils.getInt(ConfigConstant.VIP_OPEN) == Constant.INT_ONE) {
            final BigDecimal tenderApr = ConfigUtils.getBigDecimal(ConfigConstant.VIP_GROWTH_RATE);
            // 获取的成长值
            final BigDecimal addGrowth = BigDecimalUtils.mul(tenderMoney, tenderApr);
            if (addGrowth.doubleValue() > 0) {
                proxySelf.addVipGrowthByValue(user, addGrowth, remark);
            }
        }
    }

    /**
     * 添加vip成长值
     */
    @Override
    @Transactional
    public void addVipGrowthByValue(final User user, final BigDecimal value, final String remark) {
        // 获取vip等级
        UserVip userVip = this.getUserVip(user.getUuid());
        BigDecimal growthValueOld;
        BigDecimal growthValueNew;
        String vipLevel = String.valueOf(Constant.INT_ZERO);
        // 根据用户当前vip 和 获取的 成长值 获取用户的vip等级
        if (null == userVip) {
            growthValueOld = BigDecimal.ZERO;
            growthValueNew = BigDecimalUtils.add(growthValueOld, value);
            userVip = new UserVip(null, user, value);
            this.insert(userVip);
        } else {
            growthValueOld = userVip.getGrowthValue();
            userVip = updateVipGrowth(value, user.getUuid());
            growthValueNew = userVip.getGrowthValue();
            vipLevel = userVip.getVipLevel();
        }
        final UserVipLevel newVipLevel = userVipLevelService.getUserVipLevel(growthValueNew, vipLevel);
        int newLevel = NumberUtils.getInt(newVipLevel != null ? StringUtils.isNull(newVipLevel.getVipLevel()) : "0");
        int oldLevel = NumberUtils.getInt(userVip.getVipLevel());
        if (newVipLevel != null && newLevel > oldLevel) {//满足vip等级设置
            // 加载新vip奖励规则
            reloadUserVipLevel(newVipLevel);
            // 发放奖励
            grantAward(newVipLevel, user);
            // 升级发放礼包--生日
            grantBirthdayGift(user, newVipLevel, userVip);
            // 升级发放礼包--专享
            grantExclusiveGift(user, newVipLevel, userVip, newLevel, oldLevel);
            // 设置新等级属性
            userVip.reLoadVip(newVipLevel);
            dao.updateVipLevel(userVip);
        }
        // 加vip成长记录
        final UserVipGrowthLog userVipGrowthLog = new UserVipGrowthLog
                (growthValueOld, value, remark, OperateEnum.VIP_GROWTH_OPT_ADD.getValue(), user.getUuid());
        userVipGrowthLogService.insert(userVipGrowthLog);

    }

    /**
     * 根据vip等级发放奖励
     *
     * @param userVipLevel
     * @author wyw
     * @date 2016-8-3
     */
    private void grantAward(final UserVipLevel userVipLevel, final User user) {
        List<UserRedenvelope> urvs = new ArrayList<>();
        List<UserRateCoupon> urcs = new ArrayList<>();
        List<UserActivityAwardLog> logs = new ArrayList<>();
        RedenvelopeRule redRule = userVipLevel.getRedRule();
        if (redRule != null) {
            redRule.setActivityName(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getName());
            redenvelopeRuleService.grantRedenvelope(user, redRule, null, null, urvs, logs);
        }
        RateCouponRule rateRule = userVipLevel.getRateRule();
        if (rateRule != null) {
            rateRule.setActivityName(OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getName());
            rateCouponRuleService.grantRateCoupon(user, rateRule, null, null, urcs, logs);
        }
        if (!CollectionUtils.isEmpty(urvs)) {
            userRedenvelopeService.insertBatch(urvs);
        }
        if (!CollectionUtils.isEmpty(urcs)) {
            userRateCouponService.insertBatch(urcs);
        }
        if (!CollectionUtils.isEmpty(logs)) {
            userActivityAwardLogService.insertBatch(logs);
        }
    }

    /**
     * 发放VIP生日礼包
     *
     * @param newVipLevel
     * @param userVip
     * @author fxl
     * @date 2017年5月10日
     */
    private void grantBirthdayGift(User user, final UserVipLevel newVipLevel, final UserVip userVip) {
        if (StringUtils.isBlank(newVipLevel.getBirthdayGiftId())) {
            LOGGER.info("VIP{}级的生日礼包未配置", newVipLevel.getVipLevel());
            return;
        }
        if (StringUtils.isBlank(newVipLevel.getRedenvelopeRuleId()) && StringUtils.isBlank(newVipLevel.getExclusiveGiftId())) {
            LOGGER.info("VIP{}级的生日礼包奖励未配置", newVipLevel.getVipLevel());
            return;
        }
        if (checkOverDueGift(userVip, OperateEnum.GIFT_TYPE_BIRTHDAY.getValue())) {
            LOGGER.info("用户{}生日不在当月,不发放生日礼包", user.getUuid());
            return;
        }
        final UserGiftGrant userGifrGrant = userGiftGrantService.getAvailableUserGift(user, OperateEnum.GIFT_TYPE_BIRTHDAY.getValue());
        final UserGift userGift = userGiftService.get(newVipLevel.getBirthdayGiftId());
        if (userGifrGrant == null) {// 未发放过的则正常发放生日礼包
            userGiftGrantService.grantGift(userGift, user.getUuid(), newVipLevel.getVipLevel());
        } else if (UserGiftGrant.GIFT_STATUS_NO_RECEIVE.equals(userGifrGrant.getStatus())) {
            // 先把未领取置为失效,再领取新等级的礼包
            userGiftGrantService.reFreshBirthGift(userGifrGrant, userGift, user, userVip.getVipLevel());
        }
    }

    /**
     * 发放VIP专享礼包
     *
     * @param newVipLevel
     * @param userVip
     * @param newLevel
     * @param oldLevel
     * @author fxl
     * @date 2017年5月10日
     */
    private void grantExclusiveGift(User user, final UserVipLevel newVipLevel, final UserVip userVip, int newLevel, int oldLevel) {
        // VIP专享礼包 越级升级逐级发放礼包
        for (int vipLevel = oldLevel + 1; vipLevel <= newLevel; vipLevel++) {
            UserVipLevel level = userVipLevelService.getByVipLevel(String.valueOf(vipLevel));
            if (level == null || level.getExclusiveGiftId() == null) {
                LOGGER.info("VIP等级{}礼包未获取到", level);
                continue;
            }
            if (StringUtils.isBlank(level.getRedenvelopeRuleId()) && StringUtils.isBlank(level.getExclusiveGiftId())) {
                LOGGER.info("VIP{}级的生日礼包奖励未配置", newVipLevel.getVipLevel());
                return;
            }
            // 发放礼包
            final UserGift userGift = userGiftService.get(level.getExclusiveGiftId());
            userGiftGrantService.grantGift(userGift, user.getUuid(), level.getVipLevel());
        }
    }

    /**
     * 方法说明
     *
     * @param userVip
     * @return
     * @author wyw
     * @date 2016-8-3
     */
    private UserVip updateVipGrowth(final BigDecimal optValue, final String userId) {
        final UserVip userVip = new UserVip();
        userVip.setGrowthValue(optValue);
        userVip.setUserId(userId);
        dao.updateVipGrowth(userVip);
        return getUserVip(userId);
    }

    /**
     * 加载当前规则信息
     *
     * @param userVipLevel
     * @return
     * @author wyw
     * @date 2016-8-2
     */
    private void reloadUserVipLevel(final UserVipLevel userVipLevel) {
        //加载红包规则
        if (StringUtils.isNotBlank(userVipLevel.getRedenvelopeRuleId())) {
            final RedenvelopeRule redenvelopeRule = redenvelopeRuleService.get(userVipLevel.getRedenvelopeRuleId());
            if (redenvelopeRule != null && Constant.FLAG_YES.equals(redenvelopeRule.getStatus()) && Constant.FLAG_NO.equals(redenvelopeRule.getDeleteFlag())) {
                final List<RedenvelopeRuleDetail> detailList = redenvelopeRuleDetailService.getListByRuleId(userVipLevel.getRedenvelopeRuleId());
                redenvelopeRule.setDetailList(detailList);
                userVipLevel.setRedRule(redenvelopeRule);
            }
        }
        //加载加息券规则
        if (StringUtils.isNotBlank(userVipLevel.getRateCouponRuleId())) {
            final RateCouponRule rateCouponRule = rateCouponRuleService.get(userVipLevel.getRateCouponRuleId());
            if (rateCouponRule != null && Constant.FLAG_YES.equals(rateCouponRule.getStatus()) && Constant.FLAG_NO.equals(rateCouponRule.getDeleteFlag())) {
                final List<RateCouponRuleDetail> detailList = rateCouponRuleDetailService.getListByRuleId(userVipLevel.getRateCouponRuleId());
                rateCouponRule.setDetailList(detailList);
                userVipLevel.setRateRule(rateCouponRule);
            }
        }
    }

    /**
     * 根据用户id获取userVip
     */
    @Override
//	@Cacheable(key=CacheConstant.KEY_PREFIX_USER_VIP,expire=ExpireTime.FIVE_MIN) 
    public UserVip getUserVip(final String userId) {
        return dao.getUserVip(userId);
    }


    /**
     * 发放vip礼包
     */
    @Override
    public Map<String, Object> grantVipGift(final User user) {
        //获取用户的vip
        final UserVip userVip = this.getUserVip(user.getUuid());
        if (userVip == null || (userVip.getVipLevel().equalsIgnoreCase(String.valueOf(Constant.INT_ZERO)))) {//非vip发放失败
            throw new BussinessException("非VIP用户,无法领取礼包", BussinessException.TYPE_JSON);
        }
        // 可领取礼包
        UserGiftGrant userGiftGrant = userGiftGrantService.getAvailableUserGift(user, null);
        if (userGiftGrant != null) {
            return sentGift(userVip, userGiftGrant, user);
        } else {
            throw new BussinessException("没有礼包可以领取", BussinessException.TYPE_JSON);
        }
    }

    /**
     * 发礼包
     *
     * @return
     * @author fxl
     * @date 2016年10月11日
     */
    @Transactional(readOnly = false)
    private Map<String, Object> sentGift(final UserVip userVip, final UserGiftGrant userGiftGrant, User user) {
        if (userGiftGrant == null) {
            throw new BussinessException("礼包没有获取到", BussinessException.TYPE_JSON);
        }
        checkAvailableGift(userVip, userGiftGrant);
        final Map<String, Object> result = new HashMap<String, Object>();
        List<UserRedenvelope> urvs = new ArrayList<>();
        List<UserRateCoupon> urcs = new ArrayList<>();
        List<UserActivityAwardLog> logs = new ArrayList<>();
        if (userGiftGrantService.updateGiftStatus(userGiftGrant.getUuid(), UserGiftGrant.GIFT_STATUS_RECEIVE, UserGiftGrant.GIFT_STATUS_NO_RECEIVE)) {
            RedenvelopeRule redRule = redenvelopeRuleService.get(userGiftGrant.getRedenvelopeRuleId());
            RateCouponRule rateRule = rateCouponRuleService.get(userGiftGrant.getRateCouponRuleId());
            // 历史红包规则就算是禁用也照常发放
            if (redRule != null) redRule.setStatus(OperateEnum.STATUS_SERVICE.getValue());
            if (rateRule != null) rateRule.setStatus(OperateEnum.STATUS_SERVICE.getValue());
            result.put("redenvelope", redenvelopeRuleService.grantRedenvelope(user, redRule, null, null, urvs, logs));
            result.put("rateCoupon", rateCouponRuleService.grantRateCoupon(user, rateRule, null, null, urcs, logs));
            result.put("giftName", userGiftGrant.getGiftName());
            if (!CollectionUtils.isEmpty(urvs)) {
                userRedenvelopeService.insertBatch(urvs);
            }
            if (!CollectionUtils.isEmpty(urcs)) {
                userRateCouponService.insertBatch(urcs);
            }
            if (!CollectionUtils.isEmpty(logs)) {
                userActivityAwardLogService.insertBatch(logs);
            }
            return result;
        } else {
            throw new BussinessException("生日礼包已经发放", BussinessException.TYPE_JSON);
        }
    }

    /**
     * 校验vip礼包是否可用
     */
    @Override
    public void checkAvailableGift(final UserVip userVip, final UserGiftGrant userGiftGrant) {
        if (userVip == null) {
            throw new BussinessException("非vip用户无法领取礼包", BussinessException.TYPE_JSON);
        }
        if (OperateEnum.GIFT_TYPE_BIRTHDAY.getValue().equalsIgnoreCase(userGiftGrant.getGiftType())
                && OperateEnum.GIFT_TYPE_EXCLUSIVE.getValue().equalsIgnoreCase(userGiftGrant.getGiftType())) {
            throw new BussinessException("vip礼包类型错误", BussinessException.TYPE_JSON);
        }
        if (!UserGiftGrant.GIFT_STATUS_NO_RECEIVE.equalsIgnoreCase(userGiftGrant.getStatus())) {
            throw new BussinessException("您已经领取该礼包，不能重复领取", BussinessException.TYPE_JSON);
        }
        if (checkOverDueGift(userVip, userGiftGrant.getGiftType())) {
            throw new BussinessException("该礼包已过期", BussinessException.TYPE_JSON);
        }
    }

    /**
     * 校验礼包是否过期 true 过期  false 未过期
     *
     * @return
     * @author fxl
     * @date 2016年10月11日
     */
    //"生日礼包领取时间为您生日当月时间段内。其他时间段无法领取！";
    //"专享礼包领取时间为您vip升级后的一个月内。其他时间段无法领取！";
    private boolean checkOverDueGift(final UserVip userVip, final String giftType) {
        final Date now = DateUtils.getNow();
        if (OperateEnum.GIFT_TYPE_BIRTHDAY.eq(giftType)) {//生日礼包
            UserBaseInfo baseInfo = userBaseInfoService.findByUserId(userVip.getUserId());
            if (baseInfo != null && baseInfo.getBirthday() != null && DateUtils.checkSameMonth(baseInfo.getBirthday(), now)) {
                return false;
            }
        } else if (OperateEnum.GIFT_TYPE_EXCLUSIVE.eq(giftType)) {
            //获取用户的升级的时间
            final Date endTime = DateUtils.rollMon(userVip.getUpdateTime(), Constant.INT_ONE);//一个月内领取专项礼包
            if (now.before(endTime)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkShowVipGift(User user) {
        UserGiftGrant userGiftGrant = userGiftGrantService.getAvailableUserGift(user, null);
        if (userGiftGrant != null) {
            return true;
        }
        return false;
    }

    /**
     * 查询本月生日的用户
     *
     * @return
     * @author fxl
     * @date 2017年5月10日
     */
    @Override
    public List<UserVip> findBirthVipUser() {
        return dao.findBirthVipUser();
    }

    @Override
    public void deductionVipGrowthValue() {
        final Date date = DateUtils.rollYear(DateUtils.getNow(), Constant.INT_ONE_NEGA);
        final List<UserVip> userVipList = dao.needDeductionUser(date);
        List<UserVip> vips = new ArrayList<>();
        List<UserVipGrowthLog> logs = new ArrayList<>();
        for (UserVip userVip : userVipList) {
            if (deductionGrowthValue(userVip, logs)) {
                vips.add(userVip);
            }
        }
        super.updateBatch(vips);
        userVipGrowthLogService.insertBatch(logs);
    }

    /**
     * 扣除VIP成长值
     *
     * @param userVip
     * @author ZhangBiao
     * @date 2016年11月23日
     */
    private boolean deductionGrowthValue(UserVip userVip, List<UserVipGrowthLog> logs) {
        UserVipLevel userVipLevel = userVipLevelService.getByVipLevel(userVip.getVipLevel());
        if (userVipLevel == null || userVipLevel.getYearDeduction().doubleValue() == Constant.INT_ZERO) {
            return false;
        }
        BigDecimal yearDeduction = userVipLevel.getYearDeduction();
        BigDecimal oldGrowthValue = userVip.getGrowthValue();
        // 如果年扣除成长值大于目前成长值，则扣除值等于当前值
        if (yearDeduction.compareTo(userVipLevel.getGrowthValue()) > Constant.INT_ZERO) {
            yearDeduction = userVipLevel.getGrowthValue();
        }
        // 判断是否VIP降级
        final UserVipLevel userVipLevelNew = userVipLevelService.getUserVipLevel(BigDecimalUtils.sub(userVip.getGrowthValue(), yearDeduction), Constant.STRING_ZERO);
        if (userVipLevelNew != null && NumberUtils.getInt(userVipLevelNew.getVipLevel()) < NumberUtils.getInt(userVip.getVipLevel())) {
            userVip.setVipLevel(userVipLevelNew.getVipLevel());
        }
        // 更新VIP信息
        userVip.setGrowthValue(BigDecimalUtils.sub(userVip.getGrowthValue(), yearDeduction));
        userVip.setUpdateTime(DateUtils.getNow());
        //dao.update(userVip);

        // 扣除vip成长记录
        final UserVipGrowthLog userVipGrowthLog = new UserVipGrowthLog
                (oldGrowthValue, yearDeduction, "年扣除成长值", OperateEnum.VIP_GROWTH_OPT_SUB.getValue(), userVip.getUserId());
        logs.add(userVipGrowthLog);
        return true;
        //userVipGrowthLogService.insert(userVipGrowthLog);
    }

}
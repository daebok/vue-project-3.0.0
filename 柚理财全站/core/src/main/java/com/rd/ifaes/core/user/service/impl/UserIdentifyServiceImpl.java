package com.rd.ifaes.core.user.service.impl;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.domain.UserSecurityAnswer;
import com.rd.ifaes.core.user.mapper.UserIdentifyMapper;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserSecurityAnswerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户认证信息处理类
 *
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
@Service("userIdentifyService")
@Transactional
public class UserIdentifyServiceImpl extends BaseServiceImpl<UserIdentifyMapper, UserIdentify> implements UserIdentifyService {

    /**
     * 用户密保问题
     */
    @Resource
    private UserSecurityAnswerService userSecAnswerService;
    /**
     * 用户附属信息类
     */
    @Resource
    private UserCacheService userCacheService;
    /**
     * 授权认证
     */
    @Resource
    private UserAuthSignLogService userAuthSignLogService;

    /**
     * 根据userId从缓存中获取认证信息
     */
    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + "{userId}", expire = ExpireTime.HALF_AN_HOUR)
    public UserIdentify findByUserId(final String userId) {
        return dao.findByUserId(userId);
    }

    /**
     * 更新认证信息
     */
    @Override
    public void update(final UserIdentify entity) {
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(entity.getUserId()));
        super.update(entity);
    }

    /**
     * 更新手机状态
     */
    @Override
    public void modifyMobileStatus(final String userId, final String status, final String preStatus) {
        final UserIdentify identify = findByUserId(userId);
        if (!status.equals(identify.getMobilePhoneStatus())) {
            final UserIdentifyModel model = new UserIdentifyModel();
            model.setUuid(identify.getUuid());
            model.setUserId(userId);
            model.setMobilePhoneStatus(status);
            model.setPreStatus(preStatus);
            model.setMobilePhoneVerifyTime(DateUtils.getNow());
            final int count = dao.modifyMobileStatus(model);
            if (count <= 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.UPDATE_MOBILE_STATUS_FAIL), BussinessException.TYPE_JSON);
            }
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(userId));
            if (Constant.FLAG_YES.equals(status)) { // 手机认证通过
/*				BaseScoreLog bLog = new MobilePhoneScoreLog(userId);
                bLog.doEvent();*/
            }
        }
    }

    /**
     * 更新邮箱状态
     */
    @Override
    public void modifyEmailStatus(final String userId, final String status, final String preStatus) {
        final UserIdentify identify = findByUserId(userId);
        if (!status.equals(identify.getEmailStatus())) {
            final UserIdentifyModel model = new UserIdentifyModel();
            model.setUuid(identify.getUuid());
            model.setUserId(userId);
            model.setEmailStatus(status);
            model.setPreStatus(preStatus);
            model.setEmailVerifyTime(DateUtils.getNow());
            final int count = dao.modifyEmailStatus(model);
            if (count <= 0) {
                throw new BussinessException(ResourceUtils.get(ResourceConstant.UPDATE_EMAIL_STATUS_FAIL), BussinessException.TYPE_JSON);
            }
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(userId));
            if (Constant.FLAG_YES.equals(status)) { // 手机认证通过
/*				BaseScoreLog bLog = new MobilePhoneScoreLog(userId);
				bLog.doEvent();*/
            }
        }
    }

    /**
     * 充值进行认证校验
     */
    @Override
    public void validIdentifyForRecharge() {
        final User user = SessionUtils.getSessionUser();
        final UserIdentify identify = findByUserId(user.getUuid());
        if (Constant.FLAG_NO.equals(identify.getRealNameStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), BussinessException.TYPE_CLOSE);
        }
    }

    /**
     * 获得用户安全系数值，以%为单位，返回类似80,60,40
     *
     * @param userIdentify
     */
    @Override
    public BigDecimal getSecurityValue(final User user) {
        final UserIdentify userIdentify = findByUserId(user.getUuid());
        final BigDecimal securityTotalValue;
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        final Boolean isInvest = userCache != null && !UserCache.USER_NATURE_VOUCH.equalsIgnoreCase(userCache.getUserNature());//是否投资人
        if (isInvest) {
            securityTotalValue = BigDecimal.valueOf(7);
        } else {
            securityTotalValue = BigDecimal.valueOf(5);
        }
        BigDecimal securityValue = BigDecimal.ONE; //登录密码
        if (Constant.FLAG_YES.equals(userIdentify.getRealNameStatus())) {  //实名认证
            securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
        }
        if (Constant.FLAG_YES.equals(userIdentify.getEmailStatus())) {  //邮箱绑定
            securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
        }
        if (Constant.FLAG_YES.equals(userIdentify.getMobilePhoneStatus())) { //手机绑定
            securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
        }
        //密保问题所有账户都计算
        UserSecurityAnswer answerModel = new UserSecurityAnswer();
        answerModel.setUserId(user.getUuid());
        if (userSecAnswerService.getCount(answerModel) > 0) { //密保问题
            securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
        }

        if (isInvest) {
            if (Constant.FLAG_YES.equals(userIdentify.getAuthSignStatus())) { //授权
                securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
            }
            if (StringUtils.isNotBlank(userCache.getRiskLevel())) { //风险评估
                securityValue = BigDecimalUtils.add(securityValue, BigDecimal.ONE);
            }
        }
        return BigDecimalUtils.mul(BigDecimalUtils.div(securityValue, securityTotalValue, 2), BigDecimal.valueOf(100));
    }

    /**
     * 用户安全系数级别
     *
     * @param userIdentify
     */
    @Override
    public String getSecurityLevel(final User user) {
        final BigDecimal value = getSecurityValue(user);
        String flag = "高";
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        if (userCache != null && !UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())) {//非担保机构
            if (Constant.SECURITY_LEVEL_LOW.compareTo(value) >= 0) {
                flag = "低";
            } else if (Constant.SECURITY_LEVEL_MIDDLE.compareTo(value) >= 0) {
                flag = "中";
            }
        } else {
            if (Constant.SECURITY_LEVEL_LOW_VOUCH.compareTo(value) >= 0) {
                flag = "低";
            } else if (Constant.SECURITY_LEVEL_MIDDLE_VOUCH.compareTo(value) >= 0) {
                flag = "中";
            }
        }
        return flag;
    }

    @Override
    public void setAuthSignStatus(String userId, String authOptionOpen) {
        dao.setAuthSignStatus(userId, authOptionOpen, new Date());
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(userId));
    }

    @Override
    public void validIdentifyForInvest(final User user, final String backUrl) {
        final UserIdentify identify = findByUserId(user.getUuid());
        if (!Constant.FLAG_YES.equals(identify.getMobilePhoneStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NOT_AUTH), backUrl, BussinessException.TYPE_CLOSE);
        }
        if (!Constant.FLAG_YES.equals(identify.getRealNameStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS), backUrl, BussinessException.TYPE_CLOSE);
        }
        if (userAuthSignLogService.checkAuthSign() && !Constant.FLAG_YES.equals(identify.getAuthSignStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_AUTH_SIGN_STATUS), backUrl, BussinessException.TYPE_CLOSE);
        }
    }

    @Override
    public void setAutoCreditInvestAuthStatus(String userId, String status) {
        dao.setAutoCreditInvestAuthStatus(userId, status);
        CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID.concat(userId));
    }

}
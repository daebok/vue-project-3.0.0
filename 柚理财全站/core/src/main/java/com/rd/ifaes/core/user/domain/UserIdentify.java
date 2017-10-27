package com.rd.ifaes.core.user.domain;

import com.rd.ifaes.common.entity.BaseEntity;

import java.util.Date;

/**
 * entity:UserIdentify
 *
 * @author xhf
 * @version 3.0
 * @date 2016-6-15
 */
public class UserIdentify extends BaseEntity<UserIdentify> {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过
     */
    private String realNameStatus;
    /**
     * 实名认证时间
     */
    private Date realNameVerifyTime;
    /**
     * 邮箱认证 0:未认证 1：邮箱认证通过
     */
    private String emailStatus;
    /**
     * 邮箱认证时间
     */
    private Date emailVerifyTime;
    /**
     * 手机认证 -1:未通过,0:未认证,1:已认证
     */
    private String mobilePhoneStatus;
    /**
     * 手机认证时间
     */
    private Date mobilePhoneVerifyTime;
    /**
     * 业务授权状态（0.未授权 1.已授权，默认0）
     */
    private String authSignStatus;
    /**
     * 业务授权时间
     */
    private Date authSignVerifyTime;
    /**
     * 投资人自动债权转让签约授权状态（0.未授权 1.已授权，默认0）
     */
    private String autoCreditInvestAuthStatus;
    /**
     * 投资人自动债权转让签约授权时间
     */
    private Date autoCreditInvestAuthTime;

    //其他自定义属性


    /**
     * 构造函数
     */
    public UserIdentify() {
        super();
    }

    /**
     * 构造函数
     */
    public UserIdentify(final String userId) {
        super();
        this.userId = userId;
    }

    /**
     * 构造函数
     */
    public UserIdentify(final String uuid, final String userId, final String realNameStatus,
                        final Date realNameVerifyTime, final String emailStatus, final Date emailVerifyTime,
                        final String mobilePhoneStatus, final Date mobilePhoneVerifyTime) {
        super();
        setUuid(uuid);
        this.userId = userId;
        this.realNameStatus = realNameStatus;
        this.realNameVerifyTime = realNameVerifyTime;
        this.emailStatus = emailStatus;
        this.emailVerifyTime = emailVerifyTime;
        this.mobilePhoneStatus = mobilePhoneStatus;
        this.mobilePhoneVerifyTime = mobilePhoneVerifyTime;
    }

    /**
     * 获取用户ID
     *
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(final String userId) {
        this.userId = userId;
    }

    /**
     * 获取实名认证0:未认证1：实名认证通过-1：实名认证未通过
     *
     * @return realNameStatus
     */
    public String getRealNameStatus() {
        return realNameStatus;
    }

    /**
     * 设置实名认证0:未认证1：实名认证通过-1：实名认证未通过
     *
     * @param realNameStatus
     */
    public void setRealNameStatus(final String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    /**
     * 获取实名认证时间
     *
     * @return realNameVerifyTime
     */
    public Date getRealNameVerifyTime() {
        return realNameVerifyTime;
    }

    /**
     * 设置实名认证时间
     *
     * @param realNameVerifyTime
     */
    public void setRealNameVerifyTime(final Date realNameVerifyTime) {
        this.realNameVerifyTime = realNameVerifyTime;
    }

    /**
     * 获取邮箱认证0:未认证1：邮箱认证通过
     *
     * @return emailStatus
     */
    public String getEmailStatus() {
        return emailStatus;
    }

    /**
     * 设置邮箱认证0:未认证1：邮箱认证通过
     *
     * @param emailStatus
     */
    public void setEmailStatus(final String emailStatus) {
        this.emailStatus = emailStatus;
    }

    /**
     * 获取邮箱认证时间
     *
     * @return emailVerifyTime
     */
    public Date getEmailVerifyTime() {
        return emailVerifyTime;
    }

    /**
     * 设置邮箱认证时间
     *
     * @param emailVerifyTime
     */
    public void setEmailVerifyTime(final Date emailVerifyTime) {
        this.emailVerifyTime = emailVerifyTime;
    }

    /**
     * 获取手机认证-1:未通过0:未认证1:已认证
     *
     * @return mobilePhoneStatus
     */
    public String getMobilePhoneStatus() {
        return mobilePhoneStatus;
    }

    /**
     * 设置手机认证-1:未通过0:未认证1:已认证
     *
     * @param mobilePhoneStatus
     */
    public void setMobilePhoneStatus(final String mobilePhoneStatus) {
        this.mobilePhoneStatus = mobilePhoneStatus;
    }

    /**
     * 获取手机认证时间
     *
     * @return mobilePhoneVerifyTime
     */
    public Date getMobilePhoneVerifyTime() {
        return mobilePhoneVerifyTime;
    }

    /**
     * 设置手机认证时间
     *
     * @param mobilePhoneVerifyTime
     */
    public void setMobilePhoneVerifyTime(final Date mobilePhoneVerifyTime) {
        this.mobilePhoneVerifyTime = mobilePhoneVerifyTime;
    }

    /**
     * 获取业务授权状态（0.未授权1.已授权，默认0）
     *
     * @return authSignStatus
     */
    public String getAuthSignStatus() {
        return authSignStatus;
    }

    /**
     * 设置业务授权状态（0.未授权1.已授权，默认0）
     *
     * @param authSignStatus
     */
    public void setAuthSignStatus(final String authSignStatus) {
        this.authSignStatus = authSignStatus;
    }

    /**
     * 获取业务授权时间
     *
     * @return authSignVerifyTime
     */
    public Date getAuthSignVerifyTime() {
        return authSignVerifyTime;
    }

    /**
     * 设置业务授权时间
     *
     * @param authSignVerifyTime
     */
    public void setAuthSignVerifyTime(final Date authSignVerifyTime) {
        this.authSignVerifyTime = authSignVerifyTime;
    }

    public String getAutoCreditInvestAuthStatus() {
        return autoCreditInvestAuthStatus;
    }

    public void setAutoCreditInvestAuthStatus(String autoCreditInvestAuthStatus) {
        this.autoCreditInvestAuthStatus = autoCreditInvestAuthStatus;
    }

    public Date getAutoCreditInvestAuthTime() {
        return autoCreditInvestAuthTime;
    }

    public void setAutoCreditInvestAuthTime(Date autoCreditInvestAuthTime) {
        this.autoCreditInvestAuthTime = autoCreditInvestAuthTime;
    }

    /**
     * 重写toString()方法
     */
    @Override
    public String toString() {
        return "UserIdentify [" + "uuid=" + uuid + ", userId=" + userId + ", realNameStatus=" + realNameStatus + ", realNameVerifyTime=" + realNameVerifyTime + ", emailStatus=" + emailStatus + ", emailVerifyTime=" + emailVerifyTime + ", mobilePhoneStatus=" + mobilePhoneStatus + ", mobilePhoneVerifyTime=" + mobilePhoneVerifyTime + "]";
    }
}

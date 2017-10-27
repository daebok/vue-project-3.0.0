package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:UserActivityAwardLog
 * 
 * @author
 * @version 3.0
 * @date 2016-8-15
 */
public class UserActivityAwardLog extends BaseEntity<UserActivityAwardLog> {

	/** 序列号 */
	private static final long serialVersionUID = -4011255361697685514L;
	/** 用户id */
	private String userId;
	/** 发放的奖励(红包：金额，加息券：加息比例) */
	private String award;
	/** 红包规则id */
	private String redRuleId;
	/** 加息券规则id */
	private String rateRuleId;
	/** 发放的红包id */
	private String userRedId;
	/** 发放的加息券id */
	private String userRateId;
	/** 投标id */
	private String tenderId;
	/** 被邀请人id */
	private String inviteUserId;
	/** 奖励类型 1 红包、2加息券 */
	private String awardType;
	/** 奖发放时间 */
	private Date createTime;
	/** 活动名称 */
	private String activityName;
	/** 规则名称 */
	private String ruleName;
	/** 活动标志 */
	private String activityCode;

	// 其他自定义属性
	/** 状态 */
	private String status;
	/** 用户名 */
	private String userName;
	/** 手机号 */
	private String mobile;
	/** 真实姓名 */
	private String realName;

	/** 投资金额 */
	private BigDecimal amount;
	/** 投资项目 */
	private String projectName;

	/** 被邀请人用户名 */
	private String inviteUserName;
	/** 被邀请人手机号 */
	private String inviteMobile;
	/** 被邀请人真实姓名 */
	private String inviteRealName;
	/** 活动标示 */
	private String[] codeSet;

	/** Constructor */
	public UserActivityAwardLog() {
		super();
	}

	/**
	 * full Constructor
	 */
	public UserActivityAwardLog(final String uuid, final String userId,final String award,final String redRuleId, final String rateRuleId,
			final String userRedId,final String userRateId, final String tenderId, final String inviteUserId, final String awardType, 
			final Date createTime, final String activityName,
			final String ruleName, final String activityCode) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.award = award;
		this.redRuleId = redRuleId;
		this.rateRuleId = rateRuleId;
		this.userRedId = userRedId;
		this.userRateId = userRateId;
		this.tenderId = tenderId;
		this.inviteUserId = inviteUserId;
		this.awardType = awardType;
		this.createTime = createTime;
		this.activityName = activityName;
		this.ruleName = ruleName;
		this.activityCode = activityCode;
	}
	/** 获取 用户id */
	public String getUserId() {
		return userId;
	}
	/** 设置 用户id */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/** 获取 奖励值 */
	public String getAward() {
		return award;
	}
	/** 设置 奖励值 */
	public void setAward(final String award) {
		this.award = award;
	}
	/** 获取 红包规则id*/
	public String getRedRuleId() {
		return redRuleId;
	}
	/** 设置 红包规则id*/
	public void setRedRuleId(final String redRuleId) {
		this.redRuleId = redRuleId;
	}
	/** 获取 加息券规则id*/
	public String getRateRuleId() {
		return rateRuleId;
	}
	/** 设置 加息券规则id*/
	public void setRateRuleId(final String rateRuleId) {
		this.rateRuleId = rateRuleId;
	}
	/** 获取 用户红包id*/
	public String getUserRedId() {
		return userRedId;
	}
	/** 设置 用户红包id*/
	public void setUserRedId(final String userRedId) {
		this.userRedId = userRedId;
	}
	/** 获取 用户加息券id*/
	public String getUserRateId() {
		return userRateId;
	}
	/** 设置 用户加息券id*/
	public void setUserRateId(final String userRateId) {
		this.userRateId = userRateId;
	}
	/** 获取 投标id*/
	public String getTenderId() {
		return tenderId;
	}
	/** 设置 投标id*/
	public void setTenderId(final String tenderId) {
		this.tenderId = tenderId;
	}

	/** 获取 邀请人id*/
	public String getInviteUserId() {
		return inviteUserId;
	}
	/** 设置 邀请人id*/
	public void setInviteUserId(final String inviteUserId) {
		this.inviteUserId = inviteUserId;
	}
	/** 获取 奖励类型*/
	public String getAwardType() {
		return awardType;
	}
	/** 设置 奖励类型*/
	public void setAwardType(final String awardType) {
		this.awardType = awardType;
	}
	/** 获取 发放时间*/
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置 发放时间*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取 活动名称*/
	public String getActivityName() {
		return activityName;
	}
	/** 设置 活动名称*/
	public void setActivityName(final String activityName) {
		this.activityName = activityName;
	}
	/** 获取 规则名称*/
	public String getRuleName() {
		return ruleName;
	}
	/** 设置 规则名称*/
	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}
	/** 获取 活动标识*/
	public String getActivityCode() {
		return activityCode;
	}
	/** 设置 活动标识*/
	public void setActivityCode(final String activityCode) {
		this.activityCode = activityCode;
	}
	/** toString*/
	@Override
	public String toString() {
		return "UserActivityAwardLog [" + "uuid=" + uuid + ", userId=" + userId + ", award=" + award + ", redRuleId=" + redRuleId
				+ ", rateRuleId=" + rateRuleId + ", userRedId=" + userRedId + ", userRateId=" + userRateId + ", tenderId="
				+ tenderId + ", inviteUserId=" + inviteUserId + ", awardType=" + awardType + ", createTime=" + createTime
				+ ", activityName=" + activityName + ", ruleName=" + ruleName + ", activityCode=" + activityCode + "]";
	}

	/** 获取状态*/
	public String getStatus() {
		return status;
	}
	/** 设置状态*/
	public void setStatus(final String status) {
		this.status = status;
	}
	/** 获取用户名*/
	public String getUserName() {
		return userName;
	}
	/** 设置用户名*/
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	/** 获取手机号*/
	public String getMobile() {
		return mobile;
	}
	/** 设置手机号*/
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}
	/** 获取真实姓名*/
	public String getRealName() {
		return realName;
	}
	/** 设置真实姓名*/
	public void setRealName(final String realName) {
		this.realName = realName;
	}
	/** 获取金额*/
	public BigDecimal getAmount() {
		return amount;
	}
	/** 设置金额*/
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}
	/** 获取项目名称*/
	public String getProjectName() {
		return projectName;
	}
	/** 设置项目名称*/
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}
	/** 获取邀请人用户名*/
	public String getInviteUserName() {
		return inviteUserName;
	}
	/** 设置邀请人用户名*/
	public void setInviteUserName(final String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}
	/** 获取邀请人手机号*/
	public String getInviteMobile() {
		return inviteMobile;
	}
	/** 设置邀请人手机号*/
	public void setInviteMobile(final String inviteMobile) {
		this.inviteMobile = inviteMobile;
	}
	/** 获取邀请人真实姓名*/
	public String getInviteRealName() {
		return inviteRealName;
	}
	/** 设置邀请人真实姓名*/
	public void setInviteRealName(final String inviteRealName) {
		this.inviteRealName = inviteRealName;
	}
	/** 获取 活动标识集合*/
	public String[] getCodeSet() {
		return codeSet;
	}
	/** 设置 活动标识集合*/
	public void setCodeSet(final String[] codeSet) {
		this.codeSet = codeSet;
	}

}

package com.rd.ifaes.core.user.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.util.ObjectUtils;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;

/**
 * entity:UserInvite
 * 
 * @author wyw
 * @version 3.0
 * @date 2016-8-17
 */
public class UserInvite extends BaseEntity<UserInvite> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6018903659943378357L;
	private Date	inviteTime;		 /* 邀请时间 */ 
	private String	inviteUserId;		 /* 邀请人 */ 
	private String	inviteeUserId;		 /* 被邀请人 */ 

	//其他自定义属性
	private String inviteUserName; /* 邀请人用户名 */
	private String inviteUserRealName; /* 邀请人真实姓名 */
	private String inviteUserMobile; /* 邀请人手机号 */
	
	private String inviteeUserName; /* 被邀请人用户名 */
	private String inviteeUserRealName; /* 被邀请人真实姓名 */
	private String inviteeUserMobile; /* 被邀请人手机号 */
	private Date   inviteeUserTime; /*  被邀请人注册时间 */

	private BigDecimal awardRedTotal;/*  红包奖励总额 */
	private BigDecimal awardRateTotal;/*  加息券总个数 */
	
	private String userNature; /*  用户类型 */
	
	private BigDecimal level1Invest; /*  一级邀请好友投资总额 */
	private Integer level1Count; /*  一级邀请好友总数 */
	
	private BigDecimal level2Invest; /*  二级邀请好友投资总额 */
	private Integer level2Count; /*  二级级邀请好友总数 */
	// Constructor
	public UserInvite() {
	}

	/**
	 * full Constructor
	 */
	public UserInvite(String uuid, Date inviteTime, String inviteUserId, String inviteeUserId) {
		setUuid(uuid);
		this.inviteTime = inviteTime;
		this.inviteUserId = inviteUserId;
		this.inviteeUserId = inviteeUserId;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public String getInviteUserId() {
		return inviteUserId;
	}

	public void setInviteUserId(String inviteUserId) {
		this.inviteUserId = inviteUserId;
	}

	public String getInviteeUserId() {
		return inviteeUserId;
	}

	public void setInviteeUserId(String inviteeUserId) {
		this.inviteeUserId = inviteeUserId;
	}

	@Override
	public String toString() {
		return "UserInvite [" + "uuid=" + uuid + ", inviteTime=" + inviteTime + ", inviteUserId=" + inviteUserId + ", inviteeUserId=" + inviteeUserId +  "]";
	}

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public String getInviteUserRealName() {
		return inviteUserRealName;
	}

	public void setInviteUserRealName(String inviteUserRealName) {
		this.inviteUserRealName = inviteUserRealName;
	}

	public String getInviteUserMobile() {
		if (StringUtils.isNotBlank(inviteUserMobile) && inviteUserMobile.length() == 11) {
			return inviteUserMobile.substring(0, 3) + "****"
					+ inviteUserMobile.substring(7, 11);
		}
		return inviteUserMobile;
	}
	public String getInviteUserMobileShow() {
		return inviteUserMobile;
	}
	public void setInviteUserMobile(String inviteUserMobile) {
		this.inviteUserMobile = inviteUserMobile;
	}

	public String getInviteeUserName() {
		return inviteeUserName;
	}

	public void setInviteeUserName(String inviteeUserName) {
		this.inviteeUserName = inviteeUserName;
	}

	public String getInviteeUserRealName() {
		return inviteeUserRealName;
	}

	public void setInviteeUserRealName(String inviteeUserRealName) {
		this.inviteeUserRealName = inviteeUserRealName;
	}

	public String getInviteeUserMobile() {
		if (StringUtils.isNotBlank(inviteeUserMobile) && inviteeUserMobile.length() == 11) {
			return inviteeUserMobile.substring(0, 3) + "****"
					+ inviteeUserMobile.substring(7, 11);
		}
		return inviteeUserMobile;
	}
	public String getInviteeUserMobileShow() {
		return inviteeUserMobile;
	}
	public void setInviteeUserMobile(String inviteeUserMobile) {
		this.inviteeUserMobile = inviteeUserMobile;
	}

	public Date getInviteeUserTime() {
		return inviteeUserTime;
	}

	public void setInviteeUserTime(Date inviteeUserTime) {
		this.inviteeUserTime = inviteeUserTime;
	}

	public BigDecimal getAwardRedTotal() {
		return awardRedTotal;
	}

	public void setAwardRedTotal(BigDecimal awardRedTotal) {
		this.awardRedTotal = awardRedTotal;
	}

	public BigDecimal getLevel1Invest() {
		if(ObjectUtils.isEmpty(level1Invest)){
			level1Invest =  BigDecimalUtils.valueOf("0.00");
		}
		return level1Invest;
	}

	public void setLevel1Invest(BigDecimal level1Invest) {
		this.level1Invest = level1Invest;
	}

	public Integer getLevel1Count() {
		if(ObjectUtils.isEmpty(level1Count)){
			level1Count =  Integer.valueOf("0");
		}
		return level1Count;
	}

	public void setLevel1Count(Integer level1Count) {
		this.level1Count = level1Count;
	}

	public BigDecimal getLevel2Invest() {
		if(ObjectUtils.isEmpty(level2Invest)){
			level2Invest =  BigDecimalUtils.valueOf("0.00");
		}
		return level2Invest;
	}

	public void setLevel2Invest(BigDecimal level2Invest) {
		this.level2Invest = level2Invest;
	}

	public Integer getLevel2Count() {
		if(ObjectUtils.isEmpty(level2Count)){
			level2Count =  Integer.valueOf("0");
		}
		return level2Count;
	}

	public void setLevel2Count(Integer level2Count) {
		this.level2Count = level2Count;
	}

	public String getUserNature() {
		return userNature;
	}

	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 获取属性awardRateTotal的值
	 * @return awardRateTotal属性值
	 */
	public BigDecimal getAwardRateTotal() {
		return awardRateTotal;
	}

	/**
	 * 设置属性awardRateTotal的值
	 * @param  awardRateTotal属性值
	 */
	public void setAwardRateTotal(BigDecimal awardRateTotal) {
		this.awardRateTotal = awardRateTotal;
	}
	
}

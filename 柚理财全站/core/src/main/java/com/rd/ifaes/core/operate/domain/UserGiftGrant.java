package com.rd.ifaes.core.operate.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 用户礼包发放
 * @author fxl
 * @version 3.0
 * @date 2017-5-10
 */
public class UserGiftGrant extends BaseEntity<UserGiftGrant> {
	
	private static final long serialVersionUID = 1L;
	
	private String	giftName;		 /* 礼包名称 */ 
	private String	giftType;		 /* 礼包类型 （0生日礼包 1专享礼包） */ 
	private String	giftLevel;
	private String	userId;		 /* 用户ID */ 
	private String	redenvelopeRuleId;		 /* 红包发放规则id */ 
	private String	rateCouponRuleId;		 /* 加息券发放规则id */ 
	private String	status;		 /* 状态 */ 
	private Date	createTime;		 /* 添加时间 */ 

	//其他自定义属性
	/** 状态   未领取 */
	public static final String GIFT_STATUS_NO_RECEIVE = "0";
	/** 状态   无礼包 */
	public static final String GIFT_STATUS_NONE = "2";
	/** 状态   已经领取 */
	public static final String GIFT_STATUS_RECEIVE = "1";
	/** 状态   已经作废 */
	public static final String GIFT_STATUS_CANCLE = "3";

	// Constructor
	public UserGiftGrant() {
	}

	public UserGiftGrant(UserGift userGift,String userId,String giftLevel) {
		this.giftName = userGift.getGiftName();
		this.giftType = userGift.getGiftType();
		this.giftLevel = giftLevel;
		this.redenvelopeRuleId = userGift.getRedenvelopeRuleId();
		this.rateCouponRuleId = userGift.getRateCouponRuleId();
		this.status = GIFT_STATUS_NO_RECEIVE;
		this.userId = userId;
		this.createTime = DateUtils.getNow();
	}
	/**
	 * full Constructor
	 */
	public UserGiftGrant(String giftName, String giftType, String userId, String redenvelopeRuleId, String rateCouponRuleId, String status, Date createTime) {
		this.giftName = giftName;
		this.giftType = giftType;
		this.userId = userId;
		this.redenvelopeRuleId = redenvelopeRuleId;
		this.rateCouponRuleId = rateCouponRuleId;
		this.status = status;
		this.createTime = createTime;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getGiftType() {
		return giftType;
	}

	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}

	public String getGiftLevel() {
		return giftLevel;
	}

	public void setGiftLevel(String giftLevel) {
		this.giftLevel = giftLevel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRedenvelopeRuleId() {
		return redenvelopeRuleId;
	}

	public void setRedenvelopeRuleId(String redenvelopeRuleId) {
		this.redenvelopeRuleId = redenvelopeRuleId;
	}

	public String getRateCouponRuleId() {
		return rateCouponRuleId;
	}

	public void setRateCouponRuleId(String rateCouponRuleId) {
		this.rateCouponRuleId = rateCouponRuleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "UserGiftGrant [" + "uuid=" + uuid + ", giftName=" + giftName + ", giftType=" + giftType + ", userId=" + userId + ", redenvelopeRuleId=" + redenvelopeRuleId + ", rateCouponRuleId=" + rateCouponRuleId + ", status=" + status + ", createTime=" + createTime +  "]";
	}
}

package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 
 * 用户加息券记录
 * 
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class UserRateCoupon extends BaseUserVoucher<UserRateCoupon> {

	/** 序列号 */
	private static final long serialVersionUID = 3898781420012841067L;
	/** 加息利率 */
	private BigDecimal upApr;
	/** 加息券发放方式：1 固定金额、2金额满返 */
	private String grantType;
	/** 加息劵编号 */
	private String rateCouponUuid ;

	// 其他自定义属性
	/** 用户账号 */
	private String userName;
	/** 手机号码 */
	private String mobile;
	/** 真实姓名 */
	private String realName;
	
	/** Constructor */
	public UserRateCoupon() {
		super();
	}

	/**
	 * full Constructor
	 */
	public UserRateCoupon(final String uuid, final String userId, final String ruleName, final String activityName,
			final BigDecimal upApr, final Date useTime, final Date createTime, final String tenderId, final String status,
			final String useValidDay, final Date useExpireTime, final String grantType,
			 final BigDecimal useTenderMoney,final String useProjectType) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.ruleName = ruleName;
		this.activityName = activityName;
		this.upApr = upApr;
		this.useTime = useTime;
		this.createTime = createTime;
		this.tenderId = tenderId;
		this.status = status;
		this.useValidDay = useValidDay;
		this.useExpireTime = useExpireTime;
		this.grantType = grantType;
		this.useTenderMoney=useTenderMoney;
		this.useProjectType=useProjectType;
	}

	/**
	 * 根据红包规则设置 构造红包实体类
	 * 
	 * @param rule
	 * @param amount
	 * @param ruleDetail
	 */
	public UserRateCoupon(final RateCouponRule rule, final BigDecimal upApr, final RateCouponRuleDetail ruleDetail) {
		// 封装规则数据
		super();
		this.ruleName = rule.getRuleName();
		this.activityName = rule.getActivityName();
		this.upApr = upApr;
		this.useValidDay = rule.getUseValidDay();
		this.useExpireTime = rule.getUseExpireTime();
		this.grantType = rule.getGrantType();
		if(ruleDetail != null){
			this.useTenderMoney=ruleDetail.getUseTenderMoney()==null?BigDecimal.ZERO:ruleDetail.getUseTenderMoney();	
		}else{
			this.useTenderMoney=BigDecimal.ZERO;
		}
		this.useProjectType=rule.getUseProjectType();
		// 默认属性
		this.createTime = DateUtils.getNow();
		this.status = Constant.STATUS_NO_USE;
		// 到期属性
		if (NumberUtils.getInt(rule.getUseValidDay()) > 0) {
			this.useExpireTime = DateUtils.rollDay(new Date(), NumberUtils.getInt(rule.getUseValidDay()));
		} else if (rule.getUseExpireTime() != null) {
			this.useExpireTime = rule.getUseExpireTime();
		}
	}

	/** 获取加息利率 */
	public BigDecimal getUpApr() {
		return upApr;
	}

	/** 设置加息利率 */
	public void setUpApr(final BigDecimal upApr) {
		this.upApr = upApr;
	}

	/** 获取 发放类型 */
	public String getGrantType() {
		return grantType;
	}

	/** 设置 发放类型 */
	public void setGrantType(final String grantType) {
		this.grantType = grantType;
	}

	/** 获取 用户名 */
	public String getUserName() {
		return userName;
	}

	/** 设置 用户名 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/** 获取 手机号 */
	public String getMobile() {
		return mobile;
	}

	/** 设置 手机号 */
	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	/** 获取 真实姓名 */
	public String getRealName() {
		return realName;
	}

	/** 设置 真实姓名 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}
	
	public String getRateCouponUuid() {
		return rateCouponUuid;
	}

	public void setRateCouponUuid(String rateCouponUuid) {
		this.rateCouponUuid = rateCouponUuid;
	}

	@Override
	/** toString */
	public String toString() {
		return "UserRatecoupon [" + "uuid=" + uuid + ", userId=" + userId + ", ruleName=" + ruleName + ", activityName="
				+ activityName + ", upApr=" + upApr + ", useTime=" + useTime + ", createTime=" + createTime + ", tenderId="
				+ tenderId + ", status=" + status + ", useTenderMoney=" + useTenderMoney + ", useProjectType=" + useProjectType + ", useValidDay=" + useValidDay
				+ ", useExpireTime=" + useExpireTime + ", grantType=" + grantType +  "]";
	}
}

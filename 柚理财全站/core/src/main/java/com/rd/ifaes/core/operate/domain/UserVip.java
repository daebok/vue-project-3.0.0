package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.user.domain.User;

/**
 * entity:UserVip
 * @author 
 * @version 3.0
 * @date 2016-8-1
 */
public class UserVip extends BaseEntity<UserVip> {
	
	
	/**序列号*/
	private static final long serialVersionUID = -3053015292823111352L;
	/**用户的uuid*/
	private String	userId;		 
	/**成长值*/
	private BigDecimal	growthValue;		
	/**Vip等级 1,2,3等 */
	private String	vipLevel;		
	/**额外收益率  */
	private BigDecimal	upApr;		 
	/**生日礼包状态（0未领取，1已经领取，3已经作废）  */
	private String	birthdayGiftStatus;		 
	/** 专享礼包状态（0未领取，1已经领取，3已经作废）  */
	private String	exclusiveGiftStatus;	
	/** 获取vip等级时间  */
	private Date	createTime;		
	/** vip状态更新时间 */
	private Date	updateTime;		
	/** vip生成/更新时的vip等级json字符串 * */
	private String	vipLevelRule;	

	//其他自定义属性
	/** 用户账号 */
	private String userName; 
	/** 手机号码 */
	private String mobile; 
	/** 真实姓名 */
	private String realName;
	/** 有未领取礼包 */
	public static final String GIFT_CAN_RECEIVE = "1";
	/** 没有可领取礼包 */
	public static final String GIFT_CAN_NOT_RECEIVE = "0";
	
	/**  Constructor */
	public UserVip() {
		super();
	}
	/**  根据vip等级设置构造uservip */
	public UserVip(final UserVipLevel userVipLevel,final User user,final BigDecimal growthValue ){
		super();
		this.userId = user.getUuid();
		this.createTime =DateUtils.getNow();
		this.updateTime =DateUtils.getNow();
		this.growthValue =growthValue;
		if(userVipLevel==null){
			this.vipLevel = String.valueOf(Constant.INT_ZERO);
				
		}else{
			this.vipLevel = userVipLevel.getVipLevel();
			this.upApr = userVipLevel.getUpApr();
			this.vipLevelRule =JSONObject.toJSONString(userVipLevel);
		}
		
	}
	/**
	 * 
	 * 更新vip等级重新设置属性
	 * @author wyw
	 * @date 2016-8-3
	 * @param userVipLevel
	 */
	public void reLoadVip(final UserVipLevel userVipLevel){
		this.vipLevel = userVipLevel.getVipLevel();
		this.upApr = userVipLevel.getUpApr();
		this.updateTime =DateUtils.getNow();
		//this.vipLevelRule =JSONObject.toJSONString(userVipLevel);
	}
	/** full Constructor */
	public UserVip(final String uuid, final String userId, final BigDecimal growthValue, final String vipLevel, final BigDecimal upApr,
			final String birthdayGiftStatus, final String exclusiveGiftStatus,final  Date createTime, 
			final Date updateTime, final String vipLevelRule) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.growthValue = growthValue;
		this.vipLevel = vipLevel;
		this.upApr = upApr;
		this.birthdayGiftStatus = birthdayGiftStatus;
		this.exclusiveGiftStatus = exclusiveGiftStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.vipLevelRule = vipLevelRule;
	}
	
	public UserVip(String vipLevel, BigDecimal growthValue) {
		this.vipLevel = vipLevel;
		this.growthValue = growthValue;
	}
	/** 获取用户id */
	public String getUserId() {
		return userId;
	}
	/** 设置用户id */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	/** 获取成长值 */
	public BigDecimal getGrowthValue() {
		return growthValue;
	}
	/** 设置成长值 */
	public void setGrowthValue(final BigDecimal growthValue) {
		this.growthValue = growthValue;
	}
	/** 获取vip等级 */
	public String getVipLevel() {
		return vipLevel;
	}
	/** 设置vip等级 */
	public void setVipLevel(final String vipLevel) {
		this.vipLevel = vipLevel;
	}
	/** 获取加息利率 */
	public BigDecimal getUpApr() {
		return upApr;
	}
	/** 设置加息利率 */
	public void setUpApr(final BigDecimal upApr) {
		this.upApr = upApr;
	}
	/** 获取生日礼包状态*/
	public String getBirthdayGiftStatus() {
		return birthdayGiftStatus;
	}
	/** 设置生日礼包状态*/
	public void setBirthdayGiftStatus(final String birthdayGiftStatus) {
		this.birthdayGiftStatus = birthdayGiftStatus;
	}
	/** 获取专享礼包状态*/
	public String getExclusiveGiftStatus() {
		return exclusiveGiftStatus;
	}
	/** 设置专享礼包状态*/
	public void setExclusiveGiftStatus(final String exclusiveGiftStatus) {
		this.exclusiveGiftStatus = exclusiveGiftStatus;
	}
	/** 获取vip创建日期*/
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置vip创建日期*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
	/** 获取vip等级更新日期*/
	public Date getUpdateTime() {
		return updateTime;
	}
	/** 设置vip等级更新日期*/
	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}
	/** 获取vip等级规则*/
	public String getVipLevelRule() {
		return vipLevelRule;
	}
	/** 设置vip等级规则*/
	public void setVipLevelRule(final String vipLevelRule) {
		this.vipLevelRule = vipLevelRule;
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
	/** toString*/
	@Override
	public String toString() {
		return "UserVip [" + "uuid=" + uuid + ", userId=" + userId + ", growthValue=" + growthValue + ", vipLevel=" + vipLevel + ", upApr=" + upApr + ", birthdayGiftStatus=" + birthdayGiftStatus + ", exclusiveGiftStatus=" + exclusiveGiftStatus + ", createTime=" + createTime + ", updateTime=" + updateTime + ", vipLevelRule=" + vipLevelRule +  "]";
	}
}

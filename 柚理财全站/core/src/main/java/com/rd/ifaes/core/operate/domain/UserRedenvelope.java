package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 用户红包实体类
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class UserRedenvelope extends BaseUserVoucher<UserRedenvelope> {
	
	/**序列号*/
	private static final long serialVersionUID = -7355876229191226818L;
	/**红包金额*/
	private BigDecimal	amount;	
	/**红包发放方式：1 固定金额、2固定比例、3金额满返、4比例满返*/
	private String	grantType;	
	/**自定义红包编号*/
	private String redenvelopeUuid;

	//其他自定义属性
	/**用户账号*/
	private String userName;
	/**手机号码*/
	private String mobile;
	/**真实姓名*/
	private String realName;
	
	private String orderNo;
	/**投资期限标志（天标或者月标）*/
	private String investExpireTime;
	/**红包适用投资期限*/
	private int redInvestExpireTime;
	
	/** Constructor*/
	public UserRedenvelope() {
		super();
	}
	/**
	 * 根据红包规则设置 构造红包实体类
	 * @param rule
	 * @param amount
	 * @param ruleDetail
	 */
	public UserRedenvelope(final RedenvelopeRule rule,final BigDecimal amount,final RedenvelopeRuleDetail ruleDetail){
		// 封装规则数据
		super();
		this.ruleName = rule.getRuleName();
		this.activityName = rule.getActivityName();
		this.amount =  amount;
		this.useValidDay = rule.getUseValidDay();
		this.useExpireTime = rule.getUseExpireTime();
		this.grantType = rule.getGrantType();
		this.useProjectType=rule.getUseProjectType();
		if(ruleDetail != null){
			this.useTenderMoney=ruleDetail.getUseTenderMoney()==null?BigDecimal.ZERO:ruleDetail.getUseTenderMoney();	
		}else{
			this.useTenderMoney=BigDecimal.ZERO;
		}
		//默认属性
		this.createTime = DateUtils.getNow();
		this.status = Constant.STATUS_NO_USE;
		//到期属性
		if(NumberUtils.getInt(rule.getUseValidDay())>0){
			this.useExpireTime=DateUtils.rollDay(new Date(), NumberUtils.getInt(rule.getUseValidDay()));
		}else if(rule.getUseExpireTime()!=null){
			this.useExpireTime=rule.getUseExpireTime();
		}
	}
	/** full Constructor */
	public UserRedenvelope(final String uuid, final String userId, final String ruleName, final String activityName,
			final BigDecimal amount,final  Date useTime,final  Date createTime,final  String tenderId,
			final String status, final  String useValidDay,final Date useExpireTime,
			final String grantType,  final BigDecimal useTenderMoney,final String useProjectType) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.ruleName = ruleName;
		this.activityName = activityName;
		this.amount = amount;
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

	/** 获取红包金额 */
	public BigDecimal getAmount() {
		return amount;
	}
	/** 设置 红包金额 */
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
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
	/** 获取 发放类型 */
	public String getGrantType() {
		return grantType;
	}
	/** 设置 发放类型 */
	public void setGrantType(final String grantType) {
		this.grantType = grantType;
	}
	
	public String getRedenvelopeUuid() {
		return redenvelopeUuid;
	}
	public void setRedenvelopeUuid(String redenvelopeUuid) {
		this.redenvelopeUuid = redenvelopeUuid;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获得投资期限
	 * @return
	 */
	public String getInvestExpireTime() {
		return investExpireTime;
	}
	/**
	 * 设置投资期限
	 * @param investExpireTime
	 */
	public void setInvestExpireTime(String investExpireTime) {
		this.investExpireTime = investExpireTime;
	}
	/**
	 * 获得红包投资期限
	 * @return
	 */
	public int getRedInvestExpireTime() {
		return redInvestExpireTime;
	}
	/**
	 * 设置红包投资期限
	 * @param redInvestExpireTime
	 */
	public void setRedInvestExpireTime(int redInvestExpireTime) {
		this.redInvestExpireTime = redInvestExpireTime;
	}
	@Override
	public String toString() {
		return "UserRedenvelope [amount=" + amount + ", grantType=" + grantType + ", redenvelopeUuid=" + redenvelopeUuid
				+ ", userName=" + userName + ", mobile=" + mobile + ", realName=" + realName + ", orderNo=" + orderNo + "]";
	}
}

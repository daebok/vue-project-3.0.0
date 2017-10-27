package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

public class UserRateCouponModel {

	
	/**
	 *  加息利率
	 */
	private String upApr;
	/**
	 * 加息劵规则名称
	 */
	private String ruleName;
	/**
	 * 加息劵规则说明
	 */
	private String ruleRemark;
	public String getRuleRemark() {
		return ruleRemark;
	}
	public void setRuleRemark(String ruleRemark) {
		this.ruleRemark = ruleRemark;
	}
	/**
	 * 加息劵状态：0未使用,1已使用，2已过期 3已作废
	 */
	private String status;
	/**
	 * 使用到期时间
	 */
	private Date useExpireTime;
	/**
	 * 加息劵id
	 */
	private String uuid;
	
	/**
	 * 最低起投金额
	 */
	private BigDecimal UseTenderMoney;
	/** 
	 * 使用产品分类
	 */
	protected String useProjectType;
	/** 
	 * 使用产品分类名称 
	 */
	protected String useProjectTypeName;
	public String getUpApr() {
		return upApr;
	}
	public void setUpApr(String upApr) {
		this.upApr = upApr;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUseExpireTime() {
		return useExpireTime;
	}
	public BigDecimal getUseTenderMoney() {
		return UseTenderMoney;
	}
	public void setUseTenderMoney(BigDecimal useTenderMoney) {
		UseTenderMoney = useTenderMoney;
	}
	public String getUseProjectTypeName() {
		return useProjectTypeName;
	}
	public void setUseProjectTypeName(String useProjectTypeName) {
		this.useProjectTypeName = useProjectTypeName;
	}
	public void setUseExpireTime(Date useExpireTime) {
		this.useExpireTime = useExpireTime;
	}
	public String getUseProjectType() {
		return useProjectType;
	}
	public void setUseProjectType(String useProjectType) {
		this.useProjectType = useProjectType;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}

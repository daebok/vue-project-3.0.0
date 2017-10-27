/**
 * banner模型
 */
package com.rd.ifaes.mobile.model.account;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yoseflin
 * banner模型
 */
public class UserRedenvelopeModel {
	/**
	 * 红包金额
	 */
	private int amount;
	/**
	 * 红包规则名称
	 */
	private String ruleName;
	/**
	 * 红包规则说明
	 */
	private String ruleRemark;
	public String getRuleRemark() {
		return ruleRemark;
	}
	public void setRuleRemark(String ruleRemark) {
		this.ruleRemark = ruleRemark;
	}
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
	public String getUseProjectType() {
		return useProjectType;
	}
	public void setUseProjectType(String useProjectType) {
		this.useProjectType = useProjectType;
	}
	public String getUseProjectTypeName() {
		return useProjectTypeName;
	}
	public void setUseProjectTypeName(String useProjectTypeName) {
		this.useProjectTypeName = useProjectTypeName;
	}
	public BigDecimal getUseTenderMoney() {
		return UseTenderMoney;
	}
	public void setUseTenderMoney(BigDecimal useTenderMoney) {
		UseTenderMoney = useTenderMoney;
	}
	/**
	 * 红包状态：0未使用,1已使用，2已过期 3已作废'
	 */
	private String status;
	/**
	 * 使用到期时间
	 */
	private Date useExpireTime;
	/**
	 * 红包id
	 */
	private String uuid;
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	public void setUseExpireTime(Date useExpireTime) {
		this.useExpireTime = useExpireTime;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
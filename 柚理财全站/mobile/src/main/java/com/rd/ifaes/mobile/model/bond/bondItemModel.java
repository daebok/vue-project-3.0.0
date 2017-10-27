/**
 * banner模型
 */
package com.rd.ifaes.mobile.model.bond;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author LGX
 * banner模型
 */
public class bondItemModel {
	/**
	 * 债权名称
	 */
	private String bondName;
	/**
	 *原资产预期年化(预期年化收益率)
	 */
	private BigDecimal apr;
	public BigDecimal getApr() {
		return apr;
	}
	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}
	/**
	 * 债权有效时长
	 */
	private Integer	limitHours;
	/**
	 * 可转让金额   计算得来 
	 */
	private BigDecimal remainMoney;
	public BigDecimal getRemainMoney() {
		return remainMoney;
	}
	public void setRemainMoney(BigDecimal remainMoney) {
		this.remainMoney = remainMoney;
	}
	/**
	 * 转让成功时间
	 */
	private Date successTime;
	public Integer getLimitHours() {
		return limitHours;
	}
	public void setLimitHours(Integer limitHours) {
		this.limitHours = limitHours;
	}
	public Date getSuccessTime() {
		return successTime;
	}
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	/**
	 * 项目ID
	 */
	private String	projectId;
	/**
	 *折溢价率  
	 */
	private BigDecimal	bondApr;
	/**
	 * 债权金额
	 */
	private BigDecimal	bondMoney;
	
	/**
	 * 债权状态:发布0， 转让完成3，自动撤回4，后台撤回5
	 */
	private String	status;
	
	/**
	 * 剩余期限（天）
	 */
	private int remainDays;
	/**
	 * 开始期数
	 */
	private Integer	startPeriod;
	public String getBondName() {
		return bondName;
	}
	public void setBondName(String bondName) {
		this.bondName = bondName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public BigDecimal getBondApr() {
		return bondApr;
	}
	public void setBondApr(BigDecimal bondApr) {
		this.bondApr = bondApr;
	}
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	public Integer getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Integer startPeriod) {
		this.startPeriod = startPeriod;
	}
	
	
	
}

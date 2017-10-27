package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 受让日志model
 * @author Administrator
 *
 */
public class BondInvestLogModel {
	/**
	 * 添加IP
	 */
	private String	addIp;
	/**
	 * 债权ID  
	 */
	private String	bondId;		
	public String getBondId() {
		return bondId;
	}
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}
	/**
	 * 债权本金 
	 */
	private BigDecimal	amount;
	/**
	 * 状态 
	 */
	private String	status;	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRemainTimes() {
		return remainTimes;
	}
	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}
	/**
	 * 待支付的剩余时间 
	 */
	private int remainTimes;
	/**
	 * 债权投资订单号
	 */
	private String  investOrderNo;   
	public String getInvestOrderNo() {
		return investOrderNo;
	}
	public void setInvestOrderNo(String investOrderNo) {
		this.investOrderNo = investOrderNo;
	}
	/**
	 * 折溢价率
	 */
	private BigDecimal bondApr;
	/**
	 * 债权名称
	 */
	private String bondName;
	/**
	 * 添加时间 
	 */
	private Date	createTime;	
	/**
	 * 支付金额
	 */
	private BigDecimal paidMoney;
	/**
	 * 待收本息
	 */
	private BigDecimal waitMoney;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 *投资人债权协议
	 */
	private String BondProtocolUrl;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * InvestId（用于债权已受让列表中查询回款计划和转让操作）
	 */
	private String investId;
	/**
	 * 是否可以再次转让的标识   true 显示转让按钮；false 不显示转让按钮
	 */
	private int showBond;
	public int getShowBond() {
		return showBond;
	}
	public void setShowBond(int showBond) {
		this.showBond = showBond;
	}
	public String getAddIp() {
		return addIp;
	}
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBondApr() {
		return bondApr;
	}
	public void setBondApr(BigDecimal bondApr) {
		this.bondApr = bondApr;
	}
	public String getBondName() {
		return bondName;
	}
	public void setBondName(String bondName) {
		this.bondName = bondName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(BigDecimal paidMoney) {
		this.paidMoney = paidMoney;
	}
	public BigDecimal getWaitMoney() {
		return waitMoney;
	}
	public void setWaitMoney(BigDecimal waitMoney) {
		this.waitMoney = waitMoney;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBondProtocolUrl() {
		return BondProtocolUrl;
	}
	public void setBondProtocolUrl(String bondProtocolUrl) {
		BondProtocolUrl = bondProtocolUrl;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getInvestId() {
		return investId;
	}
	public void setInvestId(String investId) {
		this.investId = investId;
	}
	
	
}

package com.rd.ifaes.mobile.model.account.log;

import java.math.BigDecimal;
import java.util.Date;

public class InvestLogItemModel {

	/**
	 * 投资金额
	 */
	private BigDecimal	amount;
	/**
	 * 投标添加时间
	 */
	private	Date	createTime;
	/**
	 * 预期利息 
	 */
	private BigDecimal	interest;
	/**
	 * 变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0)
	 */
	protected String realizeFlag;
	public String getRealizeFlag() {
		return realizeFlag;
	}
	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}
	/**
	 * 投资方式  0手动投资;1自动投资
	 */
	private String	investType;
	/**
	 * 下一次还款日
	 */
	private Date nextRepayDate;
	/**
	 * 预收本息
	 */
	private BigDecimal	payment;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 项目状态
	 */
	private String projectStatus;
	/**
	 * 剩余期限
	 */
	private int remainDays;
	/**
	 * 已还金额 
	 */
	private BigDecimal	repayedAmount;
	/**
	 * 已还利息
	 */
	private BigDecimal	repayedInterest;
	/**
	 * 投资状态 0:待支付 1:投资成功 2:投资失败 3：退款处理中 4：投资作废 5：超时取消 6：订单失效
	 */
	private String	status;
	/**
	 * 投资状态说明
	 */
	private String statusStr;
	/**
	 * 待还总额
	 */
	private BigDecimal	waitAmount;
	/**
	 * 待还利息
	 */
	private BigDecimal	waitInterest;
	/**
	 * 起息日
	 */
	private Date interestDate;
	/**
	 * 结束日
	 */
	private Date endDate;
	/**
	 * uuid(对应回款计划中的investId)
	 */
	private String uuid;
	/**
	 *投资人下载协议
	 */
	private String ProtocolUrl;		
	/**
	 * 项目ID
	 */
	private String	projectId;
	/**
	 * 剩余支付时间
	 */
	private int remainTimes;
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getInvestType() {
		return investType;
	}
	public void setInvestType(String investType) {
		this.investType = investType;
	}
	public Date getNextRepayDate() {
		return nextRepayDate;
	}
	public void setNextRepayDate(Date nextRepayDate) {
		this.nextRepayDate = nextRepayDate;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public int getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}
	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public BigDecimal getWaitAmount() {
		return waitAmount;
	}
	public void setWaitAmount(BigDecimal waitAmount) {
		this.waitAmount = waitAmount;
	}
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}
	public Date getInterestDate() {
		return interestDate;
	}
	public void setInterestDate(Date interestDate) {
		this.interestDate = interestDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getProtocolUrl() {
		return ProtocolUrl;
	}
	public void setProtocolUrl(String protocolUrl) {
		ProtocolUrl = protocolUrl;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public int getRemainTimes() {
		return remainTimes;
	}
	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}
	
}

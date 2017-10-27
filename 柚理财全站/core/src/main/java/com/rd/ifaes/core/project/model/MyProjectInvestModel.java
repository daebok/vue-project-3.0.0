/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 账户中心-我的投资VO
 * @version 3.0
 * @author fxl
 * @date 2016年8月17日
 */
public class MyProjectInvestModel extends BaseEntity<MyProjectInvestModel> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 持有中
	 */
	public final static String TYPE_HOLDING = "1";
	/**
	 * 申请
	 */
	public final static String TYPE_APPLY = "2";
	/**
	 * 结束
	 */
	public final static String TYPE_CLOSED = "3";
	
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * 项目状态
	 */
	private String projectStatus;
	/**
	 * 投资状态
	 */
	private String	status;	
	/**
	 * 查询类型 1持有中 2 申请 3结束
	 */
	private String type;
	/**
	 * 投资金额
	 */
	private BigDecimal	amount;
	/**
	 * 预收本息
	 */
	private BigDecimal	payment;
	/**
	 * 预期利息 
	 */
	private BigDecimal	interest;
	/**
	 * 已还金额 
	 */
	private BigDecimal	repayedAmount;
	/**
	 * 已还利息
	 */
	private BigDecimal	repayedInterest;
	/**
	 * 待还总额
	 */
	private BigDecimal	waitAmount;
	/**
	 * 待还利息
	 */
	private BigDecimal	waitInterest;
	/**
	 * 剩余期限
	 */
	private int remainDays;
	/**
	 * 下一次还款日
	 */
	private Date nextRepayDate;
	/**
	 * 起息日
	 */
	private Date interestDate;
	/**
	 * 结束日
	 */
	private Date endDate;
	/**
	 * 投标添加时间
	 */
	private	Date	createTime;
	/**
	 * 投资方式  0手动投资;1自动投资
	 */
	private String	investType;
	/**
	 * 投资类型  0 普通 1 转让 2受让
	 */
	private String investStyle;
	/**
	 * 状态集合
	 */
	private String[] projectStatusSet;
	/**
	 * 投资状态集合
	 */
	private String[] statusSet;
	
	/**
	 * 变现借款标识( 1是产品变现后发布借款，0 普通借款，默认0)
	 */
	protected String realizeFlag;
	
	/**
	 * 剩余支付时间
	 */
	private int remainTimes;
	
	/**
	 * 转让的时候  持有利息
	 */
	private BigDecimal soldInterest;
	
	/**
	 * 待收加息利息 
	 */
	private BigDecimal	waitRaiseInterest;
	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取属性projectName的值
	 * @return projectName属性值
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * 设置属性projectName的值
	 * @param  projectName属性值
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取属性projectStatus的值
	 * @return projectStatus属性值
	 */
	public String getProjectStatus() {
		return projectStatus;
	}
	/**
	 * 设置属性projectStatus的值
	 * @param  projectStatus属性值
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取属性payment的值
	 * @return payment属性值
	 */
	public BigDecimal getPayment() {
		return payment;
	}
	/**
	 * 设置属性payment的值
	 * @param  payment属性值
	 */
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	/**
	 * 获取属性interest的值
	 * @return interest属性值
	 */
	public BigDecimal getInterest() {
		return interest;
	}
	/**
	 * 设置属性interest的值
	 * @param  interest属性值
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	/**
	 * 获取属性repayedAmount的值
	 * @return repayedAmount属性值
	 */
	public BigDecimal getRepayedAmount() {
		return repayedAmount;
	}
	/**
	 * 设置属性repayedAmount的值
	 * @param  repayedAmount属性值
	 */
	public void setRepayedAmount(BigDecimal repayedAmount) {
		this.repayedAmount = repayedAmount;
	}
	/**
	 * 获取属性repayedInterest的值
	 * @return repayedInterest属性值
	 */
	public BigDecimal getRepayedInterest() {
		return repayedInterest;
	}
	/**
	 * 设置属性repayedInterest的值
	 * @param  repayedInterest属性值
	 */
	public void setRepayedInterest(BigDecimal repayedInterest) {
		this.repayedInterest = repayedInterest;
	}
	/**
	 * 获取属性remainDays的值
	 * @return remainDays属性值
	 */
	public int getRemainDays() {
		return remainDays;
	}
	/**
	 * 设置属性remainDays的值
	 * @param  remainDays属性值
	 */
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}
	/**
	 * 获取属性nextRepayDate的值
	 * @return nextRepayDate属性值
	 */
	public Date getNextRepayDate() {
		return nextRepayDate;
	}
	/**
	 * 设置属性nextRepayDate的值
	 * @param  nextRepayDate属性值
	 */
	public void setNextRepayDate(Date nextRepayDate) {
		this.nextRepayDate = nextRepayDate;
	}
	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取属性waitAmount的值
	 * @return waitAmount属性值
	 */
	public BigDecimal getWaitAmount() {
		return waitAmount;
	}
	/**
	 * 设置属性waitAmount的值
	 * @param  waitAmount属性值
	 */
	public void setWaitAmount(BigDecimal waitAmount) {
		this.waitAmount = waitAmount;
	}
	/**
	 * 获取属性interestDate的值
	 * @return interestDate属性值
	 */
	public Date getInterestDate() {
		return interestDate;
	}
	/**
	 * 设置属性interestDate的值
	 * @param  interestDate属性值
	 */
	public void setInterestDate(Date interestDate) {
		this.interestDate = interestDate;
	}
	/**
	 * 获取属性endDate的值
	 * @return endDate属性值
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * 设置属性endDate的值
	 * @param  endDate属性值
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取属性waitInterest的值
	 * @return waitInterest属性值
	 */
	public BigDecimal getWaitInterest() {
		return waitInterest;
	}
	/**
	 * 设置属性waitInterest的值
	 * @param  waitInterest属性值
	 */
	public void setWaitInterest(BigDecimal waitInterest) {
		this.waitInterest = waitInterest;
	}
	/**
	 * 获取属性projectStatusSet的值
	 * @return projectStatusSet属性值
	 */
	public String[] getProjectStatusSet() {
		return projectStatusSet;
	}
	/**
	 * 设置属性projectStatusSet的值
	 * @param  projectStatusSet属性值
	 */
	public void setProjectStatusSet(String[] projectStatusSet) {
		this.projectStatusSet = projectStatusSet;
	}
	/**
	 * 获取属性type的值
	 * @return type属性值
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置属性type的值
	 * @param  type属性值
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取属性investType的值
	 * @return investType属性值
	 */
	public String getInvestType() {
		return investType;
	}
	/**
	 * 设置属性investType的值
	 * @param  investType属性值
	 */
	public void setInvestType(String investType) {
		this.investType = investType;
	}
	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	public String[] getStatusSet() {
		return statusSet;
	}
	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}
	/**
	 * 获取属性investStyle的值
	 * @return investStyle属性值
	 */
	public String getInvestStyle() {
		return investStyle;
	}
	/**
	 * 设置属性investStyle的值
	 * @param  investStyle属性值
	 */
	public void setInvestStyle(String investStyle) {
		this.investStyle = investStyle;
	}
	public String getRealizeFlag() {
		return realizeFlag;
	}
	public void setRealizeFlag(String realizeFlag) {
		this.realizeFlag = realizeFlag;
	}
	public int getRemainTimes() {
		return remainTimes;
	}
	public void setRemainTimes(int remainTimes) {
		this.remainTimes = remainTimes;
	}
	/**
	 * 获取属性soldInterest的值
	 * @return soldInterest属性值
	 */
	public BigDecimal getSoldInterest() {
		return soldInterest;
	}
	/**
	 * 设置属性soldInterest的值
	 * @param  soldInterest属性值
	 */
	public void setSoldInterest(BigDecimal soldInterest) {
		this.soldInterest = soldInterest;
	}
	/**
	 * 获取属性waitRaiseInterest的值
	 * @return waitRaiseInterest属性值
	 */
	public BigDecimal getWaitRaiseInterest() {
		return waitRaiseInterest;
	}
	/**
	 * 设置属性waitRaiseInterest的值
	 * @param  waitRaiseInterest属性值
	 */
	public void setWaitRaiseInterest(BigDecimal waitRaiseInterest) {
		this.waitRaiseInterest = waitRaiseInterest;
	}
	
}

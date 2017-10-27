package com.rd.ifaes.core.statistic.model;

import java.math.BigDecimal;

/**
 * 统计返回model
 * @version 3.0
 * @author fxl
 * @date 2017年2月21日
 */
public class StatisticResultModel{

	/**
	 * 时间
	 */
	private String statDate;
	
	/**
	 * 总计
	 */
	private BigDecimal totalCount;
	
	/**
	 * 担保计数
	 */
	private BigDecimal vouchCount;
	
	/**
	 * 抵押计数
	 */
	private BigDecimal mortgageCount;
	
	/**
	 * 其他
	 */
	private BigDecimal otherCount;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 金额
	 */
	private BigDecimal totalAmnt;
	
	/**
	 * 还款总额
	 */
	private BigDecimal payment;
	
	/**
	 * 本金
	 */
	private BigDecimal capital;
	
	/**
	 * 利息
	 */
	private BigDecimal interest;

	/**
	 * 还款笔数
	 */
	private BigDecimal repayCount;
	
	/**
	 * 人数
	 */
	private int userNum;
	
	/**
	 * 地区
	 */
	private String area;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 借款人数
	 */
	private BigDecimal borrowerNum;
	
	/**
	 * 借款金额
	 */
	private BigDecimal account;
	
	/**
	 * 借款笔数
	 */
	private BigDecimal borrowNum;
	
	/**
	 * 收入金额
	 */
	private BigDecimal incomeAmnt;
	
	/**
	 * 支出金额
	 */
	private BigDecimal payAmnt;
	
	
	public StatisticResultModel(){
		
	}
	
	public StatisticResultModel(String statDate,BigDecimal totalCount,BigDecimal mortgageCount,BigDecimal vouchCount,BigDecimal otherCount){
		this.statDate = statDate;
		this.totalCount = totalCount;
		this.mortgageCount = mortgageCount;
		this.vouchCount = vouchCount;
		this.otherCount = otherCount;
	}
	
	public StatisticResultModel(String statDate, BigDecimal totalAmnt,BigDecimal incomeAmnt, BigDecimal payAmnt) {
		this.statDate = statDate;
		this.totalAmnt = totalAmnt;
		this.incomeAmnt = incomeAmnt;
		this.payAmnt = payAmnt;
	}

	/**
	 * 获取属性statDate的值
	 * @return statDate属性值
	 */
	public String getStatDate() {
		return statDate;
	}

	/**
	 * 设置属性statDate的值
	 * @param  statDate属性值
	 */
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	/**
	 * @return the 总计
	 */
	public BigDecimal getTotalCount() {
		return totalCount;
	}

	/**
	 * @param 总计 the totalCount to set
	 */
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获取属性vouchCount的值
	 * @return vouchCount属性值
	 */
	public BigDecimal getVouchCount() {
		return vouchCount;
	}

	/**
	 * 设置属性vouchCount的值
	 * @param  vouchCount属性值
	 */
	public void setVouchCount(BigDecimal vouchCount) {
		this.vouchCount = vouchCount;
	}

	/**
	 * 获取属性mortgageCount的值
	 * @return mortgageCount属性值
	 */
	public BigDecimal getMortgageCount() {
		return mortgageCount;
	}

	/**
	 * 设置属性mortgageCount的值
	 * @param  mortgageCount属性值
	 */
	public void setMortgageCount(BigDecimal mortgageCount) {
		this.mortgageCount = mortgageCount;
	}

	/**
	 * 获取属性otherCount的值
	 * @return otherCount属性值
	 */
	public BigDecimal getOtherCount() {
		return otherCount;
	}

	/**
	 * 设置属性otherCount的值
	 * @param  otherCount属性值
	 */
	public void setOtherCount(BigDecimal otherCount) {
		this.otherCount = otherCount;
	}

	/**
	 * @return the 类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param 类型 the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the 金额
	 */
	public BigDecimal getTotalAmnt() {
		return totalAmnt;
	}

	/**
	 * @param 金额 the totalAmnt to set
	 */
	public void setTotalAmnt(BigDecimal totalAmnt) {
		this.totalAmnt = totalAmnt;
	}

	/**
	 * @return the 人数
	 */
	public int getUserNum() {
		return userNum;
	}

	/**
	 * @param 人数 the userNum to set
	 */
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	/**
	 * @return the 地区
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param 地区 the area to set
	 */
	public void setArea(String area) {
		this.area = area;
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
	 * 获取属性capital的值
	 * @return capital属性值
	 */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * 设置属性capital的值
	 * @param  capital属性值
	 */
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
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
	 * 获取属性repayCount的值
	 * @return repayCount属性值
	 */
	public BigDecimal getRepayCount() {
		return repayCount;
	}

	/**
	 * 设置属性repayCount的值
	 * @param  repayCount属性值
	 */
	public void setRepayCount(BigDecimal repayCount) {
		this.repayCount = repayCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取属性borrowerNum的值
	 * @return borrowerNum属性值
	 */
	public BigDecimal getBorrowerNum() {
		return borrowerNum;
	}

	/**
	 * 设置属性borrowerNum的值
	 * @param  borrowerNum属性值
	 */
	public void setBorrowerNum(BigDecimal borrowerNum) {
		this.borrowerNum = borrowerNum;
	}

	/**
	 * 获取属性account的值
	 * @return account属性值
	 */
	public BigDecimal getAccount() {
		return account;
	}

	/**
	 * 设置属性account的值
	 * @param  account属性值
	 */
	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	/**
	 * 获取属性borrowNum的值
	 * @return borrowNum属性值
	 */
	public BigDecimal getBorrowNum() {
		return borrowNum;
	}

	/**
	 * 设置属性borrowNum的值
	 * @param  borrowNum属性值
	 */
	public void setBorrowNum(BigDecimal borrowNum) {
		this.borrowNum = borrowNum;
	}

	/**
	 * @return the 收入金额
	 */
	public BigDecimal getIncomeAmnt() {
		return incomeAmnt;
	}

	/**
	 * @param 收入金额 the incomeAmnt to set
	 */
	public void setIncomeAmnt(BigDecimal incomeAmnt) {
		this.incomeAmnt = incomeAmnt;
	}

	/**
	 * @return the 支出金额
	 */
	public BigDecimal getPayAmnt() {
		return payAmnt;
	}

	/**
	 * @param 支出金额 the payAmnt to set
	 */
	public void setPayAmnt(BigDecimal payAmnt) {
		this.payAmnt = payAmnt;
	}
	
}

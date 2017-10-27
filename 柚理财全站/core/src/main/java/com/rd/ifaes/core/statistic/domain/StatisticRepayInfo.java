package com.rd.ifaes.core.statistic.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 还款信息
 * @version 3.0
 * @author fxl
 * @date 2017年3月1日
 */
public class StatisticRepayInfo extends BaseEntity<StatisticRepayInfo> {
	
	/** 序列号*/
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日期
	 */
	private Date recordDate;
	
	/**
	 * 待还总额
	 */
	private BigDecimal waitTotal;
	
	/**
	 * 最大单户借款余额
	 */
	private BigDecimal biggestOne;
	
	/**
	 * 最大单户余额占比
	 */
	private BigDecimal biggestOneRatio;
	
	/**
	 * 最大十户借款余额
	 */
	private BigDecimal biggestTen;
	
	/**
	 * 最大十户余额占比
	 */
	private BigDecimal biggestTenRatio;
	//以下为逾期统计字段
	/**
	 * 逾期项目数
	 */
	private BigDecimal overdueNum;
	
	/**
	 * 逾期率
	 */
	private BigDecimal overdueRatio;
	
	/**
	 * 逾期金额
	 */
	private BigDecimal overdueAccount;
	
	/**
	 * 逾期金额比率
	 */
	private BigDecimal overdueAccountRatio;
	
	/**
	 * 历史项目逾期金额
	 */
	private BigDecimal oldOverdueAccount;
	
	/**
	 * 历史项目逾期金额比率
	 */
	private BigDecimal oldOverdueRatio;
	
	/**
	 * 代偿笔数
	 */
	private BigDecimal commutationNum;
	
	/**
	 * 代偿金额
	 */
	private BigDecimal commutationAccount;
	
	/**
	 * 逾期人数
	 */
	private BigDecimal overdueBorrower;
	
	//其他属性
	/**
	 * 总金额
	 */
	private BigDecimal totalAccount;
	/**
	 * 总项目数
	 */
	private BigDecimal totalCount;
	/**
	 * 待偿总额
	 */
	private BigDecimal waitAccount;
	/**
	 * 待偿项目数
	 */
	private BigDecimal waitNum;
	
	// Constructor
	public StatisticRepayInfo() {
	}

	// 初始化
	public StatisticRepayInfo(Date recordDate) {
		this.recordDate = recordDate;
		this.waitTotal = BigDecimal.ZERO;
		this.biggestOne = BigDecimal.ZERO;
		this.biggestOneRatio = BigDecimal.ZERO;
		this.biggestTen = BigDecimal.ZERO;
		this.biggestTenRatio = BigDecimal.ZERO;
		this.overdueNum = BigDecimal.ZERO;
		this.overdueRatio = BigDecimal.ZERO;
		this.overdueAccount = BigDecimal.ZERO;
		this.overdueAccountRatio = BigDecimal.ZERO;
		this.oldOverdueAccount = BigDecimal.ZERO;
		this.oldOverdueRatio = BigDecimal.ZERO;
		this.commutationNum = BigDecimal.ZERO;
		this.commutationAccount = BigDecimal.ZERO;
		this.overdueBorrower = BigDecimal.ZERO;
	}

	/**
	 * 获取属性recordDate的值
	 * @return recordDate属性值
	 */
	public Date getRecordDate() {
		return recordDate;
	}

	/**
	 * 设置属性recordDate的值
	 * @param  recordDate属性值
	 */
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * 获取属性waitTotal的值
	 * @return waitTotal属性值
	 */
	public BigDecimal getWaitTotal() {
		return waitTotal;
	}

	/**
	 * 设置属性waitTotal的值
	 * @param  waitTotal属性值
	 */
	public void setWaitTotal(BigDecimal waitTotal) {
		this.waitTotal = waitTotal;
	}

	/**
	 * 获取属性biggestOne的值
	 * @return biggestOne属性值
	 */
	public BigDecimal getBiggestOne() {
		return biggestOne;
	}

	/**
	 * 设置属性biggestOne的值
	 * @param  biggestOne属性值
	 */
	public void setBiggestOne(BigDecimal biggestOne) {
		this.biggestOne = biggestOne;
	}

	/**
	 * 获取属性biggestOneRatio的值
	 * @return biggestOneRatio属性值
	 */
	public BigDecimal getBiggestOneRatio() {
		return biggestOneRatio;
	}

	/**
	 * 设置属性biggestOneRatio的值
	 * @param  biggestOneRatio属性值
	 */
	public void setBiggestOneRatio(BigDecimal biggestOneRatio) {
		this.biggestOneRatio = biggestOneRatio;
	}

	/**
	 * 获取属性biggestTen的值
	 * @return biggestTen属性值
	 */
	public BigDecimal getBiggestTen() {
		return biggestTen;
	}

	/**
	 * 设置属性biggestTen的值
	 * @param  biggestTen属性值
	 */
	public void setBiggestTen(BigDecimal biggestTen) {
		this.biggestTen = biggestTen;
	}

	/**
	 * 获取属性biggestTenRatio的值
	 * @return biggestTenRatio属性值
	 */
	public BigDecimal getBiggestTenRatio() {
		return biggestTenRatio;
	}

	/**
	 * 设置属性biggestTenRatio的值
	 * @param  biggestTenRatio属性值
	 */
	public void setBiggestTenRatio(BigDecimal biggestTenRatio) {
		this.biggestTenRatio = biggestTenRatio;
	}

	/**
	 * 获取属性overdueNum的值
	 * @return overdueNum属性值
	 */
	public BigDecimal getOverdueNum() {
		return overdueNum;
	}

	/**
	 * 设置属性overdueNum的值
	 * @param  overdueNum属性值
	 */
	public void setOverdueNum(BigDecimal overdueNum) {
		this.overdueNum = overdueNum;
	}

	/**
	 * 获取属性overdueRatio的值
	 * @return overdueRatio属性值
	 */
	public BigDecimal getOverdueRatio() {
		return overdueRatio;
	}

	/**
	 * 设置属性overdueRatio的值
	 * @param  overdueRatio属性值
	 */
	public void setOverdueRatio(BigDecimal overdueRatio) {
		this.overdueRatio = overdueRatio;
	}

	/**
	 * 获取属性overdueAccount的值
	 * @return overdueAccount属性值
	 */
	public BigDecimal getOverdueAccount() {
		return overdueAccount;
	}

	/**
	 * 设置属性overdueAccount的值
	 * @param  overdueAccount属性值
	 */
	public void setOverdueAccount(BigDecimal overdueAccount) {
		this.overdueAccount = overdueAccount;
	}

	/**
	 * 获取属性overdueAccountRatio的值
	 * @return overdueAccountRatio属性值
	 */
	public BigDecimal getOverdueAccountRatio() {
		return overdueAccountRatio;
	}

	/**
	 * 设置属性overdueAccountRatio的值
	 * @param  overdueAccountRatio属性值
	 */
	public void setOverdueAccountRatio(BigDecimal overdueAccountRatio) {
		this.overdueAccountRatio = overdueAccountRatio;
	}

	/**
	 * 获取属性oldOverdueAccount的值
	 * @return oldOverdueAccount属性值
	 */
	public BigDecimal getOldOverdueAccount() {
		return oldOverdueAccount;
	}

	/**
	 * 设置属性oldOverdueAccount的值
	 * @param  oldOverdueAccount属性值
	 */
	public void setOldOverdueAccount(BigDecimal oldOverdueAccount) {
		this.oldOverdueAccount = oldOverdueAccount;
	}

	/**
	 * 获取属性oldOverdueRatio的值
	 * @return oldOverdueRatio属性值
	 */
	public BigDecimal getOldOverdueRatio() {
		return oldOverdueRatio;
	}

	/**
	 * 设置属性oldOverdueRatio的值
	 * @param  oldOverdueRatio属性值
	 */
	public void setOldOverdueRatio(BigDecimal oldOverdueRatio) {
		this.oldOverdueRatio = oldOverdueRatio;
	}

	/**
	 * 获取属性commutationNum的值
	 * @return commutationNum属性值
	 */
	public BigDecimal getCommutationNum() {
		return commutationNum;
	}

	/**
	 * 设置属性commutationNum的值
	 * @param  commutationNum属性值
	 */
	public void setCommutationNum(BigDecimal commutationNum) {
		this.commutationNum = commutationNum;
	}

	/**
	 * 获取属性commutationAccount的值
	 * @return commutationAccount属性值
	 */
	public BigDecimal getCommutationAccount() {
		return commutationAccount;
	}

	/**
	 * 设置属性commutationAccount的值
	 * @param  commutationAccount属性值
	 */
	public void setCommutationAccount(BigDecimal commutationAccount) {
		this.commutationAccount = commutationAccount;
	}

	/**
	 * 获取属性overdueBorrower的值
	 * @return overdueBorrower属性值
	 */
	public BigDecimal getOverdueBorrower() {
		return overdueBorrower;
	}

	/**
	 * 设置属性overdueBorrower的值
	 * @param  overdueBorrower属性值
	 */
	public void setOverdueBorrower(BigDecimal overdueBorrower) {
		this.overdueBorrower = overdueBorrower;
	}

	/**
	 * 获取属性totalAccount的值
	 * @return totalAccount属性值
	 */
	public BigDecimal getTotalAccount() {
		return totalAccount;
	}

	/**
	 * 设置属性totalAccount的值
	 * @param  totalAccount属性值
	 */
	public void setTotalAccount(BigDecimal totalAccount) {
		this.totalAccount = totalAccount;
	}

	/**
	 * 获取属性totalCount的值
	 * @return totalCount属性值
	 */
	public BigDecimal getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置属性totalCount的值
	 * @param  totalCount属性值
	 */
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 获取属性waitAccount的值
	 * @return waitAccount属性值
	 */
	public BigDecimal getWaitAccount() {
		return waitAccount;
	}

	/**
	 * 设置属性waitAccount的值
	 * @param  waitAccount属性值
	 */
	public void setWaitAccount(BigDecimal waitAccount) {
		this.waitAccount = waitAccount;
	}

	/**
	 * 获取属性waitNum的值
	 * @return waitNum属性值
	 */
	public BigDecimal getWaitNum() {
		return waitNum;
	}

	/**
	 * 设置属性waitNum的值
	 * @param  waitNum属性值
	 */
	public void setWaitNum(BigDecimal waitNum) {
		this.waitNum = waitNum;
	}

}

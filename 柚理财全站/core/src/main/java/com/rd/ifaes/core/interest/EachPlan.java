package com.rd.ifaes.core.interest;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.util.DateUtils;

/**
 * 每期还款计划
 * 
 * @author：xx
 * @version 3.0
 * @since 2016年7月6日
 */
public class EachPlan {

	/** 本金 */
	private BigDecimal capital;
	/** 利息 */
	private BigDecimal interest;
	/** 加息利息 */
	private BigDecimal addInterest;
	/** 净收利息（扣除管理费后） */
	private BigDecimal netInterest;
	/** 本息 */
	private BigDecimal total;
	/** 净收本息（扣除管理费后） */
	private BigDecimal netTotal;
	/** 未还本息 */
	private BigDecimal needRepay;
	/** 开始计息日 */
	private Date interestTime;
	/** 期限开始日 */
	private Date periodTime;
	/** 还款日 */
	private Date repayTime;

	/**
	 * 获取本金
	 * 
	 * @return 本金
	 */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * 设置本金
	 * 
	 * @param capital 要设置的本金
	 */
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	/**
	 * 获取利息
	 * 
	 * @return 利息
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * 设置利息
	 * 
	 * @param interest 要设置的利息
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * 获取净收利息（扣除管理费后）
	 * 
	 * @return 净收利息（扣除管理费后）
	 */
	public BigDecimal getNetInterest() {
		return netInterest;
	}

	/**
	 * 设置净收利息（扣除管理费后）
	 * 
	 * @param netInterest 要设置的净收利息（扣除管理费后）
	 */
	public void setNetInterest(BigDecimal netInterest) {
		this.netInterest = netInterest;
	}

	/**
	 * 获取本息
	 * 
	 * @return 本息
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * 设置本息
	 * 
	 * @param total 要设置的本息
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * 获取净收本息（扣除管理费后）
	 * 
	 * @return 净收本息（扣除管理费后）
	 */
	public BigDecimal getNetTotal() {
		return netTotal;
	}

	/**
	 * 设置净收本息（扣除管理费后）
	 * 
	 * @param netTotal 要设置的净收本息（扣除管理费后）
	 */
	public void setNetTotal(BigDecimal netTotal) {
		this.netTotal = netTotal;
	}

	/**
	 * 获取未还本息
	 * 
	 * @return 未还本息
	 */
	public BigDecimal getNeedRepay() {
		return needRepay;
	}

	/**
	 * 设置未还本息
	 * 
	 * @param needRepay 要设置的未还本息
	 */
	public void setNeedRepay(BigDecimal needRepay) {
		this.needRepay = needRepay;
	}

	/**
	 * 获取开始计息日
	 * 
	 * @return 开始计息日
	 */
	public Date getInterestTime() {
		return interestTime;
	}

	/**
	 * 设置开始计息日
	 * 
	 * @param interestTime 要设置的开始计息日
	 */
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}

	/**
	 * 获取期限开始日
	 * @author lh
	 * @date 2016年8月2日
	 * @return
	 */
	public Date getPeriodTime() {
		return periodTime;
	}

	/**
	 * 设置期限开始日
	 * @author lh
	 * @date 2016年8月2日
	 * @param periodTime
	 */
	public void setPeriodTime(Date periodTime) {
		this.periodTime = periodTime;
	}

	/**
	 * 获取还款日
	 * 
	 * @return 还款日
	 */
	public Date getRepayTime() {
		return repayTime;
	}

	/**
	 * 设置还款日
	 * 
	 * @param repayTime 要设置的还款日
	 */
	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}
	
	public BigDecimal getAddInterest() {
		return addInterest;
	}

	public void setAddInterest(BigDecimal addInterest) {
		this.addInterest = addInterest;
	}

	@Override
	public String toString() {
		return "EachPlan [capital=" + capital + ", interest=" + interest + ", addInterest=" + addInterest + ", netInterest=" + netInterest + ", total="
				+ total + ", netTotal=" + netTotal + ", needRepay=" + needRepay + ", interestTime=" + DateUtils.formatDate(interestTime, "yyyy-MM-dd")
				+ ", periodTime=" + DateUtils.formatDate(periodTime, "yyyy-MM-dd") + ", repayTime=" + DateUtils.formatDate(repayTime, "yyyy-MM-dd") + "]";
	}

	
	

}

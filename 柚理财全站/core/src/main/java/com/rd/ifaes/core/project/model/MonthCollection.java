package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 月还款model
 * @author lh
 * @version 3.0
 * @since 2016-10-18
 *
 */
public class MonthCollection extends BaseEntity<MonthCollection> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 还款月份
	 */
	private String repayMonth;
	
	/**
	 * 还款金额
	 */
	private BigDecimal repayment;

	public String getRepayMonth() {
		return repayMonth;
	}

	public void setRepayMonth(String repayMonth) {
		this.repayMonth = repayMonth;
	}

	public BigDecimal getRepayment() {
		return repayment;
	}

	public void setRepayment(BigDecimal repayment) {
		this.repayment = repayment;
	}
	
	public MonthCollection() {
	}
	public MonthCollection(String repayMonth, BigDecimal repayment) {
		super();
		this.repayMonth = repayMonth;
		this.repayment = repayment;
	}

	@Override
	public String toString() {
		return "MonthCollection [repayMonth=" + repayMonth + ", repayment=" + repayment + "]";
	}


}

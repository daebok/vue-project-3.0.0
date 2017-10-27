package com.rd.ifaes.core.sys.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 客户-业绩统计model
 * @author lh
 * @since 2017-03-27
 * @version 3.0
 *
 */
public class SaleStatisticsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -496308292406143981L;
	
	private int saleMonth;//月份
	private BigDecimal saleAmount;//金额
	
	public int getSaleMonth() {
		return saleMonth;
	}
	public void setSaleMonth(int saleMonth) {
		this.saleMonth = saleMonth;
	}
	public BigDecimal getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}
	public SaleStatisticsModel() {
		super();
	}
	
	public SaleStatisticsModel(int saleMonth, BigDecimal saleAmount) {
		super();
		this.saleMonth = saleMonth;
		this.saleAmount = saleAmount;
	}
	
	
	
	
	
}

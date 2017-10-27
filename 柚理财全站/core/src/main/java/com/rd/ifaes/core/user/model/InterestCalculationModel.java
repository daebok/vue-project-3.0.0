package com.rd.ifaes.core.user.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 
 * 收益计算数据对象
 * @version 3.0
 * @author xxb
 * @date 2016年10月13日
 */
public class InterestCalculationModel implements Serializable{

	private static final long serialVersionUID = 7262124600061268151L;
	
	private static final String PERIOD_TYPE_DAY = "1";
	private static final String PERIOD_TYPE_MONTH = "0";
	private static final String PERIOD_TYPE_SEASONS = "2";

	/**
	 * 计算本金
	 */
	private BigDecimal principal;
	/**
	 * 期限
	 */
	private int period;
	/**
	 * 期限类型（天、月、季）
	 */
	private String periodType;
	/**
	 * 年化收益率
	 */
	private BigDecimal apr;
	/**
	 * 还款方式
	 */
	private String repayStyle;
	
	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public BigDecimal getApr() {
		return apr;
	}

	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}

	public String getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(String repayStyle) {
		this.repayStyle = repayStyle;
	}
	
	
	/**
	 * 参数校验非法输入统一提示为必填项(不做利率范围校验)
	 * 期限类型（天、月、季）和还款方式对应校验
	 * @param  
	 * @return void
	 * @author xxb
	 * @date 2016年10月17日
	 */
	public void checkCalculation(){
		
		if(this.getPrincipal() == null || BigDecimal.ZERO.compareTo(this.getPrincipal()) >= Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PRINCIPAL_NOT_EMPTY));
		}
		if(this.getPeriod() <= Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PERIOD_NOT_EMPTY));
		}
		if(this.getApr() == null || BigDecimal.ZERO.compareTo(this.getApr()) >= Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_NOTEMPTY));
		}
		
		if((PERIOD_TYPE_DAY.equals(this.getPeriodType()) && !LoanEnum.STYLE_ONETIME_REPAYMENT.getValue().equals(this.getRepayStyle()))
			 || (PERIOD_TYPE_MONTH.equals(this.getPeriodType()) && LoanEnum.STYLE_QUARTER_INTEREST.getValue().equals(this.getRepayStyle()))	
			 ||	(PERIOD_TYPE_SEASONS.equals(this.getPeriodType()) && !LoanEnum.STYLE_QUARTER_INTEREST.getValue().equals(this.getRepayStyle()))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.CALCULATOR_REPAY_STYLE_ERROR));
		}
	}
}

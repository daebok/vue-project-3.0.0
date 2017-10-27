package com.rd.ifaes.core.project.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 变现规则实体
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public class RealizeRule extends BaseEntity<RealizeRule> {
	
	private static final long serialVersionUID = 1L;
	
	private Date	createTime;		 /* 添加时间 */ 
	private String	ruleName;		/* 规则名称*/
	private String	ruleStatus;		 /* 1 开启 0未开启  默认1,未开启不允许发布变现 */ 
	private Integer	holdDays;		 /* 起息持有天数  默认 0 不开启此规则 */ 
	private Integer	remainDays;		 /* 还款到期剩余天数,默认0 不开启此规则 */ 
	private Integer	periodRemainDays;		 /* 本期还款剩余天数 0 不开启此规则 */ 
	private String	sellStyle;		 /* 变现金额方式:0 部分变现;1全额变现,默认0 */ 
	private BigDecimal	sellAmountMin;		 /* 变现最小金额 */ 
	private String	buyStyle;		 /* 投资金额方式:0 部分投资;1全额投资,默认0 */ 
	private BigDecimal	buyAmountMin;		 /* 单笔投资最小金额 */ 
	private BigDecimal	buyAmountMax;		 /* 单笔投资最大金额 */ 
	@NotNull(message = "{realize.realizeRateMin.emptyMsg}")
	private BigDecimal	realizeRateMin;		 /* 变现利率下限值 */
	@NotNull(message = "{realize.realizeRateMax.emptyMsg}")
	private BigDecimal	realizeRateMax;		 /* 变现利率上限值 */ 
	private BigDecimal	overdueFeeRate;		/* 逾期罚息 */ 
	@NotNull(message = "{realize.feeRate.emptyMsg}")
	private BigDecimal	feeRate;		 /* 转让金额百分比收取手续费（默认为0，单位是%） */
	@NotNull(message = "{realize.feeSingleMax.emptyMsg}")
	private BigDecimal	feeSingleMax;		 /* 单笔手续费上限值（单位:元） */ 
	private String	issueTime;		 /* 发布时间 */ 
	private String	raiseEndTime;		 /* 募集结束时间 */ 
	private BigDecimal	interestManageRate;		 /* 利息管理费 */ 
	
	//其他自定义属性
	private String	isHoldDays;			/* 起息日限制是否开启  */ 
	private String	isRemainDays;			/* 到期日时限是否开启 */ 
	private String	isPeriodRemainDays;			/* 本期还款时限是否开启  */
	private String	issueTimeHour;		/* 发布时(前台用)*/ 
	private String	issueTimeMin;		/* 发布分 (前台用)*/ 
	private String	raiseEndTimeHour;		/* 募集结束时 (前台用)*/ 
	private String	raiseEndTimeMin;		/* 募集结束分 (前台用)*/ 
	
	/** 部分变现 */
	public static final String PART_REALIZE = "0";
	/** 全额变现 */
	public static final String FULL_REALIZE = "1";
	/** 部分投资 */
	public static final String PART_INVEST = "0";
	/** 全额投资 */
	public static final String FULL_INVEST = "1";
	
	// Constructor
	public RealizeRule() {
	}

	
	/**
	 * 获取属性ruleName的值
	 * @return ruleName属性值
	 */
	public String getRuleName() {
		return ruleName;
	}


	/**
	 * 设置属性ruleName的值
	 * @param  ruleName属性值
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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
	 * 获取属性ruleStatus的值
	 * @return ruleStatus属性值
	 */
	public String getRuleStatus() {
		return ruleStatus;
	}

	/**
	 * 设置属性ruleStatus的值
	 * @param  ruleStatus属性值
	 */
	public void setRuleStatus(String ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	/**
	 * 获取属性holdDays的值
	 * @return holdDays属性值
	 */
	public Integer getHoldDays() {
		return holdDays;
	}

	/**
	 * 设置属性holdDays的值
	 * @param  holdDays属性值
	 */
	public void setHoldDays(Integer holdDays) {
		this.holdDays = holdDays;
	}

	/**
	 * 获取属性remainDays的值
	 * @return remainDays属性值
	 */
	public Integer getRemainDays() {
		return remainDays;
	}

	/**
	 * 设置属性remainDays的值
	 * @param  remainDays属性值
	 */
	public void setRemainDays(Integer remainDays) {
		this.remainDays = remainDays;
	}

	/**
	 * 获取属性periodRemainDays的值
	 * @return periodRemainDays属性值
	 */
	public Integer getPeriodRemainDays() {
		return periodRemainDays;
	}

	/**
	 * 设置属性periodRemainDays的值
	 * @param  periodRemainDays属性值
	 */
	public void setPeriodRemainDays(Integer periodRemainDays) {
		this.periodRemainDays = periodRemainDays;
	}

	/**
	 * 获取属性sellStyle的值
	 * @return sellStyle属性值
	 */
	public String getSellStyle() {
		return sellStyle;
	}

	/**
	 * 设置属性sellStyle的值
	 * @param  sellStyle属性值
	 */
	public void setSellStyle(String sellStyle) {
		this.sellStyle = sellStyle;
	}

	/**
	 * 获取属性sellAmountMin的值
	 * @return sellAmountMin属性值
	 */
	public BigDecimal getSellAmountMin() {
		return sellAmountMin;
	}

	/**
	 * 设置属性sellAmountMin的值
	 * @param  sellAmountMin属性值
	 */
	public void setSellAmountMin(BigDecimal sellAmountMin) {
		this.sellAmountMin = sellAmountMin;
	}

	/**
	 * 获取属性buyStyle的值
	 * @return buyStyle属性值
	 */
	public String getBuyStyle() {
		return buyStyle;
	}

	/**
	 * 设置属性buyStyle的值
	 * @param  buyStyle属性值
	 */
	public void setBuyStyle(String buyStyle) {
		this.buyStyle = buyStyle;
	}

	/**
	 * 获取属性buyAmountMin的值
	 * @return buyAmountMin属性值
	 */
	public BigDecimal getBuyAmountMin() {
		return buyAmountMin;
	}

	/**
	 * 设置属性buyAmountMin的值
	 * @param  buyAmountMin属性值
	 */
	public void setBuyAmountMin(BigDecimal buyAmountMin) {
		this.buyAmountMin = buyAmountMin;
	}

	/**
	 * 获取属性buyAmountMax的值
	 * @return buyAmountMax属性值
	 */
	public BigDecimal getBuyAmountMax() {
		return buyAmountMax;
	}

	/**
	 * 设置属性buyAmountMax的值
	 * @param  buyAmountMax属性值
	 */
	public void setBuyAmountMax(BigDecimal buyAmountMax) {
		this.buyAmountMax = buyAmountMax;
	}

	/**
	 * 获取属性realizeRateMin的值
	 * @return realizeRateMin属性值
	 */
	public BigDecimal getRealizeRateMin() {
		return realizeRateMin;
	}

	/**
	 * 设置属性realizeRateMin的值
	 * @param  realizeRateMin属性值
	 */
	public void setRealizeRateMin(BigDecimal realizeRateMin) {
		this.realizeRateMin = realizeRateMin;
	}

	/**
	 * 获取属性realizeRateMax的值
	 * @return realizeRateMax属性值
	 */
	public BigDecimal getRealizeRateMax() {
		return realizeRateMax;
	}

	/**
	 * 设置属性realizeRateMax的值
	 * @param  realizeRateMax属性值
	 */
	public void setRealizeRateMax(BigDecimal realizeRateMax) {
		this.realizeRateMax = realizeRateMax;
	}

	/**
	 * 获取属性overdueFeeRate的值
	 * @return overdueFeeRate属性值
	 */
	public BigDecimal getOverdueFeeRate() {
		return overdueFeeRate;
	}

	/**
	 * 设置属性overdueFeeRate的值
	 * @param  overdueFeeRate属性值
	 */
	public void setOverdueFeeRate(BigDecimal overdueFeeRate) {
		this.overdueFeeRate = overdueFeeRate;
	}

	/**
	 * 获取属性feeRate的值
	 * @return feeRate属性值
	 */
	public BigDecimal getFeeRate() {
		return feeRate;
	}

	/**
	 * 设置属性feeRate的值
	 * @param  feeRate属性值
	 */
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	/**
	 * 获取属性feeSingleMax的值
	 * @return feeSingleMax属性值
	 */
	public BigDecimal getFeeSingleMax() {
		return feeSingleMax;
	}

	/**
	 * 设置属性feeSingleMax的值
	 * @param  feeSingleMax属性值
	 */
	public void setFeeSingleMax(BigDecimal feeSingleMax) {
		this.feeSingleMax = feeSingleMax;
	}

	/**
	 * 获取属性issueTime的值
	 * @return issueTime属性值
	 */
	public String getIssueTime() {
		return issueTime;
	}

	/**
	 * 设置属性issueTime的值
	 * @param  issueTime属性值
	 */
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	/**
	 * 获取属性raiseEndTime的值
	 * @return raiseEndTime属性值
	 */
	public String getRaiseEndTime() {
		return raiseEndTime;
	}

	/**
	 * 设置属性raiseEndTime的值
	 * @param  raiseEndTime属性值
	 */
	public void setRaiseEndTime(String raiseEndTime) {
		this.raiseEndTime = raiseEndTime;
	}

	/**
	 * 获取属性interestManageRate的值
	 * @return interestManageRate属性值
	 */
	public BigDecimal getInterestManageRate() {
		return interestManageRate;
	}

	/**
	 * 设置属性interestManageRate的值
	 * @param  interestManageRate属性值
	 */
	public void setInterestManageRate(BigDecimal interestManageRate) {
		this.interestManageRate = interestManageRate;
	}

	/**
	 * 获取属性isHoldDays的值
	 * @return isHoldDays属性值
	 */
	public String getIsHoldDays() {
		return isHoldDays;
	}

	/**
	 * 设置属性isHoldDays的值
	 * @param  isHoldDays属性值
	 */
	public void setIsHoldDays(String isHoldDays) {
		this.isHoldDays = isHoldDays;
	}

	/**
	 * 获取属性isRemainDays的值
	 * @return isRemainDays属性值
	 */
	public String getIsRemainDays() {
		return isRemainDays;
	}

	/**
	 * 设置属性isRemainDays的值
	 * @param  isRemainDays属性值
	 */
	public void setIsRemainDays(String isRemainDays) {
		this.isRemainDays = isRemainDays;
	}

	/**
	 * 获取属性isPeriodRemainDays的值
	 * @return isPeriodRemainDays属性值
	 */
	public String getIsPeriodRemainDays() {
		return isPeriodRemainDays;
	}

	/**
	 * 设置属性isPeriodRemainDays的值
	 * @param  isPeriodRemainDays属性值
	 */
	public void setIsPeriodRemainDays(String isPeriodRemainDays) {
		this.isPeriodRemainDays = isPeriodRemainDays;
	}

	/**
	 * 获取属性issueTimeHour的值
	 * @return issueTimeHour属性值
	 */
	public String getIssueTimeHour() {
		return issueTimeHour;
	}

	/**
	 * 设置属性issueTimeHour的值
	 * @param  issueTimeHour属性值
	 */
	public void setIssueTimeHour(String issueTimeHour) {
		this.issueTimeHour = issueTimeHour;
	}

	/**
	 * 获取属性issueTimeMin的值
	 * @return issueTimeMin属性值
	 */
	public String getIssueTimeMin() {
		return issueTimeMin;
	}

	/**
	 * 设置属性issueTimeMin的值
	 * @param  issueTimeMin属性值
	 */
	public void setIssueTimeMin(String issueTimeMin) {
		this.issueTimeMin = issueTimeMin;
	}

	/**
	 * 获取属性raiseEndTimeHour的值
	 * @return raiseEndTimeHour属性值
	 */
	public String getRaiseEndTimeHour() {
		return raiseEndTimeHour;
	}

	/**
	 * 设置属性raiseEndTimeHour的值
	 * @param  raiseEndTimeHour属性值
	 */
	public void setRaiseEndTimeHour(String raiseEndTimeHour) {
		this.raiseEndTimeHour = raiseEndTimeHour;
	}

	/**
	 * 获取属性raiseEndTimeMin的值
	 * @return raiseEndTimeMin属性值
	 */
	public String getRaiseEndTimeMin() {
		return raiseEndTimeMin;
	}

	/**
	 * 设置属性raiseEndTimeMin的值
	 * @param  raiseEndTimeMin属性值
	 */
	public void setRaiseEndTimeMin(String raiseEndTimeMin) {
		this.raiseEndTimeMin = raiseEndTimeMin;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = DateUtils.getNow();
		this.ruleStatus = Constant.ENABLE;
	}

	public void validBase() {
		if(StringUtils.isBlank(this.isHoldDays)){
			this.holdDays = 0;
		}
		if(StringUtils.isBlank(this.isRemainDays)){
			this.remainDays = 1;
		}else if(this.remainDays == 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REMAINDAYS_MUST_GT_ZERO));
		}
		if(StringUtils.isBlank(this.isPeriodRemainDays)){
			this.periodRemainDays = 0;
		}
		// 全额变现
		if(FULL_REALIZE.equals(this.sellStyle)){
			this.sellAmountMin = new BigDecimal(0);
		}
		// 部分变现
		if(PART_REALIZE.equals(this.sellStyle) && this.sellAmountMin == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.SELL_AMOUNTMIN_IS_EMPTY));
		}
		if(PART_INVEST.equals(this.buyStyle)){
			if(this.buyAmountMin == null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BUY_AMOUNTMIN_IS_EMPTY));
			}else if(this.buyAmountMin.doubleValue() <1){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BUY_AMOUNTMIN_MUST_GT_ONE));
			}
			if(this.buyAmountMax == null){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.BUY_AMOUNTMAX_IS_EMPTY));
			}
			
		}
		
		// 变现利率区间校验
		double minApr = ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_APR);
		double maxApr = ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_APR);
		if (minApr > 0 && maxApr > 0 && (minApr > this.realizeRateMin.doubleValue() || maxApr < this.realizeRateMax.doubleValue())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_RANGE_ERROR, realizeRateMin, realizeRateMax));
		}
		// 变现利率最多保留两位小数
		if ((!BigDecimalUtils.modZero(this.realizeRateMin.multiply(BigDecimal.valueOf(100)), BigDecimal.ONE)) 
				|| (!BigDecimalUtils.modZero(this.realizeRateMax.multiply(BigDecimal.valueOf(100)), BigDecimal.ONE)) ) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_APR_REMAINTWODECIMAL));
		}
		if (this.realizeRateMin.doubleValue() > this.realizeRateMax.doubleValue()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_REALIZERATEMIN_MUSTLT_REALIZERATEMAX));
		}
		if(StringUtils.toLong(this.issueTimeHour) > StringUtils.toLong(this.raiseEndTimeHour)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RAISEENDTIME_MUST_GT_ISSUETIME));
		}
		if(StringUtils.toLong(this.issueTimeHour) == StringUtils.toLong(this.raiseEndTimeHour) && 
				StringUtils.toLong(this.issueTimeMin) >= StringUtils.toLong(this.raiseEndTimeMin)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RAISEENDTIME_MUST_GT_ISSUETIME));
		}
		this.issueTime = this.issueTimeHour + ":" + this.issueTimeMin + ":" + "00" ;
		this.raiseEndTime = this.raiseEndTimeHour + ":" + this.raiseEndTimeMin + ":" + "00" ;
	}
	@Override
	public String toString() {
		return "RealizeRule [" + "uuid=" + uuid + ", createTime=" + createTime + ", ruleStatus=" + ruleStatus + ", holdDays=" + holdDays + ", remainDays=" + remainDays + ", periodRemainDays=" + periodRemainDays + ", sellStyle=" + sellStyle + ", sellAmountMin=" + sellAmountMin + ", buyStyle=" + buyStyle + ", buyAmountMin=" + buyAmountMin + ", buyAmountMax=" + buyAmountMax + ", realizeRateMin=" + realizeRateMin + ", realizeRateMax=" + realizeRateMax + ", overdueFeeRate=" + overdueFeeRate + ", feeRate=" + feeRate + ", feeSingleMax=" + feeSingleMax + ", issueTime=" + issueTime + ", raiseEndTime=" + raiseEndTime + ", interestManageRate=" + interestManageRate +  "]";
	}
}

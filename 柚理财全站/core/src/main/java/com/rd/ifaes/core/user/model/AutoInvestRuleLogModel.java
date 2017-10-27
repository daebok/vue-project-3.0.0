package com.rd.ifaes.core.user.model;

import java.math.BigDecimal;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.user.domain.AutoInvestRuleLog;

import org.springframework.beans.BeanUtils;

/**
 * 自动投资规则记录
 * 
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
public class AutoInvestRuleLogModel extends AutoInvestRuleLog {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户状态
	 */
	private String userStatus;
	
	/**
	 * 是否启用VIP功能
	 */
	private String userVipStatus;
	
	/**
	 * 校验参数是否正确
	 * @param model
	 */
	public void checkParams() {
		// 单日最高投资金额不能为空
		if(getAmountDayMax() == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_NOT_EMPTY));
		}
		// 单日最高投资金额只能为正整数
		if(!ValidateUtils.isIntegerForDouble(getAmountDayMax().doubleValue()) || getAmountDayMax().doubleValue() < 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_AMOUNT_DAY_MAX_FORMAT_ERROR));
		}
		// 请至少填写一个投资期限区间
		if(StringUtils.isBlank(getMonthType()) && StringUtils.isBlank(getDayType())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_TIME_TYPE_NOT_EMPTY));
		}
		// 月范围最大值不能小于最小值
		if(Constant.FLAG_YES.equals(getMonthType()) && getMonthLimitMax() < getMonthLimitMin()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_MONTH_LIMIT_RANGE_ERROR));
		}
		// 月范围最大值不能大于99
		if(Constant.FLAG_YES.equals(getMonthType()) && getMonthLimitMax() >= Constant.INT_ONE_HUNDRED){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_MONTH_RANGE_ERROR));
		}
		// 天范围最大值不能小于最小值
		if(Constant.FLAG_YES.equals(getDayType()) && getDayLimitMax() < getDayLimitMin()){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_DAY_LIMIT_RANGE_ERROR));
		}
		// 天范围最大值不能大于999
		if(Constant.FLAG_YES.equals(getDayType()) && getDayLimitMax() >= Constant.INT_ONE_THOUSAND){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_DAY_RANGE_ERROR));
		}
		// 请填写投资收益最低值
		if(getAprMin().doubleValue() < Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_APR_MIN_NOT_EMPTY));
		}
		// 投资收益范围值错误
		if(getAprMin().compareTo(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)) >= Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_APR_MIN_RANGE_ERROR));
		}
		// 投资收益最多保留两位小数
		if (!BigDecimalUtils.modZero(getAprMin().multiply(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)), BigDecimal.ONE)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.AUTO_INVEST_RULE_LOG_APR_MIN_ERROR));
		}
	}
	
	public AutoInvestRuleLogModel() {
		
	}
	
	public AutoInvestRuleLogModel(final Project base) {
		setRepayStyles(base.getRepayStyle());
		if(LoanEnum.TIME_TYPE_MONTH.getValue().equals(base.getTimeType())){
			setMonthType(Constant.FLAG_YES);
			setMonthLimitMax(base.getTimeLimit());
		}else if(LoanEnum.TIME_TYPE_DAY.getValue().equals(base.getTimeType())){
			setDayType(Constant.FLAG_YES);
			setDayLimitMax(base.getTimeLimit());
		}
		setAprMin(base.getApr());
		setStatus(CommonEnum.YES.getValue());
		this.userStatus = Constant.USER_STATUS_NORMAL;
	}
	
	public AutoInvestRuleLog prototype() {
		final AutoInvestRuleLog autoInvestRuleLog = new AutoInvestRuleLog();
		BeanUtils.copyProperties(this, autoInvestRuleLog);
		return autoInvestRuleLog;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserVipStatus() {
		return userVipStatus;
	}

	public void setUserVipStatus(String userVipStatus) {
		this.userVipStatus = userVipStatus;
	}
}

/** 
 * Project Name:core 
 * File Name:BondRuleModel.java 
 * Package Name:com.rd.ifaes.core.bond.model 
 * Date:2016年7月21日下午9:24:48 
 * Copyright (c) 2016, qpz@erongdu.com All Rights Reserved. 
 */ 
package com.rd.ifaes.core.bond.model;


import java.math.BigDecimal;

import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.BondRuleEnum;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 债权规则扩展类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月28日
 */
public class BondRuleModel extends BondRule{
	private static final long serialVersionUID = 1L;
	/**
	 * 是否选择待收持有天数
	 */
	private String isHoldDays;
	/**
	 * 是否选择还款到期剩余天数
	 */
	private String isRemainDays;
	/**
	 * 是否选择本期还款剩余天数
	 */
	private String isPeriodRemainDays;
	
	/**
	 * 债权规则配置修改添加校验
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param bondRule
	 * @return
	 */
	public String validModelAdd(final BondRule bondRule){
		String message = StringUtils.EMPTY;
		//汇付限制
		if(UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())){
			if(BondRuleEnum.RULE_SELL_PART.eq(StringUtils.isNull(bondRule.getSellStyle()))){
				message += ResourceUtils.get(BondResource.BOND_RULE_UPDATE_TPP_SELL_LIMIT);
			}
			if(BondRuleEnum.RULE_BUY_PART.eq(StringUtils.isNull(bondRule.getBuyStyle()))){
				message += ResourceUtils.get(BondResource.BOND_RULE_UPDATE_TPP_BUY_LIMIT);
			}
		}
		//单笔受让金额限制校验
		if(BondRuleEnum.RULE_BUY_PART.eq(StringUtils.isNull(bondRule.getBuyStyle()))
				&& bondRule.getBuyAmountMin() <= Constant.DOUBLE_ZERO ){
			message +=ResourceUtils.get(BondResource.BOND_RULE_LOWEST_BUYACCOUNT_NOT_EXISTS);
		}
		//折溢价率限制校验
		if(bondRule.getDiscountRateMin() >= bondRule.getDiscountRateMax()){
			message +=ResourceUtils.get(BondResource.BOND_RULE_BONDAPR_MIN_OVER_MAX);
		}
		// 折溢价率的小数部分不能大于1位
		if(BigDecimalUtils.getDoubleValueLength(bondRule.getDiscountRateMax()) >1 
				|| BigDecimalUtils.getDoubleValueLength(bondRule.getDiscountRateMin()) > 1 ){
			message+= ResourceUtils.get(BondResource.BOND_DISRATE_VALIDNUM_LIMIT);
		}
		//折溢价率的最大值不能大于100
		if(BigDecimal.valueOf(bondRule.getDiscountRateMax()).compareTo(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED)) >= Constant.INT_ZERO){
			message += ResourceUtils.get(BondResource.BOND_DISRATE_MAX_LIMIT);
		}
		//折溢价率的最小值不能小于-100
		if(BigDecimal.valueOf(bondRule.getDiscountRateMin()).compareTo(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED_NEGA)) <= Constant.INT_ZERO){
			message += ResourceUtils.get(BondResource.BOND_DISRATE_MIN_LIMIT);
		}
		//转让手续费配置校验
		if(!BondRuleEnum.RULE_FEE_STYLE_FREE.eq(bondRule.getFeeStyle())){	
			if(bondRule.getFeeInitiateAmount() <= Constant.DOUBLE_ZERO ){
				message += ResourceUtils.get(BondResource.BOND_RULE_FEE_INITIATEAMOUNT_NOT_EXISTS);
			}else if(bondRule.getFeeSingleMax() <= Constant.DOUBLE_ZERO){
				message +=ResourceUtils.get(BondResource.BOND_RULE_FEEBUYFMAX_NOT_EXISTS);
			}else if(BondRuleEnum.RULE_FEE_STYLE_FIXED.eq(bondRule.getFeeStyle()) && 
					bondRule.getFeeFixed() <= Constant.DOUBLE_ZERO){
				message +=ResourceUtils.get(BondResource.BOND_RULE_FEE_FIXD_NOT_EXISTS);
			}else if(BondRuleEnum.RULE_FEE_STYLE_RATE.eq(bondRule.getFeeStyle()) && 
					bondRule.getFeeRate() <= Constant.DOUBLE_ZERO){
				message+=ResourceUtils.get(BondResource.BOND_RULE_FEE_RATE_NOT_EXISTS);
			}
			//转让手续费三方金额比例限制  --不能超过承接金额的10%
			if(UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName()) && BondRuleEnum.RULE_FEE_STYLE_RATE.eq(bondRule.getFeeStyle())
					&& bondRule.getFeeRate() > ConfigUtils.getDouble(ConfigConstant.UFX_BOND_FEE_LIMIT)){
				message +=String.format(ResourceUtils.get(BondResource.BOND_RULE_FEE_RATE_OVER_LIMIT),ConfigUtils.getDouble(ConfigConstant.UFX_BOND_FEE_LIMIT)+"%");
			}
		}
		return message;
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
	
	
	
	
	
	
	
	
	
}

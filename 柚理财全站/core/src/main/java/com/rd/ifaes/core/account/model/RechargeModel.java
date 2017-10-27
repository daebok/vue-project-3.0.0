package com.rd.ifaes.core.account.model;

import java.math.BigDecimal;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;

/**
 * 充值model
 * @version 3.0
 * @author xhf
 * @date 2016年8月30日
 */
public class RechargeModel extends Recharge {

	private static final long serialVersionUID = 1L;
	
	private String smsCode;
	
	private String smsSeq;
	
	private String mobile;

	/**
	 * 校验冻结功能
	 * @param userId
	 */
	public void checkFreeze(final String userId){
		final UserFreezeService userFreezeService = (UserFreezeService)SpringContextHolder.getBean("userFreezeService");
		final boolean isFreezed = userFreezeService.isFreezed(userId, UserFreezeModel.STATUS_USER_FREEZE_RECHARGE);
		if(isFreezed){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_IS_FREEZE), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 校验用户认证信息
	 * @param userId
	 */
	public void checkIdentify(final String userId){
		final UserIdentifyService userIdentifyService = (UserIdentifyService)SpringContextHolder.getBean("userIdentifyService");
		final UserIdentify userIdentify = userIdentifyService.findByUserId(userId);
		if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 校验充值金额
	 */
	public void checkRechargeMoney(){
		final BigDecimal amount = getAmount();
		if(amount==null||amount.compareTo(BigDecimal.ZERO)<=0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_MONEY_POSITIVE), BussinessException.TYPE_CLOSE); 
		}
		if (!StringUtils.isMoney(String.valueOf(amount))) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_MONEY_ERROR), BussinessException.TYPE_CLOSE); 
		}
		final BigDecimal minRehcargeAmount = ConfigUtils.getBigDecimal(Constant.RECHARGE_MIN_AMNT);
		if(minRehcargeAmount.compareTo(BigDecimal.ZERO)>0 && minRehcargeAmount.compareTo(amount)>0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_MONEY_MIN, minRehcargeAmount), BussinessException.TYPE_CLOSE);
		}
		final BigDecimal maxRehcargeAmount = ConfigUtils.getBigDecimal(Constant.RECHARGE_MAX_AMNT);
		if(maxRehcargeAmount.compareTo(BigDecimal.ZERO)>0 && maxRehcargeAmount.compareTo(amount)<0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.RECHARGE_MONEY_MAX, maxRehcargeAmount), BussinessException.TYPE_CLOSE);
		}
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getSmsSeq() {
		return smsSeq;
	}

	public void setSmsSeq(String smsSeq) {
		this.smsSeq = smsSeq;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}

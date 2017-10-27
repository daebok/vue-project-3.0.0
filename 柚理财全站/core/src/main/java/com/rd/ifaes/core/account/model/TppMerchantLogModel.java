package com.rd.ifaes.core.account.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.user.domain.User;

public class TppMerchantLogModel extends TppMerchantLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 未处理
	 */
	public final static String STATUS_UNTREATED = "0";

	/**
	 * 处理成功
	 */
	public final static String STATUS_SUCEESS = "1";
	
	/**
	 * 处理失败
	 */
	public final static String STATUS_FAIL = "2";
	
	/**
	 * 商户充值  + 
	 */
	public final static String TYPE_MERCHANT_RECHARGE = "1";
	
	/**
	 * 商户取现  -
	 */
	public final static String TYPE_MERCHANT_CASH = "2";
	
	/**
	 * 商户子账户互转 
	 */
	public final static String TYPE_MERCHANT_TRANSFER = "3";
	/**
	 * 商户给用户转账  - 
	 */
	public final static String TYPE_TRANSFER = "4";
	/**
	 * 利息管理费  + 
	 */
	public final static String TYPE_INTEREST_FEE = "5";
	/**
	 * 借款管理费  + 
	 */
	public final static String TYPE_BORROW_FEE = "6";
	/**
	 * 垫付加息利息  -
	 */
	public final static String TYPE_RATECOUPON = "7";
	/**
	 * 垫付红包   -
	 */
	public final static String TYPE_REDENVELOPE = "8";
	/**
	 * 债权手续费  +
	 */
	public final static String TYPE_BOND_FEE = "9";
	
	/**
	 * 逾期罚息给平台  + 
	 */
	public final static String TYPE_OVERDUE_MERCHANT ="10";
	
	/**
	 * 变现管理费 +
	 */
	public final static String TYPE_REALIZE_FEE = "11";
	
	/**
	 * 用户充值手续费垫付 -
	 */
	public final static String TYPE_CUSTOMER_RECHARGE = "12";
	
	/**
	 * 用户提现手续费垫付 -
	 */
	public final static String TYPE_CUSTOMER_CASH = "13";
	
	/**
	 * 用户提现服务费 +
	 */
	public final static String TYPE_CUSTOMER_CASH_SERVER = "14";
	
	public final static String OPERATE_TYPE = "platAccountType";
	
	/**
	 * 商户收入统计类型，排除商户充值
	 * 借款管理费、债券手续费、变现管理费、利息管理费、逾期罚息、用户提现服务费
	 */
	public final static String INCOME_ARR = StringUtils.join(new String[]{TYPE_BORROW_FEE, TYPE_BOND_FEE, TYPE_REALIZE_FEE, TYPE_INTEREST_FEE, TYPE_OVERDUE_MERCHANT, TYPE_CUSTOMER_CASH_SERVER}, ",");
	
	/**
	 * 商户支出统计类型，排除商户提现
	 * 充值手续费垫付、提现手续费垫付、加息利息（产品和加息券）、投资红包
	 */
	public final static String PAY_ARR = StringUtils.join(new String[]{TYPE_CUSTOMER_RECHARGE, TYPE_CUSTOMER_CASH, TYPE_RATECOUPON, TYPE_REDENVELOPE}, ",");
	
	
	/**
	 * 充值校验
	 */
	public void validMoney() {
		if (this.getMoney().doubleValue() < 0.01) {
			throw new UfxException("充值金额过小，请重新输入金额", BussinessException.TYPE_CLOSE);
		}
		if (this.getMoney().doubleValue() >= 100000000) {
			throw new UfxException("充值金额过大，目前仅支持千万级别充值", BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 提现校验
	 */
	public void validCashMoney() {
		if (this.getMoney().doubleValue() < 0.01) {
			throw new UfxException("提现金额过小，请重新输入金额", BussinessException.TYPE_CLOSE);
		}
		if (this.getMoney().doubleValue() >= 100000000) {
			throw new UfxException("提现金额过大，目前仅支持千万级别提现", BussinessException.TYPE_CLOSE);
		}
	}
	
	/**
	 * 转账参数校验
	 */
	public void validTransfer() {
		if (this.getMoney().doubleValue() < 0.01) {
			throw new UfxException("转账金额过小", BussinessException.TYPE_JSON);
		}
	}
	/**
	 * 校验收款账户
	 * 
	 * @param user
	 */
	public void validUser(User user) {
		if (user == null) {
			throw new UfxException("接收账户不存在", BussinessException.TYPE_JSON);
		}
		if (!"1".equals(user.getTppStatus())) {
			throw new UfxException("接收账户没有开户", BussinessException.TYPE_JSON);
		}
	}
	
	public static TppMerchantLogModel instance(TppMerchantLog tppMerchantLog) {
		TppMerchantLogModel tppMerchantLogModel = new TppMerchantLogModel();
		BeanUtils.copyProperties(tppMerchantLog, tppMerchantLogModel);
		return tppMerchantLogModel;
	}

	public TppMerchantLog prototype() {
		TppMerchantLog tppMerchantLog = new TppMerchantLog();
		BeanUtils.copyProperties(this, tppMerchantLog);
		return tppMerchantLog;
	}


}

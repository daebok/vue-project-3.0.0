package com.rd.ifaes.core.project.model;


import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.project.domain.BorrowBespeak;

/**
 * 预约借款model
 * @version 3.0
 * @author ZhangBiao
 * @date 2016年7月30日
 */
public class BorrowBespeakModel extends BorrowBespeak {
 
	private static final long serialVersionUID = 1L;
	
	private String validCode; // 图形验证码
	
	public BorrowBespeak prototype() {
		final BorrowBespeak borrowBespeak = new BorrowBespeak();
		BeanUtils.copyProperties(this, borrowBespeak);
		return borrowBespeak;
	}

	/**
	 * 参数校验
	 * @author ZhangBiao
	 * @date 2016年7月30日
	 */
	public void checkParams() {
		final double borrowMinAccount =  ConfigUtils.getDouble(ConfigConstant.BORROW_LOWEST_ACCOUNT);
		final double borrowMaxAccount =  ConfigUtils.getDouble(ConfigConstant.BORROW_MOST_ACCOUNT);
		final BigDecimal money = getMoney();
		if(StringUtils.isBlank(getContactName())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_CONTACT_NAME_EMPTYMSG));
		}
		if(getContactName().length() > Constant.INT_TEN){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_CONTACT_NAME_LENGTH_ERROR));
		}
		if(StringUtils.isBlank(getSex())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_SEX_NOTEMPTY));
		}
		if(StringUtils.isBlank(DictUtils.getItemName(DictTypeEnum.SEX.getValue(), String.valueOf(getSex())))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_SEX_ERROR));
		}
		if(!ValidateUtils.isPhone(getMobile())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_ERROR));
		}
		if(getLimitTime() == null || getLimitTime() <= Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_LIMIT_TIME_EMPTYMSG));
		}
		if(StringUtils.isBlank(DictUtils.getItemName(DictTypeEnum.BESPEAK_LIMIT_TIME.getValue(), String.valueOf(getLimitTime())))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_LIMIT_TIME_ERROR));
		}
		if(money == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_MONEY_NOT_EMPTY));
		}
		if(!ValidateUtils.isIntegerForDouble(money.doubleValue())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_MONEY_FORMAT_ERROR));
		}
		if(borrowMinAccount > Constant.INT_ZERO && (borrowMinAccount > money.doubleValue() 
				|| borrowMaxAccount < money.doubleValue())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_MONEY_RANGE_ERROR,
					borrowMinAccount,borrowMaxAccount));
		}
	}
	
	/**
	 * 借款预约验证码校验
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 */
	public void validRegRule() {
		String vCode = StringUtils.isNull(getValidCode());
		if (vCode.isEmpty()) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_NOTEMPTY), BussinessException.TYPE_JSON);
		}
		if (!ValidateUtils.checkValidCode(vCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	
}

package com.rd.ifaes.core.account.model;

import com.rd.ifaes.common.util.StringUtils;

/**
 * 银行卡model
 * 
 * @author xhf
 * @version 3.0
 * @since 2016年6月29日
 */
public class AccountBankModel {
	
	/**
	 * 银行编号，非空
	 */
	private String bankCode;

	/**
	 * 银行名称，非空
	 */
	private String bankName;
	
	/**
	 * 银行卡号，非空
	 */
	private String bankNo;
	
	/**
	 * 图像路径
	 */
	private String picPath;
	
	/**
	 *  获取是否允许解绑
	 *  Y：允许N：不允许  
	 */
	private boolean canDisable;
	
	/**
	 * 是否快捷卡
	 * Y：快捷卡
	 * N：普通卡
	 */
	private String  fastPayFlag;
	
	/**
	 * 银行卡类型
	 */
	private String bankCardType;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}
	/**
	 * 银行卡色值
	 */
	private String bankColor;

	public String getBankColor() {
		return bankColor;
	}

	public void setBankColor(String bankColor) {
		this.bankColor = bankColor;
	}
	/**
	 * 显示隐藏银行卡号（类似****8910）
	 */
	public String getBankNoSuffix() {
		String temp = bankNo;
		if (StringUtils.isNotBlank(temp)) {
			return "******" + temp.substring(temp.length() - 4, temp.length());
		}
		return "";
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public boolean isCanDisable() {
		return canDisable;
	}

	public void setCanDisable(boolean canDisable) {
		this.canDisable = canDisable;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getFastPayFlag() {
		return fastPayFlag;
	}

	public void setFastPayFlag(String fastPayFlag) {
		this.fastPayFlag = fastPayFlag;
	}

	public String getBankCardType() {
		return bankCardType;
	}

	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}

	/**
	 * 获取(只显示尾号)银行账号
	 * 
	 * @return 银行账号
	 */
	public String getHideBankNo() {
		if (StringUtils.isNotBlank(bankNo)) {
			return bankNo.substring(bankNo.length() - 4, bankNo.length());
		}
		return "";
	}
}

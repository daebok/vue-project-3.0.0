package com.rd.ifaes.mobile.model.score;

import com.rd.ifaes.common.util.StringUtils;

import java.util.Date;

public class ScoreLotteryItemModel {
	/**
	 * 中奖商品
	 */
	private String lotteryValue;
	
	/**
	 * 中奖时间
	 */
	private Date lotteryTime;
	
	/**
	 * 中奖人手机号
	 */
	private String mobile;

	public String getLotteryValue() {
		return lotteryValue;
	}

	public void setLotteryValue(String lotteryValue) {
		this.lotteryValue = lotteryValue;
	}

	public Date getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(Date lotteryTime) {
		this.lotteryTime = lotteryTime;
	}

	public String getMobile() {
		if (StringUtils.isNotBlank(mobile) && mobile.length() == 11) {
            return mobile.substring(0, 3) + "****"
                    + mobile.substring(7, 11);
        }
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}

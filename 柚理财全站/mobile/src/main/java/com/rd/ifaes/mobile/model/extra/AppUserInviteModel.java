package com.rd.ifaes.mobile.model.extra;

import java.math.BigDecimal;

/**
 * 邀请功能模型
 * @author yoseflin
 */
public class AppUserInviteModel {

	/**
	 * 邀请注册地址
	 */
	private StringBuilder inviteUrl;
	/**
	 * 红包奖励数目 
	 */
	private int redCount;
	public StringBuilder getInviteUrl() {
		return inviteUrl;
	}

	public void setInviteUrl(StringBuilder inviteUrl) {
		this.inviteUrl = inviteUrl;
	}

	/**
	 * 红包金额
	 */
	private BigDecimal redAmount;
	/**
	 * 加息券数目
	 */
	private int rateCount;
	
	
	/**
	 * 奖励规则说明
	 */
	private String drawRuleStr;
	
	public String getDrawRuleStr() {
		return drawRuleStr;
	}

	public void setDrawRuleStr(String drawRuleStr) {
		this.drawRuleStr = drawRuleStr;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	/**
	 * 分享活动标题
	 */
	private String shareTitle;
	
	/**
	 * 分享活动内容
	 */
	private String shareContent;
	
	public int getRedCount() {
		return redCount;
	}

	public void setRedCount(int redCount) {
		this.redCount = redCount;
	}

	public BigDecimal getRedAmount() {
		return redAmount;
	}

	public void setRedAmount(BigDecimal redAmount) {
		this.redAmount = redAmount;
	}

	public int getRateCount() {
		return rateCount;
	}

	public void setRateCount(int rateCount) {
		this.rateCount = rateCount;
	}
}

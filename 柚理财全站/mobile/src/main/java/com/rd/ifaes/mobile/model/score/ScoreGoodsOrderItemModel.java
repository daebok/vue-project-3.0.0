package com.rd.ifaes.mobile.model.score;

import java.util.Date;

public class ScoreGoodsOrderItemModel {

	/** 兑换日期 */
	private Date convertTime;
	
	/**
	 * 兑换商品
	 */
	private String convertGoods;
	
	/**
	 * 消耗积分
	 */
	private Integer score;

	public Date getConvertTime() {
		return convertTime;
	}

	public void setConvertTime(Date convertTime) {
		this.convertTime = convertTime;
	}

	public String getConvertGoods() {
		return convertGoods;
	}

	public void setConvertGoods(String convertGoods) {
		this.convertGoods = convertGoods;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public String getScoreStr() {
		return "-" + score;
	}
}

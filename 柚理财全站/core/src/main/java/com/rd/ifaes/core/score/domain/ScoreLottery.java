package com.rd.ifaes.core.score.domain;

import com.rd.ifaes.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 中奖概率表
 * @version 3.0
 * @author jxx
 * @date 2017年6月13日
 */
public class ScoreLottery extends BaseEntity<ScoreLottery> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 中奖名称
	 */
	private String lotteryName;
	
	/**
	 * 中奖类型 1，红包 2，实物 3积分
	 */
	private String lotteryType;
	
	/**
	 * 实物有的话，实物的id，暂时不用
	 */
	private String goodsId;
	
	/**
	 * 中奖区域
	 */
	private String lotteryArea;
	
	/**
	 * 中奖值
	 */
	private String lotteryValue;
	
	/**
	 * 中奖概率
	 */
	private BigDecimal rate;
	
	/**
	 * 添加时间
	 */
	private Date createTime;
	
	/**
	 * 状态，1：启用，0：关闭
	 */
	private String status;
	
	/**
	 * 备注
	 */
	private String remark;

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getLotteryValue() {
		return lotteryValue;
	}

	public void setLotteryValue(String lotteryValue) {
		this.lotteryValue = lotteryValue;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLotteryArea() {
		return lotteryArea;
	}

	public void setLotteryArea(String lotteryArea) {
		this.lotteryArea = lotteryArea;
	}

	@Override
	public String toString() {
		return "ScoreLottery [lotteryName=" + lotteryName + ", lotteryType=" + lotteryType + ", goodsId=" + goodsId
				+ ", lotteryArea=" + lotteryArea + ", lotteryValue=" + lotteryValue + ", rate=" + rate + ", createTime="
				+ createTime + ", status=" + status + ", remark=" + remark + "]";
	}

}

package com.rd.ifaes.core.score.domain;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.sys.domain.DictData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 用户中奖记录表
 * @version 3.0
 * @author jxx
 * @date 2017年6月13日
 */
public class UserScoreLotteryRecord extends BaseEntity<UserScoreLotteryRecord> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 中奖人id
	 */
	private String userId;
	
	/**
	 * 中奖类型 1，红包 2，加息券 3积分
	 */
	@DictType(type = "lotteryType")
	private String lotteryType;
	
	/**
	 * 中奖奖品
	 */
	private String lotteryValue;
	
	/**
	 * 消耗的积分
	 */
	private int score;
	
	/**
	 * 中奖奖品实物的id
	 */
	private String goodsId;
	
	/**
	 * 中奖时间
	 */
	private Date createTime;
	
	/**
	 * 中奖人ip
	 */
	private String createIp;
	
	/**
	 * 状态，0：已中奖，未处理，1：已发奖'
	 */
	private String status;
	
	/**
	 * 备注
	 */
	private String remark;
	
	private String lotteryArea;//中奖区域
	
	//其他自定义
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 号码
	 */
	private String mobile;
	/**
	 * LotteryType 中奖类型集合
	 */
	private List<String> lotteryTypeList;
	/**
	 * 是否查看中奖记录,如果是1表示查看中奖记录，否则查看全部记录
	 */
	private String queryType;
	private int showCount;//显示条数

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getLotteryValue() {
		return lotteryValue;
	}

	public void setLotteryValue(String lotteryValue) {
		this.lotteryValue = lotteryValue;
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


	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	
	
	public List<String> getLotteryTypeList() {
		return lotteryTypeList;
	}

	public void setLotteryTypeList(List<String> lotteryTypeList) {
		this.lotteryTypeList = lotteryTypeList;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	

	public int getShowCount() {
		return showCount;
	}

	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}


	public String getLotteryArea(){
		return lotteryArea;
	}

	public void setLotteryArea(String lotteryArea) {
		this.lotteryArea = lotteryArea;
	}

	@Override
	public String toString() {
		return "UserScoreLotteryRecord [userId=" + userId + ", lotteryType=" + lotteryType + ", lotteryValue=" + lotteryValue
				+ ", score=" + score + ", goodsId=" + goodsId + ", createTime=" + createTime + ", createIp=" + createIp
				+ ", status=" + status + ", remark=" + remark + "]";
	}
	
	
	
}

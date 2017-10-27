/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.project.model;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 前台投资记录VO
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月10日
 */
public class ProjectInvestRecord extends BaseEntity<ProjectInvestRecord> {

	private static final long serialVersionUID = 7474835405549410420L;

	private String projectId; /* 项目ID */
	private String status; /* 投资状态 0 待支付，1成功，2失败 */
	private BigDecimal money; /* 投标金额 */
	private BigDecimal amount; /* 有效投资金额 */
	private BigDecimal realAmount; /* 用户实付资金 */
	private String investType; /* 投资方式 0手动投资;1自动投资 */
	private String investChannel; /* 投资渠道 1 PC 2 APP 3 微信 */
	private String userFirstInvest; /* 用户首次投资，0：非首投，1：首投 */
	private Date createTime; /* 添加时间 */
	private String investStyle;/* 投资类型方式: 0通投资,1转让投资,2受让投资 */
	/**
	 * 使用红包总金额（有效投资金额-用户实付资金）
	 */
	private  BigDecimal redenvelopeAmount;
	/**
	 * 加息劵数值
	 */
	private  BigDecimal rateCouponVal ;
	
	// 其他自定义属性
	private String userName; // 用户账号
	private String[] statusSet;//投资状态集合
	
	public ProjectInvestRecord() {
		super();
	}
 
	/**
	 * 获取属性redenvelopeAmount的值
	 * @return redenvelopeAmount属性值
	 */
	public BigDecimal getRedenvelopeAmount() {
		return redenvelopeAmount;
	}

	/**
	 * 设置属性redenvelopeAmount的值
	 * @param  redenvelopeAmount属性值
	 */
	public void setRedenvelopeAmount(BigDecimal redenvelopeAmount) {
		this.redenvelopeAmount = redenvelopeAmount;
	}

	/**
	 * 获取属性rateCouponVal的值
	 * @return rateCouponVal属性值
	 */
	public BigDecimal getRateCouponVal() {
		return rateCouponVal;
	}

	/**
	 * 设置属性rateCouponVal的值
	 * @param  rateCouponVal属性值
	 */
	public void setRateCouponVal(BigDecimal rateCouponVal) {
		this.rateCouponVal = rateCouponVal;
	}

	/**
	 * 获取属性projectId的值
	 * @return projectId属性值
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * 设置属性projectId的值
	 * @param  projectId属性值
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取属性money的值
	 * @return money属性值
	 */
	public BigDecimal getMoney() {
		return money;
	}
	/**
	 * 设置属性money的值
	 * @param  money属性值
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取属性amount的值
	 * @return amount属性值
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * 设置属性amount的值
	 * @param  amount属性值
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取属性realAmount的值
	 * @return realAmount属性值
	 */
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	/**
	 * 设置属性realAmount的值
	 * @param  realAmount属性值
	 */
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	/**
	 * 获取属性investType的值
	 * @return investType属性值
	 */
	public String getInvestType() {
		return investType;
	}
	/**
	 * 设置属性investType的值
	 * @param  investType属性值
	 */
	public void setInvestType(String investType) {
		this.investType = investType;
	}
	/**
	 * 获取属性investChannel的值
	 * @return investChannel属性值
	 */
	public String getInvestChannel() {
		return investChannel;
	}
	/**
	 * 设置属性investChannel的值
	 * @param  investChannel属性值
	 */
	public void setInvestChannel(String investChannel) {
		this.investChannel = investChannel;
	}
	/**
	 * 获取属性userFirstInvest的值
	 * @return userFirstInvest属性值
	 */
	public String getUserFirstInvest() {
		return userFirstInvest;
	}
	/**
	 * 设置属性userFirstInvest的值
	 * @param  userFirstInvest属性值
	 */
	public void setUserFirstInvest(String userFirstInvest) {
		this.userFirstInvest = userFirstInvest;
	}
	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取属性investStyle的值
	 * @return investStyle属性值
	 */
	public String getInvestStyle() {
		return investStyle;
	}
	/**
	 * 设置属性investStyle的值
	 * @param  investStyle属性值
	 */
	public void setInvestStyle(String investStyle) {
		this.investStyle = investStyle;
	}
	/**
	 * 获取属性userName的值
	 * @return userName属性值
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置属性userName的值
	 * @param  userName属性值
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取属性statusSet的值
	 * @return statusSet属性值
	 */
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * 设置属性statusSet的值
	 * @param  statusSet属性值
	 */
	public void setStatusSet(String[] statusSet) {
		this.statusSet = statusSet;
	}
	
}

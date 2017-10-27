package com.rd.ifaes.core.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.Global;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.user.domain.User;

/**
 * entity:Recharge
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-6-30
 */
public class Recharge extends BaseEntity<Recharge> {
	
	private static final long serialVersionUID = 1L;
	/** 订单号 **/ 
	private String	orderNo;
	/** UFX处理流水号 **/ 
	private String	tradeNo;	
	 /** 用户ID **/ 
	private String	userId;	
	/** 状态 0 充值申请中 1充值成功 2充值失败 **/ 
	@DictType(type="rechargeStatus")
	private String	status;		
	/** 充值银行卡 **/ 
	private String	cardId;		
	/** 金额 **/ 
	private BigDecimal	amount;		
	/** 支付方式(0：网银充值  2：快捷支付 3：线下充值 ) **/ 
	@DictType(type="rechargePayWay")
	private String	payWay;		
	/** 手续费 **/ 
	private BigDecimal	fee;	
	/** 手续费类型(01 用户，02商户) **/ 
	@DictType(type="rechargeFeeType")
	private String	feeType;		 
	/** 用户承担手续费 **/ 
	private BigDecimal	rechargeFee;	
	/** 添加时间 **/ 
	private Date	createTime;		
	/** 添加IP **/ 
	private String	addIp;		
	/** 备注 **/ 
	private String	remark;		
	/** 渠道(1-PC,2-APP,3-微信,默认PC) **/ 
	private String	channel;

	
	//其他自定义属性
	/**用户名**/ 
	private String userName; 
	/**真实姓名**/ 
	private String realName; 
	/**用户类型**/ 
	@DictType(type="userNature")
	private String userNature; 
	/**充值用户**/ 
	private User user;
	
	/**
	 * 构造函数
	 */
	public Recharge() {
		super();
	}
	
	/**
	 * 构造函数
	 * @param userId
	 * @param orderNo
	 * @param tradeNo
	 */
	public Recharge(final String userId, final String orderNo, final String tradeNo) {
		super();
		this.orderNo = orderNo;
		this.userId = userId;
		this.tradeNo = tradeNo;
	}
	
	/**
	 * 构造函数
	 * @param tradeNo
	 * @param userId
	 * @param amount
	 * @param payWay
	 */
	public Recharge(final String tradeNo, final String userId, final BigDecimal amount, final String payWay) {
		super();
		this.tradeNo = tradeNo;
		this.userId = userId;
		this.amount = amount;
		this.payWay = payWay;
		this.createTime = DateUtils.getNow();
		this.addIp = Global.getIP();
	}
 
	/**
	 * 获得交易订单号
	 * @return
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置交易订单号
	 * @param tradeNo
	 */
	public void setTradeNo(final String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获得用户ID
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获得状态
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * @param status
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * 获得银行卡ID
	 * @return
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 设置银行卡ID
	 * @param cardId
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 获得金额
	 * @return
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 设置金额
	 * @param amount
	 */
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获得支付方式
	 * @return
	 */
	public String getPayWay() {
		return payWay;
	}

	/**
	 * 设置支付方式
	 * @param payWay
	 */
	public void setPayWay(final String payWay) {
		this.payWay = payWay;
	}

	/**
	 * 获得手续费
	 * @return
	 */
	public BigDecimal getFee() {
		return fee;
	}

	/**
	 * 设置手续费
	 * @param fee
	 */
	public void setFee(final BigDecimal fee) {
		this.fee = fee;
	}

	/**
	 * 获得手续费类型
	 * @return
	 */
	public String getFeeType() {
		return feeType;
	}

	/**
	 * 设置手续费类型
	 * @param feeType
	 */
	public void setFeeType(final String feeType) {
		this.feeType = feeType;
	}

	/**
	 * 获得用户承担手续费 
	 * @return
	 */
	public BigDecimal getRechargeFee() {
		return rechargeFee;
	}

	/**
	 * 设置用户承担手续费 
	 * @param rechargeFee
	 */
	public void setRechargeFee(final BigDecimal rechargeFee) {
		this.rechargeFee = rechargeFee;
	}

	/**
	 * 获得创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 * @param createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获得添加IP
	 * @return
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置IP
	 * @param addIp
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获得备注信息
	 * @return
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注信息
	 * @param remark
	 */
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	
	/**
	 * 获得订单号
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 设置订单号
	 * @param orderNo
	 */
	public void setOrderNo(final String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 覆盖toString()方法
	 */
	@Override
	public String toString() {
		return "Recharge [" + "uuid=" + uuid + ", orderNo=" + orderNo + ", tradeNo=" + tradeNo + ", userId=" + userId + ", status=" + status + ", cardId=" + cardId + ", amount=" + amount + ", payWay=" + payWay + ", fee=" + fee + ", feeType=" + feeType + ", rechargeFee=" + rechargeFee + ", createTime=" + createTime + ", addIp=" + addIp + ", remark=" + remark +  "]";
	}

	/**
	 * 获得状态显示说明值
	 * @return
	 */
	public String getStatusStr() {
		return DictUtils.getItemName(DictTypeEnum.RECHARGE_STATUS.getValue(), getStatus());
	}

	/**
	 * 获得用户名
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 设置用户名
	 * @param userName
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * 获得真实姓名
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置真实姓名
	 * @param realName
	 */
	public void setRealName(final String realName) {
		this.realName = realName;
	}

	/**
	 * 获得用户类型
	 * @return
	 */
	public String getUserNature() {
		return userNature;
	}

	/**
	 * 设置用户类型
	 * @param userNature
	 */
	public void setUserNature(final String userNature) {
		this.userNature = userNature;
	}

	/**
	 * 获取充值用户
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置充值用户
	 * @param  user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the 渠道(1-PC2-APP3-微信默认PC)
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param 渠道(1-PC2-APP3-微信默认PC) the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}

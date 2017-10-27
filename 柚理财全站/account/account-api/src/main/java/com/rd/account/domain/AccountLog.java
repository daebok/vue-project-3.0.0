package com.rd.account.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.annotation.DictType;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.BigDecimalUtils;

/**
 * entity:AccountLog
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
public class AccountLog extends BaseEntity<AccountLog> {
	
	private static final long serialVersionUID = 1L;
//	@NotNull
	private String	userId;		 	/* 用户ID */ 
	private String	accountCode;		 /* 账户编号 */ 
	private String	toUser;		 	/* 交易对方的用户ID */
	@DictType(type="accountType")
	private String	accountType;		 /* 账户类别 */ 
	private BigDecimal	money;		 	/* 交易金额 */ 
	private BigDecimal	useMoney;		 	/* 可用余额 */ 
	private BigDecimal	noUseMoney;		 	/* 冻结余额 */ 
	private String	paymentsType;		 /* 收支类型:0不变  1收入  2支出 */ 
	private String  tradeNo;		/*交易流水号*/
	private Date	createTime;		 /* 添加时间 */ 
	private String	remark;		 	/* 备注 */ 
	private String	orderNo;		 /* 订单号 */ 
	private BigDecimal earn;		 /* 收益金额 */ 
	private long  createTimestamp; /* 时间戳(排序用)*/

	//其他自定义属性
	/** 收支类型：不变 **/
	public static final String PAYMENTS_TYPE_REMAIN = "0";
	/** 收支类型：收入 **/
	public static final String PAYMENTS_TYPE_INCOME = "1";
	/** 收支类型：支出 **/
	public static final String PAYMENTS_TYPE_EXPEND = "2";
	
	public static final String REMARK_RECHARGE = "充值";
	public static final String REMARK_CASH = "取现";
	public static final String REMARK_TRANSFER_IN = "转入";
	public static final String REMARK_TRANSFER_OUT = "转出";
	public static final String REMARK_FREEZE = "冻结";
	public static final String REMARK_UNFREEZE = "解冻";

	
	private String userName;
	private String realName;
	private String toUserName;
	private String toRealName;
	private String tppUserCustId;//托管账户ID
	private String toTppUserCustId;//托管账户ID
	private String tppStatus;	// 第三方账号是否激活 1 激活， 0未激活 
	
	private String[] accountTypeSet;
	private String dateTypeTime;

	private String accountTypeStr;
	private String htmlAUrl;//日志中的 <a> html标签中的url地址 
	// Constructor
	public AccountLog() {
		super();
	}
	
	public AccountLog(final String accountCode, final String accountType, final String userId, final BigDecimal money, final String toUser, final String paymentsType, final String remark) {
		this.accountCode = accountCode;
		this.accountType = accountType;
		this.userId = userId;
		this.money = money;
		this.toUser = toUser;
		this.paymentsType = paymentsType;
		this.remark = remark;
	}
	
	public AccountLog(final String accountCode, final String accountType, final String userId, final BigDecimal money, final String toUser, final String paymentsType, final String tradeNo, final String orderNo, final String remark) {
		this.accountCode = accountCode;
		this.accountType = accountType;
		this.userId = userId;
		this.money = money;
		this.toUser = toUser;
		this.paymentsType = paymentsType;
		this.tradeNo = tradeNo;
		this.orderNo = orderNo;
		this.remark = remark;
	}
	
	public AccountLog(final String accountCode, final String userId, final BigDecimal money, final String toUser) {
		this.accountCode = accountCode;
		this.userId = userId;
		this.money = money;
		this.toUser = toUser;
	}
	
	public AccountLog(final String accountCode, final String accountType, final String userId,final BigDecimal money, final String remark) {
		this.accountCode = accountCode;
		this.userId = userId;
		this.accountType = accountType;
		this.money = money;
		this.remark = remark;
	}

	/**
	 * full Constructor
	 */
	public AccountLog(final String uuid, final String userId, final String accountCode,final String toUser, final String accountType, final BigDecimal money, final Date createTime,final String paymentsType,final String tradeNo, final String remark) {
		setUuid(uuid);
		this.userId = userId;
		this.accountCode = accountCode;
		this.toUser = toUser;
		this.accountType = accountType;
		this.money = money;
		this.createTime = createTime;
		this.paymentsType = paymentsType;
		this.tradeNo = tradeNo;
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	
	public BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public BigDecimal getNoUseMoney() {
		return noUseMoney;
	}

	public void setNoUseMoney(BigDecimal noUseMoney) {
		this.noUseMoney = noUseMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPaymentsType() {
		return paymentsType;
	}

	public void setPaymentsType(String paymentsType) {
		this.paymentsType = paymentsType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	@Override
	public String toString() {
		return "AccountLog [" + "uuid=" + uuid + ", userId=" + userId + ", accountCode=" + accountCode + ", toUser=" + toUser + ", accountType=" + accountType + ", money=" + money +", useMoney=" + useMoney +", noUseMoney=" + noUseMoney + ", createTime=" + createTime + ", paymentsType=" + paymentsType+ ", tradeNo=" + tradeNo  + ", remark=" + remark +  "]";
	}
	
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = new Date();
		this.createTimestamp = System.currentTimeMillis();
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

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getToRealName() {
		return toRealName;
	}

	public void setToRealName(String toRealName) {
		this.toRealName = toRealName;
	}

	public String getAccountTypeStr() {
		return accountTypeStr;
	}

	public void setAccountTypeStr(String accountTypeStr) {
		this.accountTypeStr = accountTypeStr;
	}

	public String getTppUserCustId() {
		return tppUserCustId;
	}

	public void setTppUserCustId(String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}

	public String getToTppUserCustId() {
		return toTppUserCustId;
	}

	public void setToTppUserCustId(String toTppUserCustId) {
		this.toTppUserCustId = toTppUserCustId;
	}

	public String getDateTypeTime() {
		return dateTypeTime;
	}

	public void setDateTypeTime(String dateTypeTime) {
		this.dateTypeTime = dateTypeTime;
	}

	public String getTppStatus() {
		return tppStatus;
	}

	public void setTppStatus(String tppStatus) {
		this.tppStatus = tppStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getEarn() {
		return earn;
	}

	public void setEarn(BigDecimal earn) {
		this.earn = earn;
	}

	/**
	 * 获取属性createTimestamp的值
	 * @return createTimestamp属性值
	 */
	public long getCreateTimestamp() {
		return createTimestamp;
	}

	/**
	 * 设置属性createTimestamp的值
	 * @param  createTimestamp属性值
	 */
	public void setCreateTimestamp(long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	/**
	 * 获取属性accountTypeSet的值
	 * @return accountTypeSet属性值
	 */
	public String[] getAccountTypeSet() {
		return accountTypeSet;
	}

	/**
	 * 设置属性accountTypeSet的值
	 * @param  accountTypeSet属性值
	 */
	public void setAccountTypeSet(String[] accountTypeSet) {
		this.accountTypeSet = accountTypeSet;
	}

	/**
	 * 获取属性htmlAUrl的值
	 * @return htmlAUrl属性值
	 */
	public String getHtmlAUrl() {
		return htmlAUrl;
	}

	/**
	 * 设置属性htmlAUrl的值
	 * @param  htmlAUrl属性值
	 */
	public void setHtmlAUrl(String htmlAUrl) {
		this.htmlAUrl = htmlAUrl;
	}
	
	/**
	 * 带符号的金额
	 */
	public String getMoneyStr() {
		if(AccountLog.PAYMENTS_TYPE_INCOME.equals(paymentsType)){
			return "+"+BigDecimalUtils.round(money);
		}else if(AccountLog.PAYMENTS_TYPE_EXPEND.equals(paymentsType)){
			return "-"+BigDecimalUtils.round(money);
		}else{
			return BigDecimalUtils.round(money).toString();
		}
	}

}

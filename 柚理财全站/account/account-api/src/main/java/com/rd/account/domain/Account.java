package com.rd.account.domain;

import java.math.BigDecimal;
import java.util.Date;

//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:Account
 * 
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Account extends BaseEntity<Account> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAILT_STATUS = "0";
	
//	@NotNull
	private String	userId;		 		/* 用户ID */	
	@JsonProperty("accountCode")
    @XmlElement(name = "accountCode")
//    @NotNull
//    @Size(min = 1, max = 32)
	private String	accountCode;		 /* 账户编号 */ 
//	@NotNull
	private String	accountType;		 /* 账户类别 */ 
	private String	parentCode;		 /* 上级账户编号 */ 
//	@Min(0)
	private BigDecimal	total;		 		/* 账户总额 */ 
	private BigDecimal	useMoney;		 /* 可用余额 */ 
	private BigDecimal	noUseMoney;		 /* 冻结金额 */ 
	private Date	createTime;		 /* 添加时间 */ 
	private String	status;		 	 /* 账户状态 0 未启用；1 启用 */ 
	private BigDecimal cashFeeMark; /* >=0，提现收取手续费标记（如果大于0说明需要手续费） */ 
	//其他自定义属性
	private String userName;
	private String realName;
	private String userNature;
	private String tppStatus;
	private String tppUserCustId;//第三方id

	// Constructor
	public Account() {
	}

	
	/**
	 * 
	 * @param userId		用户ID
	 * @param accountCode	账户编号
	 * @param accountType	账户类别
	 */
	public Account(String userId, String accountCode, String accountType) {
		this.userId = userId;
		this.accountCode = accountCode;
		this.accountType = accountType;
	}

	public Account(String uuid, String userId, String accountCode, String accountType, String parentCode, BigDecimal total,
			BigDecimal useMoney, BigDecimal noUseMoney, Date createTime, String status) {
		super(uuid);
		this.userId = userId;
		this.accountCode = accountCode;
		this.accountType = accountType;
		this.parentCode = parentCode;
		this.total = total;
		this.useMoney = useMoney;
		this.noUseMoney = noUseMoney;
		this.createTime = createTime;
		this.status = status;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Override
	public String toString() {
		return "Account [" + "uuid=" + uuid + ", userId=" + userId + ", accountCode=" + accountCode + ", accountType=" + accountType + ", parentCode=" + parentCode + ", total=" + total + ", useMoney=" + useMoney + ", noUseMoney=" + noUseMoney + ", createTime=" + createTime + ", status=" + status +  "]";
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


	public String getTppUserCustId() {
		return tppUserCustId;
	}

	public void setTppUserCustId(String tppUserCustId) {
		this.tppUserCustId = tppUserCustId;
	}


	public String getTppStatus() {
		return tppStatus;
	}


	public void setTppStatus(String tppStatus) {
		this.tppStatus = tppStatus;
	}

	public String getUserNature() {
		return userNature;
	}

	public void setUserNature(String userNature) {
		this.userNature = userNature;
	}


	public BigDecimal getCashFeeMark() {
		return cashFeeMark;
	}


	public void setCashFeeMark(BigDecimal cashFeeMark) {
		this.cashFeeMark = cashFeeMark;
	}
	
	
}

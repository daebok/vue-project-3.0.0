package com.rd.account.model;

import java.io.Serializable;

/**
 * 账户注册 model
 * @author lh
 *
 */
public class AccountRegisterModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String accountCode;
	private String accountType;
	private String parentCode;
	
	public AccountRegisterModel() {
	}
	
	public AccountRegisterModel(String userId, String accountCode, String accountType, String parentCode) {
		super();
		this.userId = userId;
		this.accountCode = accountCode;
		this.accountType = accountType;
		this.parentCode = parentCode;
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
	
	

}

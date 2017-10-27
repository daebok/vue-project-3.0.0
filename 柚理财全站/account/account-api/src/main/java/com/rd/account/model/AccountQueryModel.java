package com.rd.account.model;

import java.io.Serializable;

public class AccountQueryModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String accountCode;
	private String accountType;
	
	public AccountQueryModel() {
		super();
	}
	
	
	public AccountQueryModel(String userId) {
		super();
		this.userId = userId;
	}


	public AccountQueryModel(String userId, String accountCode) {
		super();
		this.userId = userId;
		this.accountCode = accountCode;
	}


	public AccountQueryModel(String userId, String accountCode, String accountType) {
		super();
		this.userId = userId;
		this.accountCode = accountCode;
		this.accountType = accountType;
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
	
	
	
}

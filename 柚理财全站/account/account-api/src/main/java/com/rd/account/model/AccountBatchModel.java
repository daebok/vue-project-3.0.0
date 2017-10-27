package com.rd.account.model;

import java.io.Serializable;
import java.util.List;

import com.rd.account.domain.AccountLog;

public class AccountBatchModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6667013000181793592L;
	private List<AccountModel> accountList;	
	private List<AccountLog> logList;
	
	public AccountBatchModel() {
		super();
	}
	
	
	public AccountBatchModel(List<AccountModel> accountList, List<AccountLog> logList) {
		super();
		this.accountList = accountList;
		this.logList = logList;
	}

	public List<AccountModel> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<AccountModel> accountList) {
		this.accountList = accountList;
	}
	public List<AccountLog> getLogList() {
		return logList;
	}
	public void setLogList(List<AccountLog> logList) {
		this.logList = logList;
	}
	
	

}

package com.rd.account.domain;

import org.junit.Test;

import com.rd.account.util.DomainExecUtils;

public class AccountLogTest {
	@Test
	public void testAll() {
		DomainExecUtils.exec(new AccountLog());
		
		AccountLog log = new AccountLog();
		log.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
		log.getMoneyStr();
		log.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
		log.getMoneyStr();		
	}
}

package com.rd.account.model;

import org.junit.Test;

import com.rd.account.util.DomainExecUtils;

public class ModelTest {
	@Test
	public void testAll() {
		DomainExecUtils.exec(new AccountBatchModel());
		DomainExecUtils.exec(new AccountModel());
		DomainExecUtils.exec(new AccountQueryModel());
		DomainExecUtils.exec(new AccountRegisterModel());
	}
}

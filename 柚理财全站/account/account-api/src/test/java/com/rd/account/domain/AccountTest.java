package com.rd.account.domain;

import org.junit.Test;

import com.rd.account.util.DomainExecUtils;

public class AccountTest {
	@Test
	public void testAll() {
		DomainExecUtils.exec(new Account());
	}
}

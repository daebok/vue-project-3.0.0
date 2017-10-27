package com.rd.account.web;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.rd.account.domain.Account;
import com.rd.account.service.AccountService;

public class AccountControllerTest {
	@Test
	public void testList() throws Exception {
		AccountService accountService = EasyMock.createMock(AccountService.class);
		EasyMock.expect(accountService.findPage(EasyMock.anyObject(Account.class))).andReturn(null);
		EasyMock.replay(accountService);
		
		AccountController controller = new AccountController();
		ReflectionTestUtils.setField(controller, "accountService", accountService);
		controller.list(new Account());
		
		EasyMock.verify(accountService);
	}
}

package com.rd.account.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.account.domain.Account;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.orm.Page;

@Controller
public class AccountController {
	
	@Resource
	private AccountService accountService;
	
	@RequestMapping(value = "/sys/account/list")
	@ResponseBody
	public Page<Account> list(Account account){
		return accountService.findPage(account);
	}	

}

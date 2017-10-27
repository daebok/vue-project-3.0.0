package com.rd.ifaes.manage.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.user.domain.UserLoginLog;
import com.rd.ifaes.core.user.service.UserLoginLogService;

/**
 * 前台用户登录
 * @author Administrator
 *
 */
@Controller
public class UserLoginLogController {
	
	@Resource
	private UserLoginLogService userLoginLogService ;
	
	/**
	 * 前台用户登录列表页面
	 * @return
	 */
	@RequestMapping(value = "/sys/user/user_login")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG)
	public String manage() {
		return "/sys/user/user_login";
	}
	
	/**
	 * 前台用户登录列表
	 */
	@RequestMapping(value = "/sys/user/userLoginLogList")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG)
	public Object list(UserLoginLog model) {
		return userLoginLogService.findPage(model) ;
	}
}

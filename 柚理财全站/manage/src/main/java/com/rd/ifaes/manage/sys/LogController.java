package com.rd.ifaes.manage.sys;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.sys.domain.Log;
import com.rd.ifaes.core.sys.service.LogService;
/**
 * 后台操作日志
 * @version 3.0
 * @author fxl
 * @date 2016年10月12日
 */
@Controller
public class LogController extends SystemController {
	
	@Resource
	private LogService logService;
	
	@RequiresPermissions("set:operLog:query")
	@RequestMapping(value = "/sys/log/logManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG)
	public String manage(){
		return "/sys/log/logManage";
	}
	
	@RequestMapping(value = "/sys/log/list")
	@RequiresPermissions("set:operLog:query")
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.LOG)
	public Object list(Log log){
		return logService.getLogList(log);
	}
	

}

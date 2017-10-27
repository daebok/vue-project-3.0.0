package com.rd.ifaes.core.sys.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Log;

/**
 * Service Interface:LogService
 * @author lh
 * @version 3.0
 * @date 2016-5-18
 */
public interface LogService extends BaseService<Log>{
	
	/**
	 * 查询日志分页
	 * @author fxl
	 * @date 2016年10月12日
	 * @param log
	 * @return
	 */
	Page<Log> getLogList(Log log);

}
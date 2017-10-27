package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Log;

/**
 * Dao Interface:LogMapper
 * @author lh
 * @version 3.0
 * @date 2016-5-18
 */
public interface LogMapper extends BaseMapper<Log> {

	/**
	 * 查询日志分页
	 * @author fxl
	 * @date 2016年10月12日
	 * @param log
	 * @return
	 */
	List<Log> getLogList(Log log);
}
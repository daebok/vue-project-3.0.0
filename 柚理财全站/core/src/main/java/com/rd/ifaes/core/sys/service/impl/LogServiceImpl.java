package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.Log;
import com.rd.ifaes.core.sys.mapper.LogMapper;
import com.rd.ifaes.core.sys.service.LogService;

import javax.annotation.Resource;

/**
 * ServiceImpl:LogServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-18
 */
@Service("logService") 
public class LogServiceImpl  extends BaseServiceImpl<LogMapper, Log> implements LogService{
	
    @Resource
    private LogMapper logMapper;
    
    @Override
    @Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
    public void save(Log entity) {
    	super.save(entity);
    }

	@Override
	public Page<Log> getLogList(Log log) {
		Page<Log> page = log.getPage();
		if(page==null){
			page=new Page<Log>();
		}
		page.setRows(dao.getLogList(log));
		return page;
	}

}
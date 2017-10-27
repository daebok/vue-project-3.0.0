package com.rd.ifaes.core.project.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.project.domain.UrgeRepayLog;
import com.rd.ifaes.core.project.mapper.UrgeRepayLogMapper;
import com.rd.ifaes.core.project.service.UrgeRepayLogService;

/**
 * ServiceImpl:UrgeRepayLogServiceImpl
 * @author wyw
 * @version 3.0
 * @date 2016-8-17
 */
@Service("urgeRepayLogService") 
public class UrgeRepayLogServiceImpl  extends BaseServiceImpl<UrgeRepayLogMapper, UrgeRepayLog> implements UrgeRepayLogService{
	
    //@Resource
    //private UrgeRepayLogMapper urgeRepayLogMapper;

}
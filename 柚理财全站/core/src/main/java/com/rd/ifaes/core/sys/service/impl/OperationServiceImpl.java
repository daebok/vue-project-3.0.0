package com.rd.ifaes.core.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.sys.domain.Operation;
import com.rd.ifaes.core.sys.mapper.OperationMapper;
import com.rd.ifaes.core.sys.service.OperationService;

import javax.annotation.Resource;

/**
 * ServiceImpl:OperationServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-6-2
 */
@Service("operationService") 
public class OperationServiceImpl  extends BaseServiceImpl<OperationMapper, Operation> implements OperationService{
	
    @Resource
    private OperationMapper operationMapper;

    @Override
    public List<Operation> findByMenuId(String menuId){
    	return operationMapper.findListByMenuId(menuId, null);
    }
}
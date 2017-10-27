package com.rd.ifaes.core.tpp.service.impl;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.tpp.domain.TppCbhbRequest;
import com.rd.ifaes.core.tpp.mapper.TppCbhbRequestMapper;
import com.rd.ifaes.core.tpp.service.TppCbhbRequestService;

/**
 * ServiceImpl:TppCbhbRequestServiceImpl
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
@Service("tppCbhbRequestService") 
public class TppCbhbRequestServiceImpl  extends BaseServiceImpl<TppCbhbRequestMapper, TppCbhbRequest> implements TppCbhbRequestService{
	
    //@Resource
    //private TppCbhbRequestMapper tppCbhbRequestMapper;

}
package com.rd.ifaes.core.tpp.service.impl;


import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.tpp.domain.TppCbhbResponse;
import com.rd.ifaes.core.tpp.mapper.TppCbhbResponseMapper;
import com.rd.ifaes.core.tpp.service.TppCbhbResponseService;

/**
 * ServiceImpl:TppCbhbResponseServiceImpl
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
@Service("tppCbhbResponseService") 
public class TppCbhbResponseServiceImpl  extends BaseServiceImpl<TppCbhbResponseMapper, TppCbhbResponse> implements TppCbhbResponseService{

	@Override
	public TppCbhbResponse getByMerbillNo(String merBillNo) {
		return dao.getByMerbillNo(merBillNo);
	}
	

}
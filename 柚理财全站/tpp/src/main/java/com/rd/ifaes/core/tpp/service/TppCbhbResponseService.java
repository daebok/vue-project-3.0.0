package com.rd.ifaes.core.tpp.service;


import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.tpp.domain.TppCbhbResponse;

/**
 * Service Interface:TppCbhbResponseService
 * @author qpz
 * @version 3.0
 * @date 2017-3-23
 */
public interface TppCbhbResponseService extends BaseService<TppCbhbResponse>{
	
	/**
	 * 根据请求流水号查询响应记录
	 * @author QianPengZhan
	 * @date 2017年3月23日
	 * @param merBillNo
	 * @return
	 */
	TppCbhbResponse getByMerbillNo(String merBillNo);
}
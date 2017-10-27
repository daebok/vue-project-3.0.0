package com.rd.ifaes.core.sys.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.OperatorCustomer;
import com.rd.ifaes.core.sys.model.SaleStatisticsModel;

/**
 * Service Interface:OperatorCustomerService
 * @author wyw
 * @version 3.0
 * @date 2016-8-19
 */
public interface OperatorCustomerService extends BaseService<OperatorCustomer>{
	/**
	 * 
	 * 后台经纪人 添加客户
	 * @author wyw
	 * @date 2016-8-19
	 * @param operatorCustomer
	 * @return
	 */
	OperatorCustomer addCustomerManager(OperatorCustomer operatorCustomer);
	/**
	 * 
	 * 后台经纪人 修改客户
	 * @author wyw
	 * @date 2016-8-19
	 * @param operatorCustomer
	 * @return
	 */
	OperatorCustomer editCustomerManager(OperatorCustomer operatorCustomer);
	
	/**
	 * 
	 * 业绩统计
	 * @author wyw
	 * @date 2016-8-19
	 * @param operatorCustomer
	 * @return
	 */
	List<SaleStatisticsModel> findSaleStatistics(OperatorCustomer operatorCustomer);
	
}
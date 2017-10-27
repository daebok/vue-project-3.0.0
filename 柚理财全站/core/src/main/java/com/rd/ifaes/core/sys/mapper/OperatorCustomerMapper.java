package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.sys.domain.OperatorCustomer;
import com.rd.ifaes.core.sys.model.SaleStatisticsModel;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:OperatorCustomerMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-8-19
 */
public interface OperatorCustomerMapper extends BaseMapper<OperatorCustomer> {
	/**
	 * 
	 * 统计经纪人业绩
	 * @author wyw
	 * @date 2016-8-19
	 * @param model
	 * @return
	 */
	List<SaleStatisticsModel> findSaleStatistics(OperatorCustomer model);
}
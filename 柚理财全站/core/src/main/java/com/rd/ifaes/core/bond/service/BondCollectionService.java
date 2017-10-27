package com.rd.ifaes.core.bond.service;

import java.math.BigDecimal;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.bond.domain.BondCollection;

/**
 * 债权待收
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月30日
 */
public interface BondCollectionService extends BaseService<BondCollection>{
	
	/**
	 * 根据原始表的投资ID 查询本息和
	 * @author QianPengZhan
	 * @date 2016年8月6日
	 * @param investId
	 * @return
	 */
	BigDecimal getSumByInvestId(String investId);
	
	/**
	 * 根据原始表的投资ID 查询本金和利息
	 * @author zhangxj
	 * @date 2017年10月09日
	 * @param investId
	 */
	BondCollection getSumCapAndInByInvestId(String investId);
	
	/**
	 * 根据期数和对应的投资ID 查询单条待收记录
	 * @author QianPengZhan
	 * @date 2016年8月10日
	 * @param investId
	 * @param period
	 * @return
	 */
	BondCollection getBondCollectionByInvestIdAndPeriod(String investId,int period);

	/**
	 * 根据bondInvestId更新investId
	 * @param investId
	 * @param uuid
	 */
	int updateInvestIdByBondInvestId(String investId, String bondInvestId);
}
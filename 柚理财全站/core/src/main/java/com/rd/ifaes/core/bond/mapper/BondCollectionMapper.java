/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.bond.mapper;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.bond.domain.BondCollection;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Dao Interface:BondCollectionMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface BondCollectionMapper extends BaseMapper<BondCollection> {
    /**
     * 根据原始表的投资ID 查询本息和
     * @author QianPengZhan
     * @date 2016年8月6日
     * @param investId
     * @return
     */
    BigDecimal getSumByInvestId(String investId);

    /**
     * 根据期数和对应的投资ID 查询单条待收记录
     * @author QianPengZhan
     * @date 2016年8月10日
     * @param investId
     * @param period
     * @return
     */
    BondCollection getBondCollectionByInvestIdAndPeriod(@Param("investId") String investId, @Param("period") int period);

    /**
     * 根据bondInvestId更新investId
     * @param investId
     * @param uuid
     */
    int updateInvestIdByBondInvestId(@Param("investId") String investId, @Param("bondInvestId") String bondInvestId);

	BondCollection getSumCapAndInByInvestId(String investId);
}
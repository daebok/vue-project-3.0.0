package com.rd.ifaes.core.bond.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.mapper.BondCollectionMapper;
import com.rd.ifaes.core.bond.service.BondCollectionService;

/**
 * 债权待收类
 *
 * @author QianPengZhan
 * @version 3.0
 * @date 2016年8月6日
 */
@Service("bondCollectionService")
public class BondCollectionServiceImpl extends BaseServiceImpl<BondCollectionMapper, BondCollection> implements BondCollectionService {

    /**
     * 根据原始表的投资ID 查询本息和
     */
    @Override
    public BigDecimal getSumByInvestId(final String investId) {
        return dao.getSumByInvestId(investId);
    }

    /**
     * 根据期数和对应的投资ID 查询单条待收记录
     */
    @Override
    public BondCollection getBondCollectionByInvestIdAndPeriod(final String investId,
                                                               final int period) {
        return dao.getBondCollectionByInvestIdAndPeriod(investId, period);
    }

    @Override
    public int updateInvestIdByBondInvestId(String investId, String bondInvestId) {
        return dao.updateInvestIdByBondInvestId(investId, bondInvestId);
    }

	@Override
	public BondCollection getSumCapAndInByInvestId(String investId) {
		return dao.getSumCapAndInByInvestId(investId);
	}


}
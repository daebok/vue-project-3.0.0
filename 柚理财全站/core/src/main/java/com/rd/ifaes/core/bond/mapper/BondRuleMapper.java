package com.rd.ifaes.core.bond.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.bond.domain.BondRule;

/**
 * Dao Interface:BondRuleMapper
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-21
 */
public interface BondRuleMapper extends BaseMapper<BondRule> {

	/**
	 * bondRuleService:(获取最新的债权规则配置). <br/> 
	 * @author QianPengZhan
	 * @return
	 */
	List<BondRule> getRecentBondRuleModel();
}
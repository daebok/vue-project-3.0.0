package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.operate.mapper.RedenvelopeRuleDetailMapper;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;

/**
 * 
 * 红包规则明细业务实现类
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
@Service("redenvelopeRuleDetailService") 
public class RedenvelopeRuleDetailServiceImpl  extends BaseServiceImpl<RedenvelopeRuleDetailMapper, RedenvelopeRuleDetail> implements RedenvelopeRuleDetailService{

	/**
	 * 根据规则id删除规则明细
	 */
	@Override
	public void deleteDetailByRuleId(final String ruleId) {
	    this.dao.deleteByRuleId(ruleId);
	}

	/**
	 * 根据红包规则id获取规则明细
	 */
	@Override
	public List<RedenvelopeRuleDetail> getListByRuleId(final String ruleId) {
		final RedenvelopeRuleDetail queryDetail = new RedenvelopeRuleDetail();
		queryDetail.setRuleId(ruleId);
	    return dao.findList(queryDetail);
	}

	/**
	 * 汇总红包明细金额
	 */
	@Override
	public BigDecimal collectByRuleId(final String ruleId) {
		return dao.collectByRuleId(ruleId);
	}

	@Override
	public BigDecimal getAmountByRuleId(
			String redenvelopeRuleId) {
		return dao.getAmountByRuleId(redenvelopeRuleId);
	}

	@Override
	public List<RedenvelopeRuleDetail> getTenderGroup(String ruleId) {
		return dao.getTenderGroup(ruleId);
	}
}
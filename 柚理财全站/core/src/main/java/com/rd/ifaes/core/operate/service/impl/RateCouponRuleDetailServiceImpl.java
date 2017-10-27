package com.rd.ifaes.core.operate.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;
import com.rd.ifaes.core.operate.mapper.RateCouponRuleDetailMapper;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;

/**
 * ServiceImpl:RatecouponRuleDetailServiceImpl
 * @author 
 * @version 3.0
 * @date 2016-7-27
 */
@Service("rateCouponRuleDetailService") 
public class RateCouponRuleDetailServiceImpl  extends BaseServiceImpl<RateCouponRuleDetailMapper, RateCouponRuleDetail> implements RateCouponRuleDetailService{

	/**
	 * 根据规则id删除古规则明细列表
	 */
	@Override
	public void deleteDetailByRuleId(final String ruleId) {
		this.dao.deleteByRuleId(ruleId);
		
	}
	/**
	 * 根据规则id获取规则明细列表
	 */
	@Override
	public List<RateCouponRuleDetail> getListByRuleId(final String ruleId) {
		final RateCouponRuleDetail queryDetail = new RateCouponRuleDetail();
		queryDetail.setRuleId(ruleId);
	   return dao.findList(queryDetail);
	}
	
	/**
	 * 统计最大最小加息率
	 */
	@Override
	public Map<String, BigDecimal> collectByRuleId(String ruleId) {
		return dao.collectByRuleId(ruleId);
	}
	@Override
	public Map<String, BigDecimal> getIntervalAprByRuleId(
			String ruleId) {
		return dao.getIntervalAprByRuleId(ruleId);
	}
	@Override
	public List<Map<String, BigDecimal>> getTenderGroup(String ruleId) {
		return dao.getTenderGroup(ruleId);
	}
	
}
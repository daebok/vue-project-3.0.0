package com.rd.ifaes.core.operate.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;

/**
 * Dao Interface:RedenvelopeRuleDetailMapper
 * @author 
 * @version 3.0
 * @date 2016-7-22
 */
public interface RedenvelopeRuleDetailMapper extends BaseMapper<RedenvelopeRuleDetail> {
	/**
	 * 
	 * 根据规则id删除
	 * @author wyw
	 * @date 2016-8-5
	 * @param ruleId
	 */
	 void deleteByRuleId(String ruleId);

	 /**
	  * 
	  *  汇总红包明细金额
	  * @param  
	  * @return BigDecimal
	  * @author xxb
	  * @date 2016年10月28日
	  */
	 BigDecimal collectByRuleId(String ruleId);

	/**
	 * 根据红包规则id获取未删除的规则明细金额
	 * @author ZhangBiao
	 * @date 2016年11月3日
	 * @param redenvelopeRuleId
	 * @return
	 */
	BigDecimal getAmountByRuleId(String ruleId);
	
	/**
	 * 获取投资金额分组
	 * @author fxl
	 * @date 2016年12月26日
	 * @param ruleId
	 * @return
	 */
	List<RedenvelopeRuleDetail> getTenderGroup(String ruleId);
	 
}

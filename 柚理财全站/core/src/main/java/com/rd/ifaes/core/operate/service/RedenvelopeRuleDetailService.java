package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;

import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.base.service.BaseService;

/**
 *  红包规则
 * @version 3.0
 * @author wyw
 * @date 2016-7-26
 */
public interface RedenvelopeRuleDetailService extends BaseService<RedenvelopeRuleDetail>{
	/**
	 * 根据规则id删除规则明细
	 * @author wyw
	 * @param ruleId 规则id
	 */
	  void deleteDetailByRuleId(String ruleId);
	/**
	 * 根据红包规则获取规则明细
	 * @author wyw
	 * @param ruleId 规则id
	 * @return
	 */
	  List<RedenvelopeRuleDetail> getListByRuleId(String ruleId);
	  
	  /**
	   * 获取红包规则下的红包汇总值
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
	 * @param flagNo
	 * @return
	 */
	BigDecimal getAmountByRuleId(String redenvelopeRuleId);
	
	
	/**
	 * 获取投资金额分组
	 * @author fxl
	 * @date 2016年12月26日
	 * @param ruleId
	 * @return
	 */
	List<RedenvelopeRuleDetail> getTenderGroup(String ruleId);
}
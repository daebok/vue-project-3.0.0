package com.rd.ifaes.core.operate.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;

/**
 * 
 * 加息券规则明细mapper接口类
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface RateCouponRuleDetailMapper extends BaseMapper<RateCouponRuleDetail> {
	/**
	 * 
	 * 根据加息券规则id删除规则明细
	 * @author wyw
	 * @date 2016-7-28
	 * @param ruleId
	 */
	 void deleteByRuleId(String ruleId);
	 
	 /**
	  *  统计最大最小加息率
	  * @param  
	  * @return RateCouponRuleDetail
	  * @author xxb
	  * @date 2016年10月28日
	  */
	 Map<String, BigDecimal> collectByRuleId(String ruleId);

	 /**
	  * 根据加息券规则id获取加息的最大最小值
	  * @author ZhangBiao
	  * @date 2016年11月3日
	  * @param ruleId
	  * @return
	  */
	Map<String, BigDecimal> getIntervalAprByRuleId(String ruleId);
	
	/**
	 * 获取投资金额分组
	 * @author fxl
	 * @date 2016年12月26日
	 * @param ruleId
	 * @return
	 */
	List<Map<String, BigDecimal>> getTenderGroup(String ruleId);
}
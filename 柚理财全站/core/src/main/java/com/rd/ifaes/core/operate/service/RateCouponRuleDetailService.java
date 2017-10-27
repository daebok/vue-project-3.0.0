package com.rd.ifaes.core.operate.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;

/**
 * 
 *  规则明细业务处理类接口
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface RateCouponRuleDetailService extends BaseService<RateCouponRuleDetail>{
	/** 
	 *  根据规则id删除规则明细
	 * @author wyw
	 * @date 2016-7-28
	 * @param ruleId 规则id
	 */
    void deleteDetailByRuleId(String ruleId);
	/**
	 * 根据规则id获取规则明细
	 * @author wyw
	 * @date 2016-8-5
	 * @param ruleId
	 * @return
	 */
	 List<RateCouponRuleDetail> getListByRuleId(String ruleId);
	 

	 /**
	  *统计最大最小加息率
	  * @param  
	  * @return RateCouponRuleDetail
	  * @author xxb
	  * @date 2016年10月28日
	  */
	 Map<String, BigDecimal> collectByRuleId(String ruleId);
	 
	/**
	 * 根据加息券规则id获取未删除的规则明细最大最小值
	 * @author ZhangBiao
	 * @date 2016年11月3日
	 * @param rateCouponRuleId
	 * @return
	 */
	Map<String, BigDecimal> getIntervalAprByRuleId(String rateCouponRuleId);
	
	/**
	 * 获取投资金额分组
	 * @author fxl
	 * @date 2016年12月26日
	 * @param ruleId
	 * @return
	 */
	List<Map<String, BigDecimal>> getTenderGroup(String ruleId);
	  
}
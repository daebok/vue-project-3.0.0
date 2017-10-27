package com.rd.ifaes.core.project.mapper;

import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * Dao Interface:RealizeRuleMapper
 * @author fxl
 * @version 3.0
 * @date 2016-7-22
 */
public interface RealizeRuleMapper extends BaseMapper<RealizeRule> {

	/**
	 * 获取当前变现规则
	 * @return
	 */
	public RealizeRule getRule();
	
	/**
	 * 新增规则前关闭其他规则
	 * @author fxl
	 * @date 2016年8月2日
	 */
	public void closeOther(String ruleStatus);
}
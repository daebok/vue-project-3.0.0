package com.rd.ifaes.core.project.service;

import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.base.service.BaseService;

/**
 * 变现规则业务类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public interface RealizeRuleService extends BaseService<RealizeRule>{

	/**
	 * 获取变现规则
	 * @author fxl
	 * @date 2016年7月26日
	 * @return
	 */
	public RealizeRule getRule();
	
}
package com.rd.ifaes.core.operate.mapper;

import com.rd.ifaes.core.operate.domain.ActivityPlan;
import com.rd.ifaes.core.base.mapper.BaseMapper;

/**
 * 活动方案 mapper接口
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public interface ActivityPlanMapper extends BaseMapper<ActivityPlan> {
	
	ActivityPlan findActivityPlanByCode(String code);
	
	/**
	  * 通过code查询活动方案
	  * @author ywt
	  * @date 2016-11-05
     * @param user
	 */
	 String[] findRuleByActivityCode(String code);

}
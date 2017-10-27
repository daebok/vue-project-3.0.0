package com.rd.ifaes.core.project.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.Realize;

/**
 * 变现还款类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
public interface RealizeRepaymentService extends BaseService<ProjectRepayment>{
	
	/**
	 * 变现自动还款
	 * @author fxl
	 * @date 2016年8月10日
	 */
	void autoRepay();
	
	/**
	 * 变现还款
	 * @author fxl
	 * @date 2016年8月01日
	 * @param entity
	 * @return
	 */
	void repay(Realize entity);

	/**
	 * 渤海银行 - 还款
	 * @author ZhangBiao
	 * @date 2017年3月17日
	 * @param entity
	 */
	void repayCbhb(Realize entity);
	
}
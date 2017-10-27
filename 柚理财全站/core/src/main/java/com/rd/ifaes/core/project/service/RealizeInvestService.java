package com.rd.ifaes.core.project.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;


/**
 * 变现投资业务类
 * @version 3.0
 * @author fxl
 * @date 2016年7月28日
 */
public interface RealizeInvestService extends BaseService<ProjectInvest>{
	
	/**
	 * 变现投资
	 * @param invest 用户投资信息
	 * @return
	 */
	public UfxInvestModel doRealizeInvest(ProjectInvestModel invest);
	
	/**
	 * 获取可变现的投资记录
	 * @param model
	 * @return
	 */
	Page<ProjectInvestModel> findRealizeAbleList(ProjectInvestModel model); 
	
	
}
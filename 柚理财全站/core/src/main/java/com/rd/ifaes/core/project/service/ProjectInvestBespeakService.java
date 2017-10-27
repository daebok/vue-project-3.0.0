package com.rd.ifaes.core.project.service;

import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvestBespeak;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;

/**
 * Service Interface:ProjectInvestBespeakService
 * @author wyw
 * @version 3.0
 * @date 2016-8-20
 */
public interface ProjectInvestBespeakService extends BaseService<ProjectInvestBespeak>{
	/**
	 * 
	 * 投资预约方法
	 * @author wyw
	 * @date 2016-8-20
	 * @param userId
	 * @param projectId
	 */
	void investBespeak(String userId,Project project);
	/**
	 * 
	 * 投资提醒 -定时任务
	 * @author wyw
	 * @date 2016-8-24
	 */
	void investRemind();
	/**
	 * 获取用户预约未开售的所有项目的ID
	 * @param userId 用户ID
	 * @return 预约未开售的所有项目ID列表 
	 */
	 List<String> getUserBespeakProjectIds(String userId);
}
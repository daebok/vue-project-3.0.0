package com.rd.ifaes.core.project.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectInvestBespeak;

/**
 * Dao Interface:ProjectInvestBespeakMapper
 * @author FangJun
 * @version 3.0
 * @date 2016-8-20
 */
public interface ProjectInvestBespeakMapper extends BaseMapper<ProjectInvestBespeak> {

	/**
	 * 获取用户预约未开售的所有项目的ID
	 * @param projectIds
	 * @return
	 */
	List<String> getUserBespeakProjectIds(String userId);
}
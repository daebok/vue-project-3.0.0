package com.rd.ifaes.core.project.service;

import java.util.List;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.ProjectVerifyLog;

/**
 * Service Interface:VerifyLogService
 * @author lh
 * @version 3.0
 * @date 2016-9-26
 */
public interface ProjectVerifyLogService extends BaseService<ProjectVerifyLog>{
	
	/**
	 * 保存审核记录
	 * @param projectId
	 * @param operatorId
	 * @param node
	 * @param result
	 * @param remark
	 */
	void save(String projectId, String operatorId, String node, String result, String remark);
	
	/**
	 * 根据项目名称查询审核记录
	 * @param projectId
	 * @return
	 */
	List<ProjectVerifyLog> findByProjectId(String projectId);
	
	/**
	 * 取得备注信息及添加时间
	 * @param projectId
	 * @param processNode
	 * @return
	 */
	ProjectVerifyLog getRemarkCreateTime(String projectId, String processNode);

	/**
	 * 获取最新审核记录
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 */
	String getRemarkByProjectId(String projectId);
	
	/**
	 * 取得项目最后的审核记录
	 * @param projectIds
	 * @return
	 */
	List<ProjectVerifyLog> findRemarkByProjectIds(String[] projectIds);

}
package com.rd.ifaes.core.project.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.project.domain.ProjectVerifyLog;
import com.rd.ifaes.core.project.mapper.ProjectVerifyLogMapper;
import com.rd.ifaes.core.project.service.ProjectVerifyLogService;

/**
 * ServiceImpl:VerifyLogServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-9-26
 */
@Service("projectVerifyLogService") 
public class ProjectVerifyLogServiceImpl  extends BaseServiceImpl<ProjectVerifyLogMapper, ProjectVerifyLog> implements ProjectVerifyLogService{
		
	@Override
	public void save(String projectId, String verifyName, String node, String result, String remark) {
		ProjectVerifyLog log = new ProjectVerifyLog(IdGen.uuid(), projectId, verifyName, node, result, null, remark);
		dao.insert(log);
	}
	
	@Override
	public List<ProjectVerifyLog> findByProjectId(String projectId) {
		return dao.findByProjectId(projectId);
	}
	
	@Override
	public ProjectVerifyLog getRemarkCreateTime(String projectId, String processNode) {
		return dao.getRemarkCreateTime(projectId, processNode);
	}

	@Override
	public String getRemarkByProjectId(String projectId) {
		return dao.getRemarkByProjectId(projectId);
	}
	
	@Override
	public List<ProjectVerifyLog> findRemarkByProjectIds(String[] projectIds) {
		return dao.findRemarkByProjectIds(projectIds);
	}

}
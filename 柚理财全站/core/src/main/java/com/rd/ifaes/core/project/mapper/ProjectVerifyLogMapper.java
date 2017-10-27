package com.rd.ifaes.core.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.project.domain.ProjectVerifyLog;

/**
 * Dao Interface:VerifyLogMapper
 * @author lh
 * @version 3.0
 * @date 2016-9-26
 */
public interface ProjectVerifyLogMapper extends BaseMapper<ProjectVerifyLog> {
	
	/**
	 * 根据项目id查询项目审核记录
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
	ProjectVerifyLog getRemarkCreateTime(@Param("projectId") String projectId, @Param("processNode") String processNode);

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
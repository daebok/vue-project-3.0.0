package com.rd.ifaes.core.workflow.service;

import java.util.List;

import com.rd.ifaes.core.workflow.domain.TaskExt;

/**
 * 工作流服务接口
 * @author xxb
 *
 */
public interface TaskExtService {

	List<TaskExt> findTasksByUserId(String userId);
}

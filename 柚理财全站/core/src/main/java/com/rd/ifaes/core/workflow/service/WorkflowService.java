package com.rd.ifaes.core.workflow.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.rd.ifaes.core.workflow.domain.NotionExt;
import com.rd.ifaes.core.workflow.domain.TaskExt;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;

/**
 * 工作流服务接口
 * @author xxb
 *
 */
public interface WorkflowService {

	/**
	 * 启动流程,向前推进返回待办任务
	 * @param processkey
	 * @param busnissId
	 * @param busnissTitle
	 * @param parameters
	 * @return
	 */
	public Task startProcess(String processkey, String busnissId, String busnissTitle, Map<String, Object> parameters, WorkflowBaseModel workflowBaseModel);
	
	/**
	 * 执行流程（推进、回退）
	 * @param busnissTitle
	 * @param parameters
	 * @param bean
	 * @return
	 */
	public Task executeProcess(String busnissTitle, Map<String, Object> parameters, WorkflowBaseModel workflowBaseModel);
	
	
	
	/**
	 * 查询（已办、待办）任务信息
	 * @param taskExt
	 * @return
	 */
	public List<TaskExt> findHisTask(TaskExt taskExt);
	
	/**
	 * 
	 * @param taskExt
	 * @return
	 */
	public List<NotionExt> findNotion(NotionExt notionExt);
}

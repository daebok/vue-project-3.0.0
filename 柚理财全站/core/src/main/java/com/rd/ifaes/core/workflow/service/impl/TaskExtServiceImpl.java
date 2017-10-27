package com.rd.ifaes.core.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.workflow.domain.TaskExt;
import com.rd.ifaes.core.workflow.service.TaskExtService;

/**
 * 工作流服务接口
 * 
 * @author xxb
 *
 */
@Service("taskExtService")
public class TaskExtServiceImpl implements TaskExtService {

	@Override
	public List<TaskExt> findTasksByUserId(final String userId) {
		final RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringContextHolder.getBean("runtimeService");
		return (List<TaskExt>) runtimeService.getCommandExecutor().execute(new Command<List<TaskExt>>() {
			@Override
			public List<TaskExt> execute(CommandContext commandContext) {
				TaskService taskService = (TaskService) SpringContextHolder.getBean("taskService");
				TaskQueryImpl taskQuery = (TaskQueryImpl) taskService.createTaskQuery()
																	 .taskCandidateOrAssigned(userId);
				List<TaskExt> taskExtList = new ArrayList<TaskExt>();
				List<Task> taskList =  commandContext.getTaskEntityManager().findTasksByQueryCriteria(taskQuery);
				for(Task t : taskList){
					TaskExt taskExt = new TaskExt();
					taskExt.setActivityName(t.getName());
					taskExt.setAssignee(t.getAssignee());
					taskExt.setCreateTime(t.getCreateTime());
					taskExt.setDescription(t.getDescription());
					taskExt.setDueDate(t.getDueDate());
					taskExt.setBussnissId(ObjectUtils.toString(runtimeService.getVariable(t.getProcessInstanceId(), Constant.PROCESS_BUSINESS_ID)));
					taskExt.setTitle(ObjectUtils.toString(runtimeService.getVariable(t.getProcessInstanceId(), Constant.PROCESS_BUSINESS_TITLE)));
					taskExt.setUuid(t.getId());
					taskExt.setFormKey(t.getFormKey());
					taskExtList.add(taskExt);
				}
				return taskExtList;
			}
		});
	}
/*
	public List<TaskExt> getTaskByUserId(final String userId){
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) SpringContextHolder.getBean("runtimeService");
		List<TaskExt> execute = (List<TaskExt>) runtimeService.getCommandExecutor().execute(new Command<TaskExt>() {
			@Override
			public TaskExt execute(CommandContext commandContext) {
				TaskService taskService = (TaskService) SpringContextHolder.getBean("taskService");
				TaskQueryImpl taskQuery = (TaskQueryImpl) taskService.createTaskQuery()
						.taskCandidateOrAssigned(userId);
				List<Task> taskList =  commandContext.getTaskEntityManager().findTasksByQueryCriteria(taskQuery);
				return null;
			}
		});
		return execute;
		
	}
	*/
	
	/*@Override
	public List<TaskExt> execute(CommandContext commandContext) {
		TaskService taskService = (TaskService) SpringContextHolder.getBean("taskService");
		TaskQueryImpl taskQuery = (TaskQueryImpl) taskService.createTaskQuery()
				.taskCandidateOrAssigned("0319e6e12698443b9f6f6a4edb2dbeeb");
		List<Task> taskList =  commandContext.getTaskEntityManager().findTasksByQueryCriteria(taskQuery);
		return null;
	}*/

}

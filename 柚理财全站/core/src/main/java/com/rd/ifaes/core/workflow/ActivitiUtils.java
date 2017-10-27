package com.rd.ifaes.core.workflow;

import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import com.rd.ifaes.common.util.SpringContextHolder;

public class ActivitiUtils {

	protected static ProcessEngine processEngine = (ProcessEngine) SpringContextHolder.getBean("processEngine");

	/**
	 * 获取当前任务
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public static List<Task> getTaskByProcessInstance(String processInstanceId) {
		TaskService taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		return taskList;
	}

	/**
	 * 获取第一任务
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public static Task getFirstTaskByProcessInstance(String processInstanceId) {
		List<Task> taskList = getTaskByProcessInstance(processInstanceId);
		if (taskList != null && taskList.size() > 0) {
			return taskList.get(0);
		}
		return null;
	}

	/**
	 * 根据当前任务获取对应的环节
	 * @param task
	 * @return
	 */
	public static ActivityImpl getActivityByTask(Task task) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService
												.getProcessDefinition(task.getProcessDefinitionId());
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				                                .processInstanceId(task.getProcessInstanceId())
												.singleResult();
		String activitiId = pi.getActivityId();
		ActivityImpl activity = pd.findActivity(activitiId);
		return activity;
	}
	
	
	/**
	 * 获取流程中第一个任务节点
	 * 
	 * @param task
	 * @return
	 */
	public static ActivityImpl getPreActivity(ActivityImpl activityImpl) {
		List<PvmTransition> inTransitions = activityImpl.getIncomingTransitions();
		//没有进入线说明为开始节点，【开始】、【流程发起】节点都是唯一节点
		if (inTransitions == null || inTransitions.size() == 0) {
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			return (ActivityImpl) outTransitions.get(0).getDestination();
		}else{
			// 非开始节点
			return getPreActivity((ActivityImpl) inTransitions.get(0).getSource());
		}
	}

	/**
	 * 清空环节所有流出分支线
	 * @param activityImpl
	 * @return
	 */
	public static List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    }  
}

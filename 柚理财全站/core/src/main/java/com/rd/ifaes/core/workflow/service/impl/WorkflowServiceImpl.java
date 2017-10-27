package com.rd.ifaes.core.workflow.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.workflow.ActivitiUtils;
import com.rd.ifaes.core.workflow.domain.NotionExt;
import com.rd.ifaes.core.workflow.domain.TaskExt;
import com.rd.ifaes.core.workflow.mapper.NotionExtMapper;
import com.rd.ifaes.core.workflow.mapper.TaskExtMapper;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;
import com.rd.ifaes.core.workflow.service.WorkflowService;

/**
 * 工作流服务接口
 * @author xxb
 *
 */
@Service("workflowService") 
public class WorkflowServiceImpl implements WorkflowService {

	@Resource
    private NotionExtMapper notionExtMapper;

	@Resource
    private TaskExtMapper taskExtMapper;

	/**
	 * processkey  业务相关流程key
	 * busnissId   业务主键
	 * busnissTitle 业务标题
	 * parameters 流程运行参数
	 * model 自定义数据对象
	 * 返回：流程启动的首个任务task对象
	 */
	@Override
	public Task startProcess(String processkey, String busnissId, String busnissTitle, Map<String, Object> parameters, WorkflowBaseModel model) {
	    Map<String, Object> variables = new HashMap<String, Object>();
	    if (parameters != null) {
	      variables.putAll(parameters);
	    }
	    variables.put(Constant.PROCESS_BUSINESS_ID, busnissId);
	    variables.put(Constant.PROCESS_BUSINESS_CREATOR, model.getOperator());
	    variables.put(Constant.PROCESS_BUSINESS_TITLE, busnissTitle);
	    RuntimeService runtimeService = (RuntimeService)SpringContextHolder.getBean("runtimeService");
	    
	    //启动流程
	    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processkey, variables);
	    //获取流程首个任务(即流程申请节点)
	    Task task = ActivitiUtils.getFirstTaskByProcessInstance(processInstance.getId());
	    if (task != null) {
	        model.setActionName(WorkflowBaseModel.ActionName.COMMIT.name());  //默认：提交动作
	        model.setProcessInstanceId(processInstance.getId());
	        model.setCurrentActivityName(task.getName());
	        model.setTaskId(task.getId());
	        executeProcess(busnissTitle, variables, model);   //业务提交，同步完成流程中的首个任务，流程继续推进
	    }
	    return task;
	}

	@Override
	public Task executeProcess(String busnissTitle, Map<String, Object> parameters,  WorkflowBaseModel model) {
	    String taskId = model.getTaskId();
	    TaskService taskService = (TaskService)SpringContextHolder.getBean("taskService");
	    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	    String actionName = model.getActionName();
	    Map<String, Object> variables = new HashMap<String, Object>();
	    if (parameters != null) {
	      variables.putAll(parameters);
	    }
	    model.setTitle(busnissTitle);
	    model.setCurrentActivityName(task.getName());
	    //提交动作，流程推进
	    if (StringUtils.equals(actionName, WorkflowBaseModel.ActionName.COMMIT.name())) {
	    	taskService.complete(taskId, variables);
	    }
	    //回退动作，流程指向第一个任务节点
	    if (StringUtils.equals(actionName, WorkflowBaseModel.ActionName.ROLLBACK.name())) {
	    	//获取当前节点
	    	ActivityImpl currentActivity = ActivitiUtils.getActivityByTask(task); 
	    	//获取流程发起节点
	        ActivityImpl targetActivity =  ActivitiUtils.getPreActivity(currentActivity);
	        ActivitiUtils.clearTransition(currentActivity);
	        TransitionImpl transitionImpl = currentActivity.createOutgoingTransition();
	        transitionImpl.setDestination(targetActivity);
	    	taskService.complete(taskId, variables);
	    }
	    //获取下一任务
	    Task nextTask = ActivitiUtils.getFirstTaskByProcessInstance(model.getProcessInstanceId());
	    afterCompleteTask(task, nextTask, model);
	    return task;
	}
	
	protected void afterCompleteTask(Task task, Task nextTask, WorkflowBaseModel model){
	    addTaskExt(task, nextTask, model);
	    addNotionExt(model);
	  }
	
	public void addTaskExt(Task task, Task nextTask, WorkflowBaseModel model) {
		
		HistoryService historyService = (HistoryService)SpringContextHolder.getBean("historyService");
		//处理当前任务
		HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
		TaskExt taskExt = new TaskExt(taskInstance);
		if (taskExt.getAssignee() == null) {
			taskExt.setAssignee(model.getOperator());
		}
		
		if (taskExtMapper.get(taskInstance.getId()) != null) {
			taskExtMapper.update(taskExt);
		} else {
			taskExtMapper.insert(taskExt);
		}
		
		if (nextTask != null) {
			HistoricTaskInstance nextTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(nextTask.getId()).singleResult();
		    TaskExt nextTaskExt = new TaskExt(nextTaskInstance);
		    if(StringUtils.isBlank(model.getTitle())){
		    	RuntimeService runtimeService = (RuntimeService)SpringContextHolder.getBean("runtimeService");
				Object title = runtimeService.getVariable(nextTask.getProcessInstanceId(), Constant.PROCESS_BUSINESS_TITLE);
			    nextTaskExt.setTitle(ObjectUtils.toString(title));
		    }else{
		    	nextTaskExt.setTitle(model.getTitle());
		    }
		    
		    taskExtMapper.insert(nextTaskExt);
		}
	  }
	
	 public void addNotionExt(WorkflowBaseModel model){
	    NotionExt notion = new NotionExt();
	    notion.preInsert();
	    notion.setProcessInstanceId(model.getProcessInstanceId());
	    notion.setActivityName(model.getCurrentActivityName());
	    notion.setTaskId(model.getTaskId());
	    notion.setOperator(model.getOperator());
	    notion.setOperateDate(new Timestamp(System.currentTimeMillis()));
	    notion.setActionName(model.getActionName());
	    notion.setRemark(model.getNotionContent());   /// 意见
	    notionExtMapper.insert(notion);
	 }

	@Override
	public List<TaskExt> findHisTask(TaskExt taskExt) {
		taskExt.setDeleteReason("completed");
		return taskExtMapper.findList(taskExt);
	}

	@Override
	public List<NotionExt> findNotion(NotionExt notionExt) {
		return notionExtMapper.findList(notionExt);
	}
	
}

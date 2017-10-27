package com.rd.ifaes.manage.sys;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.workflow.domain.NotionExt;
import com.rd.ifaes.core.workflow.domain.TaskExt;
import com.rd.ifaes.core.workflow.service.TaskExtService;
import com.rd.ifaes.core.workflow.service.WorkflowService;



/*import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;*/
/**
 * 工作流管理 Controller
 * @author xxb
 * @since 2016-6-2
 * @version 3.0
 */
//@Controller
public class WorkflowController extends BaseController {
	
	@Resource
	private ProcessEngine processEngine;
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;
	@Resource
	private FormService formService;
	@Resource
	private ManagementService managementService;
	@Resource
	private WorkflowService workflowService;
	@Resource
	private TaskExtService taskExtService;
	
	
	/**
	 * 待办列表界面
	 * @return
	 */
	@RequestMapping(value = "/workflow/taskManage")
	public String taskManage(){
		return "/workflow/taskManage";
	}
	
	/**
	 * 历史待办列表
	 * @return
	 */
	@RequestMapping(value = "/workflow/hisTaskManage")
	public String hismanage(){
		return "/workflow/hisTaskManage";
	}
	
	
	/**
	 * 流程发布
	 */
	@RequestMapping(value = "/workflow/deploy")
	@ResponseBody
	public Map<String, Object> processDeploy(){
		String fileName = "ProcessDemo";
		//发布流程
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource(Constant.PROCESS_RESOURCES + fileName + ".bpmn")
				//.addClasspathResource(Constant.PROCESS_RESOURCES + fileName + ".png")
				.deploy();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (deployment != null) {  
	    	resultMap = renderSuccessResult();    	
	    }
	    return resultMap;
	}
	
	/**
	 * 查询待办
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflow/queryTask")
	@ResponseBody
	public Object queryTask(HttpServletRequest request){
		String userId = request.getParameter("userId");
		return taskExtService.findTasksByUserId(userId);
	}
	
	
	/**
	 * 查询已办
	 * @return
	 */
	@RequestMapping(value = "/workflow/queryHisTask")
	@ResponseBody
	public Object queryHisTask(){
		TaskExt taskExt = new TaskExt();
		return workflowService.findHisTask(taskExt);
		
	}
		
	//流程实例的办理意见列表（办理意见待扩展）
	@RequestMapping(value = "/workflow/queryNotion")
	@ResponseBody
	public Object queryNotion(final String processInstanceId){
		NotionExt notionExt = new NotionExt();
		notionExt.setProcessInstanceId(processInstanceId);
		return workflowService.findNotion(notionExt); 
	}
	
}

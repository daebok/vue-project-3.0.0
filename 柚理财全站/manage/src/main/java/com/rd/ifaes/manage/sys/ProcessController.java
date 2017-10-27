package com.rd.ifaes.manage.sys;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.core.base.web.BaseController;

/*import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;*/
/**
 * 工作流Controller
 * @author xxb
 * @since 2016-6-16
 * @version 3.0
 */
//@Controller
public class ProcessController extends BaseController {
	
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
	
	//---------------------流程接口测试-------------------------------------------
	
	@RequestMapping(value = "/sys/process/processManage")
	public String manage(){
		return "/sys/process/processManage";
	}
	
	
	@RequestMapping(value = "/process/deploy")
	public void processDeploy(){
		String fileName = "";
		repositoryService.createDeployment()
				.addClasspathResource("/activiti-process/" + fileName + ".bpmn")
				.deploy();
	}
	
	//个人待办列表
	@RequestMapping(value = "/process/queryTask")
	public void queryTask(){
		
	}
	
	
	//个人已办列表
	@RequestMapping(value = "/process/queryHisTask")
	public void queryHisTask(){
		
	}
		
	//流程实例的办理意见列表（办理意见待扩展）
	@RequestMapping(value = "/process/queryNotion")
	public void queryNotion(){
		
	}
		
	
	/*@RequestMapping(value = "/process/start")
	@ResponseBody
	public Object startProcess(LogTemplate logTemplate){
		//启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
		System.out.println(processInstance.getName() + "启动成功。。。。");
		//推进申请节点
		recmdProcess(null);
		
	}*/
	
	/*@RequestMapping(value = "/sys/logTemplate/process/takeTask")
	@ResponseBody
	public Object taskTask(LogTemplate logTemplate){
		System.out.println(processEngine.toString());
		// 获取待办
		String assignee = "xxb@erongdu.com";
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee)
				.list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("#######################################");
			}
		}
		return null;
	}*/
	
	/*@RequestMapping(value = "/sys/logTemplate/process/recmdTask")
	@ResponseBody
	public Object recmdProcess(LogTemplate logTemplate){
		String assignee = "xxb@erongdu.com";
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee)
				.list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务的办理人：" + task.getAssignee());
				System.out.println("任务名称：" + task.getName());
				System.out.println("任务的创建时间：" + task.getCreateTime());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("#######################################");
				taskService.complete(task.getId());
				//ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");
				//System.out.println("完成任务： " + task.getName() + " 流程是否结束："+ processInstance.isEnded() +"") ;
			}
		}
		return null;
	}
	*/
	/*//获取待办页面
	@RequestMapping(value = "/sys/logTemplate/process/todotask")
	public String getTaskPage(LogTemplate logTemplate){
		String taskId = "22518";
		TaskFormData formData = formService.getTaskFormData(taskId);;
		String formKey = formData.getFormKey();
		System.out.println("formKey ---- " + formKey);
		return "/sys/logTemplate/dotask";
	}
	*/
	//---------------------流程接口测试-------------------------------------------
	

}

package com.rd.ifaes.manage.sys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.workflow.domain.ProcessTest;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel.ActionName;
import com.rd.ifaes.core.workflow.service.WorkflowTestService;

/**
 * 工作流测试Controller
 * @author xxb
 * @since 2016-6-16
 * @version 3.0
 */
//@Controller
public class WorkflowTestController extends BaseController {
	
	@Resource
	private WorkflowTestService workflowTestService;
	
	//申请
	@RequestMapping(value = "/workflowtest/manage_1")
	public String applyManage(){
		return "/workflowtest/manage_1";
	}
	
	//审核
	@RequestMapping(value = "/workflowtest/manage_2")
	public String verfiyManage(){
		return "/workflowtest/manage_2";
	}
	
	//审批
	@RequestMapping(value = "/workflowtest/manage_3")
	public String checkManage(){
		return "/workflowtest/manage_3";
	}
	
	@RequestMapping(value = "/workflowtest/list")
	@ResponseBody
	public Object list(ProcessTest processTest){
		return workflowTestService.findPage(processTest);
	}
	
	/**
	 * 申请
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/apply")
	public String apply(){
		ProcessTest processTest = new ProcessTest();
		workflowTestService.workflowTestApply(processTest);
		return null;
	}

	
	/**
	 * 审核
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/toVerify_1")
	public String toVerify(final Model model ,final String taskId, final String bussnissId) {
		 model.addAttribute("processTest", workflowTestService.get(bussnissId));
		 model.addAttribute("taskId", taskId);
		return "/workflowtest/verify_1";
	}
	/**
	 * 审核
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/verify_1")
	public String verify(HttpServletRequest request){
		String actionFlag = request.getParameter("actionFlag");  // 提交、回退操作标识
		String uuid = request.getParameter("uuid");
		String taskId = request.getParameter("taskId");
		String notionContent = request.getParameter("notionContent");
		ProcessTest processTest = workflowTestService.get(uuid);
		WorkflowBaseModel workflowBaseModel = new WorkflowBaseModel();
		workflowBaseModel.setOperator(processTest.getCreateId());
		workflowBaseModel.setTaskId(taskId);
		workflowBaseModel.setProcessInstanceId(processTest.getProcessId());
		if("1".equals(actionFlag)){
			workflowBaseModel.setActionName(ActionName.COMMIT.name());
		}else{
			workflowBaseModel.setActionName(ActionName.ROLLBACK.name());
		}
		workflowBaseModel.setNotionContent(notionContent);
		workflowTestService.workflowTestVerify(processTest, workflowBaseModel);
		return null;
	}
	
	/**
	 * 审核
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/toVerify_2")
	public String toVerifyPage(final Model model, final String taskId, final String bussnissId) {
		 model.addAttribute("processTest", workflowTestService.get(bussnissId));
		 model.addAttribute("taskId", taskId);
		return "/workflowtest/verify_2";
	}
	
	/**
	 * 审批
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/verify_2")
	public String verifyPage(HttpServletRequest request){
		String actionFlag = request.getParameter("actionFlag");
		String uuid = request.getParameter("uuid");
		String taskId = request.getParameter("taskId");
		String notionContent = request.getParameter("notionContent");
		
		ProcessTest processTest = workflowTestService.get(uuid);
		
		WorkflowBaseModel workflowBaseModel = new WorkflowBaseModel();
		workflowBaseModel.setOperator(processTest.getCreateId());
		workflowBaseModel.setTaskId(taskId);
		workflowBaseModel.setProcessInstanceId(processTest.getProcessId());
		if("1".equals(actionFlag)){
			workflowBaseModel.setActionName(ActionName.COMMIT.name());
		}else{
			workflowBaseModel.setActionName(ActionName.ROLLBACK.name());
		}
		workflowBaseModel.setNotionContent(notionContent);
		workflowTestService.workflowTestVerify(processTest, workflowBaseModel);
		return null;
	}
	
	/**
	 * 回退
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/workflowtest/rollback")
	public String rollback(HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		String taskId = request.getParameter("taskId");
		ProcessTest processTest = workflowTestService.get(uuid);
		
		WorkflowBaseModel workflowBaseModel = new WorkflowBaseModel();
		workflowBaseModel.setOperator(processTest.getCreateId());
		workflowBaseModel.setTaskId(taskId);
		workflowBaseModel.setProcessInstanceId(processTest.getProcessId());
		workflowBaseModel.setActionName(ActionName.ROLLBACK.name());
		workflowTestService.workflowTestRollback(processTest, workflowBaseModel);
		return null;
	}
}

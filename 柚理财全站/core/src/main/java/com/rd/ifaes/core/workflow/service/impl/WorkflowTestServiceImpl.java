package com.rd.ifaes.core.workflow.service.impl;

import javax.annotation.Resource;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.workflow.domain.ProcessTest;
import com.rd.ifaes.core.workflow.mapper.ProcessTestMapper;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;
import com.rd.ifaes.core.workflow.service.WorkflowService;
import com.rd.ifaes.core.workflow.service.WorkflowTestService;


@Service("workflowTestService") 
public class WorkflowTestServiceImpl  extends BaseServiceImpl<ProcessTestMapper, ProcessTest> implements WorkflowTestService{

	@Resource
	private WorkflowService workflowService;
	
	/**
	 * 提交（新增提交、编辑提交、流程回退重新提交）动作：发起流程
	 */
	@Override
	@Transactional
	public int workflowTestApply(ProcessTest processTest) {
		//
		//System.out.println("流程测试");
		processTest.preInsert();
		String userId= "0007b852817c4c1890987cb3ad6c5265";
		processTest.setCreateId(userId);
		insert(processTest);
		
		String processkey = "processDemo";
		String busnissId = processTest.getUuid();
		String busnissTitle = "流程测试" + System.currentTimeMillis(); //processTest.getTitle();
		WorkflowBaseModel workflowBaseModel = new WorkflowBaseModel();
		workflowBaseModel.setOperator(userId);
		Task task = workflowService.startProcess(processkey, busnissId, busnissTitle, null, workflowBaseModel);
		if (task != null) {
			processTest.setProcessId(task.getProcessInstanceId());
			processTest.setTaskId(task.getId());
			processTest.setStatus("1");  //已提交
			this.update(processTest);
		}
		return 1;
	}

	/**
	 * 流程推进(审核、审批)
	 */
	@Override
	public int workflowTestVerify(ProcessTest processTest, WorkflowBaseModel workflowBaseModel) {
		//
		//System.out.println("流程推进执行");
		Task task = workflowService.executeProcess(processTest.getTitle(), null, workflowBaseModel);
		if (task != null) {
			//processTest.setProcessId(task.getProcessInstanceId());
			processTest.setTaskId(task.getId());
			processTest.setStatus("2");
		}
		//processTest.setStatus(); 根据提交动作做相应的设置
		this.update(processTest);
		return 1;
	}

	
	@Override
	public int workflowTestRollback(ProcessTest processTest, WorkflowBaseModel workflowBaseModel) {
		//
		//System.out.println("流程推进执行");
		Task task = workflowService.executeProcess(processTest.getTitle(), null, workflowBaseModel);
		if (task != null) {
			//processTest.setProcessId(task.getProcessInstanceId());
			processTest.setTaskId(task.getId());
			processTest.setStatus("0");
		}
		//processTest.setxxx();
		this.update(processTest);
		return 1;
	}
	

}
package com.rd.ifaes.core.workflow.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.workflow.domain.ProcessTest;
import com.rd.ifaes.core.workflow.model.WorkflowBaseModel;

public interface WorkflowTestService extends BaseService<ProcessTest>{
	
	int workflowTestApply(ProcessTest processTest);
	
	int workflowTestVerify(ProcessTest processTest, WorkflowBaseModel workflowBaseModel);
	
	int workflowTestRollback(ProcessTest processTest, WorkflowBaseModel workflowBaseModel);

}
package com.rd.ifaes.core.workflow.listener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 任务创建初始解析任务分配人员
 * @author xxb
 *
 */
public class FirstTaskCreationListener extends DefaultTaskListener {
	
	private static final long serialVersionUID = 8751069785732454948L;
	
	
	@Override
	public void onCreate(DelegateTask delegateTask){
		RuntimeService runtimeService = (RuntimeService)SpringContextHolder.getBean("runtimeService");
		String businessCreator = ObjectUtils.toString(runtimeService.getVariable(delegateTask.getProcessInstanceId(), Constant.PROCESS_BUSINESS_CREATOR));
		if (StringUtils.isBlank(businessCreator)) {
			throw new BussinessException("流程创建人不能为空!");
		}
		delegateTask.setAssignee(businessCreator);
	}

}

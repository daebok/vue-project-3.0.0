package com.rd.ifaes.core.workflow.listener;


import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.workflow.handler.DistributeHandle;
import com.rd.ifaes.core.workflow.handler.impl.RoleBasedDistributeHandle;

/**
 * 任务创建初始解析任务分配人员
 * @author xxb
 *
 */
public class TaskCreationListener extends DefaultTaskListener {
	
	private static final long serialVersionUID = 8751069785732454948L;
	
	
	@Override
	public void onCreate(DelegateTask delegateTask){
		String assignee = delegateTask.getAssignee();
		DistributeHandle handle = null;
		if (StringUtils.isNotBlank(assignee)) {
			if(assignee.contains(Constant.DEFAULT_ROLE_STRING)){
				handle = new RoleBasedDistributeHandle();
			}
			//
			if(handle == null){
				throw new BussinessException("assignee:{"+assignee+"} not support!");
			}
			handle.distribute(delegateTask);
		}else{
			throw new BussinessException("环节名称 [" + delegateTask.getName() + "] 未配置办理人!");
		}
		
	}

}

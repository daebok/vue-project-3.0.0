package com.rd.ifaes.core.workflow.handler.impl;

import java.util.List;
import org.activiti.engine.delegate.DelegateTask;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.mapper.OperatorMapper;
import com.rd.ifaes.core.workflow.handler.DistributeHandle;


/**
 * 基于角色-人员解析类
 * 与业务相关
 * @author xxb
 *
 */
public class RoleBasedDistributeHandle implements DistributeHandle{

	@Override
	public void distribute(DelegateTask delegateTask) {
		String assignee = delegateTask.getAssignee();
		String roleId = assignee.substring(assignee.indexOf(":") + 1);
		OperatorMapper operatorMapper = (OperatorMapper)SpringContextHolder.getBean("operatorMapper");
		Operator operator = new Operator();
		operator.setRoleId(roleId);
		List<Operator> operatorList = operatorMapper.findRoleOperators(operator);
		if (operatorList == null || operatorList.size() == 0) {
			throw new BussinessException("环节名称 [" + delegateTask.getName() + "] 办理人配置有误!");
		}
		for(Operator op : operatorList){
			delegateTask.addCandidateUser(op.getUuid());  //角色下所有人员共享一个待办，其中某人处理即完成任务。
		}
		//清理配置数据
		delegateTask.setAssignee(null);
	}

}

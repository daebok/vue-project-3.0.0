package com.rd.ifaes.core.workflow.handler;

import org.activiti.engine.delegate.DelegateTask;

/**
 * 分派任务
 * @version 3.0
 * @author xxb
 * @date 2016年6月3日
 */
public interface DistributeHandle {

	void distribute(DelegateTask delegateTask);
}

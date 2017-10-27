package com.rd.ifaes.core.workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.rd.ifaes.core.core.constant.Constant;


/**
 * 事件派发
 * @author xxb
 *
 */
public class DefaultTaskListener implements TaskListener {

	private static final long serialVersionUID = -4027954410706896567L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String eventName = delegateTask.getEventName();

		if(Constant.EVENT_CREATE.equals(eventName)){
			this.onCreate(delegateTask);
		}
		
		if(Constant.EVENT_COMPETE.equals(eventName)){
			this.onComplete(delegateTask);
		}
	}

	
	protected void onCreate(DelegateTask delegateTask){
		
	}
	
	protected void onComplete(DelegateTask delegateTask){
		
	}
}
